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

package org.cauldron.einstein.ri.provider.jgroups;

import org.contract4j5.contract.*;
import org.cauldron.einstein.api.common.euri.EinsteinURI;
import org.cauldron.einstein.api.model.lifecycle.LifecycleContext;
import org.cauldron.einstein.api.model.lifecycle.exception.InitializationRuntimeException;
import org.cauldron.einstein.api.model.lifecycle.exception.StartRuntimeException;
import org.cauldron.einstein.api.model.resource.Resource;
import org.cauldron.einstein.api.provider.facade.ListenFacade;
import org.cauldron.einstein.api.provider.facade.ReadFacade;
import org.cauldron.einstein.api.provider.facade.WriteFacade;
import org.cauldron.einstein.ri.core.providers.base.AbstractResource;
import org.cauldron.einstein.ri.core.log.Logger;
import org.cauldron.einstein.ri.provider.jgroups.facade.JGroupsListenFacade;
import org.cauldron.einstein.ri.provider.jgroups.facade.JGroupsReadFacade;
import org.cauldron.einstein.ri.provider.jgroups.facade.JGroupsWriteFacade;
import org.jgroups.ChannelException;
import org.jgroups.JChannel;

import java.util.Arrays;
import java.util.List;

/**
 * @author Neil Ellis
 */
@Contract
public class JGroupsResource extends AbstractResource implements Resource {

   private static final Logger log = Logger.getLogger(JGroupsResource.class);
    private JChannel channel;
    private final String channelName;

    public JGroupsResource(EinsteinURI uri, Class[] facades) {
        super(uri);

        List<Class> facadeList = Arrays.asList(facades);
        channelName = uri.getDescriptor().asURLLocation().getPath().getPathElements()[0];

        if (facadeList.contains(ListenFacade.class)) {
            addMapping(ListenFacade.class, new JGroupsListenFacade(this, uri));
        }
        if (facadeList.contains(ReadFacade.class)) {
            addMapping(ReadFacade.class, new JGroupsReadFacade(this, uri));
        }
        if (facadeList.contains(WriteFacade.class)) {
            addMapping(WriteFacade.class, new JGroupsWriteFacade(this, uri));
        }
    }

    @Override
    public void init(LifecycleContext context) {
        try {
            channel = new JChannel();
        } catch (ChannelException e) {
            throw new InitializationRuntimeException(e);
        }
        super.init(context);
    }

    @Override
    public void start(LifecycleContext context) {
        try {
            log.info("Starting jGroups channel {0}.", channelName);
            channel.connect(channelName);
            
        } catch (ChannelException e) {
            throw new StartRuntimeException(e);
        }
        super.start(context);
    }

    @Override
    public void stop(LifecycleContext context) {
        channel.disconnect();
        super.stop(context);
    }

    @Override
    public void destroy(LifecycleContext context) {
        channel.close();
        super.destroy(context);
    }

     public JChannel getChannel() {
        return channel;
    }
}
