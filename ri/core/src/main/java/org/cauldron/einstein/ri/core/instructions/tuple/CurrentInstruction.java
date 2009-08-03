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
import org.cauldron.einstein.api.assembly.instruction.InstructionMetaData;
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterInstruction;
import org.cauldron.einstein.api.common.metadata.CoreMetaData;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.model.execution.ExecutionContext;
import org.cauldron.einstein.ri.core.instructions.AbstractInstruction;


/**
 * See annotations for description of this instruction.
 *
 * @author Neil Ellis
 */
@RegisterInstruction(name = "current",
                     metadata = @InstructionMetaData(
                             core = @CoreMetaData(
                                     name = "current",
                                     shortDescription = "The value of the current message (a no-op instruction)",
                                     description = "This instruction has no effect except to refer to the current inbound message, it is useful when building tuples. ",
                                     syntax = "'current'",
                                     example = "[current, read \"time:hh:mm:ss\",  read \"text:Note\"];"
                             ),
                             result = "The current message."
                     ))
@org.contract4j5.contract.Contract
public class CurrentInstruction extends AbstractInstruction {

    public CurrentInstruction(CompilationInformation debug) {
        super(debug);
    }


    public MessageTuple execute(ExecutionContext context, MessageTuple message) {
        assertStarted();
        logExecution();

        return message;
    }
}