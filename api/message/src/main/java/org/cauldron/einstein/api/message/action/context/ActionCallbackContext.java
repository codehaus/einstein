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

package org.cauldron.einstein.api.message.action.context;

import org.cauldron.einstein.api.common.history.History;
import org.cauldron.einstein.api.message.action.MessageActionResult;
import org.cauldron.einstein.api.message.view.MessageView;
import org.contract4j5.contract.Contract;
import org.contract4j5.contract.Post;
import org.contract4j5.contract.Pre;

/**
 * Classes implementing this interface are provided to {@link org.cauldron.einstein.api.message.action.Action} classes
 * by the Message object. Each callback is paramterized in the same way as the original Action. I.e. An action relates
 * to a specific {@link MessageView} and the callback is paramterized so that getMessageHistory() returns a history of
 * the message with the correct view.
 *
 * @author Neil Ellis
 */

@Contract
public interface ActionCallbackContext<MV extends MessageView, RV> {

    /**
     * Set the result of the action.
     */
    @Pre
    void setActionResult(MessageActionResult<RV> result);

    /**
     * Returns a view of the message that relates to the action performed. Note that the method is called getMessage()
     * to make it scripting friendly.
     */
    @Post
    History<MV> getMessageHistory();
}
