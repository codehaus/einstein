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

package org.cauldron.einstein.provider.camel.facade;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Producer;
import org.cauldron.einstein.api.common.euri.EinsteinURI;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.message.action.context.ActionCallbackContext;
import org.cauldron.einstein.api.message.action.types.ReadAction;
import org.cauldron.einstein.api.message.view.composites.ReadOnlyView;
import org.cauldron.einstein.api.model.lifecycle.LifecycleContext;
import org.cauldron.einstein.api.model.lifecycle.exception.InitializationRuntimeException;
import org.cauldron.einstein.api.model.lifecycle.exception.StartRuntimeException;
import org.cauldron.einstein.api.model.lifecycle.exception.StopRuntimeException;
import org.cauldron.einstein.api.provider.facade.WriteFacade;
import org.cauldron.einstein.api.provider.facade.context.WriteContext;
import org.cauldron.einstein.api.provider.facade.exception.WriteFailureRuntimeException;
import org.cauldron.einstein.provider.camel.CamelUtil;
import org.cauldron.einstein.ri.core.log.Logger;
import org.cauldron.einstein.ri.core.providers.AbstractFacade;
import org.contract4j5.contract.Contract;

/**
 * @author Neil Ellis
 */
@Contract
public class CamelWriteFacade extends AbstractFacade implements WriteFacade {
    private static final Logger log = Logger.getLogger(CamelWriteFacade.class);
    private final Endpoint endpoint;
    private Producer producer;

    public CamelWriteFacade(Endpoint endpoint, EinsteinURI uri) {
        this.endpoint = endpoint;
    }

    public void init(LifecycleContext ctx) throws InitializationRuntimeException {
        super.init(ctx);
        try {
            producer = this.endpoint.createProducer();
        } catch (Exception e) {
            throw new InitializationRuntimeException(e);
        }
    }

    public void start(LifecycleContext ctx) {
        super.start(ctx);
        try {
            producer.start();
        } catch (Exception e) {
            throw new StartRuntimeException(e);
        }
    }

    public void stop(LifecycleContext ctx) throws StartRuntimeException {
        super.stop(ctx);
        try {
            producer.stop();
        } catch (Exception e) {
            throw new StopRuntimeException(e);
        }
    }

    public MessageTuple write(WriteContext context, MessageTuple tuple, boolean payload) throws
                                                                                         WriteFailureRuntimeException {
        if (!tuple.isEmpty()) {
            assertStarted();
            final Exchange exchange = producer.createExchange(ExchangePattern.InOut);
            if (payload) {
                tuple.realiseAsOne().execute(ReadAction.class, new ReadAction() {
                    public void handle(ActionCallbackContext<ReadOnlyView, Object> ctx) {
                        exchange.getIn()
                                .setBody(ctx.getMessageHistory()
                                        .getCurrentEntry()
                                        .getNewValue()
                                        .getPayload().getValue());
                    }
                });
            } else {
                exchange.getIn().setBody(tuple);

            }
            try {
                producer.process(exchange);
                log.debug("Wrote {0} to Camel.", tuple);
                org.cauldron.einstein.api.message.Message message = CamelUtil.extractReturnMessage(exchange,
                                                                                                   tuple,
                                                                                                   context.getActiveProfile());
                log.debug("Received {0} from Camel.", message);
                return message;
            } catch (Exception e) {
                throw new WriteFailureRuntimeException(e);
            }
        } else {
            return tuple;
        }
    }

    public MessageTuple write(WriteContext context, MessageTuple tuple, boolean payload, long timeout) throws
                                                                                                       WriteFailureRuntimeException {
        //TODO: Support timeouts.
        return write(context, tuple, payload);
    }

}
