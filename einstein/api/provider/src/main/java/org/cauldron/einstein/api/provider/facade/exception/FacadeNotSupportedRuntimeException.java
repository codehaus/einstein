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

package org.cauldron.einstein.api.provider.facade.exception;

import mazz.i18n.annotation.I18NMessage;
import mazz.i18n.annotation.I18NResourceBundle;
import org.cauldron.einstein.api.common.euri.EinsteinURI;
import org.cauldron.einstein.api.common.exception.EinsteinRuntimeException;
import org.cauldron.einstein.api.model.resource.Facade;
import org.cauldron.einstein.api.provider.ProviderMetaData;
import org.cauldron.einstein.api.provider.ResourceProvider;
import org.cauldron.einstein.api.provider.annotation.RegisterProvider;
import org.contract4j5.contract.Pre;

import java.util.Arrays;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
@I18NResourceBundle(baseName = "provider-api")
public final class FacadeNotSupportedRuntimeException extends EinsteinRuntimeException {

    @I18NMessage("Facade {0} is not supported by provider {1} for URI {2}. Provider always supports {3}, optionally supports {4}.")
    public static final String FACADE_NOT_SUPPORTED = "facade.not.supported";

    @Pre
    public FacadeNotSupportedRuntimeException(Class<? extends Facade> clazz,  Class<? extends ResourceProvider> providerClass, EinsteinURI uri) {
        super("provider-api", FACADE_NOT_SUPPORTED, clazz,  providerClass, uri, extractMetadata(providerClass).supportsAll() ? "all" : Arrays.asList(extractMetadata(providerClass).alwaysSupported()), extractMetadata(providerClass).optionallySupportsAll() ? "all" : Arrays.asList(extractMetadata(providerClass).optionallySupported()));
    }

    private static ProviderMetaData extractMetadata(Class<? extends ResourceProvider> providerClass) {
        return providerClass.getAnnotation(RegisterProvider.class).metadata();
    }
}
