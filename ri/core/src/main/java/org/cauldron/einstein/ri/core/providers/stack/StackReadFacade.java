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

package org.cauldron.einstein.ri.core.providers.stack;


import org.cauldron.einstein.api.message.Message;
import org.cauldron.einstein.api.message.MessageModel;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.message.data.model.DataModel;
import org.cauldron.einstein.api.message.data.model.DataObject;
import org.cauldron.einstein.api.model.profile.Profile;
import org.cauldron.einstein.api.provider.facade.ReadFacade;
import org.cauldron.einstein.api.provider.facade.context.ReadContext;
import org.cauldron.einstein.api.provider.facade.exception.ReadFailureRuntimeException;
import org.cauldron.einstein.ri.core.log.Logger;
import org.cauldron.einstein.ri.core.providers.AbstractFacade;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class StackReadFacade extends AbstractFacade implements ReadFacade {

    private static final Logger log = Logger.getLogger(StackReadFacade.class);

    private final List stack;

    public StackReadFacade(List stack) {
        this.stack = stack;
    }


    public MessageTuple read(ReadContext context, boolean all, boolean payload) throws ReadFailureRuntimeException {
        return readInternal(context, all, payload);
    }


    private MessageTuple readInternal(ReadContext context, boolean all, boolean payload) {
        log.debug("Reading from stack {0}.", stack);
        final Profile profile = context.getActiveProfile();
        final MessageModel messageModel = profile.getMessageModel();
        final DataModel dataModel = profile.getDataModel();
        if (payload) {
            if (all) {
                List<MessageTuple> result = new ArrayList<MessageTuple>();
                synchronized (stack) {
                    for (Object item : stack) {
                        DataObject object = dataModel.getDataObjectFactory().createDataObject(item);
                        Message newMessage = messageModel.createMessage(context.getMessage().getExecutionCorrelation(),
                                                                        object);
                        result.add(newMessage);
                    }
                    stack.clear();
                }
                return messageModel.createTuple(dataModel, result);
            } else {
                synchronized (stack) {
                    if (stack.size() == 0) {
                        return messageModel.createVoidMessage(context.getMessage().getExecutionCorrelation());
                    }
                    DataObject object = dataModel.getDataObjectFactory()
                            .createDataObject(stack.remove(stack.size() - 1));
                    return messageModel.createMessage(context.getMessage().getExecutionCorrelation(), object);
                }
            }
        } else {
            if (all) {
                synchronized (stack) {
                    MessageTuple tuple = messageModel.createTuple(dataModel, stack);
                    stack.clear();
                    return tuple;
                }
            } else {
                synchronized (stack) {
                    if (stack.size() == 0) {
                        return messageModel.createVoidMessage(context.getMessage().getExecutionCorrelation());
                    }
                    return (MessageTuple) stack.remove(stack.size() - 1);
                }
            }
        }
    }


    public MessageTuple read(ReadContext context, boolean all, boolean payload, long timeout) throws
                                                                                              ReadFailureRuntimeException {
        return readInternal(context, all, payload);
    }


    public MessageTuple readNoWait(ReadContext context, boolean all, boolean payload) throws
                                                                                      ReadFailureRuntimeException {
        return readInternal(context, all, payload);
    }
}
