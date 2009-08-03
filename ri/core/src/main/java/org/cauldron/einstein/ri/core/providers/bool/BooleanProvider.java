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

package org.cauldron.einstein.ri.core.providers.bool;

import org.cauldron.einstein.api.common.euri.EinsteinURI;
import org.cauldron.einstein.api.common.metadata.CoreMetaData;
import org.cauldron.einstein.api.model.resource.Resource;
import org.cauldron.einstein.api.provider.ProviderMetaData;
import org.cauldron.einstein.api.provider.annotation.RegisterProvider;
import org.cauldron.einstein.api.provider.facade.BooleanQueryFacade;
import org.cauldron.einstein.ri.core.providers.base.AbstractProvider;

/**
 * @author Neil Ellis
 */
@RegisterProvider(name = "bool",
                  metadata = @ProviderMetaData(
                          core = @CoreMetaData(
                                  name = "bool",
                                  shortDescription = "Support for boolean true and false.",
                                  description = "This provides the ability to use simple boolean constructs for queries; correct values for the descrtiptor are true and false.",
                                  syntax = "'bool:' ( 'true' | 'false' )",
                                  example = "while \"bool:true\" { ... };"
                          ), alwaysSupported = {BooleanQueryFacade.class}
                  )
)

@org.contract4j5.contract.Contract
public class BooleanProvider extends AbstractProvider {

    public Resource getLocalResource(EinsteinURI uri, Class[] facades) {
        return new BooleanResource(uri);
    }
}
