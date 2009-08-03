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


import org.contract4j5.contract.Post;

import java.util.Properties;

/**
 * The URIMetaData provides basic name value pair configuration of a provider. E.g. : <br/>
 * <pre>
 * "dynamic(lang=groovy):'http://'+System.getProperties(\"serverName\")"
 * </pre>
 * <p/>
 * In the above example it's the (lang=groovy) part that is the meta data.
 *
 * @author Neil Ellis
 */
public interface URIMetaData {

    /**
     * Return the meta data properties as a {@link Properties} object.
     *
     * @return the meta data as {@link Properties}.
     */
    @Post
    Properties asProperties();

    /**
     * Returns true if there is no meta data.
     *
     * @return true if there is no meta data.
     */
    boolean isEmpty();
}
