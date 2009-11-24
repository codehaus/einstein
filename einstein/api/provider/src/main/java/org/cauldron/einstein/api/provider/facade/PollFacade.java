package org.cauldron.einstein.api.provider.facade;

import org.contract4j5.contract.*;

import org.cauldron.einstein.api.message.MessageListener;
import org.cauldron.einstein.api.model.resource.Facade;
import org.cauldron.einstein.api.model.resource.ResourceRef;
import org.cauldron.einstein.api.provider.facade.context.ReadContext;
import org.cauldron.einstein.api.provider.facade.exception.ReadFailureRuntimeException;

/**
 * @author Neil Ellis
 */
@Contract
public interface PollFacade extends Facade {

    void poll(@Pre ReadContext context, ResourceRef filter, boolean message, @Pre MessageListener listener) throws ReadFailureRuntimeException;

}