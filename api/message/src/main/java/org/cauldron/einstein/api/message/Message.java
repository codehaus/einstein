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

import org.cauldron.einstein.api.message.action.Action;
import org.cauldron.einstein.api.message.action.MessageActionResult;
import org.cauldron.einstein.api.message.data.model.DataModel;
import org.cauldron.einstein.api.message.view.MessageView;
import org.contract4j5.contract.Contract;
import org.contract4j5.contract.Post;
import org.contract4j5.contract.Pre;

/**
 * The state of the message is not exposed to the receiver of the object. This state is passed to an Action peformed
 * against the message. This enforces an auditable trail of read and write requests to the object.
 *
 * @author Neil Ellis
 */
@Contract
public interface Message extends MessageTuple {

  /**
   * Perform the specified action on the message. A ModificationToken must be requested before modification is
   * allowed, if the action does not perform modification then one is not required. Note the passing of the action
   * type across, this is because we're doing a double dispatch here. The action is invoked directly, it then passes
   * itself on to the message to execute it.
   */
  @Pre("!$this.isEmpty() && !$this.isVoid()")
  @Post <MV extends MessageView, RV, A extends Action<MV, RV>> MessageActionResult<RV> execute(Class<A> actionClass,
                                                                                               A action);

//  /**
//   * Creates a new Message based on the old one with a new payload. This is purely a convenience method, the
//   * implementation should internally generate the appropriate action etc.
//   */
//  @Pre
//  @Post Message duplicateWithNewPayload(DataObject payload);


  /**
   * Creates a new Message based on the old one with a new data model. This is purely a convenience method, the
   * implementation should internally generate the appropriate action etc.
   */
  @Pre
  @Post Message duplicateWithNewDataModel(Class<? extends DataModel> newDataModel);
}
