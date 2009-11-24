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

package org.cauldron.einstein.ri.core.registry;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.cauldron.einstein.api.config.registry.EinsteinRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Neil Ellis
 */

@org.contract4j5.contract.Contract
public class EinsteinHashMapRegistry implements EinsteinRegistry

{

    //private static Logger log = Logger.getLogger(EinsteinHashMapRegistry.class);
    private final ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<String, Object>();
    private final HashMap tags = new HashMap();

    public void applyTag(String path, String tag) {
        List tagList = (List) tags.get(path);
        if (tagList == null) {
            tagList = new ArrayList();
            tags.put(path, tagList);
        }
        tagList.add(tag);
    }


    public void delete(String path) {
        map.remove(path);
        tags.remove(path);
    }


    public void destroy() {
        map.clear();
        tags.clear();
    }


    public Object get(String path) {
        return map.get(path);
    }


    public List getAll(String path) {
        List<Object> results = new ArrayList<Object>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getKey().startsWith(path)) {
                results.add(entry.getValue());
            }
        }
        return results;
    }


    public void init() {
        //TODO
    }


    public void put(String path, Object o) {
        map.put(path, o);
    }


    public void removeTag(String path, String tag) {
        List tagList = (List) tags.get(path);
        if (tagList != null) {
            tagList.remove(tag);
        }
    }


    public void rename(String path, String newPath) {
        map.put(newPath, map.get(path));
    }


    public boolean resourceExists(String path) {
        return map.containsKey(path);
    }


    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
