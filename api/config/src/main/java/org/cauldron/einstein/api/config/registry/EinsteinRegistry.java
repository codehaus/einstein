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

package org.cauldron.einstein.api.config.registry;

import org.contract4j5.contract.Contract;
import org.contract4j5.contract.Pre;

import java.util.List;

/**
 * A very simple registry used by Einstein to store resources.
 *
 * @author Neil Ellis
 */
@Contract
public interface EinsteinRegistry {

    String PATH_REGEX = "[a-zA-Z0-9/]*[a-zA-Z0-9]+";
    String ENTRY_REGEX = "[a-zA-Z0-9/]+";
    String TAG_REGEX = "[a-z_\\-]+";

    void init();

    @Pre("path ==~ /[a-zA-Z0-9/]*[a-zA-Z0-9]+/ && tag ==~/[a-z_\\-]+/") void applyTag(String path, String tag);

    @Pre("path ==~ /[a-zA-Z0-9/]*[a-zA-Z0-9]+/ && tag ==~/[a-z_\\-]+/") void removeTag(String path, String tag);

    @Pre("path ==~ /[a-zA-Z0-9/]*[a-zA-Z0-9]+/ && newPath ==~ /[a-zA-Z0-9/]*[a-zA-Z0-9]+/") void rename(String path,
                                                                                                        String newPath);

    @Pre("path ==~ /[a-zA-Z0-9/]*[a-zA-Z0-9]+/") Object get(String path);

    @Pre("path ==~ /[a-zA-Z0-9/]*[a-zA-Z0-9]+/") void put(String path, Object o);

    @Pre("path ==~ /[a-zA-Z0-9/]*[a-zA-Z0-9]+/") boolean resourceExists(String path);

    @Pre("path ==~ /[a-zA-Z0-9/]*[a-zA-Z0-9]+/") void delete(String path);

    void destroy();

    @Pre("path ==~ /[a-zA-Z0-9/]*[a-zA-Z0-9]+/") List getAll(String path);
}
