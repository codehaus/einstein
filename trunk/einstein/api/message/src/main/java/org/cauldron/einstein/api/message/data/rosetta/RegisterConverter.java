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

package org.cauldron.einstein.api.message.data.rosetta;

import org.cauldron.einstein.api.common.annotation.AutoRegister;
import org.cauldron.einstein.api.common.annotation.AutoRegisterBy;
import org.cauldron.einstein.api.message.data.model.DataModel;

import java.lang.annotation.Retention;

/**
 * @author Neil Ellis
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@AutoRegister(path = "/transient/runtime/datamodel/converters/", nameAttribute = "name", registerBy = AutoRegisterBy.CLASS)
public @interface RegisterConverter {

    /**
     * The name of the converter.
     *
     * @return the name.
     */
    String name();


    /**
     * The {@link DataModel} this strategy can convert from.
     *
     * @return the {@link Class} of the DataModel.
     */
    Class<? extends DataModel> from();

    /**
     * The {@link DataModel} this strategy can convert to.
     *
     * @return the {@link Class} of the DataModel.
     */
    Class<? extends DataModel> to();
}
