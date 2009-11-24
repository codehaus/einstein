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
 * @author Neil Ellis
 */

public interface ReadAndWritePropertyNode extends PropertyNode {
    /**
     * Get a child state which has the associated label from this state.
     *
     * @param label the label for the state.
     *
     * @return a ReadOnlyPropertyNode which may or may not have children.
     */
    ReadAndWritePropertyNode get(PropertyLabel label);

    /**
     * A scripting friendly version of {@link #get(PropertyLabel)}.
     *
     * @param name the name of the
     */
    ReadAndWritePropertyNode get(String name);

    /**
     * Sets a new child state to the label.
     *
     * @param label the label associated with the state
     * @param state the new state.
     */
    void set(PropertyLabel label, PropertyNode state);

    /**
     * Performs the same as {@link #set(PropertyLabel , PropertyNode)} but is provided for scripting languages.
     */
    void set(String name, PropertyNode state);

    /**
     * Sets the value of the state node, the only acceptabel types are Immutables; currently String and Numbers.
     *
     * @param clazz the class of the value.
     * @param value the value to set this state node to.
     */
    <M> void setValue(Class<M> clazz, M value);


    void setValue(Object value);
}
