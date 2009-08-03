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
import org.cauldron.einstein.api.model.resource.Facade;
import org.cauldron.einstein.api.provider.facade.ExecuteFacade;
import org.cauldron.einstein.api.provider.facade.ReadFacade;
import org.cauldron.einstein.api.provider.facade.WriteFacade;
import org.cauldron.einstein.api.provider.facade.exception.FacadeNotSupportedRuntimeException;
import org.cauldron.einstein.api.provider.facade.exception.ReadFailureRuntimeException;
import static org.cauldron.einstein.ri.core.CoreModuleMessages.BUNDLE_NAME;
import static org.cauldron.einstein.ri.core.CoreModuleMessages.COULD_NOT_CREATE_INSTANCE;
import org.cauldron.einstein.ri.core.providers.base.AbstractResource;

import java.util.Arrays;
import java.util.List;

/**
 * @author Neil Ellis
 */

@org.contract4j5.contract.Contract class JavaResource extends AbstractResource {

    public JavaResource(EinsteinURI uri, Class<? extends Facade>[] facades) {
        super(uri);
        List<Class<? extends Facade>> facadeList = Arrays.asList(facades);
        Object instance;
        try {
            Class<?> urlClass = getClass().getClassLoader().loadClass(uri.getDescriptor().asString());
            instance = urlClass.newInstance();
        }
        catch (ClassNotFoundException e) {
            throw new ReadFailureRuntimeException(e,
                                                  BUNDLE_NAME,
                                                  COULD_NOT_CREATE_INSTANCE,
                                                  uri.getDescriptor().asString());
        } catch (IllegalAccessException e) {
            throw new ReadFailureRuntimeException(e,
                                                  BUNDLE_NAME,
                                                  COULD_NOT_CREATE_INSTANCE,
                                                  uri.getDescriptor().asString());
        } catch (InstantiationException e) {
            throw new ReadFailureRuntimeException(e,
                                                  BUNDLE_NAME,
                                                  COULD_NOT_CREATE_INSTANCE,
                                                  uri.getDescriptor().asString());
        }
        if (facadeList.contains(ReadFacade.class)) {
            if (!(instance instanceof ReadFacade)) {
                addMapping(ReadFacade.class, new JavaReadFacade(instance));
            }
        }
        JavaWriteOrExecuteFacade sendOrExecuteFacade = new JavaWriteOrExecuteFacade(instance);
        if (facadeList.contains(WriteFacade.class)) {
            if (!(instance instanceof WriteFacade)) {
                addMapping(WriteFacade.class, sendOrExecuteFacade);
            }
        }
        if (facadeList.contains(ExecuteFacade.class)) {
            if (!(instance instanceof ExecuteFacade)) {
                addMapping(ExecuteFacade.class, sendOrExecuteFacade);
            }
        }
        for (Class<? extends Facade> aClass : facadeList) {
            if (aClass.isAssignableFrom(instance.getClass())) {
                addMapping((Class<Facade>) aClass, (Facade) instance);
            } else {
                if (!hasMapping(aClass)) {
                    throw new FacadeNotSupportedRuntimeException(aClass, JavaProvider.class, uri);
                }
            }
        }
    }
}