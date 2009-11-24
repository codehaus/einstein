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

package org.cauldron.einstein.ri.core.providers.stack;

import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.message.action.context.ActionCallbackContext;
import org.cauldron.einstein.api.message.action.types.ReadAction;
import org.cauldron.einstein.api.message.view.composites.ReadOnlyView;
import org.cauldron.einstein.api.provider.facade.WriteFacade;
import org.cauldron.einstein.api.provider.facade.context.WriteContext;
import org.cauldron.einstein.api.provider.facade.exception.WriteFailureRuntimeException;
import org.cauldron.einstein.ri.core.log.Logger;
import org.cauldron.einstein.ri.core.providers.AbstractFacade;

import java.util.List;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class StackWriteFacade extends AbstractFacade implements WriteFacade {

    private static final Logger log = Logger.getLogger(StackWriteFacade.class);

    private final List stack;

    public StackWriteFacade(List stack) {
        this.stack = stack;
    }


    public MessageTuple write(WriteContext context, MessageTuple tuple, boolean payload) throws
                                                                                         WriteFailureRuntimeException {
        return writeInternal(tuple, payload);
    }


    private MessageTuple writeInternal(MessageTuple tuple, boolean payload) {
        log.debug("Writing to stack {0}.", stack);

        if (payload) {
            tuple.realiseAsOne().execute(ReadAction.class, new ReadAction() {

                public void handle(ActionCallbackContext<ReadOnlyView, Object> ctx) {
                    synchronized (stack) {
                        stack.add(ctx.getMessageHistory().getCurrentEntry().getNewValue().getPayload().getValue());
                    }
                }
            });
        } else {
            synchronized (stack) {
                stack.add(tuple);
            }
        }
        return tuple;
    }


    public MessageTuple write(WriteContext context, MessageTuple tuple, boolean payload, long timeout) throws
                                                                                                       WriteFailureRuntimeException {
        return writeInternal(tuple, payload);
    }
}
