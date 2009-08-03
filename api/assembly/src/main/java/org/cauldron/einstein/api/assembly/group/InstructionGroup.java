/******************************************************************************
 *                                                                            *
 *                                                                            *
 *     All works are (C) 2008- Mangala Solutions Ltd and Paremus Ltd.         *
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

package org.cauldron.einstein.api.assembly.group;

import org.contract4j5.contract.*;

import org.cauldron.einstein.api.assembly.instruction.Instruction;
import org.cauldron.einstein.api.model.execution.ExecutableGroup;
import org.cauldron.einstein.api.model.profile.Profile;

/**
 * An InstructionGroup is a collection of instructions, it can be executed directly as it will call the appropriate
 * {@link org.cauldron.einstein.api.model.ExecutionModel} itself.
 *
 * @author Neil Ellis
 * 
 */
@Contract
public interface InstructionGroup extends Instruction, ExecutableGroup {


    /**
     * This method sets the profile to be used by the instruction group and is typically of use to meta instructions.
     * This should never be called once a group has been initialized.
     * 
     * @param profile the new profile.
     */
    @Pre
    void setProfile( Profile profile);
}
