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

package org.cauldron.einstein.api.message;

import org.cauldron.einstein.api.message.correlation.Correlation;
import org.cauldron.einstein.api.message.data.model.DataModel;
import org.cauldron.einstein.api.message.data.model.DataObject;
import org.contract4j5.contract.Contract;
import org.contract4j5.contract.Post;
import org.contract4j5.contract.Pre;

import java.util.List;
import java.util.Map;

/**
 * @author Neil Ellis
 */
@Contract
public interface MessageModel {

    @Pre
    @Post("executionCorrelation == $return.getExecutionCorrelation()")
    Message createMessage(
            Correlation executionCorrelation, DataObject dataObject);

    @Pre
    @Post("executionCorrelation == $return.getExecutionCorrelation()")
    Message createMessage(
            Correlation executionCorrelation, MessageProperties properties, DataObject object);

    @Pre
    @Post
    MessageProperties createProperties(Map<String, Object> map);

    @Pre
    @Post
    Message merge(MessageTuple list, DataModel dataModel);

    /**
     * Has a null payload.
     */
    @Pre
    @Post("executionCorrelation == $return.getExecutionCorrelation()")
    Message createNullMessage(
            Correlation executionCorrelation);

    /**
     * Has no payload.
     */
    @Pre
    @Post("executionCorrelation == $return.getExecutionCorrelation()")
    Message createEmptyMessage(
            Correlation executionCorrelation);

    /**
     * Is void, i.e. does not exist. Void messages are empty, but empty messages are not void.
     */
    @Pre
    @Post("executionCorrelation == $return.getExecutionCorrelation()")
    Message createVoidMessage(
            Correlation executionCorrelation);

    @Pre
    @Post
    MessageTuple createTuple(DataModel dataModel, List<MessageTuple>... members);

    @Pre
    @Post
    MessageTuple createTuple(DataModel dataModel, Map<Object, MessageTuple>... members);

    @Pre
    @Post
    MessageTuple createTuple(DataModel dataModel, MessageTuple... members);

    /**
     * This creates a tuple fit for consumption by non Einstein code, i.e. external providers. Each model has to decide
     * which is the most appropriate way to protect against modification, some models will trust the providers and
     * return the inbound tuple others will deep duplicate the tuple, others may instrument the payload to protect
     * against modification.
     *
     * @param tuple the original tuple
     *
     * @return a tuple for the external code, this may mean it has been proxied, copied, who knows.
     */
    @Pre
    @Post("tuple.size() == $return.size() && tuple.getExecutionCorrelation() == $return.getExecutionCorrelation()")
    MessageTuple createExternalisableTuple(
            MessageTuple tuple);
}
