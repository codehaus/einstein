package org.cauldron.einstein.api.provider.facade.context;

import org.cauldron.einstein.api.model.profile.ProfileAware;
import org.cauldron.einstein.api.message.MessageAware;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public interface ProfileAndMessageAwareContext extends ProfileAware, MessageAware {
}
