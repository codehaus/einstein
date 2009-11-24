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

package org.cauldron.einstein.ri.core.providers.text;


import org.cauldron.einstein.api.common.euri.EinsteinURI;
import org.cauldron.einstein.api.message.Message;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.model.execution.ExecutionContext;
import org.cauldron.einstein.api.provider.facade.ExecuteFacade;
import org.cauldron.einstein.api.provider.facade.ReadFacade;
import org.cauldron.einstein.api.provider.facade.context.ReadContext;
import org.cauldron.einstein.api.provider.facade.exception.ReadFailureRuntimeException;
import org.cauldron.einstein.ri.core.providers.AbstractFacade;
import org.cauldron.einstein.ri.core.providers.base.AbstractResource;

/**
 * @author Neil Ellis
 */

@org.contract4j5.contract.Contract
public class TextResource extends AbstractResource {

    public TextResource(EinsteinURI uri) {
        super(uri);
        addMapping(ReadFacade.class, new TextReadFacade(uri));
    }


    private static class TextReadFacade extends AbstractFacade implements ReadFacade, ExecuteFacade {

        private final EinsteinURI url;

        public TextReadFacade(EinsteinURI url) {
            this.url = url;
        }


        public Message read(ReadContext context) throws ReadFailureRuntimeException {
            return context.getActiveProfile()
                    .getMessageModel()
                    .createMessage(context.getMessage().getExecutionCorrelation(),
                                   context.getActiveProfile()
                                           .getDataModel()
                                           .getDataObjectFactory().createDataObject(url.getDescriptor().asString()));
        }


        public MessageTuple execute(ExecutionContext context, MessageTuple message) {
            return context.getActiveProfile()
                    .getMessageModel()
                    .createMessage(message.getExecutionCorrelation(),
                                   context.getActiveProfile()
                                           .getDataModel()
                                           .getDataObjectFactory().createDataObject(url.getDescriptor().asString()));
        }


        public MessageTuple read(ReadContext context, boolean all, boolean payload, long timeout) throws
                                                                                                  ReadFailureRuntimeException {
            return read(context);
        }


        public MessageTuple read(ReadContext context, boolean all, boolean payload) throws ReadFailureRuntimeException {
            return read(context);
        }


        public MessageTuple readNoWait(ReadContext context, boolean all, boolean payload) throws
                                                                                          ReadFailureRuntimeException {
            return read(context);
        }
    }
}
