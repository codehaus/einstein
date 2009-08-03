package org.cauldron.einstein.provider.esper;

import org.cauldron.einstein.api.common.euri.EinsteinURI;
import org.cauldron.einstein.api.common.metadata.CoreMetaData;
import org.cauldron.einstein.api.model.resource.Resource;
import org.cauldron.einstein.api.provider.ProviderMetaData;
import org.cauldron.einstein.api.provider.annotation.RegisterProvider;
import org.cauldron.einstein.api.provider.facade.DispatchFacade;
import org.cauldron.einstein.api.provider.facade.ListenFacade;
import org.cauldron.einstein.api.provider.facade.ReadFacade;
import org.cauldron.einstein.api.provider.facade.WriteFacade;
import org.cauldron.einstein.ri.core.providers.base.AbstractProvider;

/**
 * @author Neil Ellis
 */
@RegisterProvider(name = "esper",
        metadata = @ProviderMetaData(
                core = @CoreMetaData(
                        name = "esper",
                        shortDescription = "Provies CEP functionality using the Esper libraries.",
                        description = "Supports the Esper CEP library, the standard pattern is to write to a resource and then add a listener to the resource with a text based query.",
                        syntax = "'esper:'",
                        example = "     resource \"esper:\" esper;\n\n    listen esper \"text:select avg(value) from org.cauldron.einstein.ri.examples.esper.TessaractWidget.win:time(30 sec)\" {\n         ( -> \"xpath:/underlying\" ) >> \"console:Tesseract average value is: \";\n\n    };execute \"java:org.cauldron.einstein.ri.examples.esper.EventMaker\" >> esper;"
                ),
                alwaysSupported = {ReadFacade.class, WriteFacade.class, DispatchFacade.class, ListenFacade.class}

        )
)
@org.contract4j5.contract.Contract
public class EsperProvider extends AbstractProvider {

    public Resource getLocalResource(EinsteinURI uri, Class[] facades) {
        return new EsperResource(uri);
    }


}