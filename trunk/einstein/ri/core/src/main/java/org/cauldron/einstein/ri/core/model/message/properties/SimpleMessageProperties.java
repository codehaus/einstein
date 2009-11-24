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

package org.cauldron.einstein.ri.core.model.message.properties;

import org.cauldron.einstein.api.message.MessageProperties;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class SimpleMessageProperties implements MessageProperties {

    private final Map<String, Object> map;

    public SimpleMessageProperties() {
        map = new HashMap<String, Object>();
    }


    public SimpleMessageProperties(Map<String, Object> map) {
        this.map = map;
    }


    public Object get(String name) {
        return map.get(name);
    }


    public Object put(String s, Object o) {
        return map.put(s, o);
    }


    public Object get(Object o) {
        return map.get(o);
    }


    public int size() {
        return map.size();
    }


    public boolean isEmpty() {
        return map.isEmpty();
    }


    public boolean containsKey(Object o) {
        return map.containsKey(o);
    }


    public boolean containsValue(Object o) {
        return map.containsValue(o);
    }


    public Object remove(Object o) {
        return map.remove(o);
    }


    public void putAll(Map<? extends String, ?> map) {
        this.map.putAll(map);
    }


    public void clear() {
        map.clear();
    }


    public Set<Map.Entry<String, Object>> entrySet() {
        return map.entrySet();
    }


    public Collection<Object> values() {
        return map.values();
    }


    public Set<String> keySet() {
        return map.keySet();
    }


    @SuppressWarnings({"EqualsWhichDoesntCheckParameterClass"})
    public boolean equals(Object o) {
        return map.equals(o);
    }


    public int hashCode() {
        return map.hashCode();
    }
}
