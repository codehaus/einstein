/******************************************************************************
 *                                                                            *
 *                                                                            *
 *     All works are (C) 2008- Mangala Solutions Ltd and Paremus Ltd.         *
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

package org.cauldron.einstein.ri.test;

import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.model.execution.ExecutionContext;
import org.cauldron.einstein.api.model.execution.MappedExecutableGroup;
import org.cauldron.einstein.api.model.profile.Profile;
import org.cauldron.einstein.api.provider.facade.RoutingFacade;
import org.cauldron.einstein.api.provider.facade.context.RoutingContext;
import org.cauldron.einstein.api.provider.facade.exception.RoutingFailureRuntimeException;
import org.cauldron.einstein.ri.core.providers.AbstractFacade;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class AlternatingRouter extends AbstractFacade implements RoutingFacade {

    private boolean even = true;

    public MessageTuple route(final RoutingContext context, final MessageTuple tuple, final MappedExecutableGroup group) {

        final MessageTuple[] result = new MessageTuple[]{null};
        result[0] = context.getActiveProfile().getExecutionModel().getSingleInstructionExecutor(group.getExecutables().get(even ? 0 : 1)).execute(new ExecutionContext() {
            public Profile getActiveProfile() {
                return context.getActiveProfile();
            }
        }, tuple);
        if (result[0] == null) {
            throw new RoutingFailureRuntimeException(new RuntimeException("Choice did not match."));
        }
        even = !even;
        return result[0];
    }



}