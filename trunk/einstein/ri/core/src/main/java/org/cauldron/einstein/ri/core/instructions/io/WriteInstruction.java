/******************************************************************************
 *                                                                            *
 *                                                                            *
 *     All works are (C) 2008 - Mangala Solutions Ltd and Paremus Ltd.
 *                                                                            *
 *     Jointly liicensed to Mangala Solutions and Paremus under one           *
 *     or more contributor license agreements.  See the NOTICE file           *
 *     distributed with this work for additional information                  *
 *     regarding copyright ownership.  Mangala Solutions and Paremus          *
 *     licenses this file to you under the Apache License, Version            *
 *     2.0 (the "License"); you may not use this file except in               *
 *     compliance with the License.  You may obtain a copy of the             *
 *     License at                                                             *
 *                                                                            *
 *             http://www.apache.org/licenses/LICENSE-2.0                     *
 *                                                                            *
 *     Unless required by applicable law or agreed to in writing,             *
 *     software distributed under the License is distributed on an            *
 *     "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY                 *
 *     KIND, either express or implied.  See the License for the              *
 *     specific language governing permissions and limitations                *
 *     under the License.                                                     *
 *                                                                            *
 ******************************************************************************/

package org.cauldron.einstein.ri.core.instructions.io;

import org.cauldron.einstein.api.assembly.debug.CompilationInformation;
import org.cauldron.einstein.api.assembly.instruction.InstructionMetaData;
import org.cauldron.einstein.api.assembly.instruction.annotation.OperatorType;
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterInstruction;
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterOperator;
import org.cauldron.einstein.api.assembly.instruction.annotation.ResourceReference;
import org.cauldron.einstein.api.common.metadata.CoreMetaData;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.model.execution.ExecutionContext;
import org.cauldron.einstein.api.model.lifecycle.LifecycleContext;
import org.cauldron.einstein.api.model.profile.Profile;
import org.cauldron.einstein.api.model.resource.ResourceRef;
import org.cauldron.einstein.api.provider.facade.WriteFacade;
import org.cauldron.einstein.ri.core.instructions.AbstractInstruction;
import org.cauldron.einstein.ri.core.instructions.context.MessageAwareContext;

import java.util.ArrayList;
import java.util.List;

/*
 * See annotations for description of this instruction.
 *
 * @author Neil Ellis
 */

@RegisterInstruction(name = "write", qualifiers = {"each", "payload", "to"},
                     metadata = @InstructionMetaData(core = @CoreMetaData(
                             name = "write",
                             shortDescription = "Blocking write to a resource.",
                             description = "Sends a message/payload to a resource, blocks awaiting a response or timeout.",
                             syntax = "( 'write' [ 'each' ] [ 'payload' ]<uri> [ <long> ] ) | ( [ <instruction-group> ] '>>' <uri>  [ ':' <long> ] )",
                             example = "write \"jms:queue\" 1000"

                     ), qualifiers = {"each - writes each element of a tuple individually",
                             "payload - the payload is written to the resource."},
                        result = "The response to the operation if any, if the resource does not produce responses an empty message should be returned, if a timeout is exceeded a void message should be returned."
                     ),
                     resources = @ResourceReference(facadesUsed = WriteFacade.class))
@RegisterOperator(name = "write", symbol = ">>", types = {OperatorType.BINARY, OperatorType.UNARY, OperatorType.BINARY})
@org.contract4j5.contract.Contract
public class WriteInstruction extends AbstractInstruction {

    private ResourceRef ref;
    private Long timeout;
    private boolean each;
    private boolean payload;

    public WriteInstruction(CompilationInformation debug, ResourceRef ref) {
        super(debug);
        this.ref = ref;
        this.ref = ref;
    }


    public WriteInstruction(CompilationInformation debug, ResourceRef ref, String... qualifiers) {
        super(debug);
        this.ref = ref;
        this.ref = ref;
        if (hasQualifier("each", qualifiers)) {
            each = true;
        }
        if (hasQualifier("payload", qualifiers)) {
            payload = true;
        }
    }


    public WriteInstruction(CompilationInformation debug, ResourceRef ref, Long timeout) {
        this(debug, ref);
        this.timeout = timeout;
    }


    public WriteInstruction(CompilationInformation debug, ResourceRef ref, Long timeout, String... qualifiers) {
        this(debug, ref, qualifiers);
        this.timeout = timeout;
    }


    public MessageTuple execute(ExecutionContext context, MessageTuple tuple) {
        assertStarted();
        logExecution(ref.getURI().asString());

        if (!tuple.isEmpty()) {
            WriteFacade writeFacade = ref.getResource(new MessageAwareContext(this, tuple))
                    .getFacade(WriteFacade.class);
            if (each) {
                List<MessageTuple> result = new ArrayList<MessageTuple>();
                List<MessageTuple> list = tuple.realiseAsList();
                for (MessageTuple innerTuple : list) {
                    final MessageTuple externalisableTuple = context.getActiveProfile()
                            .getMessageModel()
                            .createExternalisableTuple(innerTuple);
                    if (timeout == null) {
                        final MessageTuple resultTuple = writeFacade.write(this, externalisableTuple, payload);
                        resultTuple.setExecutionCorrelation(tuple.getExecutionCorrelation());
                        result.add(resultTuple);
                    } else {
                        final MessageTuple resultTuple = writeFacade.write(this, externalisableTuple, payload, timeout);
                        resultTuple.setExecutionCorrelation(tuple.getExecutionCorrelation());
                        result.add(resultTuple);
                    }
                }
                final Profile profile = context.getActiveProfile();
                return profile.getMessageModel().createTuple(profile.getDataModel(), result);
            } else {
                final MessageTuple result = writeFacade.write(this,
                                                              context.getActiveProfile()
                                                                      .getMessageModel().createExternalisableTuple(tuple),
                                                              payload);
                result.setExecutionCorrelation(tuple.getExecutionCorrelation());
                return result;
            }
        } else {
            return tuple;
        }
    }


    @Override
    public void init(LifecycleContext ctx) {
        super.init(ctx);
        ref.init(ctx);
    }


    @Override
    public void start(LifecycleContext ctx) {
        super.start(ctx);
        ref.start(ctx);
    }


    @Override
    public void stop(LifecycleContext ctx) {
        super.stop(ctx);
        ref.stop(ctx);
    }


    @Override
    public void destroy(LifecycleContext context) {
        super.destroy(context);
        ref.destroy(context);
    }
}