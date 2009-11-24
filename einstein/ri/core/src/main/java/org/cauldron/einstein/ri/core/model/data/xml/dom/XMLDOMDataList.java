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
import org.cauldron.einstein.ri.core.log.Logger;
import org.contract4j5.contract.Post;
import org.contract4j5.contract.Pre;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class XMLDOMDataList implements DataList {
    private static final Logger log = Logger.getLogger(XMLDOMDataList.class);
    private static final TransformerFactory transformerFactory = TransformerFactory.newInstance();
    private static final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    private final List<DataObject> list = new ArrayList<DataObject>();

    public XMLDOMDataList(List<DataObject> list) {
        this.list.addAll(list);
    }

    public XMLDOMDataList(NodeList nodeList) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            list.add(DOMUtil.createXMLDOMObjectFromArbitraryObject(nodeList.item(i)));
        }
    }


    @Post public int size() {
        return list.size();
    }

    @Post public boolean isEmpty() {
        return list.isEmpty();
    }

    @Pre public boolean contains(DataObject object) {
        return list.contains(object);
    }

    @Post public Iterator<DataObject> iterator() {
        return list.iterator();
    }

    @Pre public DataObject get(int i) {
        return list.get(i);
    }

    @Post public DataObject asDataObject() {
        Document doc;
        DocumentFragment fragment;
        /*
            I know this is convoluted but it's very difficult to actually add a bunch of random nodes to
            a document fragment without getting errors. This works by normalizing the nodes.
         */
        try {
            final DocumentBuilder documentBuilder = docBuilderFactory.newDocumentBuilder();
            StringBuffer sb = new StringBuffer();
            sb.append("<dummy>");
            Transformer xformer = transformerFactory.newTransformer();
            xformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            for (DataObject object : list) {
                sb.append(object.asString());

            }
            sb.append("</dummy>");
            final String s = sb.toString();
            log.debug("Dummy XML is {0}.", s);
            doc = documentBuilder.parse(new ByteArrayInputStream(s.getBytes()));
            fragment = doc.createDocumentFragment();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                final Node node = nodeList.item(i);
                doc.adoptNode(node);
                fragment.appendChild(node);
            }
            final XMLDOMDataObject xmldomDataObject = new XMLDOMDataObject(fragment);
            log.debug("New DataObject is {0}.",xmldomDataObject.asString());
            return xmldomDataObject;
        } catch (ParserConfigurationException e) {
            throw new DataConversionRuntimeException(e);
        } catch (TransformerConfigurationException e) {
            throw new DataConversionRuntimeException(e);
        } catch (SAXException e) {
            throw new DataConversionRuntimeException(e);
        } catch (IOException e) {
            throw new DataConversionRuntimeException(e);
        }
    }
}
