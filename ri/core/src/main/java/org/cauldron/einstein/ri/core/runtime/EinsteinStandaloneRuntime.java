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

package org.cauldron.einstein.ri.core.runtime;

import org.cauldron.einstein.api.message.data.rosetta.RosettaStone;
import org.cauldron.einstein.api.runtime.EinsteinRuntime;
import org.cauldron.einstein.api.runtime.Serializer;
import org.cauldron.einstein.api.runtime.exception.EinsteinRuntimeRuntimeException;
import static org.cauldron.einstein.ri.core.CoreModuleMessages.BUNDLE_NAME;
import static org.cauldron.einstein.ri.core.CoreModuleMessages.RUNTIME_NOT_INITIALIZED;
import org.cauldron.einstein.ri.core.io.XStreamSerializer;
import org.cauldron.einstein.ri.core.log.Logger;
import org.cauldron.einstein.ri.core.model.rosetta.RosettaStoneImpl;
import org.cauldron.einstein.ri.core.registry.scan.RegisterClasses;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class EinsteinStandaloneRuntime implements EinsteinRuntime {

    private static final Logger log = Logger.getLogger(EinsteinStandaloneRuntime.class);

    private RosettaStoneImpl rosettaStone;
    private boolean inited;


    public EinsteinStandaloneRuntime() {
        init();
    }


    void init() {
        RegisterClasses.register();
        rosettaStone = new RosettaStoneImpl();

        log.debug("*********************************************************");
        log.debug("*********************************************************");
        log.debug("*********** Einstein Standalone Initialized *************");
        log.debug("*********************************************************");
        log.debug("*********************************************************");

        System.out
                .println(
                        "Einstein Runtime Started - Einstein is (C) 2008 Paremus Ltd & Mangala Solutions Ltd released under the Apache 2.0 and AGPL 3.0 licenses.");

        inited = true;
    }


    public void destroy() {
        //TODO
    }


    public RosettaStone getRosettaStone() {
        if (!inited) {
            throw new EinsteinRuntimeRuntimeException(BUNDLE_NAME, RUNTIME_NOT_INITIALIZED);
        }
        return rosettaStone;
    }


    public Serializer getSerializer() {
        return new XStreamSerializer();
    }


    public void start() {
        if (!inited) {
            throw new EinsteinRuntimeRuntimeException(BUNDLE_NAME, RUNTIME_NOT_INITIALIZED);
        }
        //TODO
    }


    public void stop() {
        if (!inited) {
            throw new EinsteinRuntimeRuntimeException(BUNDLE_NAME, RUNTIME_NOT_INITIALIZED);
        }
        //TODO
    }
}
