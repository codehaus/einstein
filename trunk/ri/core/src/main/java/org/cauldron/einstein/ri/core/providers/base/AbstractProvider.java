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

package org.cauldron.einstein.ri.core.providers.base;

import org.cauldron.einstein.api.common.euri.EinsteinURI;
import org.cauldron.einstein.api.model.resource.Resource;
import org.cauldron.einstein.api.provider.ResourceParseException;
import org.cauldron.einstein.api.provider.ResourceProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Neil Ellis
 */
public abstract class AbstractProvider<R extends Resource> implements ResourceProvider {

    private final Map<EinsteinURI, R> instanceMap = new ConcurrentHashMap<EinsteinURI, R>();

    protected AbstractProvider() {

    }


    public Resource getStaticResource(EinsteinURI uri, Class[] facades) {
        if (!instanceMap.containsKey(uri)) {
            instanceMap.put(uri, (R) getLocalResource(uri, facades));
        }

        return instanceMap.get(uri);
    }


    public Resource getSystemResource(EinsteinURI uri, Class[] facades) {
        return getLocalResource(uri, facades);
    }


    public void parse(EinsteinURI uri) throws ResourceParseException {
        //TODO
    }
}
