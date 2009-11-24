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

package org.cauldron.einstein.ri.core.euri;


import org.cauldron.einstein.api.common.euri.*;
import org.cauldron.einstein.api.common.euri.exception.MalformedEinsteinURIRuntimeException;
import static org.cauldron.einstein.ri.core.CoreModuleMessages.*;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Neil Ellis
 */

@org.contract4j5.contract.Contract
public class EinsteinURIImpl implements EinsteinURI {

    private final String providerName;
    private final URIMetaData metaData;
    private String descriptionAsString;
    private EinsteinURI nestedURI;


    public EinsteinURIImpl(String providerName, URIMetaData metaData, String description) {
        this.descriptionAsString = description;
        this.metaData = metaData;
        this.providerName = providerName;
    }


    public EinsteinURIImpl(String providerName, URIMetaData metaData, EinsteinURI nested) {
        this.nestedURI = nested;
        this.metaData = metaData;
        this.providerName = providerName;
    }


    public EinsteinURIImpl(String providerName, URIMetaData metaData, String description, EinsteinURI nestedURI) {
        this.descriptionAsString = description;
        this.nestedURI = nestedURI;
        this.metaData = metaData;
        this.providerName = providerName;
    }


    public String asString() {
        return toString();
    }


    public String toString() {
        if (metaData.isEmpty()) {
            return providerName + ':' + getDescriptor().asString();
        } else {
            return providerName + '(' + metaData.toString() + "):" + getDescriptor().asString();
        }
    }


    public URL asURL() throws MalformedEinsteinURIRuntimeException {
        try {
            return new URL(toString());
        } catch (MalformedURLException e) {
            throw new MalformedEinsteinURIRuntimeException(e);
        }
    }


    public URIDescriptor getDescriptor() {
        if (nestedURI == null) {
            return new URIDescriptor() {

                public EinsteinURI asEinsteinURI() throws MalformedEinsteinURIRuntimeException {
                    return EinsteinURIFactory.createURI(descriptionAsString);
                }


                public URL asURL() throws MalformedEinsteinURIRuntimeException {
                    try {
                        return new URL(descriptionAsString);
                    } catch (MalformedURLException e) {
                        throw new MalformedEinsteinURIRuntimeException(e);
                    }
                }


                public String asString() {
                    return descriptionAsString;
                }


                public URLLocation asURLLocation() throws MalformedEinsteinURIRuntimeException {
                    return EinsteinURIFactory.createURLLocation(descriptionAsString);
                }


                public URLPath asPath() throws MalformedEinsteinURIRuntimeException {
                    return EinsteinURIFactory.createURLLocation(descriptionAsString).getPath();
                }
            };
        } else {
            return new URIDescriptor() {

                public EinsteinURI asEinsteinURI() throws MalformedEinsteinURIRuntimeException {
                    return nestedURI;
                }


                public URL asURL() throws MalformedEinsteinURIRuntimeException {
                    return nestedURI.asURL();
                }


                public String asString() {
                    return nestedURI.asString();
                }


                public URLLocation asURLLocation() throws MalformedEinsteinURIRuntimeException {
                    throw new MalformedEinsteinURIRuntimeException(BUNDLE_NAME,
                                                                   CANNOT_CONVERT_NESTED_URI_TO_LOCATION,
                                                                   nestedURI);
                }


                public URLPath asPath() throws MalformedEinsteinURIRuntimeException {
                    throw new MalformedEinsteinURIRuntimeException(BUNDLE_NAME,
                                                                   CANNOT_CONVERT_NESTED_URI_TO_PATH,
                                                                   nestedURI);
                }
            };
        }
    }


    public URIMetaData getProviderMetadata() {
        return metaData;
    }


    public String getProviderName() {
        return providerName;
    }
}
