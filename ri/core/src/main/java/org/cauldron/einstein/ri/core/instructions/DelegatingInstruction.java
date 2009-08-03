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
import org.cauldron.einstein.api.assembly.instruction.Instruction;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.model.execution.ExecutionContext;
import org.cauldron.einstein.api.model.lifecycle.LifecycleContext;
import org.cauldron.einstein.api.model.lifecycle.exception.DestructionRuntimeException;
import org.cauldron.einstein.api.model.lifecycle.exception.InitializationRuntimeException;
import org.cauldron.einstein.api.model.lifecycle.exception.StartRuntimeException;
import org.cauldron.einstein.api.model.lifecycle.exception.StopRuntimeException;
import org.cauldron.einstein.api.model.profile.Profile;

/**
 * @author Neil Ellis
 */
public abstract class DelegatingInstruction extends AbstractInstruction {

    protected Instruction implementation;

    protected DelegatingInstruction(CompilationInformation debug) {
        super(debug);
    }


    @Override
    public Profile getActiveProfile() {
        return implementation.getActiveProfile();
    }


    @Override
    public Instruction getParent() {
        return implementation.getParent();
    }


    @Override
    public void setParent(Instruction parent) {
        implementation.setParent(parent);
    }


    @Override
    public void init(LifecycleContext ctx) throws InitializationRuntimeException {
        implementation.init(ctx);
    }


    @Override
    public void start(LifecycleContext ctx) throws StopRuntimeException {
        implementation.start(ctx);
    }


    @Override
    public void stop(LifecycleContext ctx) throws StartRuntimeException {
        implementation.stop(ctx);
    }


    @Override
    public void destroy(LifecycleContext ctx) throws DestructionRuntimeException {
        implementation.destroy(ctx);
    }


    public MessageTuple execute(ExecutionContext context, MessageTuple message) {
        return implementation.execute(context, message);
    }
}
