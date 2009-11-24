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

package org.cauldron.einstein.ri.core.providers;

import org.cauldron.einstein.api.model.lifecycle.LifecycleContext;
import org.cauldron.einstein.api.model.lifecycle.exception.DestructionRuntimeException;
import org.cauldron.einstein.api.model.lifecycle.exception.InitializationRuntimeException;
import org.cauldron.einstein.api.model.lifecycle.exception.StartRuntimeException;
import org.cauldron.einstein.api.model.lifecycle.exception.StopRuntimeException;
import org.cauldron.einstein.api.model.resource.Facade;
import org.cauldron.einstein.ri.core.lifecycle.LifecycleStateMachine;
import org.cauldron.einstein.ri.core.log.Logger;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class AbstractFacade implements Facade {
    private static final Logger log = Logger.getLogger(AbstractFacade.class);

    private final LifecycleStateMachine state = new LifecycleStateMachine();


    public void start(LifecycleContext ctx) throws StartRuntimeException {
        state.start();
        log.debug("{0} starting.", getClass());
    }


    public void init(LifecycleContext ctx) throws InitializationRuntimeException {
        state.init();
        log.debug("{0} inititalizing.", getClass());
    }


    public void stop(LifecycleContext ctx) throws StopRuntimeException {
        state.stop();
        log.debug("{0} stopping.", getClass());
    }


    public void destroy(LifecycleContext ctx) throws DestructionRuntimeException {
        state.destroy();
        log.debug("{0} destroying.", getClass());
    }

    protected void assertStarted() {
        state.assertStarted();
    }
}
