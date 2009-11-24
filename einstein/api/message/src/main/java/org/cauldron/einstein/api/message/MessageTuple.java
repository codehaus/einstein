/******************************************************************************
 *                                                                            *
 *                                                                            *
 *     All works are (C) 2008- Mangala Solutions Ltd and Paremus Ltd.         *
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
import org.cauldron.einstein.api.message.correlation.CorrelationParticipant;
import org.cauldron.einstein.api.message.exception.MessageDuplicationException;
import org.contract4j5.contract.Contract;
import org.contract4j5.contract.Post;
import org.contract4j5.contract.Pre;

import java.util.List;
import java.util.Map;

/**
 * A MessageTuple is a collection of messages travelling together, it is usually a temporary arrangement. The messages
 * themselves might well be 'Futured' messages, i.e. they themselves are not concrete until 'realize() is called.
 *
 * @author Neil Ellis
 */
@Contract
public interface MessageTuple {

  @Pre("$this.isList()") @Post List<MessageTuple> realiseAsList();

  @Post Message realiseAsOne();

  @Pre("$this.isMap()") @Post  Map<?, MessageTuple> realiseAsMap();

  boolean isOne();

  boolean isMap();

  boolean isList();

  @Post("$return == ($this.size() == 0)")
  boolean isEmpty();

  boolean isVoid();

  @Pre CorrelationParticipant getCorrelationParticipant();

  @Pre void setExecutionCorrelation(Correlation executionCorrelation);

  @Post Correlation getExecutionCorrelation();

  @Post("$return.size() == $this.size()  && $this.size() == $old($this.size())") MessageTuple duplicate() throws MessageDuplicationException;

  @Post("$return.size() == $this.size()  && $this.size() == $old($this.size())") MessageTuple deepDuplicate() throws MessageDuplicationException;

  @Post("$return >= 0")  int size();
}
