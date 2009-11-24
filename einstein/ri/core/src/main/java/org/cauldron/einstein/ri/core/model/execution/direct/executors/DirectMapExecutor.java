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

package org.cauldron.einstein.ri.core.model.execution.direct.executors;


import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.message.correlation.Correlation;
import org.cauldron.einstein.api.model.execution.Executable;
import org.cauldron.einstein.api.model.execution.ExecutionContext;
import org.cauldron.einstein.ri.core.model.correlation.SimpleCorrelation;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class DirectMapExecutor extends AbstractDirectExecutionExecutor {

    private final Map<?, ? extends Executable> instructions;

    public DirectMapExecutor(Map<?, ? extends Executable> instructions) {
        this.instructions = instructions;
    }


    public MessageTuple execute(ExecutionContext context, MessageTuple inboundMessageTuple) {
        Correlation executionCorrelation = inboundMessageTuple.getExecutionCorrelation();
        Map<Object, MessageTuple> result = new LinkedHashMap<Object, MessageTuple>();
        for (Map.Entry<?, ? extends Executable> entry : instructions.entrySet()) {
            MessageTuple tuple = inboundMessageTuple.duplicate();
            Correlation childCorrelation = new SimpleCorrelation(executionCorrelation);
            tuple.setExecutionCorrelation(childCorrelation);
            MessageTuple messageTuple = TemporaryExceptionHandler.executeWithExceptionHandling(entry.getValue(),
                                                                                               context,
                                                                                               tuple);
            result.put(entry.getKey(), messageTuple);
        }
        MessageTuple tuple = context.getActiveProfile()
                .getMessageModel()
                .createTuple(context.getActiveProfile().getDataModel(), result);
        tuple.setExecutionCorrelation(executionCorrelation);
        return tuple;
    }
}
