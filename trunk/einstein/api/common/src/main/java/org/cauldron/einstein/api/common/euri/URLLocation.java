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

/**
 * The location part of an {@link EinsteinURI}. This contains all the non-protocol parts of a conventional {@link
 * java.net.URL}
 *
 * @author Neil Ellis
 */
@Contract
public interface URLLocation {

    /**
     * Returns the server (and port) part of the URL if it was not supplied then null will be returned.
     *
     * @return the server (and port) or null
     */
     @Post("(!$this.hasServer()) || ($return != null)")
    URLServer getServer();

    /**
     * Returns the username and password supplied in the URL or null if not supplied.
     *
     * @return the username/password part.
     */
    @Post("(!$this.hasCredentials()) || ($return != null)")
    URLCredentials getCredentials();

    /**
     * Returns the path part of the URL (i.e. everything past the server and before the query) or null if not supplied.
     *
     * @return the
     */
    @Post("(!$this.hasPath()) || ($return != null)")
    URLPath getPath();

    /**
     * return the query
     */
    @Post("(!$this.hasQuery()) || ($return != null)")
    URLQuery getQuery();

    boolean hasCredentials();

    boolean hasPath();

    boolean hasServer();

    boolean hasQuery();

}
