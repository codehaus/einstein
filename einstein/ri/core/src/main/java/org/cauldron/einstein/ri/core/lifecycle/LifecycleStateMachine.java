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

package org.cauldron.einstein.ri.core.lifecycle;

import static org.cauldron.einstein.ri.core.CoreModuleMessages.BUNDLE_NAME;
import static org.cauldron.einstein.ri.core.CoreModuleMessages.INVALID_LIFECYCLE_STATE;
import static org.cauldron.einstein.ri.core.lifecycle.LifecycleStateMachine.State.*;
import org.contract4j5.contract.Contract;

/**
 * @author Neil Ellis
 */
@SuppressWarnings({"UnusedDeclaration"})
@Contract
public class LifecycleStateMachine {

    private State state = UN_INIT;

    enum State {

        UN_INIT("Uninitialized"), INIT("Initialized"), STARTED("Started"), STOPPED("Stopped"), DESTROYED("Destroyed");
        private final String s;

        State(String s) {
            this.s = s;
        }


        public String toString() {
            return s;
        }
    }

    public void init() {
        assertUnInited();
        state = INIT;
    }


    public void assertUnInited() {
        if (state != UN_INIT) {
            throw new LifecycleRuntimeException(BUNDLE_NAME, INVALID_LIFECYCLE_STATE, UN_INIT, state);
        }
    }


    public void start() {
        assertInitedOrStopped();
        state = STARTED;
    }


    public void assertInitedOrStopped() {
        if (!(state == INIT || state == STOPPED)) {
            throw new LifecycleRuntimeException(BUNDLE_NAME, INVALID_LIFECYCLE_STATE, "Initialized or Stopped", state);
        }
    }


    public void stop() {
        assertStarted();
        state = STOPPED;
    }


    public void assertStarted() {
        if (state != STARTED) {
            throw new LifecycleRuntimeException(BUNDLE_NAME, INVALID_LIFECYCLE_STATE, STARTED, state);
        }
    }


    public void destroy() {
        assertNotUnInited();
        state = DESTROYED;
    }


    public void assertNotUnInited() {
        if (state == UN_INIT) {
            throw new LifecycleRuntimeException(BUNDLE_NAME,
                                                INVALID_LIFECYCLE_STATE,
                                                "Initialized, Started or Stopped",
                                                state);
        }
    }


    public void assertInited() {
        if (state != INIT) {
            throw new LifecycleRuntimeException(BUNDLE_NAME, INVALID_LIFECYCLE_STATE, INIT, state);
        }
    }


    public void asserStopped() {
        if (state != STOPPED) {
            throw new LifecycleRuntimeException(BUNDLE_NAME, INVALID_LIFECYCLE_STATE, STOPPED, state);
        }
    }


    public void assertDestroyed() {
        if (state != DESTROYED) {
            throw new LifecycleRuntimeException(BUNDLE_NAME, INVALID_LIFECYCLE_STATE, DESTROYED, state);
        }
    }
}
