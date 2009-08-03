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

package org.cauldron.einstein.ri.core.group;

import org.cauldron.einstein.api.assembly.debug.CompilationInformation;
import org.cauldron.einstein.api.assembly.group.InstructionGroup;
import org.cauldron.einstein.api.assembly.instruction.Instruction;
import org.cauldron.einstein.api.model.execution.ExecutionContext;
import org.cauldron.einstein.api.model.profile.Profile;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class TupleInstructionGroup extends AbstractInstructionGroup implements InstructionGroup, ExecutionContext {

    public TupleInstructionGroup(CompilationInformation debug, Instruction... instructions) {
        super(debug, instructions);
    }


    public TupleInstructionGroup(CompilationInformation debug, Profile profile, Instruction... instructions) {
        super(debug, profile, instructions);
    }


    protected void initExecutor() {
        setExecutor(getActiveProfile().getExecutionModel().getTupleExecutor(getInstructions()));
    }
}
