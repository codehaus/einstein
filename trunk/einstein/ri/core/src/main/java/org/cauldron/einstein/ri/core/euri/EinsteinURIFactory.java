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

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Neil Ellis
 */

@org.contract4j5.contract.Contract
public class EinsteinURIFactory {

    private static final String METADATA_PATTERN = "(\\([a-zA-Z0-9]+=[a-zA-Z0-9]+([ ]*,[ ]*[a-zA-Z0-9]+=[a-zA-Z0-9]+)*\\))";
    private static final Pattern CREDENTIAL_AND_HOST = Pattern.compile(
            "^//[.a-zA-Z0-9-]*(:[.a-zA-Z0-9-]*)?@[.a-zA-Z0-9-]*:[0-9]+/");
    private static final Pattern HOST_PATTERN = Pattern.compile("^//[.a-zA-Z0-9-]*:[0-9]+/");
    private static final Pattern PROVIDER_SECTION_PATTERN = Pattern.compile("^[a-zA-Z]+" + METADATA_PATTERN + "?:");
    private static final Pattern PROVIDER_NAME_PATTERN = Pattern.compile("^[a-zA-Z]+");
    private static final Pattern META_DATA_PATTERN = Pattern.compile(METADATA_PATTERN);


    public static EinsteinURI createURI(String uri) throws MalformedEinsteinURIRuntimeException {
        final Matcher providerSectionMatcher = PROVIDER_SECTION_PATTERN.matcher(uri);
        if (!providerSectionMatcher.find(0)) {
            throw new MalformedEinsteinURIRuntimeException(BUNDLE_NAME, MALFORMED_URI, uri == null ? "<null>" : uri);
        }

        String providerSection = providerSectionMatcher.group();
        Matcher providerNameMatcher = PROVIDER_NAME_PATTERN.matcher(providerSection);

        providerNameMatcher.find(0);
        String providerName = providerNameMatcher.group();

        Matcher metaDataPatternMatcher = META_DATA_PATTERN.matcher(uri);

        URIMetaData metaData;
        Properties metaDataProperties = new Properties();

        if (metaDataPatternMatcher.find(0)) {
            String metaDataString = metaDataPatternMatcher.group();

            final String[] nameValues = metaDataString.substring(1, metaDataString.length() - 1).split("[ ],[ ]");

            for (String nameValue : nameValues) {
                final String[] pair = nameValue.split("=");
                metaDataProperties.put(pair[0], pair[1]);
            }
        }
        metaData = new URIMetaDataImpl(metaDataProperties);

        EinsteinURI nestedURI;
        final String description = uri.substring(providerSection.length(), uri.length());
        try {
            nestedURI = EinsteinURIFactory.createURI(description);
            return new EinsteinURIImpl(providerName, metaData, description, nestedURI);
        }
        catch (MalformedEinsteinURIRuntimeException e) {
            //TODO:FIXME:This is naughty, need to check properly not use an exception.
            return new EinsteinURIImpl(providerName, metaData, description);
        }
    }


    public static URLLocation createURLLocation(String descriptionAsString) throws
                                                                            MalformedEinsteinURIRuntimeException {
        String hostString = "";
        String credentialString;
        String host;
        String port = null;
        String username;
        String password = null;
        String pathAndHostString;
        String path;
        String[] pathElements;
        String queryString = null;
        boolean relative;
        int pathStart = 0;
        String[] strings = descriptionAsString.split("\\?");
        URLServer server = null;
        URLCredentials credentials = null;

        pathAndHostString = strings[0];
        if (strings.length == 2) {
            queryString = strings[1];
        } else if (strings.length > 2) {
            throw new MalformedEinsteinURIRuntimeException(BUNDLE_NAME, MULTIPLE_QUERIES_IN_URL);
        }

        if (pathAndHostString.startsWith("//")) {
            //Full syntax
            Matcher credentialAndHostMatcher = CREDENTIAL_AND_HOST.matcher(pathAndHostString);
            if (credentialAndHostMatcher.find(0)) {
                String credentialAndHostString = credentialAndHostMatcher.group();
                hostString = credentialAndHostString.substring(hostString.indexOf('@') + 1,
                                                               hostString.indexOf('/', hostString.indexOf('@')));
                credentialString = credentialAndHostString.substring(2, credentialAndHostString.indexOf('@'));
                if (credentialString.indexOf(':') != -1) {
                    username = credentialString.substring(2, credentialString.indexOf(':'));
                    password = credentialString.substring(credentialString.indexOf(':') + 1,
                                                          credentialString.indexOf('/', credentialString.indexOf(':')));
                } else {
                    username = credentialString;
                }
                pathStart = credentialAndHostString.length() - 1;
                credentials = new URLCredentialsImpl(username, password);
            }
            Matcher hostMatcher = HOST_PATTERN.matcher(pathAndHostString);
            if (hostMatcher.find(0)) {
                hostString = HOST_PATTERN.matcher(pathAndHostString).group();
                pathStart = hostString.length() - 1;
            }
            if (hostString.indexOf(':') != -1) {
                host = hostString.substring(2, hostString.indexOf(':'));
                port = hostString.substring(hostString.indexOf(':') + 1,
                                            hostString.indexOf('/', hostString.indexOf(':')));
            } else {
                host = hostString;
            }
            path = pathAndHostString.substring(pathStart);
            relative = false;
            try {
                server = new URLServerImpl(host, Integer.parseInt(port));
            } catch (NumberFormatException nfe) {
                throw new MalformedEinsteinURIRuntimeException(nfe,
                                                               BUNDLE_NAME,
                                                               COULD_NOT_PARSE_THE_PORT,
                                                               port,
                                                               descriptionAsString);
            }
        } else {
            path = pathAndHostString;
            relative = !path.startsWith("/");
        }
        pathElements = path.split("/");
        Properties queryProperties = new Properties();
        if (queryString != null) {
            String[] elements = queryString.split("&");
            for (String element : elements) {
                String[] nameValue = element.split("=");
                String name = nameValue[0];
                String value = "";
                if (nameValue.length == 2) {
                    value = nameValue[1];
                } else if (nameValue.length > 2) {
                    throw new MalformedEinsteinURIRuntimeException(BUNDLE_NAME, TOO_MANY_EQUALS, queryString);
                }
                queryProperties.put(name, value);
            }
        }
        return new URLLocationImpl(server,
                                   credentials,
                                   new URLPathImpl(pathElements, relative),
                                   new URLQueryImpl(queryProperties));
    }
}
