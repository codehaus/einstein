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

package org.cauldron.einstein.api.assembly.instruction;

import org.cauldron.einstein.api.assembly.debug.CompilationAware;
import org.cauldron.einstein.api.model.execution.Executable;
import org.cauldron.einstein.api.model.lifecycle.Lifecycle;
import org.cauldron.einstein.api.model.profile.Profile;
import org.contract4j5.contract.Contract;
import org.contract4j5.contract.Post;
import org.contract4j5.contract.Pre;

/**
 * An instruction.
 *
 * @author Neil Ellis
 */
@Contract
public interface Instruction extends Lifecycle, Executable, CompilationAware {

    /**
     * Returns the parent {@link Instruction} of this instruction.
     *
     * @return the parent.
     */
    Instruction getParent();

    /**
     * Sets the parent of this instruction.
     *
     * @param parent the new parent.
     */
    @Pre
    @Post("$this.getParent() == parent") void setParent(Instruction parent);

    /**
     * Returns the profile that would be active on execution of this instruction.
     *
     * @return the related profile.
     */
    @Post Profile getActiveProfile();

}
