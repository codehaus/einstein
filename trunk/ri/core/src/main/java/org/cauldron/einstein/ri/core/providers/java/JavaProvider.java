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

package org.cauldron.einstein.ri.core.providers.java;

import org.cauldron.einstein.api.common.euri.EinsteinURI;
import org.cauldron.einstein.api.common.metadata.CoreMetaData;
import org.cauldron.einstein.api.model.resource.Resource;
import org.cauldron.einstein.api.provider.ProviderMetaData;
import org.cauldron.einstein.api.provider.annotation.RegisterProvider;
import org.cauldron.einstein.api.provider.facade.ExecuteFacade;
import org.cauldron.einstein.api.provider.facade.ReadFacade;
import org.cauldron.einstein.api.provider.facade.WriteFacade;
import org.cauldron.einstein.ri.core.providers.base.AbstractProvider;

/**
 * @author Neil Ellis
 */

@RegisterProvider(name = "java",
                  metadata = @ProviderMetaData(
                          core = @CoreMetaData(
                                  name = "java",
                                  shortDescription = "A Provider for java classes.",
                                  description = "A Provider for java classes.",
                                  syntax = "'java:' <class-name> [ '?' ( <property>'='<value> ) ]",
                                  example = "execute \"java:org.me.MyService?initialValue=10\";"
                          ),
                          alwaysSupported = {ReadFacade.class, WriteFacade.class, ExecuteFacade.class},
                          optionallySupportsAll = true
                  )
)
@org.contract4j5.contract.Contract
public class JavaProvider extends AbstractProvider {

    public Resource getLocalResource(EinsteinURI uri, Class[] facades) {
        return new JavaResource(uri, facades);
    }
}
