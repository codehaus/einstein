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

package org.cauldron.einstein.ri.core.resource;


import org.cauldron.einstein.api.assembly.debug.CompilationInformation;
import org.cauldron.einstein.api.common.euri.EinsteinURI;
import org.cauldron.einstein.api.model.lifecycle.LifecycleContext;
import org.cauldron.einstein.api.model.lifecycle.exception.DestructionRuntimeException;
import org.cauldron.einstein.api.model.lifecycle.exception.InitializationRuntimeException;
import org.cauldron.einstein.api.model.lifecycle.exception.StartRuntimeException;
import org.cauldron.einstein.api.model.lifecycle.exception.StopRuntimeException;
import org.cauldron.einstein.api.model.resource.Resource;
import org.cauldron.einstein.api.model.resource.ResourceContext;
import org.cauldron.einstein.api.model.resource.ResourceRef;
import org.cauldron.einstein.ri.core.euri.EinsteinURIFactory;
import org.cauldron.einstein.ri.core.lifecycle.LifecycleStateMachine;
import org.cauldron.einstein.ri.core.providers.factory.ProviderFactory;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class StaticResourceRef implements ResourceRef {

    private final EinsteinURI uri;
    private Resource resource;
    private final LifecycleStateMachine state = new LifecycleStateMachine();
    private final Class[] facades;


    public StaticResourceRef(String uri, Class[] facades) {
        this.facades = facades;
        this.uri = EinsteinURIFactory.createURI(uri);
    }


    public StaticResourceRef(CompilationInformation compilationInformation, String uri, Class[] facades) {
        CompilationInformation compilationInformation1 = compilationInformation;
        this.facades = facades;
        this.uri = EinsteinURIFactory.createURI(uri);
    }


    public Resource getResource(ResourceContext context) {
        state.assertNotUnInited();
        return resource;
    }


    public EinsteinURI getURI() {
        return uri;
    }


    public void init(LifecycleContext ctx) throws InitializationRuntimeException {
        state.init();
        resource = ProviderFactory.getInstance()
                .getRegisteredProvider(uri.getProviderName())
                .getStaticResource(uri, facades);
        resource.init(ctx);
    }


    public void start(LifecycleContext ctx) throws StopRuntimeException {
        state.start();
        resource.start(ctx);
    }


    public void stop(LifecycleContext ctx) throws StartRuntimeException {
        state.stop();
        resource.stop(ctx);
    }


    public void destroy(LifecycleContext ctx) throws DestructionRuntimeException {
        state.destroy();
        resource.destroy(ctx);
    }
}
