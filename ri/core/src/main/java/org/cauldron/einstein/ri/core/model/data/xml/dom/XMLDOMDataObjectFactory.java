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

package org.cauldron.einstein.ri.core.model.data.xml.dom;

import org.cauldron.einstein.api.message.data.exception.DataConversionRuntimeException;
import org.cauldron.einstein.api.message.data.model.DataList;
import org.cauldron.einstein.api.message.data.model.DataObject;
import org.cauldron.einstein.api.message.data.model.DataObjectFactory;
import org.cauldron.einstein.ri.core.log.Logger;
import org.contract4j5.contract.Post;
import org.contract4j5.contract.Pre;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class XMLDOMDataObjectFactory implements DataObjectFactory {
    private static final Logger log = Logger.getLogger(XMLDOMDataObjectFactory.class);




    public XMLDOMDataObjectFactory() {
    }

    @Pre @Post
    public DataList createDataList(List<DataObject> objects) {

         return new XMLDOMDataList(objects);
    }


    @Post
    public DataObject createDataObject(Object o) {
        return DOMUtil.createXMLDOMObjectFromArbitraryObject( o);
    }


    @Post
    public DataObject createNullDataObject() throws DataConversionRuntimeException {
        return DOMUtil.createXMLDOMObjectFromArbitraryObject( null);
    }


    @Pre @Post
    public DataObject createDataObject(InputStream inputStream) {
        return DOMUtil.createXMLDOMObjectFromArbitraryObject(inputStream);

    }


    @Pre @Post
    public DataObject createDataObject(OutputStream outputStream) {
        return DOMUtil.createXMLDOMObjectFromArbitraryObject(outputStream);
    }

}
