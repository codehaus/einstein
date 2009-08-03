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

import org.cauldron.einstein.api.message.correlation.Correlation;

import java.util.UUID;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class SimpleCorrelation implements Correlation {

    private Correlation parent;
    private final UUID uuid;

    public SimpleCorrelation(Correlation parent) {
        this.parent = parent;
        this.uuid = UUID.randomUUID();
    }


    public Correlation createChild() {
        return new SimpleCorrelation(this);
    }


    public Correlation getParent() {
        return parent;
    }


    public Type getType() {
        return Type.TEMPORARY;
    }


    public UUID getUUID() {
        return uuid;
    }


    public String getFormattedDebugInfo() {
        Correlation currCorrelation = this;
        StringBuffer sb = new StringBuffer();
        while (currCorrelation != null) {
            sb.append(currCorrelation.getUUID()).append("<-");
            currCorrelation = currCorrelation.getParent();
        }
        return sb.toString();
    }


    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SimpleCorrelation that = (SimpleCorrelation) o;

        return !(uuid != null ? !uuid.equals(that.uuid) : that.uuid != null);
    }


    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}
