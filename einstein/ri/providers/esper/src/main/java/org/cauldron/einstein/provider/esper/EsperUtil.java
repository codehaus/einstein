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

package org.cauldron.einstein.provider.esper;

import com.espertech.esper.client.EPServiceProvider;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.message.action.context.ActionCallbackContext;
import org.cauldron.einstein.api.message.action.types.ReadAction;
import org.cauldron.einstein.api.message.view.composites.ReadOnlyView;
import org.cauldron.einstein.api.provider.facade.exception.WriteFailureRuntimeException;
import org.contract4j5.contract.Contract;

/**
 * @author Neil Ellis
 */
@Contract
public class EsperUtil {
    public static void dispatch(EPServiceProvider epService, MessageTuple tuple, boolean payload) {
        if (payload) {
            dispatchPayload(epService, tuple);
        } else {
            dispatchMessage(epService, tuple);
        }
    }

    public static void dispatchPayload(final EPServiceProvider epService, MessageTuple message) throws
                                                                                                WriteFailureRuntimeException {
        message.realiseAsOne().execute(ReadAction.class, new ReadAction() {
            public void handle(ActionCallbackContext<ReadOnlyView, Object> ctx) {
                epService.getEPRuntime().sendEvent(ctx.getMessageHistory().getCurrentEntry().getEvent().getNewState().getPayload().getValue());
            }
        });
    }

    public static void dispatchMessage(EPServiceProvider epService, MessageTuple message) throws WriteFailureRuntimeException {
        epService.getEPRuntime().sendEvent(message);
    }
}
