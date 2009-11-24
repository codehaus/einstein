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
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.provider.facade.WriteFacade;
import org.cauldron.einstein.api.provider.facade.context.WriteContext;
import org.cauldron.einstein.api.provider.facade.exception.WriteFailureRuntimeException;
import org.cauldron.einstein.ri.core.providers.AbstractFacade;
import org.cauldron.einstein.ri.core.log.Logger;
import org.cauldron.einstein.ri.provider.jgroups.JGroupsResource;
import org.cauldron.einstein.ri.provider.jgroups.JGroupsUtil;
import org.jgroups.ChannelClosedException;
import org.jgroups.ChannelNotConnectedException;

/**
 * @author Neil Ellis
 */
@Contract
public class JGroupsWriteFacade extends AbstractFacade implements WriteFacade {
    private static final Logger log = Logger.getLogger(JGroupsWriteFacade.class);
    private final JGroupsResource resource;

    public JGroupsWriteFacade(JGroupsResource resource, EinsteinURI uri) {

        this.resource = resource;
    }

    public MessageTuple write(WriteContext context, MessageTuple tuple, boolean payload) throws WriteFailureRuntimeException {
        log.debug("Writing {0}", tuple);
        try {
            resource.getChannel().send(JGroupsUtil.convert(tuple, payload));
        } catch (ChannelNotConnectedException e) {
            throw new WriteFailureRuntimeException(e);
        } catch (ChannelClosedException e) {
            throw new WriteFailureRuntimeException(e);
        }
        return context.getActiveProfile().getMessageModel().createEmptyMessage(tuple.getExecutionCorrelation());
    }

    public MessageTuple write(WriteContext context, MessageTuple tuple, boolean payload, long timeout) throws WriteFailureRuntimeException {
        return write(context, tuple, payload);
    }

   
}
