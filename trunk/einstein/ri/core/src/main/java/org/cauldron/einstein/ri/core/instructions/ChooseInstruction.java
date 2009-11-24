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
import org.cauldron.einstein.api.assembly.group.NamedTupleInstructionGroup;
import org.cauldron.einstein.api.assembly.instruction.InstructionMetaData;
import org.cauldron.einstein.api.assembly.instruction.annotation.OperatorType;
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterInstruction;
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterOperator;
import org.cauldron.einstein.api.common.metadata.CoreMetaData;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.message.action.context.ActionCallbackContext;
import org.cauldron.einstein.api.message.action.types.ReadAction;
import org.cauldron.einstein.api.message.view.composites.ReadOnlyView;
import org.cauldron.einstein.api.model.execution.Executable;
import org.cauldron.einstein.api.model.execution.ExecutionContext;
import org.cauldron.einstein.api.model.lifecycle.LifecycleContext;
import org.cauldron.einstein.api.model.profile.Profile;
import org.cauldron.einstein.api.provider.facade.exception.RoutingFailureRuntimeException;

/**
 * See annotations for description of this instruction.
 *
 * @author Neil Ellis
 */
@RegisterInstruction(name = "choose", qualifiers = {"from"},
                     metadata = @InstructionMetaData(
                             core = @CoreMetaData(
                                     name = "choose",
                                     shortDescription = "Choose from instructions in a named tuple.",
                                     description = "Converts the payload of the current message into a string and selects a labelled intrstruction with that as a label.",
                                     syntax = "( 'choose' [ 'from' ] <instruction-group>  | ( [ <instruction-group> ] '?=>' <instruction-group>  ) ",
                                     example = " choose from [ red : << \"text:Roses\", blue : << \"text:Violets\""
                             ),
                             result = "The result of executing the choice."
                     )

)

@RegisterOperator(name = "choose", symbol = "?=>", types = {OperatorType.BINARY, OperatorType.UNARY})
@org.contract4j5.contract.Contract
public class ChooseInstruction extends AbstractInstruction {

    private final NamedTupleInstructionGroup group;

    public ChooseInstruction(CompilationInformation debug, NamedTupleInstructionGroup group) {
        super(debug);
        this.group = group;
        this.group.setParent(this);
    }


    public ChooseInstruction(CompilationInformation debug, NamedTupleInstructionGroup group, String... ignore) {
        this(debug, group);
    }


    public MessageTuple execute(final ExecutionContext context, final MessageTuple inboundTuple) {
        assertStarted();
        logExecution();
        
        final MessageTuple[] result = new MessageTuple[]{null};
        if (!inboundTuple.isEmpty()) {
            inboundTuple.realiseAsOne().execute(ReadAction.class, new ReadAction() {

                public void handle(ActionCallbackContext<ReadOnlyView, Object> callbackContext) {
                    String label = callbackContext.getMessageHistory()
                            .getCurrentEntry()
                            .getNewValue()
                            .getPayload()
                            .asString();
                    Executable instruction = group.getExecutable(label);
                    if (instruction == null) {
                        instruction = group.getExecutable("default");
                    }
                    if (instruction != null) {
                        result[0] = context.getActiveProfile()
                                .getExecutionModel()
                                .getSingleInstructionExecutor(instruction)
                                .execute(new ExecutionContext() {

                                    public Profile getActiveProfile() {
                                        return context.getActiveProfile();
                                    }
                                }, inboundTuple);
                    }
                }
            });
            if (result[0] == null) {
                //TODO: i18n
                throw new RoutingFailureRuntimeException(new RuntimeException("Choice did not match."));
            }
            return result[0];
        } else {
            Executable defaultInstruction = group.getExecutable("default");
            if (defaultInstruction != null) {
                return defaultInstruction.execute(context, inboundTuple);
            } else {
                //TODO: i18n
                throw new RoutingFailureRuntimeException(new RuntimeException("Choice did not match."));
            }
        }
    }


    @Override
    public void init(LifecycleContext context) {
        super.init(context);
        group.init(context);
    }


    @Override
    public void start(LifecycleContext context) {
        super.start(context);
        group.start(context);
    }


    @Override
    public void stop(LifecycleContext context) {
        super.stop(context);
        group.stop(context);
    }


    @Override
    public void destroy(LifecycleContext context) {
        super.destroy(context);
        group.destroy(context);
    }
}