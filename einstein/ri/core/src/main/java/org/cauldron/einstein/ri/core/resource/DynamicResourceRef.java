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
import org.cauldron.einstein.api.model.execution.ExecutionScope;
import org.cauldron.einstein.api.model.lifecycle.LifecycleContext;
import org.cauldron.einstein.api.model.lifecycle.exception.DestructionRuntimeException;
import org.cauldron.einstein.api.model.lifecycle.exception.InitializationRuntimeException;
import org.cauldron.einstein.api.model.lifecycle.exception.StartRuntimeException;
import org.cauldron.einstein.api.model.lifecycle.exception.StopRuntimeException;
import org.cauldron.einstein.api.model.resource.Resource;
import org.cauldron.einstein.api.model.resource.ResourceContext;
import org.cauldron.einstein.api.model.resource.ResourceRef;
import org.cauldron.einstein.api.model.resource.exception.ResourceNotFoundRuntimeException;
import org.cauldron.einstein.ri.core.euri.EinsteinURIFactory;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class DynamicResourceRef implements ResourceRef {

    private final String name;
    private String uri;


    public DynamicResourceRef(String name) {
        this.name = name;
    }


    public DynamicResourceRef(CompilationInformation compilationInformation, String name, String uri) {
        CompilationInformation compilationInformation1 = compilationInformation;
        this.name = name;
        this.uri = uri;
    }


    public Resource getResource(ResourceContext context) {
        ExecutionScope scope = context.getActiveProfile()
                .getExecutionModel()
                .getScopeManager()
                .getScope(context.getMessage().getExecutionCorrelation());
        Resource resource = scope.getResource(name);
        if (resource == null) {
            throw new ResourceNotFoundRuntimeException(name,
                                                       uri,
                                                       scope.getFormattedDebugInfo(),
                                                       context.getMessage()
                                                               .getCorrelationParticipant().getFormattedDebugInfo());
        }
        return resource;
    }


    public EinsteinURI getURI() {
        return EinsteinURIFactory.createURI(uri);
    }


    public void init(LifecycleContext ctx) throws InitializationRuntimeException {
    }


    public void start(LifecycleContext ctx) throws StopRuntimeException {
    }


    public void stop(LifecycleContext ctx) throws StartRuntimeException {
    }


    public void destroy(LifecycleContext ctx) throws DestructionRuntimeException {
    }
}