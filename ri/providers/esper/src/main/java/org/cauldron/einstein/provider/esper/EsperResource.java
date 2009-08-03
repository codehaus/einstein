package org.cauldron.einstein.provider.esper;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import org.cauldron.einstein.api.common.euri.EinsteinURI;
import org.cauldron.einstein.api.provider.facade.DispatchFacade;
import org.cauldron.einstein.api.provider.facade.ListenFacade;
import org.cauldron.einstein.api.provider.facade.WriteFacade;
import org.cauldron.einstein.provider.esper.facade.EsperListenFacade;
import org.cauldron.einstein.provider.esper.facade.EsperWriteFacade;
import org.cauldron.einstein.ri.core.providers.base.AbstractResource;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
class EsperResource extends AbstractResource {

    private final EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();

    public EsperResource(EinsteinURI uri) {
        super(uri);
        addMapping(ListenFacade.class, new EsperListenFacade(epService, uri.getDescriptor().asString()));
        addMapping(WriteFacade.class, new EsperWriteFacade(epService, uri.getDescriptor().asString()));
        addMapping(DispatchFacade.class, new EsperWriteFacade(epService, uri.getDescriptor().asString()));
    }


}