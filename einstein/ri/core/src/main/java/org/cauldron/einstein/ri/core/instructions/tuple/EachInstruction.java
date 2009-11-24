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
import org.cauldron.einstein.api.model.execution.Executor;
import org.cauldron.einstein.api.model.lifecycle.LifecycleContext;
import org.cauldron.einstein.ri.core.instructions.AbstractInstruction;


/**
 * See annotations for description of this instruction.
 *
 * @author Neil Ellis
 */
@RegisterInstruction(name = "each",
                     metadata = @InstructionMetaData(
                             core = @CoreMetaData(
                                     name = "each",
                                     shortDescription = "Iterates over a tuple.",
                                     description = "Iterates over a tuple, executing the supplied instruction group for each entry and combining the result.",
                                     syntax = "( 'each' <instruction-group> ) | ( [ <instruction-group> ] '...' ( <instruction-group> )+ ) ",
                                     example = "each  {write  \"console:\"};"
                             ),
                             result = "A tuple containing the combined result of the execution of the instruction group over each tuple element."
                     ))

@RegisterOperator(name = "each", symbol = "...", types = {OperatorType.BINARY, OperatorType.UNARY})

@org.contract4j5.contract.Contract
public class EachInstruction extends AbstractInstruction {

    private final InstructionGroup group;
    private Executor executor;


    public EachInstruction(CompilationInformation debug, InstructionGroup group) {
        super(debug);
        this.group = group;
        this.group.setParent(this);
    }


    public MessageTuple execute(ExecutionContext context, MessageTuple message) {
        assertStarted();
        logExecution();
        
        return executor.execute(this, message);
    }


    @Override
    public void init(LifecycleContext context) {
        super.init(context);
        executor = getParent().getActiveProfile().getExecutionModel().getIteratingExecutor(group);
        group.init(context);
        executor.init(context);
    }


    @Override
    public void start(LifecycleContext ctx) {
        super.start(ctx);
        group.start(ctx);
        executor.start(ctx);
    }


    @Override
    public void stop(LifecycleContext ctx) {
        super.stop(ctx);
        group.stop(ctx);
        executor.stop(ctx);
    }


    @Override
    public void destroy(LifecycleContext ctx) {
        super.destroy(ctx);
        group.destroy(ctx);
        executor.destroy(ctx);
    }
}
