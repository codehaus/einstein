package org.cauldron.einstein.provider.esper.facade;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.UpdateListener;
import com.espertech.esper.event.EventBean;
import org.cauldron.einstein.api.message.MessageListener;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.message.data.model.DataObject;
import org.cauldron.einstein.api.model.resource.ResourceRef;
import org.cauldron.einstein.api.provider.facade.ListenFacade;
import org.cauldron.einstein.api.provider.facade.context.ListenContext;
import org.cauldron.einstein.api.provider.facade.exception.ReadFailureRuntimeException;
import org.cauldron.einstein.provider.esper.EsperUtil;
import org.cauldron.einstein.ri.core.log.Logger;
import org.cauldron.einstein.ri.core.providers.AbstractFacade;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class EsperListenFacade extends AbstractFacade implements ListenFacade {
    private static final Logger log = Logger.getLogger(EsperListenFacade.class);
    private final EPServiceProvider epService;
    private String query;

    public EsperListenFacade(EPServiceProvider epService, String query) {
        this.epService = epService;
        this.query = query;
    }

    public void listen(final ListenContext context, final MessageTuple tuple, final ResourceRef filter, boolean payload,
                       final MessageListener listener) throws ReadFailureRuntimeException {
        log.debug("Listening.");
        EPStatement statement = epService.getEPAdministrator().createEPL(query);
        statement.addListener(new UpdateListener() {
            public void update(EventBean[] eventBeans, EventBean[] eventBeans1) {
                try {
                    log.debug("Event received from Esper.");
                    for (EventBean eventBean : eventBeans) {
                        DataObject object = context.getActiveProfile()
                                .getDataModel()
                                .getDataObjectFactory()
                                .createDataObject(eventBean);
                        log.debug("Handling bean from from Esper.");
                        listener.handle(context.getActiveProfile()
                                .getMessageModel().createMessage(tuple.getExecutionCorrelation(),
                                                                 object));

                    }
                } catch (Exception e) {
                    context.getActiveProfile().getExceptionModel().handleRootlessException(e);
                }
            }
        });
    }

    public void receive(ListenContext context, MessageTuple tuple, boolean payload) {
        log.debug("Received.");
        EsperUtil.dispatch(epService, tuple, payload);
    }
}
