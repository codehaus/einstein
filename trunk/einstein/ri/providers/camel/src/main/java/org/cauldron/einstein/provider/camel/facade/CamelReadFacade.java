package org.cauldron.einstein.provider.camel.facade;

import mazz.i18n.annotation.I18NResourceBundle;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.PollingConsumer;
import org.cauldron.einstein.api.common.euri.EinsteinURI;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.model.lifecycle.LifecycleContext;
import org.cauldron.einstein.api.model.lifecycle.exception.InitializationRuntimeException;
import org.cauldron.einstein.api.model.lifecycle.exception.StartRuntimeException;
import org.cauldron.einstein.api.model.lifecycle.exception.StopRuntimeException;
import org.cauldron.einstein.api.provider.facade.ReadFacade;
import org.cauldron.einstein.api.provider.facade.context.ReadContext;
import org.cauldron.einstein.api.provider.facade.exception.ReadFailureRuntimeException;
import static org.cauldron.einstein.provider.camel.CamelProviderMessages.BUNDLE_NAME;
import static org.cauldron.einstein.provider.camel.CamelProviderMessages.FAILED_TO_CREATE_POLL;
import org.cauldron.einstein.provider.camel.CamelUtil;
import org.cauldron.einstein.ri.core.log.Logger;
import org.cauldron.einstein.ri.core.providers.AbstractFacade;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Neil Ellis
 */
@I18NResourceBundle(baseName = BUNDLE_NAME)
@org.contract4j5.contract.Contract
public class CamelReadFacade extends AbstractFacade implements ReadFacade {
    private static final Logger log = Logger.getLogger(CamelReadFacade.class);

    private final String camelURL;
    private final Endpoint endpoint;
    private PollingConsumer pollingConsumer;

    public CamelReadFacade(Endpoint endpoint, EinsteinURI uri) {
        this.endpoint = endpoint;
        this.camelURL = uri.getDescriptor().asString();
    }


    public void init(LifecycleContext ctx) throws InitializationRuntimeException {
        super.init(ctx);
        try {
            pollingConsumer = endpoint.createPollingConsumer();
        } catch (Exception e) {
            throw new ReadFailureRuntimeException(e, BUNDLE_NAME, FAILED_TO_CREATE_POLL, camelURL);
        }
    }

    public void start(LifecycleContext ctx) throws StartRuntimeException {
        super.start(ctx);
        try {
            pollingConsumer.start();
        } catch (Exception e) {
            throw new StartRuntimeException(e);
        }

    }

    public void stop(LifecycleContext ctx) throws StopRuntimeException {
        super.stop(ctx);
        try {
            pollingConsumer.stop();
        } catch (Exception e) {
            throw new StopRuntimeException(e);
        }
    }


    public MessageTuple read(ReadContext context, boolean all, boolean payload, long timeout) throws
                                                                                              ReadFailureRuntimeException {
        assertStarted();
        log.debug("Blocking read from {0} with timeout {1}.", camelURL, timeout);
        synchronized (pollingConsumer) {
            if (all) {
                return readAllAvailable(context, payload);
            } else {
                return processResult(context, payload, pollingConsumer.receive(timeout));
            }
        }

    }

    private MessageTuple processResult(ReadContext context, boolean payload, Exchange exchange) {
        if (payload) {
            return CamelUtil.extractMessage(exchange, context.getMessage(), context.getActiveProfile());
        } else {
            return (MessageTuple) exchange.getIn().getBody();
        }
    }


    public MessageTuple read(ReadContext context, boolean all, boolean payload) throws ReadFailureRuntimeException {
        assertStarted();
        log.debug("Blocking read from {0}.", camelURL);
        synchronized (pollingConsumer) {
            if (all) {
                return readAllAvailable(context, payload);
            } else {
                return processResult(context, payload, pollingConsumer.receive());
            }
        }
    }

    private MessageTuple readAllAvailable(ReadContext context, boolean payload) {
        Exchange exchange;
        List<MessageTuple> list = new ArrayList<MessageTuple>();
        exchange = pollingConsumer.receiveNoWait();
        while (exchange != null) {
            list.add(processResult(context, payload, exchange));
            exchange = pollingConsumer.receiveNoWait();
        }
        return context.getActiveProfile()
                .getMessageModel()
                .createTuple(context.getActiveProfile().getDataModel(), list);
    }


    public MessageTuple readNoWait(ReadContext context, boolean all, boolean payload) throws
                                                                                      ReadFailureRuntimeException {
        assertStarted();
        log.debug("Non-blocking read from {0}.", camelURL);
        synchronized (pollingConsumer) {
            if (all) {
                return readAllAvailable(context, payload);
            } else {
                try {
                    return processResult(context, payload, pollingConsumer.receiveNoWait());
                } catch (Exception e) {
                    throw new ReadFailureRuntimeException(e, BUNDLE_NAME, FAILED_TO_CREATE_POLL, camelURL);
                }
            }
        }
    }

}
