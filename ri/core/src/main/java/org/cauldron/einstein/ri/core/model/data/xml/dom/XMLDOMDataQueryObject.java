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

import mazz.i18n.annotation.I18NResourceBundle;
import org.apache.xpath.XPathAPI;
import org.cauldron.einstein.api.common.euri.EinsteinURI;
import org.cauldron.einstein.api.message.data.exception.DataQueryRuntimeException;
import org.cauldron.einstein.api.message.data.model.DataList;
import org.cauldron.einstein.api.message.data.model.DataObject;
import org.cauldron.einstein.api.message.data.model.DataQueryObject;
import static org.cauldron.einstein.ri.core.CoreModuleMessages.*;
import org.cauldron.einstein.ri.core.log.Logger;
import org.contract4j5.contract.Invar;
import org.contract4j5.contract.Post;
import org.contract4j5.contract.Pre;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.TransformerException;

/**
 * @author Neil Ellis
 */
@I18NResourceBundle(baseName = BUNDLE_NAME)
@org.contract4j5.contract.Contract
public class XMLDOMDataQueryObject implements DataQueryObject {
    private static final Logger log = Logger.getLogger(XMLDOMDataQueryObject.class);
    private static final XMLDOMDataObjectFactory xmldomDataObjectFactory = new XMLDOMDataObjectFactory();

    @Invar
    private final Node doc;

    @Pre
    public XMLDOMDataQueryObject(Node doc) {
        this.doc = doc;
    }


    @Pre
    public boolean matches(EinsteinURI query) {
        log.debug("Matching with {0}", query);

        try {
            return XPathAPI.selectSingleNode(doc, query.getDescriptor().asString()) != null;
        } catch (TransformerException e) {
            throw new DataQueryRuntimeException(e, BUNDLE_NAME, MATCH_EXCEPTION, query);
        }
    }

    @Pre @Post
    public DataList selectMultiple(EinsteinURI query) {
        log.debug("Splitting multiple with {0}", query);
        try {
            final NodeList list = XPathAPI.selectNodeList(doc,
                                                          query.getDescriptor().asString());

            return new XMLDOMDataList(list);
        } catch (TransformerException e) {
            throw new DataQueryRuntimeException(e, BUNDLE_NAME, SELECT_MULTIPLE_EXCEPTION, query);
        }
    }


    @Pre @Post
    public DataObject selectSingle(EinsteinURI query) {
        log.debug("Splitting single with {0}", query);
        try {
            return xmldomDataObjectFactory.createDataObject(XPathAPI.selectSingleNode(doc,
                                                                                      query.getDescriptor().asString()));
        } catch (TransformerException e) {
            throw new DataQueryRuntimeException(e, BUNDLE_NAME, SELECT_SINGLE_EXCEPTION, query);
        }
    }


    @Pre
    public boolean supportsQuery(EinsteinURI query) {
        return query.getProviderName().equals("xpath");
    }
}
