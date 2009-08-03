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

package org.cauldron.einstein.ri.core.instructions.context;

import org.cauldron.einstein.api.message.MessageModel;
import org.cauldron.einstein.api.model.execution.ExecutionContext;
import org.cauldron.einstein.api.model.lifecycle.LifecycleContext;
import org.cauldron.einstein.api.model.profile.Profile;
import org.cauldron.einstein.api.provider.ProviderContext;
import org.cauldron.einstein.api.provider.facade.context.QueryContext;
import org.cauldron.einstein.api.provider.facade.context.RoutingContext;
import org.cauldron.einstein.api.provider.facade.context.URIExecutionContext;
import org.cauldron.einstein.api.provider.facade.context.WriteContext;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class ProfileAwareContext implements RoutingContext, ProviderContext, ExecutionContext, WriteContext,
                                            URIExecutionContext, QueryContext, LifecycleContext {

    private final Profile profile;

    public ProfileAwareContext(Profile profile) {
        this.profile = profile;
    }


    public Profile getActiveProfile() {
        return profile;
    }


    public MessageModel getMessageFactory() {
        return profile.getMessageModel();
    }
}