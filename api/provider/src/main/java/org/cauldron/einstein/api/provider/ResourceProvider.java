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

package org.cauldron.einstein.api.provider;


import org.cauldron.einstein.api.common.euri.EinsteinURI;
import org.cauldron.einstein.api.model.resource.Resource;
import org.contract4j5.contract.Contract;
import org.contract4j5.contract.Post;
import org.contract4j5.contract.Pre;

/**
 * A Provider is a something that provides a set of common functionality for Einstein throw a set of {@link
 * org.cauldron.einstein.api.model.resource.Facade}s and is configured by an {@link EinsteinURI}. It is likely that you
 * would wish to make a provider self registering with the {@link org.cauldron.einstein.api.provider.annotation.RegisterProvider}
 * annotation.
 *
 * @author Neil Ellis
 */
@Contract
public interface ResourceProvider {

    /**
     * This resource will be used in a single thread of execution.
     */
    @Pre
    @Post
    Resource getLocalResource(EinsteinURI uri, Class[] facades);


    /**
     * This resource will ne shared across multiple threads and possibly nodes, it must be thread safe and serializable
     */
    @Pre
    @Post
    Resource getSystemResource(EinsteinURI uri, Class[] facades);

    /**
     * This resource will be shared amongst multiple threads, so must be thread safe.
     */
    @Pre
    @Post
    Resource getStaticResource(EinsteinURI uri, Class[] facades);

    /**
     * Executed by the compiler, to check for compiler time URI errors.
     * @param uri the URI to be syntax checked.
     *
     * @throws ResourceParseException if the uri supplied is not valid for this provider.
     */
    @Pre
    void parse(EinsteinURI uri) throws ResourceParseException;


}
