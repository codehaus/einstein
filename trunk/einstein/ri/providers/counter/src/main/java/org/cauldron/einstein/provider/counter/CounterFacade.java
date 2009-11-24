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

package org.cauldron.einstein.provider.counter;


import org.cauldron.einstein.api.message.MessageListener;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.message.action.context.ActionCallbackContext;
import org.cauldron.einstein.api.message.action.types.ReadAction;
import org.cauldron.einstein.api.message.data.model.DataObject;
import org.cauldron.einstein.api.message.view.composites.ReadOnlyView;
import org.cauldron.einstein.api.model.resource.ResourceRef;
import org.cauldron.einstein.api.provider.facade.*;
import org.cauldron.einstein.api.provider.facade.context.ReadContext;
import org.cauldron.einstein.api.provider.facade.context.WriteContext;
import org.cauldron.einstein.api.provider.facade.context.ListenContext;
import org.cauldron.einstein.api.provider.facade.exception.WriteFailureRuntimeException;
import org.cauldron.einstein.api.provider.facade.exception.ReadFailureRuntimeException;
import org.cauldron.einstein.ri.core.providers.AbstractFacade;
import org.cauldron.einstein.ri.core.log.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Neil Ellis
 */
public class CounterFacade extends AbstractFacade implements BooleanQueryFacade, ReadFacade, ListenFacade, WriteFacade {
     private static final Logger log = Logger.getLogger(CounterFacade.class);
    private final CounterResource counterResource;

    public CounterFacade(CounterResource counterResource) {
        this.counterResource = counterResource;
    }

    public boolean match( DataObject o) {
        log.debug("Matching");
        return !counterResource.isExpired();
    }


    public MessageTuple read( ReadContext context, boolean all, boolean payload, long timeout) throws ReadFailureRuntimeException {
        return read(context, all, payload);
    }


    public MessageTuple read( ReadContext context, boolean all, boolean payload) throws ReadFailureRuntimeException {
        if (all) {
            List<MessageTuple> list = new ArrayList<MessageTuple>();
            while (true) {
                DataObject object = context.getActiveProfile().getDataModel().getDataObjectFactory().createDataObject(counterResource.getCounter());
                list.add(context.getActiveProfile().getMessageModel().createMessage(context.getMessage().getExecutionCorrelation(), object));
                if(counterResource.isExpired()) {
                    break;
                }
                counterResource.count();
            }
            return context.getActiveProfile().getMessageModel().createTuple(context.getActiveProfile().getDataModel(), list);
        } else {
            DataObject object = context.getActiveProfile().getDataModel().getDataObjectFactory().createDataObject(counterResource.getCounter());
            counterResource.count();
            return context.getActiveProfile().getMessageModel().createMessage(context.getMessage().getExecutionCorrelation(), object);
        }
    }


    public MessageTuple readNoWait( ReadContext context, boolean all, boolean payload) throws ReadFailureRuntimeException {
        return read(context, all, payload);
    }

    public void listen(ListenContext context, MessageTuple tuple, ResourceRef filter, boolean message,  MessageListener listener) throws ReadFailureRuntimeException {
        if (filter != null && filter.getURI().getProviderName().equals("text")) {
            counterResource.listeners.add(new ValueListener(Integer.parseInt(filter.getURI().getDescriptor().asString()), listener, context,
                                                            tuple));
        } else {
            counterResource.listeners.add(new EverythingListener(listener, context, tuple));

        }
    }

    public MessageTuple write(WriteContext context, MessageTuple tuple, boolean payload) throws WriteFailureRuntimeException {
        tuple.realiseAsOne().execute(ReadAction.class, new ReadAction() {
            public void handle(ActionCallbackContext<ReadOnlyView, Object> ctx) {
                counterResource.setCounter(Integer.parseInt(ctx.getMessageHistory().getCurrentEntry().getNewValue().getPayload().asString()));
            }
        });
        return tuple;
    }

    public MessageTuple write(WriteContext context, MessageTuple tuple, boolean payload, long timeout) throws WriteFailureRuntimeException {
        return write(context, tuple, payload);
    }

    public void receive(ListenContext context, MessageTuple tuple, boolean payload) {
        //ignore
    }


    interface CounterListener {

        void handle(int counter);

    }

    class ValueListener implements CounterListener {
        private final int value;
        private final MessageListener listener;
        private final ListenContext context;
        private MessageTuple tuple;

        public ValueListener(int i, MessageListener listener, ListenContext context, MessageTuple tuple) {
            value = i;
            this.listener = listener;
            this.context = context;
            this.tuple = tuple;
        }


        public void handle(int counter) {
            if (counter == value) {
                DataObject object = context.getActiveProfile().getDataModel().getDataObjectFactory().createDataObject(counter);
                listener.handle(context.getActiveProfile().getMessageModel().createMessage(tuple.getExecutionCorrelation(), object));
            }
        }
    }

    class EverythingListener implements CounterListener {
        private final MessageListener listener;
        private final ListenContext context;
        private MessageTuple tuple;

        public EverythingListener(MessageListener listener, ListenContext context, MessageTuple tuple) {

            this.listener = listener;
            this.context = context;
            this.tuple = tuple;
        }


        public void handle(int counter) {
            DataObject object = context.getActiveProfile().getDataModel().getDataObjectFactory().createDataObject(counter);
            listener.handle(context.getActiveProfile().getMessageModel().createMessage(tuple.getExecutionCorrelation(), object));
        }
    }


}
