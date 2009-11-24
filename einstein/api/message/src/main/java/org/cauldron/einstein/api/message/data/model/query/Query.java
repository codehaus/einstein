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

package org.cauldron.einstein.api.message.data.model.query;

import org.cauldron.einstein.api.message.data.model.DataObject;
import org.cauldron.einstein.api.message.data.model.DataList;
import org.contract4j5.contract.Contract;
import org.contract4j5.contract.Post;
import org.contract4j5.contract.Pre;

/**
 * An interface for arbitrary queries against {@link org.cauldron.einstein.api.message.data.model.DataObject}s
 *
 * @author Neil Ellis
 */
@Contract
public interface Query {

    /**
     * Select a single {@link DataObject} using this query.
     *
     * @param o the object to be queried.
     *
     * @return the query result with the {@link DataObject} returned representing a single value or null.
     */
    @Pre
    @Post
    DataObject selectSingle(DataObject o);

    /**
     * Select multiple {@link DataObject}s using this query.
     *
     * @param o the {@link Object} to be queried.
     *
     * @return the query result with the DataObject returned representing multiple values, i.e. {@link
     *         DataObject#isList()} will return true or null if no match.
     */
    @Pre
    @Post DataList selectMultiple( DataObject o);
}
