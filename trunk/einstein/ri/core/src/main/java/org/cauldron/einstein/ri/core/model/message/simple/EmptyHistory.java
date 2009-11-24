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

package org.cauldron.einstein.ri.core.model.message.simple;

import org.cauldron.einstein.api.common.history.*;
import org.cauldron.einstein.ri.core.exception.UnsupportedOperationEinsteinRuntimeException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class EmptyHistory<T> implements History<T> {

    private final T thing;
    private final List<EntryListener<T>> entryListeners = new ArrayList<EntryListener<T>>();

    public EmptyHistory(T thing) {
        this.thing = thing;
    }


    public void addEntryListener(EntryListener<T> tEntryListener) {
        entryListeners.add(tEntryListener);
    }


    public Entry<T> getCurrentEntry() {
        return new Entry<T>() {

            public HistoryEvent<T> getEvent() {
                return new HistoryEvent<T>() {

                    public T getNewState() {
                        return thing;
                    }
                };
            }


            public boolean hasChanged() {
                return true;
            }


            public T getNewValue() {
                return thing;
            }
        };
    }


    public List<HistoryEvent<T>> getEvents() {
        throw new UnsupportedOperationEinsteinRuntimeException("getEvents", this.getClass().getCanonicalName());
    }


    public HistoryFeaturesSupported getHistoryFeaturesSupported() {
        return new HistoryFeaturesSupported() {

            public Set<HistoryFeature> getFeatureSet() {
                HashSet set = new HashSet();
                set.add(HistoryFeature.CURRENT_VALUE);
                return set;
            }


            public Integer getMaximumHistory() {
                return 1;
            }
        };
    }


    public HistoryEvent<T> getLastEvent() {
        throw new UnsupportedOperationEinsteinRuntimeException("getLastEvent", this.getClass().getCanonicalName());
    }


    public List<Entry<T>> getPastEntries() {
        throw new UnsupportedOperationEinsteinRuntimeException("getPastEntry", this.getClass().getCanonicalName());
    }


    public Entry<T> getPreviousEntry() {
        throw new UnsupportedOperationEinsteinRuntimeException("getPreviousEntry", this.getClass().getCanonicalName());
    }


    public Integer getVersionCount() {
        return 1;
    }
}
