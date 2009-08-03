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
import org.cauldron.einstein.api.model.resource.ResourceRef;
import org.cauldron.einstein.api.provider.facade.ReadFacade;
import org.cauldron.einstein.ri.core.instructions.AbstractInstruction;
import org.cauldron.einstein.ri.core.instructions.context.MessageAwareContext;


/**
 * See annotations for description of this instruction.
 *
 * @author Neil Ellis
 */
@RegisterInstruction(name = "check", qualifiers = {"all", "payload", "for"},
                     resources = @ResourceReference(facadesUsed = ReadFacade.class),
                     metadata = @InstructionMetaData(
                             core = @CoreMetaData(
                                     name = "check",
                                     shortDescription = "Destructive non-blocking read from a resource.",
                                     description = "Requests messages from a resource returns an empty message if timed out, read is destructive but not blocking.",
                                     example = "check all \"jms://in\";",
                                     syntax = "( 'check' [ 'all'] ['payload' ] [ 'for' ]  <uri>  ) | ( [ <instruction-group> ] '<&' <uri> )"

                             )
                             , qualifiers = {"all - read all messages currently avilable from the resource",
                             "payload - the resource stored is just the payload."},
                               result = "Tuple of messages or an empty tuple if non available."

                     ))

@RegisterOperator(name = "check",
                  symbol = "<&",
                  types = {OperatorType.BINARY_REVERSED, OperatorType.UNARY, OperatorType.TERNARY})

@org.contract4j5.contract.Contract
public class CheckInstruction extends AbstractInstruction {

    private final ResourceRef ref;
    private boolean payload;
    private boolean all;


    public CheckInstruction(CompilationInformation debug, ResourceRef ref) {
        super(debug);
        this.ref = ref;
    }


    public CheckInstruction(CompilationInformation debug, ResourceRef ref, String... qualifiers) {
        super(debug);
        this.ref = ref;
        if (hasQualifier("all", qualifiers)) {
            all = true;
        }
        if (hasQualifier("payload", qualifiers)) {
            payload = true;
        }
    }


    public MessageTuple execute(ExecutionContext context, MessageTuple tuple) {
        assertStarted();
        logExecution(ref.getURI().asString());
        MessageAwareContext messageAwareContext = new MessageAwareContext(this,
                                                                          context.getActiveProfile()
                                                                                  .getMessageModel().createExternalisableTuple(
                                                                                  tuple));
        ReadFacade facade = ref.getResource(messageAwareContext).getFacade(ReadFacade.class);
        final MessageTuple result = facade.readNoWait(messageAwareContext, all, payload);
        result.setExecutionCorrelation(tuple.getExecutionCorrelation());
        return result;
    }


    @Override
    public void init(LifecycleContext context) {
        super.init(context);
        ref.init(context);
    }


    @Override
    public void start(LifecycleContext context) {
        super.start(context);
        ref.start(context);
    }


    @Override
    public void stop(LifecycleContext context) {
        super.stop(context);
        ref.stop(context);
    }


    @Override
    public void destroy(LifecycleContext context) {
        super.destroy(context);
        ref.destroy(context);
    }
}