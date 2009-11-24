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
import org.cauldron.einstein.api.assembly.group.InstructionGroup;
import org.cauldron.einstein.api.assembly.instruction.InstructionMetaData;
import org.cauldron.einstein.api.assembly.instruction.annotation.OperatorType;
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterInstruction;
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterOperator;
import org.cauldron.einstein.api.assembly.instruction.annotation.ResourceReference;
import org.cauldron.einstein.api.common.metadata.CoreMetaData;
import org.cauldron.einstein.api.message.MessageListener;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.model.execution.ExecutionContext;
import org.cauldron.einstein.api.model.lifecycle.LifecycleContext;
import org.cauldron.einstein.api.model.resource.ResourceRef;
import org.cauldron.einstein.api.provider.facade.DispatchFacade;
import org.cauldron.einstein.ri.core.instructions.AbstractInstruction;
import org.cauldron.einstein.ri.core.instructions.context.MessageAwareContext;

import java.util.List;

/*
 * See annotations for description of this instruction.
 *
 * @author Neil Ellis
 */

@RegisterInstruction(name = "dispatch", qualifiers = {"each", "payload", "to"},
                     resources = @ResourceReference(facadesUsed = DispatchFacade.class),
                     metadata = @InstructionMetaData(core = @CoreMetaData(
                             name = "dispatch",
                             shortDescription = "Asynchronous non-blocking write to a resource.",
                             description = "Sends a message/payload to a resource, does not wait for a response.",
                             syntax = "( 'dispatch' [ 'each' ] [ 'payload' ] <uri>  ) | ( [ <instruction-group> ] '>>' <uri>  )",
                             example = "dispatch \"jms:queue\" 1000"

                     ), qualifiers = {"each - writes each element of a tuple individually",
                             "payload - only the payload is written to the resource."},
                        result = "The inbound message."
                     ))
@RegisterOperator(name = "dispatch",
                  symbol = "&>",
                  types = {OperatorType.BINARY, OperatorType.UNARY, OperatorType.BINARY})
@org.contract4j5.contract.Contract
public class DispatchInstruction extends AbstractInstruction {

    private final ResourceRef ref;
    private InstructionGroup group;
    private boolean all;
    private boolean payload;


    public DispatchInstruction(CompilationInformation debug, ResourceRef ref, String... qualifiers) {
        super(debug);
        this.ref = ref;
        if (hasQualifier("all", qualifiers)) {
            all = true;
        }
        if (hasQualifier("payload", qualifiers)) {
            payload = true;
        }
    }


    public DispatchInstruction(CompilationInformation debug, ResourceRef ref, InstructionGroup group,
                               String... qualifiers) {
        this(debug, ref, qualifiers);

        this.group = group;
        this.group.setParent(this);
    }


    public MessageTuple execute(final ExecutionContext context, MessageTuple tuple) {
        assertStarted();
        logExecution(ref.getURI().asString());
        DispatchFacade dispatchFacade = ref.getResource(new MessageAwareContext(this,
                                                                                context.getActiveProfile()
                                                                                        .getMessageModel().createExternalisableTuple(
                                                                                        tuple)))
                .getFacade(DispatchFacade.class);
        if (all) {
            List<MessageTuple> list = tuple.realiseAsList();
            for (MessageTuple innerTuple : list) {
                writeTuple(context, dispatchFacade, innerTuple);
            }
        } else {
            writeTuple(context, dispatchFacade, tuple);
        }
        return tuple;
    }


    private void writeTuple(final ExecutionContext context, DispatchFacade dispatchFacade, MessageTuple innerTuple) {
        if (group == null) {
            dispatchFacade.writeAsync(this, innerTuple, payload);
        } else {
            dispatchFacade.writeAsync(this, innerTuple, payload, new MessageListener() {

                public void handle(MessageTuple listenerMessage) {
                    group.execute(context, listenerMessage);
                }
            });
        }
    }


    @Override
    public void init(LifecycleContext ctx) {
        super.init(ctx);
        ref.init(ctx);
        if (group != null) {
            group.init(ctx);
        }
    }


    @Override
    public void start(LifecycleContext ctx) {
        super.start(ctx);
        ref.start(ctx);
        if (group != null) {
            group.start(ctx);
        }
    }


    @Override
    public void stop(LifecycleContext ctx) {
        super.stop(ctx);
        ref.stop(ctx);
        if (group != null) {
            group.stop(ctx);
        }
    }


    @Override
    public void destroy(LifecycleContext ctx) {
        super.destroy(ctx);
        ref.destroy(ctx);
        if (group != null) {
            group.destroy(ctx);
        }
    }
}