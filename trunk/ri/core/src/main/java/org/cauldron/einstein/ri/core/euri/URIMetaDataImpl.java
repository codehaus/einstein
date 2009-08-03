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

import org.cauldron.einstein.api.common.euri.URIMetaData;

import java.util.Map;
import java.util.Properties;

/**
 * @author Neil Ellis
 */

@org.contract4j5.contract.Contract
public class URIMetaDataImpl implements URIMetaData {

    private final Properties props;

    public URIMetaDataImpl(Properties props) {
        this.props = props;
    }


    public Properties asProperties() {
        return props;
    }


    public boolean isEmpty() {
        return props == null || props.isEmpty();
    }


    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for (Map.Entry entry : props.entrySet()) {
            buffer.append(entry.getKey()).append('=');
            Object value = entry.getValue();
            if (value != null) {
                buffer.append(value);
            }
            buffer.append(',');
        }
        if (buffer.charAt(buffer.length() - 1) == ',') {
            buffer.deleteCharAt(buffer.length() - 1);
        }
        return buffer.toString();
    }
}
