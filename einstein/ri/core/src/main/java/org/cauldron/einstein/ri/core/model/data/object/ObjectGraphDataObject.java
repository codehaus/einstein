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

package org.cauldron.einstein.ri.core.model.data.object;

import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.cauldron.einstein.api.message.data.model.*;
import org.cauldron.einstein.api.message.data.udes.UniversalDataEventStream;
import org.cauldron.einstein.api.message.data.exception.DataConversionRuntimeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class ObjectGraphDataObject implements DataObject {


    private final Object object;
    private final ObjectGraphDataModel objectGraphDataModel;
    private final ObjectGraphDataQueryObject queryObject;

    public ObjectGraphDataObject(Object object) {
        objectGraphDataModel = new ObjectGraphDataModel();
        this.object = object;
        this.queryObject = new ObjectGraphDataQueryObject(object);
    }


    public DataList asList() {
        if (object instanceof ObjectGraphDataList) {
            return (DataList) object;
        } else if (object instanceof List) {
            final ObjectGraphDataList list = new ObjectGraphDataList();
            list.addAllRaw((List<Object>) object);
            return list;
        } else {
            ObjectGraphDataList list = new ObjectGraphDataList(new ArrayList<DataObject>());
            list.add(this);
            return list;
        }
    }


    public DataMap asMap() {
        if (isMap()) {
            return new ObjectDataMap((Map) object);
        } else {
            HashMap map = new HashMap();
            map.put(object, object);
            return new ObjectDataMap(map);
        }
    }


    public boolean isMap() {
        return object instanceof Map;
    }


    public String asString() {
        if (object == null) {
            return "null";
        } else if (object instanceof byte[]) {
            return new String((byte[]) object);
        } else {
            return object.toString();
        }
    }


    public DataObject convert(Class<DataModel> model) {
        try {
            return model.newInstance().getDataObjectFactory().createDataObject(object);
        } catch (InstantiationException e) {
            throw new DataConversionRuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new DataConversionRuntimeException(e);
        }
    }


    public DataObject duplicate() {
        XStream xStream = new XStream();
        return (DataObject) xStream.fromXML(xStream.toXML(this));
    }


    public Class getCanonicalType() {
        return object.getClass();
    }


    public UniversalDataEventStream getDataEventModel() {
        return null;
    }


    public DataModel getDataModel() {
        return objectGraphDataModel;
    }


    public DataQueryObject getQueryObject() {
        return queryObject;
    }


    public Object getValue() {
        return object;
    }


    public <T> T getValue(Class<T> clazz) throws ClassCastException {
        return (T) this;
    }


    public boolean isList() {
        return object instanceof ObjectGraphDataList || object instanceof List;
    }


    public boolean isTable() {
        return false;
    }


    public boolean isTree() {
        return true;
    }


    public boolean equals(Object o) {
        return o instanceof DataObject && ((DataObject) o).getValue().equals(object);
    }


    public int hashCode() {
        if (object != null) {
            return object.hashCode();
        } else {
            return -1;
        }
    }


    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
