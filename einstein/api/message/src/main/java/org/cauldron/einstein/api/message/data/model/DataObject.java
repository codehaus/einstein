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

import org.cauldron.einstein.api.message.data.udes.UniversalDataEventStream;
import org.contract4j5.contract.Contract;
import org.contract4j5.contract.Post;
import org.contract4j5.contract.Pre;

/**
 * A transportable object wraps objects whose internal data
 * <p/>
 *
 * @author Neil Ellis
 */
@Contract
public interface DataObject {

    /**
     * This returns the canonical value as an {@link Object}.
     */
    Object getValue();


    /**
     * This is the primary means of accessing the objects value. The supplied type is used to determine the actual
     * return type - no conversion is implied so if the type is wrong a ClassCastException will be thrown.
     *
     * @param clazz the Class of the type to be returned.
     */
    <T> T getValue(Class<T> clazz) throws ClassCastException;

    /**
     * Changes the internal representation and DataModel of this object. Eg. from String to XML or XML to object.
     *
     * @param model the data model to convert to.
     *
     * @return a new DataObject with the DataModel supplied.
     */
    @Pre
    @Post("$return.getDataModel() == model")
    DataObject convert(Class<DataModel> model);

    /**
     * This allows arbitrary queries to be run against a data model, a provider can use this interface to see if the
     * data object supports the query type and if it does the data object will perform the query itself. <br/> A good
     * example is XPath, which is a fairly universal query mechanism. A CSV data model might choose to actually support
     * XPath in which case the XPath provider will detect this and use the CSV data models inbuilt support. <br/>
     *
     * @return a queriable object.
     */
    @Post
    DataQueryObject getQueryObject();

    /**
     * Returns the internal DataModel of the DataObject.
     *
     * @return the DataModel.
     */
    @Post
    DataModel getDataModel();

    /**
     * Returns this as a List
     */
    @Post
    DataList asList();

    @Post
    DataMap asMap();

    /**
     * Returns true if the data is in the form of a list without any transformation being applied. This is not
     * neccessarily a java.util.List but any representation of a logical list. To manipulate
     */
    boolean isList();

    boolean isTable();

    boolean isTree();

    boolean isMap();

    /**
     * This returns the canonical type, i.e. the actual class of the data as it is stored in the DataObject.
     */
    Class getCanonicalType();

    /**
     * Get as a format neutral event stream for general usage.
     */
    UniversalDataEventStream getDataEventModel();

    @Post
    String asString();

    /**
     * Deep clone.
     */
    @Post("$return.asString().equals($this.asString())")
    DataObject duplicate();
}
