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


package org.cauldron.einstein.ri.core.instructions.tuple;

import org.cauldron.einstein.api.assembly.debug.CompilationInformation;
import org.cauldron.einstein.api.assembly.group.InstructionGroup;
import org.cauldron.einstein.api.assembly.instruction.InstructionMetaData;
import org.cauldron.einstein.api.assembly.instruction.annotation.OperatorType;
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterInstruction;
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterOperator;
import org.cauldron.einstein.api.common.metadata.CoreMetaData;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.model.execution.ExecutionContext;
import org.cauldron.einstein.api.model.lifecycle.LifecycleContext;
import org.cauldron.einstein.ri.core.instructions.AbstractInstruction;


/**
 * See annotations for description of this instruction.
 *
 * @author Neil Ellis
 */
@RegisterInstruction(name = "append",
                     metadata = @InstructionMetaData(
                             core = @CoreMetaData(
                                     name = "append",
                                     shortDescription = "Appends the results of an execution to the current tuple.",
                                     description = "Appends the results of an execution to the current tuple.",
                                     syntax = "( 'append' <instruction-group> ) | ( <instruction-group> '+=' ( <instruction-group> )+ ) ",
                                     example = "(read \"text:A\"); += read \"text:B\";"
                             ),
                             result = "A tuple."
                     ))

@RegisterOperator(name = "append", symbol = "+=", types = {OperatorType.UNARY})
@org.contract4j5.contract.Contract
public class AppendInstruction extends AbstractInstruction {

    private final InstructionGroup group;

    public AppendInstruction(CompilationInformation debug, InstructionGroup group) {
        super(debug);
        this.group = group;
        this.group.setParent(this);
    }


    public MessageTuple execute(ExecutionContext context, MessageTuple message) {
        assertStarted();
        logExecution();

        return getParent().getActiveProfile()
                .getMessageModel()
                .createTuple(context.getActiveProfile().getDataModel(),
                             message.realiseAsList(),
                             group.execute(context, message).realiseAsList());
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
