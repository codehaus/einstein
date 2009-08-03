package org.cauldron.einstein.api.model.lifecycle;

import org.cauldron.einstein.api.model.profile.Profile;
import org.cauldron.einstein.api.model.profile.ProfileAware;
import org.contract4j5.contract.*;

/**
 * @author Neil Ellis
 */
@Contract
public interface LifecycleContext extends ProfileAware {

    static class SIMPLE_INSTANCE_FACTORY {
        public static LifecycleContext newInstance(final Profile profile) {
            return new LifecycleContext() {
                public Profile getActiveProfile() {
                    return profile;
                }
            };
        }
    }
}
