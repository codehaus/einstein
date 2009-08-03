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

package org.cauldron.einstein.ri.core.model.message.group;


import org.cauldron.einstein.api.message.Message;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.message.correlation.Correlation;
import org.cauldron.einstein.api.message.correlation.CorrelationParticipant;
import org.cauldron.einstein.api.message.exception.MessageDuplicationException;
import org.cauldron.einstein.api.model.execution.ExecutionFailureRuntimeException;
import org.cauldron.einstein.ri.core.model.correlation.SimpleCorrelationParticipant;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class FutureMessageTuple implements MessageTuple {

    private final Future<MessageTuple> messageGroupFutureTask;
    private final CorrelationParticipant correlationParticipant;

    private FutureMessageTuple(Future<MessageTuple> messageGroupFutureTask) {
        this.messageGroupFutureTask = messageGroupFutureTask;
        correlationParticipant = new SimpleCorrelationParticipant();
    }


    public MessageTuple deepDuplicate() {
        try {
            return messageGroupFutureTask.get();
        } catch (InterruptedException e) {
            throw new MessageDuplicationException(e);
        } catch (ExecutionException e) {
            throw new MessageDuplicationException(e);
        }
    }


    public MessageTuple duplicate() {
        return new FutureMessageTuple(messageGroupFutureTask);
    }


    public CorrelationParticipant getCorrelationParticipant() {
        return correlationParticipant;
    }


    public Correlation getExecutionCorrelation() {
        return correlationParticipant.getExecutionCorrelation();
    }


    public boolean isEmpty() {
        try {
            return messageGroupFutureTask.get().isEmpty();
        } catch (InterruptedException e) {
            throw new ExecutionFailureRuntimeException(e);
        } catch (ExecutionException e) {
            throw new ExecutionFailureRuntimeException(e);
        }
    }


    public boolean isList() {
        try {
            return messageGroupFutureTask.get().isList();
        } catch (InterruptedException e) {
            throw new ExecutionFailureRuntimeException(e);
        } catch (ExecutionException e) {
            throw new ExecutionFailureRuntimeException(e);
        }
    }


    public boolean isMap() {
        try {
            return messageGroupFutureTask.get().isMap();
        } catch (InterruptedException e) {
            throw new ExecutionFailureRuntimeException(e);
        } catch (ExecutionException e) {
            throw new ExecutionFailureRuntimeException(e);
        }
    }


    public boolean isOne() {
        try {
            return messageGroupFutureTask.get().isOne();
        } catch (InterruptedException e) {
            throw new ExecutionFailureRuntimeException(e);
        } catch (ExecutionException e) {
            throw new ExecutionFailureRuntimeException(e);
        }
    }


    public boolean isVoid() {
        try {
            return messageGroupFutureTask.get().isVoid();
        } catch (InterruptedException e) {
            throw new ExecutionFailureRuntimeException(e);
        } catch (ExecutionException e) {
            throw new ExecutionFailureRuntimeException(e);
        }
    }


    public List<MessageTuple> realiseAsList() {
        try {
            return messageGroupFutureTask.get().realiseAsList();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            throw new ExecutionFailureRuntimeException(e);
        }
    }


    public Map<?, MessageTuple> realiseAsMap() {
        try {
            return messageGroupFutureTask.get().realiseAsMap();
        } catch (InterruptedException e) {
            throw new ExecutionFailureRuntimeException(e);
        } catch (ExecutionException e) {
            throw new ExecutionFailureRuntimeException(e);
        }
    }


    public Message realiseAsOne() {
        try {
            return messageGroupFutureTask.get().realiseAsOne();
        } catch (InterruptedException e) {
            throw new ExecutionFailureRuntimeException(e);
        } catch (ExecutionException e) {
            throw new ExecutionFailureRuntimeException(e);
        }
    }


    public void setExecutionCorrelation(Correlation executionCorrelation) {
        correlationParticipant.setExecutionCorrelation(executionCorrelation);
    }


    public int size() {
        try {
            return messageGroupFutureTask.get().size();
        } catch (InterruptedException e) {
            throw new ExecutionFailureRuntimeException(e);
        } catch (ExecutionException e) {
            throw new ExecutionFailureRuntimeException(e);
        }
    }
}
