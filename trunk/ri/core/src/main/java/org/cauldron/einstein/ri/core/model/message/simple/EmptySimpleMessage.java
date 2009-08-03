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
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.message.action.Action;
import org.cauldron.einstein.api.message.action.MessageActionResult;
import org.cauldron.einstein.api.message.correlation.Correlation;
import org.cauldron.einstein.api.message.correlation.CorrelationParticipant;
import org.cauldron.einstein.api.message.data.model.DataModel;
import org.cauldron.einstein.api.message.view.MessageView;
import org.cauldron.einstein.ri.core.exception.UnsupportedOperationEinsteinRuntimeException;
import org.cauldron.einstein.ri.core.model.correlation.SimpleCorrelationParticipant;
import org.contract4j5.contract.Pre;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class EmptySimpleMessage implements Message {

    private final CorrelationParticipant participant = new SimpleCorrelationParticipant();

    @Pre
    public EmptySimpleMessage(Correlation executionCorrelation) {
        participant.setExecutionCorrelation(executionCorrelation);
    }


    public Message duplicateWithNewDataModel(Class<? extends DataModel> newDataModell) {
        return this;
    }


    public Correlation getExecutionCorrelation() {
        return participant.getExecutionCorrelation();
    }


    public <MV extends MessageView, RV, A extends Action<MV, RV>> MessageActionResult<RV> execute(Class<A> actionClass,
                                                                                                  A action) {
        //TODO: Make this a sensible message.
        throw new UnsupportedOperationEinsteinRuntimeException("execute", "EmptySimpleMessage");
    }


    public MessageTuple deepDuplicate() {
        return this;
    }


    public MessageTuple duplicate() {
        return this;
    }


    public CorrelationParticipant getCorrelationParticipant() {
        return participant;
    }


    public boolean isEmpty() {
        return true;
    }


    public boolean isList() {
        return true;
    }


    public boolean isMap() {
        return false;
    }


    public boolean isOne() {
        return true;
    }


    public boolean isVoid() {
        return false;
    }


    public List<MessageTuple> realiseAsList() {
        return Collections.singletonList((MessageTuple) this);
    }


    public Map<?, MessageTuple> realiseAsMap() {
        throw new UnsupportedOperationEinsteinRuntimeException("realiseAsMap", "EmptySimpleMessage");
    }


    public Message realiseAsOne() {
        return this;
    }


    public void setExecutionCorrelation(Correlation executionCorrelation) {
        participant.setExecutionCorrelation(executionCorrelation);
    }


    public int size() {
        return 0;
    }
}
