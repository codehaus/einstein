package org.cauldron.einstein.provider.camel;

import mazz.i18n.annotation.I18NMessage;
import mazz.i18n.annotation.I18NResourceBundle;

/**
 * @author Neil Ellis
 */
@I18NResourceBundle(baseName=CamelProviderMessages.BUNDLE_NAME)
public interface CamelProviderMessages {

    public static final String BUNDLE_NAME="camel";

    @I18NMessage("Failed to create polling consumer on {0}")
    public static final String FAILED_TO_CREATE_POLL = "failed.to.create.polling.consumer";

    @I18NMessage("Failed to create consumer on {0}")
    public static final String FAILED_TO_CREATE_CONSUMER = "failed.to.create.consumer";

//    @I18NMessage("Blocking parameter {0} is not valid.")
//    public static final String BLOCKING_PARAMTERE_INVALID = "blocking.parameter.invalid";


}
