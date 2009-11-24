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

import org.contract4j5.contract.*;

import org.cauldron.einstein.api.common.euri.EinsteinURI;
import org.cauldron.einstein.api.message.Message;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.provider.facade.ReadFacade;
import org.cauldron.einstein.api.provider.facade.context.ReadContext;
import org.cauldron.einstein.api.provider.facade.exception.ReadFailureRuntimeException;
import org.cauldron.einstein.ri.core.providers.AbstractFacade;
import org.cauldron.einstein.ri.provider.jgroups.JGroupsResource;
import org.cauldron.einstein.ri.provider.jgroups.JGroupsUtil;
import org.jgroups.ReceiverAdapter;

/**
 * @author Neil Ellis
 */
@Contract
public class JGroupsReadFacade extends AbstractFacade implements ReadFacade {
    private final JGroupsResource resource;
    private static final int NOMINAL_NOWAIT_TIME = 10;

    public JGroupsReadFacade(JGroupsResource resource, EinsteinURI uri) {
        this.resource = resource;
    }

    @Pre
    public MessageTuple read(@Pre final ReadContext context, boolean all, final boolean payload, long timeout) throws ReadFailureRuntimeException {
        final MessageTuple[] result = new Message[1];
        final Object lock = new Object();
        synchronized (lock) {
            resource.getChannel().setReceiver(new ReceiverAdapter() {
                @Override
                public void receive(org.jgroups.Message message) {
                    super.receive(message);
                    result[0] = JGroupsUtil.convert(context.getActiveProfile(), context.getMessage().getExecutionCorrelation(), message, payload);
                    lock.notify();
                }
            });
            try {
                if (timeout > 0) {
                    lock.wait(timeout);
                } else {
                    lock.wait();
                }
            } catch (InterruptedException e) {
                throw new ReadFailureRuntimeException(e);
            }
        }
        if (result[0] == null) {
            result[0] = context.getActiveProfile().getMessageModel().createVoidMessage(context.getMessage().getExecutionCorrelation());
        }
        return result[0];
    }

    @Pre
    public MessageTuple read(@Pre final ReadContext context, boolean all, final boolean payload) throws ReadFailureRuntimeException {
        return read(context, all, payload, -1);
    }

    @Pre
    public MessageTuple readNoWait(@Pre final ReadContext context, boolean all, final boolean payload) throws ReadFailureRuntimeException {
        return read(context, all, payload, NOMINAL_NOWAIT_TIME);
    }
}
