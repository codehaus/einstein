package org.cauldron.einstein.api.message.view.components;

import org.cauldron.einstein.api.message.correlation.Correlation;
import org.contract4j5.contract.Contract;
import org.contract4j5.contract.Pre;

/**
 * @author Neil Ellis
 */
@Contract
public interface WriteCorrelation {

    /**
     * This inserts a new correlation between the previous correlation and this participant
     */
    @Pre
    void setCorrelation(String name, Correlation replacement);

    @Pre
    void setExecutionCorrelation(Correlation executionCorrelation);
}
