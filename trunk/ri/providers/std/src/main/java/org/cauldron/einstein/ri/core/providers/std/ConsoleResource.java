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

package org.cauldron.einstein.ri.core.providers.std;

import org.cauldron.einstein.api.common.euri.EinsteinURI;
import org.cauldron.einstein.api.provider.facade.PollFacade;
import org.cauldron.einstein.api.provider.facade.ReadFacade;
import org.cauldron.einstein.api.provider.facade.WriteFacade;
import org.cauldron.einstein.ri.core.providers.base.AbstractResource;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
class ConsoleResource extends AbstractResource {

    public ConsoleResource(EinsteinURI uri) {
        super(uri);
        OutputFormat messageFormat= OutputFormat.PLAIN;
        if(uri.getProviderMetadata().asProperties().containsKey("format")) {
            if(uri.getProviderMetadata().asProperties().getProperty("format").equals("printf")) {
               messageFormat= OutputFormat.PRINTF;
            }
        }
        addMapping(WriteFacade.class, new ConsoleWriteFacade(uri.getDescriptor().asString(), messageFormat));
        addMapping(ReadFacade.class, new ConsoleReadFacade(uri));
//        addMapping(ListenFacade.class, new ConsoleReadFacade(uri));
        addMapping(PollFacade.class, new ConsoleReadFacade(uri));
    }

    protected String getName() {
        return "std";
    }

}