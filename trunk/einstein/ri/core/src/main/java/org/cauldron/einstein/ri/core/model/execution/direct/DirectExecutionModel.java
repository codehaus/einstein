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

package org.cauldron.einstein.ri.core.model.execution.direct;

import org.cauldron.einstein.api.model.ExecutionModel;
import org.cauldron.einstein.api.model.execution.Executable;
import org.cauldron.einstein.api.model.execution.ExecutionScopeManager;
import org.cauldron.einstein.api.model.execution.Executor;
import org.cauldron.einstein.ri.core.model.execution.direct.executors.*;

import java.util.List;
import java.util.Map;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class DirectExecutionModel implements ExecutionModel {

    private final DirectExcecutionScopeManager directExcecutionScopeManager;


    public DirectExecutionModel() {
        directExcecutionScopeManager = new DirectExcecutionScopeManager();
    }


    public Executor getCompetingExecutor(final List<? extends Executable> executables) {
        return new DirectCompetingExecutor(executables);
    }


    public Executor getIteratingExecutor(final Executable executable) {
        return new DirectIteratingExecutor(executable);
    }


    public Executor getMapExecutor(final Map<?, ? extends Executable> instructions) {
        return new DirectMapExecutor(instructions);
    }


    public ExecutionScopeManager getScopeManager() {
        return directExcecutionScopeManager;
    }


    public Executor getSequentialExecutor(final List<? extends Executable> executors) {
        return new DirectSequentialExecutor(executors);
    }


    public Executor getSingleInstructionExecutor(final Executable executable) {
        return new DirectSingleExecutionExecutor(executable);
    }


    public Executor getTupleExecutor(final List<? extends Executable> executors) {
        return new DirectTupleExecutor(executors);
    }
}
