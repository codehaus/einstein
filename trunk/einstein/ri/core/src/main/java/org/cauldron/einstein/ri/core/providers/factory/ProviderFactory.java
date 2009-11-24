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

package org.cauldron.einstein.ri.core.providers.factory;

import org.cauldron.einstein.api.model.resource.exception.ProviderNotFoundRuntimeException;
import org.cauldron.einstein.api.provider.ResourceProvider;
import org.cauldron.einstein.api.provider.annotation.RegisterProvider;
import org.cauldron.einstein.ri.core.log.Logger;
import org.cauldron.einstein.ri.core.registry.EinsteinRegistryFactory;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class ProviderFactory {

    private static final ProviderFactory instance = new ProviderFactory();
    private final Logger log = Logger.getLogger(ProviderFactory.class);

    private ProviderFactory() {
    }


    public static ProviderFactory getInstance() {
        return instance;
    }


    public ResourceProvider getRegisteredProvider(String providerName) throws ProviderNotFoundRuntimeException {
        ResourceProvider provider = (ResourceProvider) EinsteinRegistryFactory.getInstance()
                .get(RegisterProvider.PATH + "/" + providerName);
        if (provider != null) {
            return provider;
        } else {
            log.error("Provider not found, provide factory is {0}.", EinsteinRegistryFactory.getInstance());
            throw new ProviderNotFoundRuntimeException(providerName);
        }
    }
}
