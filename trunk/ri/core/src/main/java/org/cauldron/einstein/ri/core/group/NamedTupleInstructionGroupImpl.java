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
import org.cauldron.einstein.api.assembly.group.NamedTupleInstructionGroup;
import org.cauldron.einstein.api.assembly.instruction.Instruction;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.model.execution.Executable;
import org.cauldron.einstein.api.model.execution.ExecutionContext;
import org.cauldron.einstein.api.model.execution.Executor;
import org.cauldron.einstein.api.model.lifecycle.LifecycleContext;
import org.cauldron.einstein.api.model.profile.Profile;
import static org.cauldron.einstein.ri.core.CoreModuleMessages.*;
import org.cauldron.einstein.ri.core.exception.InvalidInstructionRuntimeException;
import org.cauldron.einstein.ri.core.instructions.AbstractInstruction;
import org.cauldron.einstein.ri.core.instructions.ProfileNotFoundRuntimeException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class NamedTupleInstructionGroupImpl extends AbstractInstruction implements NamedTupleInstructionGroup,
                                                                                   ExecutionContext {

    private final Map<Object, Instruction> labelledInstructions = new LinkedHashMap<Object, Instruction>();
    private Profile profile;
    private Executor executor;


    public NamedTupleInstructionGroupImpl(CompilationInformation debug, Object... parameters) {
        super(debug);
        Object key = null;
        boolean even = true;
        for (Object parameter : parameters) {
            if (even) {
                even = false;
                key = parameter;
            } else {
                even = true;
                if (parameter instanceof Instruction) {
                    Instruction instruction = (Instruction) parameter;
                    instruction.setParent(this);
                    if (key != null) {
                        labelledInstructions.put(key, instruction);
                    } else {
                        throw new InvalidInstructionRuntimeException(BUNDLE_NAME,
                                                                     NO_LABEL_FOR_INSTRUCTION,
                                                                     instruction.getPositionDescription());
                    }
                    key = null;
                } else {
                    throw new InvalidInstructionRuntimeException(BUNDLE_NAME,
                                                                 EXPECTED_INSTRUCTION,
                                                                 parameter.getClass().getCanonicalName());
                }
            }
        }
    }


    public Executable getExecutable(Object key) {
        return labelledInstructions.get(key);
    }


    public MessageTuple execute(ExecutionContext context, MessageTuple message) {
        getState().assertStarted();
        return executor.execute(context, message);
    }


    @Override
    public void init(LifecycleContext context) {
        super.init(context);
        for (Instruction instruction : labelledInstructions.values()) {
            instruction.init(context);
        }

        executor = getActiveProfile().getExecutionModel().getMapExecutor(labelledInstructions);
        executor.init(context);
    }


    @Override
    public void start(LifecycleContext context) {
        super.start(context);
        for (Instruction instruction : labelledInstructions.values()) {
            instruction.start(context);
        }
        executor.start(context);
    }


    @Override
    public void stop(LifecycleContext context) {
        super.stop(context);
        for (Instruction instruction : labelledInstructions.values()) {
            instruction.stop(context);
        }
        executor.stop(context);
    }


    @Override
    public void destroy(LifecycleContext context) {
        super.destroy(context);
        for (Instruction instruction : labelledInstructions.values()) {
            instruction.destroy(context);
        }
    }


    public List<Executable> getExecutables() {
        ArrayList<Executable> list = new ArrayList<Executable>();
        list.addAll(labelledInstructions.values());
        return list;
    }


    @Override public Profile getActiveProfile() {
        if (profile == null) {
            if (getParent() == null) {
                throw new ProfileNotFoundRuntimeException();
            }
            return getParent().getActiveProfile();
        } else {
            return profile;
        }
    }

    public void setProfile(Profile profile) {
        getState().assertUnInited();
        this.profile = profile;


    }
}