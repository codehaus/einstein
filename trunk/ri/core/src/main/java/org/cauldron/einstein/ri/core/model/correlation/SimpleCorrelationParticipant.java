/******************************************************************************
 *                                                                            *
 *                                                                            *
 *     All works are (C) 2008 - Mangala Solutions Ltd and Paremus Ltd.
 *                                                                            *
 *     Jointly liicensed to Mangala Solutions and Paremus under one           *
 *     or more contributor license agreements.  See the NOTICE file           *
 *     distributed with this work for additional information                  *
 *     regarding copyright ownership.  Mangala Solutions and Paremus          *
 *     licenses this file to you under the Apache License, Version            *
 *     2.0 (the "License"); you may not use this file except in               *
 *     compliance with the License.  You may obtain a copy of the             *
 *     License at                                                             *
 *                                                                            *
 *             http://www.apache.org/licenses/LICENSE-2.0                     *
 *                                                                            *
 *     Unless required by applicable law or agreed to in writing,             *
 *     software distributed under the License is distributed on an            *
 *     "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY                 *
 *     KIND, either express or implied.  See the License for the              *
 *     specific language governing permissions and limitations                *
 *     under the License.                                                     *
 *                                                                            *
 ******************************************************************************/

package org.cauldron.einstein.ri.core.model.correlation;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.cauldron.einstein.api.message.correlation.Correlation;
import org.cauldron.einstein.api.message.correlation.CorrelationParticipant;
import org.contract4j5.contract.Pre;

import java.util.*;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class SimpleCorrelationParticipant implements CorrelationParticipant {

    private final UUID uuid;
    private final Map<String, Correlation> correlations = new HashMap<String, Correlation>();

    public SimpleCorrelationParticipant() {

        uuid = UUID.randomUUID();
    }


    public Collection<Correlation> getCorrelations() {
        return correlations.values();
    }


    public Correlation getExecutionCorrelation() {
        return correlations.get("execution");
    }


    public Correlation getNamedCorrelation(String name) {
        return correlations.get(name);
    }


    public UUID getParticipantUUID() {
        return uuid;
    }

    @Pre
    public void setCorrelation(String name, Correlation newValue) {
        correlations.put(name, newValue);
    }


    @Pre
    public void setExecutionCorrelation(Correlation executionCorrelation) {
        correlations.put("execution", executionCorrelation);
    }


    public String getFormattedDebugInfo() {
        StringBuffer sb = new StringBuffer("Correlation Participant ");
        sb.append(uuid).append(" has correlations: \n\n");
        Set<Map.Entry<String, Correlation>> entries = correlations.entrySet();
        for (Map.Entry<String, Correlation> entry : entries) {
            sb.append(String.format("    %-16s - %-56s\n", entry.getKey(), entry.getValue().getFormattedDebugInfo()));
        }
        return sb.toString()
                ;
    }


    public void remove(String name) {
        correlations.remove(name);
    }


    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }



}
