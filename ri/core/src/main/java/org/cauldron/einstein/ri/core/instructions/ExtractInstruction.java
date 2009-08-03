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

package org.cauldron.einstein.ri.core.instructions;

import org.cauldron.einstein.api.assembly.debug.CompilationInformation;
import org.cauldron.einstein.api.assembly.instruction.InstructionMetaData;
import org.cauldron.einstein.api.assembly.instruction.annotation.OperatorType;
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterInstruction;
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterOperator;
import org.cauldron.einstein.api.assembly.instruction.annotation.ResourceReference;
import org.cauldron.einstein.api.common.metadata.CoreMetaData;
import org.cauldron.einstein.api.message.Message;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.message.action.context.ActionCallbackContext;
import org.cauldron.einstein.api.message.action.types.ReadAction;
import org.cauldron.einstein.api.message.data.model.DataObject;
import org.cauldron.einstein.api.message.view.composites.ReadOnlyView;
import org.cauldron.einstein.api.model.execution.ExecutionContext;
import org.cauldron.einstein.api.model.lifecycle.LifecycleContext;
import org.cauldron.einstein.api.model.resource.ResourceRef;
import org.cauldron.einstein.api.provider.facade.QueryFacade;
import org.cauldron.einstein.ri.core.instructions.context.MessageAwareContext;

/**
 * See annotations for description of this instruction.
 *
 * @author Neil Ellis
 */
@RegisterInstruction(name = "extract", qualifiers = {"with"},
                     resources = @ResourceReference(facadesUsed = QueryFacade.class),
                     metadata = @InstructionMetaData(
                             core = @CoreMetaData(
                                     name = "extract",
                                     shortDescription = "Extract data from a payload using the supplied query.",
                                     description = "Iterates over a tuple, executing the supplied instruction group for each entry and combining the result.",
                                     syntax = "( 'each' <instruction-group> ) | ( [ <instruction-group> ] '...' ( <instruction-group> )+ ) ",
                                     example = "each  {write  \"console:\"};"
                             ),
                             result = "A tuple containing the combined result of the execution of the instruction group over each tuple element."
                     ))

@RegisterOperator(name = "extract", symbol = "->", types = {OperatorType.BINARY, OperatorType.UNARY})
@org.contract4j5.contract.Contract
public class ExtractInstruction extends AbstractInstruction {

    private final ResourceRef ref;

    public ExtractInstruction(CompilationInformation debug, ResourceRef ref, String ... qualifiers) {
        this(debug, ref);
    }

    public ExtractInstruction(CompilationInformation debug, ResourceRef ref) {
        super(debug);
        this.ref = ref;
    }


    public MessageTuple execute(ExecutionContext context, final MessageTuple messageTuple) {
        assertStarted();
        logExecution(ref.getURI().asString());
        
        final DataObject[] result = new DataObject[1];
        Message msg = messageTuple.realiseAsOne();
        msg.execute(ReadAction.class, new ReadAction() {

            public void handle(ActionCallbackContext<ReadOnlyView, Object> ctx) {
                result[0] = ref.getResource(new MessageAwareContext(ExtractInstruction.this, messageTuple))
                        .getFacade(QueryFacade.class)
                        .selectSingle(ctx.getMessageHistory().getCurrentEntry().getEvent().getNewState().getPayload());
            }
        });
        return context.getActiveProfile().getMessageModel().createMessage(messageTuple.getExecutionCorrelation(), result[0]);
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