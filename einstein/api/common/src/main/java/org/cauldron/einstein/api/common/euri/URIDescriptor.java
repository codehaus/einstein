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

package org.cauldron.einstein.api.common.euri;


import org.cauldron.einstein.api.common.euri.exception.MalformedEinsteinURIRuntimeException;
import org.contract4j5.contract.Post;

import java.net.URL;

/**
 * The descriptor part of an Einstein URI can have many possible values, it could be another nested URI (as in
 * "cache:java:me.my.Thing"), a pure string value (as in "text:This is my arbitrary text ....") a valid conventional URL
 * (as in "camel:http://myserver.com"), a path (as in "jms:/myPath"). Of course these possible types overlap and the
 * descriptor portion can be viewed as more than one type.
 *
 * @author Neil Ellis
 * @throws org.cauldron.einstein.api.common.euri.exception.MalformedEinsteinURIRuntimeException if you attempt to convert a descriptor into a format that it cannot be parsed
 * into.
 */
public interface URIDescriptor {


    /**
     * Returns the descriptor as a nested URI.
     *
     * @return the descriptor as a {@link EinsteinURI}.
     *
     * @throws org.cauldron.einstein.api.common.euri.exception.MalformedEinsteinURIRuntimeException if the descriptor is not a valid {@link EinsteinURI}.
     */
    @Post
    EinsteinURI asEinsteinURI() throws MalformedEinsteinURIRuntimeException;

    /**
     * Returns the descriptor as a conventional {@link URL}.
     *
     * @return the descriptor as a {@link URL}.
     *
     * @throws org.cauldron.einstein.api.common.euri.exception.MalformedEinsteinURIRuntimeException if the descriptor is not a valid {@link URL}.
     */
    @Post
    URL asURL() throws MalformedEinsteinURIRuntimeException;

    /**
     * Returns the descriptor as a {@link String}.
     *
     * @return the descriptor as a {@link String}.
     */
    @Post
    String asString();

    /**
     * Returns the descriptor as a {@link URLLocation}.
     *
     * @return the descriptor as a {@link URLLocation}
     *
     * @throws org.cauldron.einstein.api.common.euri.exception.MalformedEinsteinURIRuntimeException ikf the descriptor is not a valid {@link URLLocation}.
     */
    @Post
    URLLocation asURLLocation() throws MalformedEinsteinURIRuntimeException;

    /**
     * Returns the descriptor as a {@link URLPath}.
     *
     * @return the descriptor as a {@link URLPath}
     *
     * @throws org.cauldron.einstein.api.common.euri.exception.MalformedEinsteinURIRuntimeException ikf the descriptor is not a valid {@link URLPath}.
     */
    @Post
    URLPath asPath() throws MalformedEinsteinURIRuntimeException;

}
