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

package org.cauldron.einstein.ri.core.model.execution.direct.executors;

import org.cauldron.einstein.api.model.execution.Executor;
import org.cauldron.einstein.api.model.lifecycle.LifecycleContext;
import org.cauldron.einstein.api.model.lifecycle.exception.DestructionRuntimeException;
import org.cauldron.einstein.api.model.lifecycle.exception.InitializationRuntimeException;
import org.cauldron.einstein.api.model.lifecycle.exception.StartRuntimeException;
import org.cauldron.einstein.api.model.lifecycle.exception.StopRuntimeException;
import org.cauldron.einstein.ri.core.lifecycle.LifecycleStateMachine;

/**
 * @author Neil Ellis
 */
abstract class AbstractDirectExecutionExecutor implements Executor {

    private final LifecycleStateMachine state = new LifecycleStateMachine();


    public void start(LifecycleContext ctx) throws StartRuntimeException {
        state.start();
    }


    public void stop(LifecycleContext ctx) throws StopRuntimeException {
        state.stop();
    }


    public void init(LifecycleContext ctx) throws InitializationRuntimeException {
        state.init();
    }


    public void destroy(LifecycleContext ctx) throws DestructionRuntimeException {
        state.destroy();
    }
}
