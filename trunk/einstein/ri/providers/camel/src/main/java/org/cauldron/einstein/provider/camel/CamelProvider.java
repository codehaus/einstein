package org.cauldron.einstein.provider.camel;

import org.cauldron.einstein.api.common.euri.EinsteinURI;
import org.cauldron.einstein.api.common.metadata.CoreMetaData;
import org.cauldron.einstein.api.model.resource.Resource;
import org.cauldron.einstein.api.provider.ProviderMetaData;
import org.cauldron.einstein.api.provider.annotation.RegisterProvider;
import org.cauldron.einstein.api.provider.facade.*;
import org.cauldron.einstein.ri.core.log.Logger;
import org.cauldron.einstein.ri.core.providers.base.AbstractProvider;

/**
 * @author Neil Ellis
 */
@RegisterProvider(name = "camel",
        metadata =
        @ProviderMetaData(
                core = @CoreMetaData(
                        name = "camel",
                        shortDescription = "Supports the providers supplied by the Camel ESB.",
                        description = "Supports the providers supplied by the Camel ESB.",
                        syntax = "'camel:' <camel-url>",
                        example = " read \"camel:http://rss.news.yahoo.com/rss/topstories\";"

                ),
                alwaysSupported = {ReadFacade.class, WriteFacade.class, ExecuteFacade.class, DispatchFacade.class, ListenFacade.class}
        ))
@org.contract4j5.contract.Contract
public class CamelProvider extends AbstractProvider {
    private static final Logger log = Logger.getLogger(CamelProvider.class);

    public Resource getLocalResource(EinsteinURI uri, Class[] facades) {
        log.fatal("Camel resource requested with support for facades {0}.", facades);
        return new CamelResource(uri, facades);
    }


}
