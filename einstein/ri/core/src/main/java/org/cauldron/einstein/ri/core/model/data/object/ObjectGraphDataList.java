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


import org.cauldron.einstein.api.message.data.model.DataList;
import org.cauldron.einstein.api.message.data.model.DataObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class ObjectGraphDataList implements DataList {

    private final List<Object> list = new ArrayList();

    public ObjectGraphDataList() {
    }


    public ObjectGraphDataList(List<DataObject> list) {
        addAll(list);
    }


    private boolean addAll(List<DataObject> newList) {
        for (DataObject dataObject : newList) {
            add(dataObject);
        }
        return true;
    }


    public boolean add(DataObject dataObject) {
        return list.add(dataObject.getValue());
    }


    public DataObject asDataObject() {
        return new ObjectGraphDataObject(list);
    }


    public boolean contains(DataObject o) {
        return list.contains(o.getValue());
    }


    public DataObject get(int i) {
        return new ObjectGraphDataObject(list.get(i));
    }


    public boolean isEmpty() {
        return list.isEmpty();
    }


    public Iterator<DataObject> iterator() {
        final Iterator<?> it = list.iterator();
        return new Iterator<DataObject>() {

            public boolean hasNext() {
                return it.hasNext();
            }


            public DataObject next() {
                return new ObjectGraphDataObject(it.next());
            }


            public void remove() {
                it.remove();
            }
        };
    }


    public int size() {
        return list.size();
    }


    public boolean addRaw(Object o) {
        return list.add(o);
    }


    public void addAllRaw(Collection<Object> objects) {
        list.addAll(objects);
    }
}
