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

package org.cauldron.einstein.ri.provider.jgroups.facade;

import org.cauldron.einstein.api.common.euri.EinsteinURI;
import org.cauldron.einstein.api.message.MessageListener;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.model.resource.ResourceRef;
import org.cauldron.einstein.api.provider.facade.ListenFacade;
import org.cauldron.einstein.api.provider.facade.context.ListenContext;
import org.cauldron.einstein.api.provider.facade.exception.ReadFailureRuntimeException;
import org.cauldron.einstein.ri.core.log.Logger;
import org.cauldron.einstein.ri.core.providers.AbstractFacade;
import org.cauldron.einstein.ri.provider.jgroups.JGroupsResource;
import org.cauldron.einstein.ri.provider.jgroups.JGroupsUtil;
import org.contract4j5.contract.Contract;
import org.contract4j5.contract.Pre;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;

/**
 * @author Neil Ellis
 */
@Contract
public class JGroupsListenFacade extends AbstractFacade implements ListenFacade {
    private static final Logger log = Logger.getLogger(JGroupsListenFacade.class);
    private final JGroupsResource resource;


    public JGroupsListenFacade(JGroupsResource resource, EinsteinURI uri) {

        this.resource = resource;
    }

    public void listen(@Pre final ListenContext context, final MessageTuple tuple, ResourceRef filter, final boolean payload, @Pre final MessageListener listener) throws ReadFailureRuntimeException {

        resource.getChannel().setReceiver(new ReceiverAdapter() {
            @Override
            public void receive(Message message) {
                try {
                    log.debug("Received {0}", new String(message.getBuffer()));
                    super.receive(message);
                    final org.cauldron.einstein.api.message.MessageTuple einsteinMessage = JGroupsUtil.convert(context.getActiveProfile(), tuple.getExecutionCorrelation(), message, payload);
                    log.debug("Converted to {0}", einsteinMessage);
                    listener.handle(einsteinMessage);
                } catch (Exception e) {
                    //TODO:Pass this to the exception handler in the context.
                    context.getActiveProfile().getExceptionModel().handleRootlessException(e);
                    log.fatal(e);
                }
            }
        });
    }

    public void receive(ListenContext context, MessageTuple tuple, boolean payload) {

    }
}
