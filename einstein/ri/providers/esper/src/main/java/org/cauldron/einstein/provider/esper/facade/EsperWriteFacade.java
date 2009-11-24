package org.cauldron.einstein.provider.esper.facade;

import com.espertech.esper.client.EPServiceProvider;
import org.cauldron.einstein.api.message.MessageListener;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.provider.facade.DispatchFacade;
import org.cauldron.einstein.api.provider.facade.WriteFacade;
import org.cauldron.einstein.api.provider.facade.context.WriteContext;
import org.cauldron.einstein.api.provider.facade.exception.WriteFailureRuntimeException;
import org.cauldron.einstein.ri.core.providers.AbstractFacade;
import org.cauldron.einstein.provider.esper.EsperUtil;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class EsperWriteFacade extends AbstractFacade implements WriteFacade, DispatchFacade {
    private final EPServiceProvider epService;

    public EsperWriteFacade(EPServiceProvider epService, String s) {

        this.epService = epService;
    }

    public MessageTuple write(WriteContext context, MessageTuple tuple, boolean payload) throws WriteFailureRuntimeException {
        EsperUtil.dispatch(epService, tuple, payload);
        return tuple;
    }

    public MessageTuple write(WriteContext context, MessageTuple tuple, boolean payload, long timeout) throws WriteFailureRuntimeException {
        EsperUtil.dispatch(epService, tuple, payload    );
        return tuple;
    }


    public void writeAsync(WriteContext context, MessageTuple tuple, boolean payload) throws WriteFailureRuntimeException {
        EsperUtil.dispatch(epService, tuple, payload);

    }

    public void writeAsync(WriteContext context, MessageTuple tuple, boolean payload, MessageListener listener) throws WriteFailureRuntimeException {
        EsperUtil.dispatch(epService,  tuple, payload);
        listener.handle(tuple);

    }

}
