package org.cauldron.einstein.provider.xpath;

import org.cauldron.einstein.api.common.euri.EinsteinURI;
import org.cauldron.einstein.api.provider.facade.BooleanQueryFacade;
import org.cauldron.einstein.api.provider.facade.QueryFacade;
import org.cauldron.einstein.ri.core.providers.base.AbstractResource;

/**
 * @author Neil Ellis
 */

@org.contract4j5.contract.Contract
public class XPathResource extends AbstractResource {

    public XPathResource(EinsteinURI uri) {
        super(uri);
        addMapping(QueryFacade.class, new XPathQueryFacade(uri));
        addMapping(BooleanQueryFacade.class, new XPathQueryFacade(uri));
    }

}
