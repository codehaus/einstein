/******************************************************************************
 *                                                                            *
 *                                                                            *
 *     All works are (C) 2008- Mangala Solutions Ltd and Paremus Ltd.         *
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

package org.cauldron.einstein.api.common.history;


import org.contract4j5.contract.*;


import java.util.List;

/**
 * The History allows access to multiple versions of an Object of type T. It is not required that implementations
 * support the actual history but they should at least support the current object.
 *
 * @author Neil Ellis
 */
@Contract
public interface History<T> {

    HistoryFeaturesSupported getHistoryFeaturesSupported();

    @Pre
    Entry<T> getCurrentEntry();

    Entry<T> getPreviousEntry();

    @Pre
    List<Entry<T>> getPastEntries();

    Integer getVersionCount();

    void addEntryListener(@Pre EntryListener<T> listener);

    HistoryEvent<T> getLastEvent();

    @Pre
    List<HistoryEvent<T>> getEvents();


}
