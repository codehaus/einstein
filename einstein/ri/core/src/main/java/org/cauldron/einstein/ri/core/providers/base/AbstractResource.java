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

package org.cauldron.einstein.ri.core.providers.base;

import org.cauldron.einstein.api.common.euri.EinsteinURI;
import org.cauldron.einstein.api.model.lifecycle.Lifecycle;
import org.cauldron.einstein.api.model.lifecycle.LifecycleContext;
import org.cauldron.einstein.api.model.resource.Facade;
import org.cauldron.einstein.api.model.resource.Resource;
import org.cauldron.einstein.api.model.resource.exception.OperationNotSupportedRuntimeException;
import org.cauldron.einstein.ri.core.lifecycle.LifecycleStateMachine;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Neil Ellis
 */
public abstract class AbstractResource implements Resource, Lifecycle {

    private final LifecycleStateMachine state = new LifecycleStateMachine();
    private final EinsteinURI uri;

    private final Map<Class<? extends Facade>, Facade> facadeMap = new HashMap<Class<? extends Facade>, Facade>();

    private final Map<Class<? extends Facade>, Boolean> preparedMap = new HashMap<Class<? extends Facade>, Boolean>();

    protected AbstractResource(EinsteinURI uri) {
        this.uri = uri;
    }


    public void init(LifecycleContext context) {
        state.init();
        Collection<Facade> facades = facadeMap.values();
        for (Facade facade : facades) {
            facade.init(context);
        }
    }


    public void start(LifecycleContext context) {
        state.start();
        Collection<Facade> facades = facadeMap.values();
        for (Facade facade : facades) {
            facade.start(context);
        }
    }


    public void stop(LifecycleContext context) {
        state.stop();
        Collection<Facade> facades = facadeMap.values();
        for (Facade facade : facades) {
            facade.stop(context);
        }
    }


    public void destroy(LifecycleContext context) {
        state.destroy();
        Collection<Facade> facades = facadeMap.values();
        for (Facade facade : facades) {
            facade.destroy(context);
        }
    }


    public <T extends Facade> T getFacade(Class<T> clazz) throws OperationNotSupportedRuntimeException {
        if (!isSupported(clazz)) {
            throw new OperationNotSupportedRuntimeException(this.getClass().getName(), clazz.getCanonicalName());
        }
        T facade = (T) facadeMap.get(clazz);
        if (!preparedMap.get(clazz)) {
            preparedMap.put(clazz, true);
        }
        return facade;
    }


    public <T extends Facade> boolean isSupported(Class<T> clazz) {
        return facadeMap.containsKey(clazz);
    }


    public EinsteinURI getURI() {
        return uri;
    }


    @SuppressWarnings({"UnusedDeclaration"})
    public void assertStarted() {
        state.assertStarted();
    }


    protected <T extends Facade> void addMapping(Class<T> clazz, T facade) {
        facadeMap.put(clazz, facade);
        preparedMap.put(clazz, false);
    }


    protected <T extends Facade> T getMapping(Class<T> clazz) {
        return (T) facadeMap.get(clazz);
    }


    protected boolean hasMapping(Class<? extends Facade> clazz) {
        return facadeMap.containsKey(clazz);
    }
}
