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


import org.apache.commons.lang.builder.ToStringBuilder;
import org.cauldron.einstein.api.message.Message;
import org.cauldron.einstein.api.message.MessageModel;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.message.correlation.Correlation;
import org.cauldron.einstein.api.message.correlation.CorrelationParticipant;
import org.cauldron.einstein.api.message.data.model.DataModel;
import org.cauldron.einstein.api.message.exception.MessageDuplicationException;
import org.cauldron.einstein.ri.core.exception.UnsupportedOperationEinsteinRuntimeException;
import org.cauldron.einstein.ri.core.model.correlation.SimpleCorrelationParticipant;
import org.contract4j5.contract.Post;

import java.util.*;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class ListMessageTuple implements MessageTuple {

    private final List<MessageTuple> list = new ArrayList<MessageTuple>();
    private final MessageModel messageModel;
    private final SimpleCorrelationParticipant simpleCorrelationParticipant;
    private final DataModel dataModel;

    private ListMessageTuple(MessageModel messageModel, SimpleCorrelationParticipant participant, DataModel dataModel) {
        this.messageModel = messageModel;
        this.dataModel = dataModel;
        simpleCorrelationParticipant = new SimpleCorrelationParticipant();
    }


    ListMessageTuple(MessageModel messageModel, DataModel dataModel, MessageTuple... entries) {
        this.messageModel = messageModel;
        this.dataModel = dataModel;
        simpleCorrelationParticipant = new SimpleCorrelationParticipant();
        list.addAll(Arrays.asList(entries));
    }


    ListMessageTuple(MessageModel messageModel, DataModel dataModel, List<MessageTuple> entries) {
        this.messageModel = messageModel;
        this.dataModel = dataModel;
        simpleCorrelationParticipant = new SimpleCorrelationParticipant();
        list.addAll(entries);
    }


    private ListMessageTuple(MessageModel messageModel, DataModel dataModel, List<MessageTuple> entries,
                             SimpleCorrelationParticipant participant) {
        this.messageModel = messageModel;
        this.dataModel = dataModel;
        simpleCorrelationParticipant = participant;
        list.addAll(entries);
    }


    public MessageTuple deepDuplicate() throws MessageDuplicationException {
        List<MessageTuple> newList = new ArrayList<MessageTuple>();
        for (MessageTuple tuple : list) {
            newList.add(tuple.deepDuplicate());
        }
        return new ListMessageTuple(messageModel, dataModel, newList, simpleCorrelationParticipant);
    }


    public MessageTuple duplicate() {
        ListMessageTuple messageGroup = new ListMessageTuple(messageModel, simpleCorrelationParticipant, dataModel);
        messageGroup.addAll(list);
        return messageGroup;
    }


    private ListMessageTuple addAll(Collection<? extends MessageTuple> messageGroups) {
        //We do this to force a class cast error if the contents are not as they should be.
        //and to ensure no voids are added by accident.
        for (MessageTuple tuple : messageGroups) {
            add(tuple);
        }
        return this;
    }


    ListMessageTuple add(MessageTuple tuple) {
        if (!tuple.isVoid()) {
            list.add(tuple);
        }
        return this;
    }


    public CorrelationParticipant getCorrelationParticipant() {
        return simpleCorrelationParticipant;
    }


    public Correlation getExecutionCorrelation() {
        return simpleCorrelationParticipant.getExecutionCorrelation();
    }


    public boolean isEmpty() {
        return list.isEmpty();
    }


    public boolean isList() {
        return true;
    }


    public boolean isMap() {
        return false;
    }


    public boolean isOne() {
        return size() == 1 && list.get(0).isOne();
    }


    @Post
    public int size() {
        return list.size();
    }


    public boolean isVoid() {
        return false;
    }


    public List<MessageTuple> realiseAsList() {
        return list;
    }


    public Map<String, MessageTuple> realiseAsMap() {
        throw new UnsupportedOperationEinsteinRuntimeException("realiseAsMap()", "ListMessageGroup");
    }


    public Message realiseAsOne() {
        return messageModel.merge(this, dataModel);
    }


    public void setExecutionCorrelation(Correlation executionCorrelation) {
        simpleCorrelationParticipant.setExecutionCorrelation(executionCorrelation);
    }


    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
