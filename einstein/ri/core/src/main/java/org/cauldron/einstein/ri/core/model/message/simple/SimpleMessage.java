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
import org.cauldron.einstein.api.common.history.History;
import org.cauldron.einstein.api.message.Message;
import org.cauldron.einstein.api.message.MessageProperties;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.message.action.Action;
import org.cauldron.einstein.api.message.action.MessageActionResult;
import org.cauldron.einstein.api.message.action.context.ActionCallbackContext;
import org.cauldron.einstein.api.message.correlation.Correlation;
import org.cauldron.einstein.api.message.correlation.CorrelationParticipant;
import org.cauldron.einstein.api.message.data.model.DataModel;
import org.cauldron.einstein.api.message.data.model.DataObject;
import org.cauldron.einstein.api.message.exception.MessageDuplicationException;
import org.cauldron.einstein.api.message.view.ExceptionState;
import org.cauldron.einstein.api.message.view.MessageView;
import org.cauldron.einstein.api.message.view.SecurityState;
import org.cauldron.einstein.api.message.view.TransactionState;
import org.cauldron.einstein.api.message.view.composites.CombinedView;
import org.cauldron.einstein.ri.core.exception.UnsupportedMessageViewRuntimeException;
import org.cauldron.einstein.ri.core.exception.UnsupportedOperationEinsteinRuntimeException;
import org.cauldron.einstein.ri.core.model.correlation.SimpleCorrelationParticipant;
import org.cauldron.einstein.ri.core.runtime.EinsteinRIRuntimeFactory;
import org.contract4j5.contract.Invar;
import org.contract4j5.contract.Post;
import org.contract4j5.contract.Pre;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Neil Ellis
 */

@org.contract4j5.contract.Contract
public class SimpleMessage implements Message, CombinedView {

    @Invar
    private final DataObject dataObject;

    @Invar
    private MessageProperties properties;

    private final EmptyHistory<CombinedView> mockMessageEmptyHistory;

    @Invar
    private final SimpleCorrelationParticipant correlationParticipant;

    //TODO: make this package friendly

    @Pre
    public SimpleMessage(Correlation executionCorrelation, MessageProperties properties, DataObject dataObject) {
        this.properties = properties;
        this.dataObject = dataObject;
        mockMessageEmptyHistory = new EmptyHistory<CombinedView>(this);
        correlationParticipant = new SimpleCorrelationParticipant();
        setExecutionCorrelation(executionCorrelation);
    }


    @Pre
    public void setExecutionCorrelation(Correlation executionCorrelation) {
        correlationParticipant.setExecutionCorrelation(executionCorrelation);
    }


    private SimpleMessage(Correlation executionCorrelation, MessageProperties properties, DataObject dataObject,
                          SimpleCorrelationParticipant correlationParticipant) {
        this.dataObject = dataObject;
        this.properties = properties;
        this.correlationParticipant = correlationParticipant;
        mockMessageEmptyHistory = new EmptyHistory<CombinedView>(this);
        setExecutionCorrelation(executionCorrelation);
    }


    @Pre
    public void addProperty(String name, Object value) {
        properties.put(name, value);
    }


    @Pre
    public void setProperties(MessageProperties properties) {
        this.properties = properties;
    }


    @Post
    public MessageTuple deepDuplicate() throws MessageDuplicationException {
        return new SimpleMessage(getExecutionCorrelation(), properties, dataObject.duplicate(), correlationParticipant);
    }


    @Post
    public Correlation getExecutionCorrelation() {
        return correlationParticipant.getExecutionCorrelation();
    }


    @Post
    public Message duplicate() {
        return new SimpleMessage(getExecutionCorrelation(), properties, dataObject, correlationParticipant);
    }


    @Post
    public CorrelationParticipant getCorrelationParticipant() {
        return correlationParticipant;
    }



    public boolean isEmpty() {
        return false;
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


    @Pre @Post
    public List<MessageTuple> realiseAsList() {
        return Collections.singletonList((MessageTuple) this);
    }


    @Pre @Post
    public Map<?, MessageTuple> realiseAsMap() {
        throw new UnsupportedOperationEinsteinRuntimeException("realiseAsMap()", "SimpleMessage");
    }


    @Post
    public Message realiseAsOne() {
        return this;
    }


    @Post
    public int size() {
        return 1;
    }


    @Pre @Post
    public Message duplicateWithNewDataModel(Class<? extends DataModel> newDataModell) {
        return new SimpleMessage(getExecutionCorrelation(),
                                 properties,
                                 EinsteinRIRuntimeFactory.getInstance()
                                         .getRuntime()
                                         .getRosettaStone().convert(newDataModell, dataObject),
                                 correlationParticipant);
    }


    @Pre @Post
    public Message duplicateWithNewPayload(DataObject payload) {
        return new SimpleMessage(getExecutionCorrelation(), properties, payload, correlationParticipant);
    }


    @Pre
    public <MV extends MessageView, RV, A extends Action<MV, RV>> MessageActionResult execute(
            final Class<A> actionClass,
            A action) {
        final MessageActionResult[] returnResult = new MessageActionResult[]{null};
        action.handle(new ActionCallbackContext<MV, RV>() {

            public void setActionResult(MessageActionResult<RV> result) {
                returnResult[0] = result;
            }


            public History<MV> getMessageHistory() {
                try {
                    return (History<MV>) mockMessageEmptyHistory;
                } catch (ClassCastException e) {
                    throw new UnsupportedMessageViewRuntimeException(actionClass);
                }
            }
        });
        return returnResult[0];
    }


    @Pre
    public Object get(String name) {
        return properties.get(name);
    }


    @Post
    public MessageProperties getProperties() {
        return properties;
    }


    public Object getProperty(String name) {
        return properties.get(name);
    }


    public ExceptionState getException() {
        return null; //TODO
    }


    @Post
    public DataObject getPayload() {
        return dataObject;
    }


    public SecurityState getSecurity() {
        return null; //TODO
    }


    public TransactionState getTransaction() {
        return null; //TODO
    }


    public void setException(ExceptionState state) {
        //TODO
    }


    public void setSecurity(SecurityState state) {
        //TODO
    }


    public void setTransaction(TransactionState state) {
        //TODO
    }


    @Post
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
