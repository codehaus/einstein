package org.cauldron.einstein.api.message;

import org.contract4j5.contract.Contract;
import org.contract4j5.contract.Post;

/**
 * @author Neil Ellis
 */
@Contract
public interface MessageAware {

    @Post
    MessageTuple getMessage();
}
