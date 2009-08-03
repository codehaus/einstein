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

package org.cauldron.einstein.ri.core.euri;

import org.cauldron.einstein.api.common.euri.*;

/**
 * @author Neil Ellis
 */

@org.contract4j5.contract.Contract
public class URLLocationImpl implements URLLocation {

    private final URLServer server;
    private final URLCredentials credentials;
    private final URLPath path;
    private final URLQuery query;

    public URLLocationImpl(URLServer server, URLCredentials credentials, URLPath path, URLQuery query) {
        this.server = server;
        this.credentials = credentials;
        this.path = path;
        this.query = query;
    }


    public URLCredentials getCredentials() {
        return credentials;
    }


    public URLPath getPath() {
        return path;
    }


    public URLQuery getQuery() {
        return query;
    }


    public URLServer getServer() {
        return server;
    }


    public boolean hasCredentials() {
        return credentials != null;
    }


    public boolean hasPath() {
        return path != null;
    }


    public boolean hasQuery() {
        return query != null;
    }


    public boolean hasServer() {
        return server != null;
    }
}
