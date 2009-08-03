package org.cauldron.einstein.api.model.resource;

import org.contract4j5.contract.*;

import org.cauldron.einstein.api.model.lifecycle.Lifecycle;
import org.cauldron.einstein.api.model.resource.Resource;
import org.cauldron.einstein.api.common.euri.EinsteinURI;

/**
 * @author Neil Ellis
 */
@Contract
public interface ResourceRef extends Lifecycle {

    @Pre
    Resource getResource(@Pre ResourceContext context);

    @Pre
    EinsteinURI getURI();
}
