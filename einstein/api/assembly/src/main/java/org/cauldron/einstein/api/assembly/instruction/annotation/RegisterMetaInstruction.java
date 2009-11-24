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

package org.cauldron.einstein.api.assembly.instruction.annotation;

import org.cauldron.einstein.api.common.annotation.AutoRegister;
import org.cauldron.einstein.api.common.annotation.AutoRegisterBy;

import java.lang.annotation.Retention;

/**
 * If a class has the RegisterMetaInstruction annotation it will be registered automatically as a MetaInstruction which
 * the MetaDataInstruction can execute.
 * <p/>
 * <p/>
 * The {@link org.cauldron.einstein.api.common.annotation.AutoRegister} meta-annotation is ued to make this annotation
 * auto-registering.
 *
 * @author Neil Ellis
 */
@AutoRegister(path = RegisterMetaInstruction.PATH, nameAttribute = "name", registerBy = AutoRegisterBy.CLASS)
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface RegisterMetaInstruction {
    String PATH = "/transient/runtime/meta-instruction";

    /**
     * The name of the meta instruction.
     *
     * @return an identifier style name.
     */
    public abstract String name();


}