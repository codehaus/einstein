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

package org.cauldron.einstein.api.model.resource;

import org.contract4j5.contract.*;

import org.cauldron.einstein.api.common.euri.EinsteinURI;
import org.cauldron.einstein.api.model.lifecycle.Lifecycle;
import org.cauldron.einstein.api.model.resource.exception.OperationNotSupportedRuntimeException;


/**
 * A Provider is a something that provides a set of common functionality for Einstein throw a set of {@link Facade}s and
 * is configured by an {@link org.cauldron.einstein.api.common.euri.EinsteinURI}. It is likely that you would wish to
 * make a provider self registering with the {@link org.cauldron.einstein.api.provider.annotation.RegisterProvider}
 * annotation.
 *
 * @author Neil Ellis
 */
@Contract
public interface Resource extends Lifecycle {

    /**
     * Returns true if the supplied Facade is supported by this provider.
     *
     * @param clazz the {@link Class} of the Facade.
     *
     * @return true if supported.
     */
    <T extends Facade> boolean isSupported(Class<T> clazz);

    /**
     * Returns a particular facade implementation based on the context, facade type and URI provided.
     *
     * @param clazz the {@link Class} of the facade interface that the returned value should implement.
     *
     * @return an object that supports the given facade. The returned value cannot be guaranteed thread safe, so
     *         consumers of the facade implementation should make sure it is not shared between threads.
     *
     * @throws org.cauldron.einstein.api.model.resource.exception.OperationNotSupportedRuntimeException if the facade is not supported (but you should check this first with
     *                                        {@link #isSupported(Class)}.
     */
    @Pre
            <T extends Facade> T getFacade(Class<T> clazz) throws OperationNotSupportedRuntimeException;

    EinsteinURI getURI();
}