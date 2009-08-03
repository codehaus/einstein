package org.cauldron.einstein.api.model.resource.exception;

import mazz.i18n.annotation.I18NMessage;
import mazz.i18n.annotation.I18NResourceBundle;
import org.cauldron.einstein.api.common.exception.EinsteinRuntimeException;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
@I18NResourceBundle(baseName = "model-api")
public class ResourceNotFoundRuntimeException extends EinsteinRuntimeException {
    
    @I18NMessage("Could not find resource reference {0} (refers to \"{1}\") in scope.\\nScope was: \\n{2}\\nAnd participant was: \\n{3}\\n")
    public static final String COULD_NOT_LOCATE_RESOURCE_REFERNCE_IN_SCOPE = "resource.not.in.scope";

    public ResourceNotFoundRuntimeException(String name, String uri, String scopeInfo, String correlationInfo) {
       super("model-api",COULD_NOT_LOCATE_RESOURCE_REFERNCE_IN_SCOPE, name, uri, scopeInfo, correlationInfo );
    }
}
