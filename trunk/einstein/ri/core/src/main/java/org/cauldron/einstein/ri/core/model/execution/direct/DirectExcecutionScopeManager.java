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
import org.cauldron.einstein.api.message.correlation.Correlation;
import org.cauldron.einstein.api.model.execution.ExecutionContext;
import org.cauldron.einstein.api.model.execution.ExecutionScope;
import org.cauldron.einstein.api.model.execution.ExecutionScopeManager;
import static org.cauldron.einstein.ri.core.CoreModuleMessages.BUNDLE_NAME;
import static org.cauldron.einstein.ri.core.CoreModuleMessages.NO_EXECUTION_CORRELATION;
import org.cauldron.einstein.ri.core.exception.InvalidStateEinsteinRuntimeException;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class DirectExcecutionScopeManager implements ExecutionScopeManager {

    private final Map<UUID, ExecutionScope> map = new ConcurrentHashMap<UUID, ExecutionScope>();


    public ExecutionScope createNewScope(Correlation correlation, String id) {
        if (correlation == null) {
            throw new InvalidStateEinsteinRuntimeException(BUNDLE_NAME, NO_EXECUTION_CORRELATION, id);
        }
        //Set the parent scope to the existing execution scope entry for this participant.
        DirectExecutionScope scope = new DirectExecutionScope(getScope(correlation), id);
        map.put(correlation.getUUID(), scope);
        return scope;
    }


    /**
     * Search for parent correlations that have a matching scope.
     */
    public ExecutionScope getScope(Correlation correlation) {
        if (correlation != null) {
            ExecutionScope executionScope = map.get(correlation.getUUID());
            if (executionScope == null) {
                return getScope(correlation.getParent());
            }
            return executionScope;
        } else {
            return null;
        }
    }


    public void endScope(ExecutionContext ctx, ExecutionScope scope) {
        //todo: throw an exception if scope not found.
        Set<Map.Entry<UUID, ExecutionScope>> entries = map.entrySet();
        map.values();
        for (Map.Entry<UUID, ExecutionScope> entry : entries) {
            if (entry.getValue().equals(scope)) {
                ExecutionScope newScope = scope.getParent();
                if (newScope != null) {
                    map.put(entry.getKey(), newScope);
                } else {
                    map.remove(entry.getKey());
                }
            }
        }
        scope.destroy(ctx);
    }


    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
