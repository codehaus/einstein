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

package org.cauldron.einstein.api.common.state;

/**
 * Classes implementing this interface can have their state read using a standard mechanism which ensures that child
 * states are also read only. {@link #getValue(Class)} can only return immutable types.
 *
 * @author Neil Ellis
 */

public interface ReadOnlyPropertyNode extends PropertyNode {

    /**
     * Get a child state which has the associated label from this state.
     *
     * @param label the label for the state.
     *
     * @return a ReadOnlyPropertyNode which may or may not have children.
     */
    ReadOnlyPropertyNode get(PropertyLabel label);

    /**
     * A scripting friendly version of {@link #get(PropertyLabel)}.
     *
     * @param name the name of the
     */
    ReadOnlyPropertyNode get(String name);


}
