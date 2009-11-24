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

import org.contract4j5.contract.*;


import org.cauldron.einstein.api.common.euri.exception.MalformedEinsteinURIRuntimeException;

import java.net.URL;

/**
 * An Einstein URI is split into three parts: the provider name, metadata and the descriptor. Because URLs can be nested
 * the descriptor can be another {@link EinsteinURI}, a conventional {@link URL} (file://thing/thang), a pure path
 * (/thing/thang) or a URL location part (//server.com:2020/thing/thang?dave=crazy).
 * <p/>
 * Examples:<br/> <br/>
 * <pre>groovy:cache[ttl=3000]:http://neil:password@myserver.co.uk/scripts/CleverGroovyScript.groovy?user=neil</pre>
 * <pre>axis:http://myserver.com:8080/thing/MyService</pre>
 *
 * @author Neil Ellis
 */
@Contract
public interface EinsteinURI {

    /**
     * The provider name is what is traditionally called the protocol part of a URL. However in Einstein a URL can
     * relate to many things which are not adequately described by the term protocol. For example booleans, text, Java
     * class names and so one. So it is referred to by the more general term 'provider'.
     *
     * @return the provider part of the URI.
     */
    @Post("$return == /[a-z]+/")
    String getProviderName();


    /**
     * This returns the provider meta-data part of the URL. This allows the providers themselves to be configure. The
     * metadata part of an Einstein URI is obviously a deviatation from a traditional URI/URL.
     *
     * @return the provider meta-data.
     */
    @Post
    URIMetaData getProviderMetadata();

    /**
     * The descriptor part is the part of the URI after the colon. A URIDescriptor can actually take many forms, see
     * please read the documentation for {@link org.cauldron.einstein.api.common.euri.URIDescriptor}.
     *
     * @return the descriptor part.
     */
    @Post
    URIDescriptor getDescriptor();

    /**
     * Returns the complete URI (including all nested URIs)as a String value.
     *
     * @return the URI as a String.
     */
    @Post
    URL asURL() throws MalformedEinsteinURIRuntimeException;

    /**
     * Returns the complete URI (including all nested URIs)as a String value.
     *
     * @return the URI as a String.
     */
    @Post
    String asString();
}
