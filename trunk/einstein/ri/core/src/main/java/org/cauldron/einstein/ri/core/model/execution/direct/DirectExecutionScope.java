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

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.cauldron.einstein.api.model.execution.ExecutionContext;
import org.cauldron.einstein.api.model.execution.ExecutionScope;
import org.cauldron.einstein.api.model.lifecycle.LifecycleContext;
import org.cauldron.einstein.api.model.resource.Resource;
import org.cauldron.einstein.ri.core.lifecycle.LifecycleStateMachine;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class DirectExecutionScope implements ExecutionScope {

    private ExecutionScope parent;
    private final Map<String, Resource> resourceMap = new HashMap<String, Resource>();
    private final String id;
    private final LifecycleStateMachine state = new LifecycleStateMachine();

    public DirectExecutionScope(ExecutionScope parent, String id) {
        this.parent = parent;
        this.id = id;
        state.init();
        state.start();
    }


    public void addResource(final ExecutionContext ctx, String name, Resource resource) {
        state.assertStarted();
        LifecycleContext lifecycleContext = LifecycleContext.SIMPLE_INSTANCE_FACTORY
                .newInstance(ctx.getActiveProfile());
        resource.init(lifecycleContext);
        resource.start(lifecycleContext);
        resourceMap.put(name, resource);
    }


    public void destroy(final ExecutionContext ctx) {
        LifecycleContext lifecycleContext = LifecycleContext.SIMPLE_INSTANCE_FACTORY
                .newInstance(ctx.getActiveProfile());
        Collection<Resource> resources = resourceMap.values();
        for (Resource resource : resources) {
            resource.stop(lifecycleContext);
            resource.destroy(lifecycleContext);
        }
        state.stop();
        state.destroy();
    }


    public String getId() {
        return id;
    }


    public ExecutionScope getParent() {
        return parent;
    }


    public Resource getResource(String name) {
        state.assertStarted();
        Resource resource = resourceMap.get(name);
        if (resource == null) {
            if (parent != null) {
                return parent.getResource(name);
            }
        }
        return resource;
    }


    public Map<String, Resource> getResourceMap() {
        return resourceMap;
    }


    public String getFormattedDebugInfo() {
        StringBuffer sb = new StringBuffer();
        ExecutionScope currScope = this;
        while (currScope != null) {
            sb.append(currScope.getId()).append("<-");
            currScope = currScope.getParent();
        }
        sb.append("\n\n");
        currScope = this;
        while (currScope != null) {
            sb.append("Contents of : ").append(currScope.getId()).append('\n');
            Map<String, Resource> map = currScope.getResourceMap();
            Set<Map.Entry<String, Resource>> entries = map.entrySet();
            for (Map.Entry<String, Resource> stringResourceEntry : entries) {
                sb.append(String.format("%-15s %-30s",
                                        stringResourceEntry.getKey(),
                                        stringResourceEntry.getValue().getURI().asString()));
            }
            currScope = currScope.getParent();
        }
        sb.append('\n');
        return sb.toString();
    }


    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
