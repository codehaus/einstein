package org.cauldron.einstein.api.provider.facade;

import org.cauldron.einstein.api.message.MessageListener;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.model.resource.Facade;
import org.cauldron.einstein.api.model.resource.ResourceRef;
import org.cauldron.einstein.api.provider.facade.context.ListenContext;
import org.cauldron.einstein.api.provider.facade.exception.ReadFailureRuntimeException;
import org.contract4j5.contract.Contract;

/**
 * @author Neil Ellis
 */
@Contract
public interface ListenFacade extends Facade {


    void listen(ListenContext context, MessageTuple tuple, ResourceRef filter, boolean payload,
                MessageListener listener) throws ReadFailureRuntimeException;

    void receive(ListenContext context, MessageTuple tuple, boolean payload);
    
}
