package org.cauldron.einstein.api.model.execution;

import org.cauldron.einstein.api.message.correlation.Correlation;
import org.contract4j5.contract.Contract;
import org.contract4j5.contract.Post;
import org.contract4j5.contract.Pre;

/**
 * @author Neil Ellis
 */
@Contract
public interface ExecutionScopeManager {

    @Pre
    @Post ExecutionScope getScope(Correlation correlation);

    @Pre
    @Post ExecutionScope createNewScope(Correlation correlation, String id);

    /**
     * @return the new scope now that one has ended.
     */
    @Pre void endScope(ExecutionContext ctx, ExecutionScope scope);

}
