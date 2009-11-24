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

package org.cauldron.einstein.ri.core.providers.std;

import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.message.action.context.ActionCallbackContext;
import org.cauldron.einstein.api.message.action.types.ReadAction;
import org.cauldron.einstein.api.message.action.types.WriteAction;
import org.cauldron.einstein.api.message.data.model.DataObject;
import org.cauldron.einstein.api.message.view.composites.ReadOnlyView;
import org.cauldron.einstein.api.message.view.composites.WriteView;
import org.cauldron.einstein.api.provider.facade.WriteFacade;
import org.cauldron.einstein.api.provider.facade.context.WriteContext;
import org.cauldron.einstein.api.provider.facade.exception.WriteFailureRuntimeException;
import org.cauldron.einstein.ri.core.providers.AbstractFacade;
import org.cauldron.einstein.ri.core.log.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class ConsoleWriteFacade extends AbstractFacade implements WriteFacade {
    private static final Logger log = Logger.getLogger(ConsoleWriteFacade.class);

    private final String prefix;
    private final OutputFormat messageFormat;

    public ConsoleWriteFacade(String prefix, OutputFormat messageFormat) {
        this.prefix = prefix;
        this.messageFormat = messageFormat;
    }


    private void output(final int depth, MessageTuple message, boolean payload) {
        if (messageFormat == OutputFormat.PRINTF) {
            log.debug("Outputting to console using printf format, message is {0}.", message);
            List<MessageTuple> list = message.realiseAsList();
            final List<Object> objects = new ArrayList<Object>();
            for (MessageTuple tuple : list) {
                tuple.realiseAsOne().execute(ReadAction.class, new ReadAction() {
                    public void handle(ActionCallbackContext<ReadOnlyView, Object> ctx) {
                        objects.add(ctx.getMessageHistory().getCurrentEntry().getNewValue().getPayload().getValue());
                    }
                });
            }
            System.out.printf(prefix, objects.toArray());
        } else {
            if (message.isOne()) {
                log.debug("Outputting to console single item message, message is {0}.", message);
                message.realiseAsOne().execute(WriteAction.class,
                        new WriteAction() {
                            public void handle(ActionCallbackContext<WriteView, Object> sendMessageViewActionCallbackContext) {
                                final DataObject object = sendMessageViewActionCallbackContext.getMessageHistory().getCurrentEntry().getNewValue().getPayload();

                                String outStr;
                                if (object == null) {
                                    outStr = "null";
                                } else {
                                    outStr = object.asString();
                                }
                                for (int i = 1; i < depth; i++) {
                                    System.out.print("    ");
                                }
                                System.out.println(prefix + outStr);
                            }
                        });

            } else {
                log.debug("Outputting to console multiple item message, message is {0}.", message);
                List<MessageTuple> list = message.realiseAsList();
                for (MessageTuple messageInTuple : list) {
                    output(depth + 1, messageInTuple, payload);
                }
            }
        }
    }


    public MessageTuple write(WriteContext context, MessageTuple tuple, boolean payload) throws WriteFailureRuntimeException {
        writeInternal(tuple, payload);
        return tuple;
    }


    private void writeInternal(MessageTuple tuple, boolean payload) {
        //Still not sure whether to support message vs payload for console:
        //will probably keep it a special case and not support message version.
//
        output(0, tuple, payload);
    }

    public MessageTuple write(WriteContext context, MessageTuple tuple, boolean payload, long timeout) throws WriteFailureRuntimeException {
        return tuple;
    }


}
