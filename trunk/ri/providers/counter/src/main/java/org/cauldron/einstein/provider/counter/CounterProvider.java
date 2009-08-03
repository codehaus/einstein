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

package org.cauldron.einstein.provider.counter;

import org.cauldron.einstein.api.common.euri.EinsteinURI;
import org.cauldron.einstein.api.common.metadata.CoreMetaData;
import org.cauldron.einstein.api.model.resource.Resource;
import org.cauldron.einstein.api.provider.ProviderMetaData;
import org.cauldron.einstein.api.provider.ResourceProvider;
import org.cauldron.einstein.api.provider.annotation.RegisterProvider;
import org.cauldron.einstein.api.provider.facade.*;
import org.cauldron.einstein.ri.core.providers.base.AbstractProvider;
import org.contract4j5.contract.Contract;


/**
 * @author Neil Ellis
 */
 @RegisterProvider(name = "counter",
        metadata =
        @ProviderMetaData(
                core = @CoreMetaData(
                        name = "counter",
                        shortDescription = "Counts between two values with an optional step",
                        description = "Supports the providers supplied by the Camel ESB.",
                        syntax = "'camel:' <start> ':' <end> [ ':' <step> ]",
                        example = " while \"counter:0:10:2\" { ... }; "

                ),
                alwaysSupported = {ReadFacade.class, WriteFacade.class, BrowseFacade.class, BooleanQueryFacade.class,  ListenFacade.class}
        ))
 @Contract
public class CounterProvider extends AbstractProvider implements ResourceProvider {
    public Resource getLocalResource( EinsteinURI uri, Class[] facades) {
        return new CounterResource(uri);
    }
}
