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

package org.cauldron.einstein.api.message.data.model;

import org.contract4j5.contract.*;



/**
 * This is a view on a DataObject which makes it appear to be a list. The underlying data structure could be anything
 *
 * @author Neil Ellis
 */
@Contract
public interface DataList {


    @Post("$return >= 0")
    int size();

    @Post("$return == ($this.size() == 0)")
    boolean isEmpty();

    @Pre
    boolean contains( DataObject object);

    @Post
    java.util.Iterator<DataObject> iterator();

    @Pre("i >= 0 && i < $this.size()")
    DataObject get(int i);

    /**
     * Gets a single object view of this list.
     */
    @Post
    DataObject asDataObject();
}
