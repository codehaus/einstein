package org.cauldron.einstein.api.model.execution;

import org.cauldron.einstein.api.common.debug.HasFormattedDebugInformation;
import org.cauldron.einstein.api.model.resource.Resource;
import org.contract4j5.contract.Contract;
import org.contract4j5.contract.Post;
import org.contract4j5.contract.Pre;

import java.util.Map;

/**
 * @author Neil Ellis
 */
@Contract
public interface ExecutionScope extends HasFormattedDebugInformation {

    @Pre Resource getResource(String name);

    @Pre void addResource(ExecutionContext ctx, String name, Resource resource);

    ExecutionScope getParent();

    @Pre void destroy(ExecutionContext ctx);

    @Post String getId();

    @Post Map<String, Resource> getResourceMap();
}
