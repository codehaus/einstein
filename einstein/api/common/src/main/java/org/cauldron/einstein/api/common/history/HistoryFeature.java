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

/**
 *
 * @author Neil Ellis
 *
 */

package org.cauldron.einstein.api.common.history;

public enum HistoryFeature {
    /**
     * The current value is kept, this should be a feature of every History.
     */
    CURRENT_VALUE,
    /**
     * Are past entries kept at all.
     */
    PAST_HISTORY,
    /**
     * Can the object tracked in the history be mutated. If not then just read events will be recorded. This is
     * incompatible with VALUES_RECORDED.
     */
    MUTATION_ALLOWED,
    /**
     * Are values kept in the history. Lightweight histories may choose not to keep a permanent records of changes.
     */
    VALUES_RECORDED,
    /**
     * Are events kept in the history.
     */
    EVENTS_RECORDED
}
