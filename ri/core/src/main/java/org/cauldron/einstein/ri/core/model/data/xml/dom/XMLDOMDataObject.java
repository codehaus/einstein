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

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.cauldron.einstein.api.message.data.exception.DataConversionRuntimeException;
import org.cauldron.einstein.api.message.data.model.*;
import org.cauldron.einstein.api.message.data.udes.UniversalDataEventStream;
import org.cauldron.einstein.api.model.lifecycle.exception.InitializationRuntimeException;
import org.cauldron.einstein.ri.core.log.Logger;
import org.cauldron.einstein.ri.core.runtime.EinsteinRIRuntimeFactory;
import org.contract4j5.contract.Invar;
import org.contract4j5.contract.Post;
import org.contract4j5.contract.Pre;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract

public class XMLDOMDataObject implements DataObject {

    private static final Logger log = Logger.getLogger(XMLDOMDataObject.class);
    private static final TransformerFactory transformerFactory = TransformerFactory.newInstance();
    private static final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();

    private static final DocumentBuilder DOCUMENT_BUILDER;

    static {
        try {
            DOCUMENT_BUILDER = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new InitializationRuntimeException(e);
        }
    }

    @Invar
    private final Node node;

    @Invar("$this.getNodes().getLength() == $target")
    private final int size;

    public XMLDOMDataObject() {
        node = DOCUMENT_BUILDER.newDocument().createDocumentFragment();
        size = getNodes().getLength();
    }

//    private final List<DocumentFragment> list = new ArrayList<DocumentFragment>();

//    @Pre
//    public XMLDOMDataObject(NodeList nodeList) {
//            int max = nodeList.getLength();
//            for (int i = 0; i < max; i++) {
//                list.add(nodeList.item(i));
//            }
//    }


    public XMLDOMDataObject(Node node) {
        this.node = node;
        size = getNodes().getLength();
    }

//    @Pre XMLDOMDataObject(List<DocumentFragment> list) {
//        list.addAll(list);
//    }

    @Post
    public DataList asList() {
        return new XMLDOMDataList(node.getChildNodes());
    }


    public DataMap asMap() {
        return null; //TODO
    }


    @Pre @Post
    public DataObject convert(Class<DataModel> model) {
        return EinsteinRIRuntimeFactory.getInstance().getRuntime().getRosettaStone().convert(model, this);
    }


    @Post
    public DataObject duplicate() {
        XMLDOMDataObject xmldomDataObject;
        try {
            xmldomDataObject = new XMLDOMDataObject(DOMUtil.buildFragmentFromNode(docBuilderFactory.newDocumentBuilder(),
                                                                                  node));
        } catch (IOException e) {
            throw new DataConversionRuntimeException(e);
        } catch (TransformerException e) {
            throw new DataConversionRuntimeException(e);
        } catch (SAXException e) {
            throw new DataConversionRuntimeException(e);
        } catch (ParserConfigurationException e) {
            throw new DataConversionRuntimeException(e);
        }
        if (!xmldomDataObject.asString().equals(asString())) {
            log.fatal("Old version was {0}", asString());
            log.fatal("New version is {0}", xmldomDataObject.asString());
        }
        return xmldomDataObject;
    }


    @Post
    public String asString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            Transformer xformer = transformerFactory.newTransformer();
            xformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
//            for (Node node : list) {
            Source source = new DOMSource(node);
            Result result = new StreamResult(byteArrayOutputStream);
            xformer.transform(source, result);
//            }
        } catch (TransformerConfigurationException e) {
            throw new DataConversionRuntimeException(e);
        } catch (TransformerException e) {
            throw new DataConversionRuntimeException(e);
        }
        return byteArrayOutputStream.toString();
    }


    @Post
    public Class getCanonicalType() {
        return DocumentFragment.class;
    }


    public UniversalDataEventStream getDataEventModel() {
        return null;
    }


    @Post
    public DataModel getDataModel() {
        return new XMLDOMDataModel();
    }


    @Post
    public DataQueryObject getQueryObject() {
        return new XMLDOMDataQueryObject(node);
    }


    @Post
    public Object getValue() {
        return node;
    }


    @Post
    public <T> T getValue(Class<T> clazz) throws ClassCastException {
        return (T) node;
    }


    public boolean isList() {
        return true;
    }


    public boolean isMap() {
        return false;
    }


    public boolean isTable() {
        return false; //TODO
    }


    public boolean isTree() {
        return true;
    }

//
//    @Post public DataObject asDataObject() {
//        return this;
//    }
//
//
//    @Pre public boolean contains(DataObject object) {
//        if (object.getDataModel() instanceof XMLDOMDataModel) {
//            final Node node = ((XMLDOMDataObject) object).getNode();
//            if (this.node.equals(node)) {
//                return true;
//            }
//            for (int i = 0; i < this.node.getChildNodes().getLength(); i++) {
//                if (this.node.getChildNodes().item(i).equals(this.node)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//
//    @Pre public DataObject get(int i) {
//        final Document document = DOCUMENT_BUILDER.newDocument();
//        final DocumentFragment newFragment = document.createDocumentFragment();
//        final Node node = getNodes().item(i).cloneNode(true);
//        document.adoptNode(node);
//        newFragment.appendChild(node);
//        return new XMLDOMDataObject(newFragment);
//    }
//
//
//    @Post public boolean isEmpty() {
//        return size == 0;
//    }
//
//
//
//    @Post public int size() {
//        return size;
//    }
//
//
//    @Post public Iterator<DataObject> iterator() {
//        final NodeList list = getNodes();
//        final int max = list.getLength();
//        return new Iterator<DataObject>() {
//
//            private int i = 0;
//
//            public boolean hasNext() {
//                return i < max-1;
//            }
//
//
//            public DataObject next() {
//
//                final DataObject object = get(i);
//                i++;
//                return object;
//            }
//
//
//            public void remove() {
//               throw new UnsupportedOperationEinsteinRuntimeException("remove", "Iterator<DataObject>");
//            }
//        };
//    }

    //
    @Post
    private NodeList getNodes() {
        return node.getChildNodes();
    }


    @Override
    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this);
    }

    @Post
    public Node getNode() {
        return node.cloneNode(true);
    }
}
