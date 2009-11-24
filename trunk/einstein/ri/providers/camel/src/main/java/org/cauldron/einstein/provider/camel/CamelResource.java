package org.cauldron.einstein.provider.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.cauldron.einstein.api.common.euri.EinsteinURI;
import org.cauldron.einstein.api.model.lifecycle.LifecycleContext;
import org.cauldron.einstein.api.model.lifecycle.exception.StartRuntimeException;
import org.cauldron.einstein.api.model.lifecycle.exception.StopRuntimeException;
import org.cauldron.einstein.api.provider.facade.DispatchFacade;
import org.cauldron.einstein.api.provider.facade.ListenFacade;
import org.cauldron.einstein.api.provider.facade.ReadFacade;
import org.cauldron.einstein.api.provider.facade.WriteFacade;
import org.cauldron.einstein.provider.camel.facade.CamelDispatchFacade;
import org.cauldron.einstein.provider.camel.facade.CamelListenFacade;
import org.cauldron.einstein.provider.camel.facade.CamelReadFacade;
import org.cauldron.einstein.provider.camel.facade.CamelWriteFacade;
import org.cauldron.einstein.ri.core.providers.base.AbstractResource;

import java.util.Arrays;
import java.util.List;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class CamelResource extends AbstractResource {
    private final CamelContext context = new DefaultCamelContext();

    public CamelResource(EinsteinURI uri, Class[] facades) {
        super(uri);
        Endpoint endpoint = context.getEndpoint(uri.getDescriptor().asString());
        
        List<Class> facadeList = Arrays.asList(facades);
        if (facadeList.contains(ListenFacade.class)) {
            addMapping(ListenFacade.class, new CamelListenFacade(endpoint, uri));
        }
        if (facadeList.contains(ReadFacade.class)) {
            addMapping(ReadFacade.class, new CamelReadFacade(endpoint, uri));
        }
        if (facadeList.contains(WriteFacade.class)) {
            addMapping(WriteFacade.class, new CamelWriteFacade(endpoint, uri));
        }
        if (facadeList.contains(DispatchFacade.class)) {
            addMapping(DispatchFacade.class, new CamelDispatchFacade(endpoint, uri));
        }
    }


    public void start(LifecycleContext ctx) throws StartRuntimeException {
        super.start(ctx);
        try {
            context.start();
        } catch (Exception e) {
            throw new StartRuntimeException(e);
        }
    }

    public void stop(LifecycleContext ctx) throws StopRuntimeException {
        super.stop(ctx);
        try {
            context.start();
        } catch (Exception e) {
            throw new StopRuntimeException(e);
        }
    }


}
