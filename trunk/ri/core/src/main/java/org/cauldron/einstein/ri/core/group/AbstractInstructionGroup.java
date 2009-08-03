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
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.model.execution.Executable;
import org.cauldron.einstein.api.model.execution.ExecutionContext;
import org.cauldron.einstein.api.model.execution.Executor;
import org.cauldron.einstein.api.model.lifecycle.LifecycleContext;
import org.cauldron.einstein.api.model.profile.Profile;
import org.cauldron.einstein.ri.core.instructions.AbstractInstruction;
import org.cauldron.einstein.ri.core.instructions.ProfileNotFoundRuntimeException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Neil Ellis
 */
abstract class AbstractInstructionGroup extends AbstractInstruction implements InstructionGroup, ExecutionContext {

    private List<Instruction> instructions;
    private Profile profile;
    private Executor executor;


    AbstractInstructionGroup(CompilationInformation debug) {
        super(debug);
    }


    AbstractInstructionGroup(CompilationInformation debug, Instruction... instructions) {
        super(debug);
        this.setInstructions(Arrays.asList(instructions));
        setParents(instructions);
    }


    void setInstructions(List<Instruction> instructions) {
        this.instructions = instructions;
    }


    void setParents(Instruction... instructions) {
        for (Instruction instruction : instructions) {
            instruction.setParent(this);
        }
    }


    AbstractInstructionGroup(CompilationInformation debug, Profile profile, Instruction... instructions) {
        super(debug);
        this.profile = profile;
        this.setInstructions(Arrays.asList(instructions));
        setParents(instructions);
    }


    public final MessageTuple execute(ExecutionContext context, MessageTuple message) {
        assertStarted();
        if (profile != null && profile.getDataModel() != null && getParent() != null) {
            if (profile.getDataModel().getClass() != getParent().getActiveProfile().getDataModel().getClass()) {
                message = message.realiseAsOne().duplicateWithNewDataModel(profile.getDataModel().getClass());
            }
        }
        return doExecute(context, message);
    }


    MessageTuple doExecute(ExecutionContext context, MessageTuple message) {
        return getExecutor().execute(this, message);
    }


    @Override
    public final void init(LifecycleContext context) {
        super.init(context);
        //TODO: Hack until systems are written, remove this when no longer relevant.
        if (profile == null && getParent() == null) {
            profile = context.getActiveProfile();
        }
        //We have to explicitly initialize all instructions before we init the executor.
        //this is not required for start and stop.
        for (Instruction instruction : getInstructions()) {
            instruction.init(context);
        }
        initExecutor();
        getExecutor().init(context);
    }


    final List<Instruction> getInstructions() {
        return Collections.unmodifiableList(instructions);
    }


    abstract void initExecutor();

    @Override
    public void start(LifecycleContext ctx) {
        super.start(ctx);
        for (Instruction instruction : getInstructions()) {
            instruction.start(ctx);
        }
        getExecutor().start(ctx);
    }


    @Override
    public final void stop(LifecycleContext ctx) {
        super.stop(ctx);
        for (Instruction instruction : getInstructions()) {
            instruction.stop(ctx);
        }
        getExecutor().stop(ctx);
    }


    @Override
    public final void destroy(LifecycleContext ctx) {
        super.destroy(ctx);
        for (Instruction instruction : getInstructions()) {
            instruction.destroy(ctx);
        }
        getExecutor().destroy(ctx);
    }


    @Override
    public final Profile getActiveProfile() {
        if (profile == null) {
            if (getParent() == null) {
                throw new ProfileNotFoundRuntimeException();
            }
            return getParent().getActiveProfile();
        } else {
            return profile;
        }
    }


    public final void setProfile(Profile profile) {
        getState().assertUnInited();
        this.profile = profile;
    }


    public final List<Executable> getExecutables() {
        ArrayList<Executable> list = new ArrayList<Executable>();
        list.addAll(getInstructions());
        return list;
    }


    Executor getExecutor() {
        return executor;
    }


    void setExecutor(Executor executor) {
        this.executor = executor;
    }
}
