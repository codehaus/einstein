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

import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.cauldron.einstein.api.message.data.exception.DataConversionRuntimeException;
import org.cauldron.einstein.api.message.data.model.DataObject;
import org.cauldron.einstein.ri.core.log.Logger;
import org.cauldron.einstein.ri.core.runtime.EinsteinRIRuntimeFactory;
import org.contract4j5.contract.Post;
import org.contract4j5.contract.Pre;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class DOMUtil {

    private static final Logger log = Logger.getLogger(DOMUtil.class);

    private static final XStream xstream = new XStream();
    private static final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    private static final TransformerFactory transformerFactory = TransformerFactory.newInstance();

    static XMLDOMDataObject createXMLDOMObjectFromArbitraryObject(Object o) {
        if (o == null) {
            log.debug("Creating XMLDOM Null DataObject.");
            try {
                return new XMLDOMDataObject(docBuilderFactory.newDocumentBuilder()
                        .newDocument().createDocumentFragment());
            } catch (ParserConfigurationException e) {
                throw new DataConversionRuntimeException(e);
            }
        }
        return new XMLDOMDataObject(createDOMFromObject(o));
    }

    @Post
    @Pre
    static DocumentFragment createDOMFromObject(List<DataObject> objects) {
        log.debug("Creating XMLDOM DataList.");
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = docBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new DataConversionRuntimeException(e);
        }
        DocumentFragment fragment;
        Document doc = docBuilder.newDocument();
        fragment = doc.createDocumentFragment();
        for (DataObject dataObject : objects) {
            final XMLDOMDataObject object = (XMLDOMDataObject) EinsteinRIRuntimeFactory.getInstance()
                    .getRuntime()
                    .getRosettaStone()
                    .convert(XMLDOMDataModel.class, dataObject);
            final Node node = object.getNode();
            doc.adoptNode(node);
            fragment.appendChild(node);
        }
        return fragment;
    }

    @Post
    @Pre
    static Node createDOMFromObject(Object o) {
        Document doc;
        try {
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            if (o instanceof String) {
                log.debug("Creating XMLDOM DataObject from String");
                doc = docBuilder.parse(new ByteArrayInputStream(((String) o).getBytes()));
            } else if (o instanceof byte[]) {
                log.debug("Creating XMLDOM DataObject from byte[]");
                doc = docBuilder.parse(new ByteArrayInputStream((byte[]) o));
            } else if (o instanceof InputStream) {
                log.debug("Creating XMLDOM DataObject from InpuStream");
                doc = docBuilder.parse((InputStream) o);
//            } else if (o instanceof NodeList) {
//                log.debug("Creating XMLDOM DataObject from NodeList");
//                if(true)throw new Error();
//                NodeList nodeList = (NodeList) o;
//                fragment = buildFragmentFromNodeList(docBuilder, nodeList);
//                return fragment;

            } else if (o instanceof Node) {
                log.debug("Building fragment from a node.");
                return (Node) o;
            } else {
                log.debug("Creating XMLDOM DataObject from Object");
                doc = docBuilder.parse(new ByteArrayInputStream(xstream.toXML(o).getBytes()));
            }
        } catch (ParserConfigurationException e) {
            throw new DataConversionRuntimeException(e);
        } catch (SAXException e) {
            throw new DataConversionRuntimeException(e);
        } catch (IOException e) {
            throw new DataConversionRuntimeException(e);
//        } catch (TransformerException e) {
//            throw new DataConversionRuntimeException(e);
        }
        final DocumentFragment fragment = doc.createDocumentFragment();
        final Element element = doc.getDocumentElement();
        fragment.getOwnerDocument().adoptNode(element);
        fragment.appendChild(element);
        return fragment;
    }

    @Post
    @Pre
    public static DocumentFragment buildFragmentFromNodeList(DocumentBuilder docBuilder, NodeList nodeList) throws
                                                                                                            IOException,
                                                                                                            TransformerException,
                                                                                                            SAXException {
        Document doc;
        DocumentFragment fragment;
        /*
            I know this is convoluted but it's very difficult to actually add a bunch of random nodes to
            a document fragment without getting errors. This works by normalizing the nodes.
         */
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write("<dummy>".getBytes());
        Transformer xformer = transformerFactory.newTransformer();
        xformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Source source = new DOMSource(nodeList.item(i));
            Result result = new StreamResult(byteArrayOutputStream);
            xformer.transform(source, result);
        }
        byteArrayOutputStream.write("</dummy>".getBytes());
        log.debug("Dumy node {0}.", byteArrayOutputStream);

        doc = docBuilder.parse(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        fragment = doc.createDocumentFragment();
        nodeList = doc.getDocumentElement().getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            log.debug("Moving temporary node.");
            final Node node = nodeList.item(i);
            doc.adoptNode(node);
            fragment.appendChild(node);
        }
        log.debug("Fragment is now {0}.", fragment);
        return fragment;
    }

//
//    @Post
//    @Pre
//    public static Node cloneNode(DocumentBuilder docBuilder, Node node) throws
//                                                                        IOException,
//                                                                        TransformerException,
//                                                                        SAXException {
//        Document doc;
//        DocumentFragment fragment;
//        /*
//            I know this is convoluted but it's very difficult to actually add a bunch of random nodes to
//            a document fragment without getting errors. This works by normalizing the nodes.
//         */
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        byteArrayOutputStream.write("<dummy>".getBytes());
//        Transformer xformer = transformerFactory.newTransformer();
//        xformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
//        Source source = new DOMSource(node);
//        Result result = new StreamResult(byteArrayOutputStream);
//        xformer.transform(source, result);
//        byteArrayOutputStream.write("</dummy>".getBytes());
//        log.debug("Dumy node {0}.", byteArrayOutputStream);
//
//        doc = docBuilder.parse(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
//        fragment = doc.createDocumentFragment();
//        NodeList nodeList = doc.getDocumentElement().getChildNodes();
//        for (int i = 0; i < nodeList.getLength(); i++) {
//            log.debug("Moving temporary node.");
//            final Node newNode = nodeList.item(i);
//            doc.adoptNode(newNode);
//            fragment.appendChild(newNode);
//        }
//        log.debug("Fragment is now {0}.", fragment);
//        return fragment;
//    }

    @Post
    @Pre
    public static DocumentFragment buildFragmentFromNode(DocumentBuilder docBuilder, Node node) throws
                                                                                                IOException,
                                                                                                TransformerException,
                                                                                                SAXException {
        Document doc;
        DocumentFragment fragment;
        /*
            I know this is convoluted but it's very difficult to actually add a bunch of random nodes to
            a document fragment without getting errors. XML DOM APIs are bloomin sketchy so this guarantees
            that the node can be added.  Need to throw this rubbish away and use a better XML API really.
         */
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write("<dummy>".getBytes());
        Transformer xformer = transformerFactory.newTransformer();
        xformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        Source source = new DOMSource(node);
        Result result = new StreamResult(byteArrayOutputStream);
        xformer.transform(source, result);
        byteArrayOutputStream.write("</dummy>".getBytes());

        log.debug("Dumy node {0}.", byteArrayOutputStream);

        doc = docBuilder.parse(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        fragment = doc.createDocumentFragment();
        final Element element = doc.getDocumentElement();
        if (element.hasChildNodes()) {
            //has child nodes, not text.
            NodeList nodeList = element.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                log.debug("Moving temporary node.");
                final Node newNode = nodeList.item(i);
                doc.adoptNode(newNode);
                fragment.appendChild(newNode);
            }
        }

        log.debug("Fragment is now {0}.", ReflectionToStringBuilder.reflectionToString(fragment));
        return fragment;
    }

}
