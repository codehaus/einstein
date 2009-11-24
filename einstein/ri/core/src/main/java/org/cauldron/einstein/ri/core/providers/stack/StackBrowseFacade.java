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
import org.cauldron.einstein.api.message.correlation.Correlation;
import org.cauldron.einstein.api.message.data.model.DataModel;
import org.cauldron.einstein.api.message.data.model.DataObject;
import org.cauldron.einstein.api.message.data.model.DataObjectFactory;
import org.cauldron.einstein.api.model.profile.Profile;
import org.cauldron.einstein.api.provider.facade.BrowseFacade;
import org.cauldron.einstein.api.provider.facade.context.ReadContext;
import org.cauldron.einstein.ri.core.providers.AbstractFacade;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class StackBrowseFacade extends AbstractFacade implements BrowseFacade {

    private final List stack;

    public StackBrowseFacade(List stack) {
        this.stack = stack;
    }


    public MessageTuple readNoWait(ReadContext context, boolean all, boolean payload) {
        final Profile profile = context.getActiveProfile();
        final MessageModel messageModel = profile.getMessageModel();
        final DataModel dataModel = profile.getDataModel();
        if (payload) {
            final Correlation executionCorrelation = context.getMessage().getExecutionCorrelation();
            final DataObjectFactory dataObjectFactory = dataModel.getDataObjectFactory();

            if (all) {
                List<MessageTuple> result = new ArrayList<MessageTuple>();
                for (Object item : stack) {
                    DataObject object = dataObjectFactory.createDataObject(item);
                    Message newMessage = messageModel.createMessage(executionCorrelation, object);
                    result.add(newMessage);
                }
                return messageModel.createTuple(dataModel, result);
            } else {
                synchronized (stack) {
                    DataObject object = dataObjectFactory.createDataObject(stack.get(stack.size() - 1));
                    return messageModel.createMessage(executionCorrelation, object);
                }
            }
        } else {
            if (all) {
                return messageModel.createTuple(dataModel, stack);
            } else {
                synchronized (stack) {
                    return (MessageTuple) stack.get(stack.size() - 1);
                }
            }
        }
    }
}