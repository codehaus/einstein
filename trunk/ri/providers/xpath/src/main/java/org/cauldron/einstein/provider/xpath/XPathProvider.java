package org.cauldron.einstein.provider.xpath;

import org.cauldron.einstein.api.common.euri.EinsteinURI;
import org.cauldron.einstein.api.common.metadata.CoreMetaData;
import org.cauldron.einstein.api.provider.ProviderMetaData;
import org.cauldron.einstein.api.provider.annotation.RegisterProvider;
import org.cauldron.einstein.api.provider.facade.BooleanQueryFacade;
import org.cauldron.einstein.api.provider.facade.QueryFacade;
import org.cauldron.einstein.ri.core.providers.base.AbstractProvider;

/**
 * @author Neil Ellis
 */
@RegisterProvider(name = "xpath",
        metadata = @ProviderMetaData(
                core = @CoreMetaData(
                        name = "xpath",
                        shortDescription = "Allows messages to be queried using XPath/JXPath.",
                        description = "Allows messages to be queried using XPath/JXPath.",
                        syntax = "'xpath:' <xpath-expression>",
                        example = "if \"xpath:.[@value >> 90]\" {\n" +
                                "            ( -> \"xpath:/value\" ) >> \"console:High value widget value= \";\n" +
                                "         };"
                ),
                alwaysSupported = {BooleanQueryFacade.class,
                        QueryFacade.class}
        )
)
@org.contract4j5.contract.Contract
public class XPathProvider extends AbstractProvider {


    public XPathResource getLocalResource(EinsteinURI uri, Class[] facades) {
        return new XPathResource(uri);
    }
}
