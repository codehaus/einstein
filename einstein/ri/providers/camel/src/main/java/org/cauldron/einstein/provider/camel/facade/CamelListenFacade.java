package org.cauldron.einstein.provider.camel.facade;

import org.apache.camel.Consumer;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.cauldron.einstein.api.common.euri.EinsteinURI;
import org.cauldron.einstein.api.message.Message;
import org.cauldron.einstein.api.message.MessageListener;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.model.lifecycle.LifecycleContext;
import org.cauldron.einstein.api.model.lifecycle.exception.StopRuntimeException;
import org.cauldron.einstein.api.model.resource.ResourceRef;
import org.cauldron.einstein.api.provider.facade.ListenFacade;
import org.cauldron.einstein.api.provider.facade.context.ListenContext;
import org.cauldron.einstein.api.provider.facade.exception.ReadFailureRuntimeException;
import static org.cauldron.einstein.provider.camel.CamelProviderMessages.BUNDLE_NAME;
import static org.cauldron.einstein.provider.camel.CamelProviderMessages.FAILED_TO_CREATE_CONSUMER;
import org.cauldron.einstein.provider.camel.CamelUtil;
import org.cauldron.einstein.ri.core.log.Logger;
import org.cauldron.einstein.ri.core.providers.AbstractFacade;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class CamelListenFacade extends AbstractFacade implements ListenFacade {
    private static final Logger log = Logger.getLogger(CamelListenFacade.class);
    private final String camelURL;
    private final List<Consumer> consumers = new ArrayList<Consumer>();

    public CamelListenFacade(Endpoint endpoint, EinsteinURI uri) {
        this.endpoint = endpoint;

        this.camelURL = uri.getDescriptor().asString();
    }

    private final Endpoint endpoint;


    public void stop(LifecycleContext ctx) throws StopRuntimeException {
        super.stop(ctx);
        for (Consumer consumer : consumers) {
            try {
                consumer.stop();
            } catch (Exception e) {
                throw new StopRuntimeException(e);
            }
        }
    }

    public void listen(final ListenContext context, final MessageTuple tuple,  ResourceRef query, boolean payload, final MessageListener listener) throws ReadFailureRuntimeException {
        assertStarted();
        try {
            if (payload) {
                Consumer consumer = endpoint.createConsumer(new Processor() {
                    public void process(Exchange exchange) throws Exception {
                        try {
                            Message message = CamelUtil.extractMessage(exchange, tuple, context.getActiveProfile());
                            listener.handle(message);
                            log.debug("Handled message {0} from Camel.", message);
                        } catch (Exception e) {
                            context.getActiveProfile().getExceptionModel().handleRootlessException(e);
                        }

                    }
                });
                consumer.start();
                consumers.add(consumer);
            } else {
                Consumer consumer = endpoint.createConsumer(new Processor() {
                    public void process(Exchange exchange) throws Exception {
                        try {
                            Message message = (Message) exchange.getIn().getBody();
                            listener.handle(message);
                            log.debug("Handled message {0} from Camel.", message);
                        } catch (Exception e) {
                            context.getActiveProfile().getExceptionModel().handleRootlessException(e);
                        }
                    }
                });
                consumer.start();
                consumers.add(consumer);
            }
        } catch (Exception e) {
            throw new ReadFailureRuntimeException(e, BUNDLE_NAME, FAILED_TO_CREATE_CONSUMER, camelURL);
        }
    }

    public void receive(ListenContext context, MessageTuple tuple, boolean payload) {
        //Ignore
    }

}
