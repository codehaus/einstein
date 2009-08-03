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

import org.contract4j5.contract.*;
import org.apache.camel.*;
import org.apache.camel.impl.converter.AsyncProcessorTypeConverter;
import org.cauldron.einstein.api.common.euri.EinsteinURI;
import org.cauldron.einstein.api.message.MessageListener;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.message.action.context.ActionCallbackContext;
import org.cauldron.einstein.api.message.action.types.ReadAction;
import org.cauldron.einstein.api.message.view.composites.ReadOnlyView;
import org.cauldron.einstein.api.model.lifecycle.LifecycleContext;
import org.cauldron.einstein.api.model.lifecycle.exception.InitializationRuntimeException;
import org.cauldron.einstein.api.model.lifecycle.exception.StartRuntimeException;
import org.cauldron.einstein.api.model.lifecycle.exception.StopRuntimeException;
import org.cauldron.einstein.api.provider.facade.DispatchFacade;
import org.cauldron.einstein.api.provider.facade.context.WriteContext;
import org.cauldron.einstein.api.provider.facade.exception.WriteFailureRuntimeException;
import org.cauldron.einstein.provider.camel.CamelUtil;
import org.cauldron.einstein.ri.core.log.Logger;
import org.cauldron.einstein.ri.core.providers.AbstractFacade;

/**
 * @author Neil Ellis
 */
@Contract
public class CamelDispatchFacade extends AbstractFacade implements DispatchFacade {
    private static final Logger log = Logger.getLogger(CamelWriteFacade.class);
    private final Endpoint endpoint;
    private Producer producer;

    public CamelDispatchFacade(Endpoint endpoint, EinsteinURI uri) {
        this.endpoint = endpoint;
        EinsteinURI uri1 = uri;
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

   

    public void writeAsync(WriteContext context, MessageTuple tuple, boolean payload) throws WriteFailureRuntimeException {
        final Exchange exchange = producer.createExchange(ExchangePattern.InOnly);
        if (payload) {
            tuple.realiseAsOne().execute(ReadAction.class, new ReadAction() {
                public void handle(ActionCallbackContext<ReadOnlyView, Object> ctx) {
                    exchange.getIn().setBody(ctx.getMessageHistory().getCurrentEntry().getNewValue().getPayload().getValue());
                }
            });
        } else {
            exchange.getIn().setBody(tuple);

        }
        try {
            producer.process(exchange);
        } catch (Exception e) {
            throw new WriteFailureRuntimeException(e);
        }
        log.debug("Wrote {0} to Camel.", tuple);
    }

    public void writeAsync(final WriteContext context, final MessageTuple tuple, boolean payload, final MessageListener listener) throws WriteFailureRuntimeException {
        final Exchange exchange = producer.createExchange(ExchangePattern.InOut);
        if (payload) {
            tuple.realiseAsOne().execute(ReadAction.class, new ReadAction() {
                public void handle(ActionCallbackContext<ReadOnlyView, Object> ctx) {
                    exchange.getIn().setBody(ctx.getMessageHistory().getCurrentEntry().getNewValue().getPayload().getValue());
                }
            });
        } else {
            exchange.getIn().setBody(tuple);

        }
        try {
            AsyncProcessorTypeConverter.convert(producer).process(exchange, new AsyncCallback() {
                public void done(boolean b) {
                    org.cauldron.einstein.api.message.Message message = CamelUtil.extractReturnMessage(exchange, tuple, context.getActiveProfile());
                    log.debug("Received {0} from Camel.", message);
                    listener.handle(message);
                }
            });
            log.debug("Wrote {0} to Camel.", tuple);
        } catch (Exception e) {
            throw new WriteFailureRuntimeException(e);
        }
    }
}