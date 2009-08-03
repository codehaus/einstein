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

package org.cauldron.einstein.ri.core.model.message.simple;

import org.cauldron.einstein.api.message.Message;
import org.cauldron.einstein.api.message.MessageModel;
import org.cauldron.einstein.api.message.MessageProperties;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.message.action.context.ActionCallbackContext;
import org.cauldron.einstein.api.message.action.types.MergeAction;
import org.cauldron.einstein.api.message.correlation.Correlation;
import org.cauldron.einstein.api.message.data.model.DataModel;
import org.cauldron.einstein.api.message.data.model.DataObject;
import org.cauldron.einstein.api.message.view.composites.MergeView;
import org.cauldron.einstein.ri.core.model.message.properties.SimpleMessageProperties;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Neil Ellis
 */

@org.contract4j5.contract.Contract
public class SimpleMessageModel implements MessageModel {

    public MessageTuple createExternalisableTuple(MessageTuple tuple) {
        return tuple.deepDuplicate();
    }


    public Message createMessage(Correlation executionCorrelation, DataObject dataObject) {
        return new SimpleMessage(executionCorrelation, new SimpleMessageProperties(), dataObject);
    }


    public Message createMessage(Correlation executionCorrelation, MessageProperties properties, DataObject object) {
        return new SimpleMessage(executionCorrelation, properties, object);
    }


    public Message createNullMessage(Correlation executionCorrelation) {
        return new NullSimpleMessage(executionCorrelation, new SimpleMessageProperties());
    }


    public MessageProperties createProperties(Map<String, Object> map) {
        return new SimpleMessageProperties(map);
    }


    public MessageTuple createTuple(DataModel dataModel, List<MessageTuple>... members) {
        List<MessageTuple> result = new ArrayList<MessageTuple>();
        for (List<MessageTuple> member : members) {
            result.addAll(member);
        }
        return new ListMessageTuple(this, dataModel, result);
    }


    public MessageTuple createTuple(DataModel dataModel, Map<Object, MessageTuple>... members) {
        Map<Object, MessageTuple> result = new LinkedHashMap<Object, MessageTuple>();
        for (Map<Object, MessageTuple> member : members) {
            result.putAll(member);
        }
        return new NamedMessageTuple(this, dataModel, result);
    }


    public MessageTuple createTuple(DataModel dataModel, MessageTuple... members) {
        return new ListMessageTuple(this, dataModel, members);
    }


    public Message createVoidMessage(Correlation executionCorrelation) {
        return new VoidSimpleMessage(executionCorrelation);
    }


    public Message merge(MessageTuple tuple, DataModel dataModel) {
        final List<MessageTuple> list = tuple.realiseAsList();
        if (list.size() == 0) {
            return createEmptyMessage(tuple.getExecutionCorrelation());
        }

        Message first = list.get(0).realiseAsOne();

        final List<DataObject> payloads = new ArrayList<DataObject>();
        for (MessageTuple item : list) {
            final Message message = item.realiseAsOne();
            if (!message.isEmpty()) {
                message.execute(MergeAction.class, new MergeAction() {

                    public void handle(ActionCallbackContext<MergeView, Object> mergeMessageViewActionCallbackContext) {
                        payloads.add(mergeMessageViewActionCallbackContext.getMessageHistory()
                                .getCurrentEntry()
                                .getNewValue().getPayload());
                    }
                });
            }
        }
        return createMessage(first.getExecutionCorrelation(), dataModel.getDataObjectFactory().createDataList(payloads).asDataObject());
    }


    public Message createEmptyMessage(Correlation executionCorrelation) {
        return new EmptySimpleMessage(executionCorrelation);
    }
}
