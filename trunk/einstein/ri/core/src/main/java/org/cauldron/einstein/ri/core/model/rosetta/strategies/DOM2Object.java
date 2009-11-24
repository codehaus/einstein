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

package org.cauldron.einstein.ri.core.model.rosetta.strategies;

import org.cauldron.einstein.api.message.data.model.DataObject;
import org.cauldron.einstein.api.message.data.rosetta.ConversionStrategy;
import org.cauldron.einstein.api.message.data.rosetta.RegisterConverter;
import org.cauldron.einstein.ri.core.model.data.object.ObjectGraphDataModel;
import org.cauldron.einstein.ri.core.model.data.object.ObjectGraphDataObject;
import org.cauldron.einstein.ri.core.model.data.xml.dom.XMLDOMDataModel;

/**
 * @author Neil Ellis
 */
@RegisterConverter(name = "dom2Object", from = XMLDOMDataModel.class, to = ObjectGraphDataModel.class)
@org.contract4j5.contract.Contract
public class DOM2Object implements ConversionStrategy {

    public DataObject convert(DataObject object) {
        return new ObjectGraphDataObject(object.getValue());
    }
}