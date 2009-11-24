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
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.message.correlation.Correlation;
import org.cauldron.einstein.api.message.correlation.CorrelationParticipant;
import org.cauldron.einstein.api.message.data.model.DataModel;
import org.cauldron.einstein.api.message.exception.MessageDuplicationException;
import org.cauldron.einstein.ri.core.model.correlation.SimpleCorrelationParticipant;
import org.contract4j5.contract.Post;

import java.util.*;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class NamedMessageTuple implements MessageTuple {

    private final Map<Object, MessageTuple> map = new LinkedHashMap<Object, MessageTuple>();
    private final MessageModel model;
    private final CorrelationParticipant correlationParticipant;
    private final DataModel dataModel;

    private NamedMessageTuple(MessageModel model, DataModel dataModel) {
        this.model = model;
        this.dataModel = dataModel;
        correlationParticipant = new SimpleCorrelationParticipant();
    }


    NamedMessageTuple(MessageModel model, DataModel dataModel, Map<Object, MessageTuple> entries) {
        this.model = model;
        this.dataModel = dataModel;
        correlationParticipant = new SimpleCorrelationParticipant();
        map.putAll(entries);
    }


    public MessageTuple deepDuplicate() throws MessageDuplicationException {
        Set<Map.Entry<Object, MessageTuple>> entries = map.entrySet();
        Map<Object, MessageTuple> newMap = new HashMap<Object, MessageTuple>();
        for (Map.Entry<?, MessageTuple> entry : entries) {
            newMap.put(entry.getKey(), entry.getValue().deepDuplicate());
        }
        return new NamedMessageTuple(model, dataModel, newMap);
    }


    public MessageTuple duplicate() {
        NamedMessageTuple messageGroup = new NamedMessageTuple(model, dataModel);
        messageGroup.map.putAll(map);
        return messageGroup;
    }


    public CorrelationParticipant getCorrelationParticipant() {
        return correlationParticipant;
    }


    @Post
    public Correlation getExecutionCorrelation() {
        return correlationParticipant.getExecutionCorrelation();
    }


    public boolean isEmpty() {
        return map.isEmpty();
    }


    public boolean isList() {
        return true;
    }


    public boolean isMap() {
        return true;
    }


    public boolean isOne() {
        return false;
    }


    public boolean isVoid() {
        return false;
    }


    @Post
    public List<MessageTuple> realiseAsList() {
        ArrayList<MessageTuple> list = new ArrayList<MessageTuple>();
        list.addAll(map.values());
        return list;
    }

    @Post
    public Map<?, MessageTuple> realiseAsMap() {
        return map;
    }


    @Post
    public Message realiseAsOne() {
        return model.merge(this, dataModel);
    }


    public void setExecutionCorrelation(Correlation executionCorrelation) {
        correlationParticipant.setExecutionCorrelation(executionCorrelation);
    }

     @Post
    public int size() {
        return map.size();
    }
}
