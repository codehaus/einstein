package org.cauldron.einstein.api.message.view.components;

import org.cauldron.einstein.api.message.correlation.Correlation;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Neil Ellis
 */
public interface ReadCorrelation {

    /**
     * A universally unique identifier belonging to the participant.
     *
     * @return a unique identifier, unique across all machines.
     */
    UUID getParticipantUUID();

    Collection<Correlation> getCorrelations();

    Correlation getNamedCorrelation(String name);

    Correlation getExecutionCorrelation();
}
