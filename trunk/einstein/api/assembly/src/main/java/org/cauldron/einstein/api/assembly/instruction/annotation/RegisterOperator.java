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

package org.cauldron.einstein.api.assembly.instruction.annotation;

import org.cauldron.einstein.api.common.annotation.AutoRegister;
import org.cauldron.einstein.api.common.annotation.AutoRegisterBy;

import java.lang.annotation.Retention;

/**
 * If a class has the RegisterInstruction annotation it will be registered automatically as an operator which the
 * Einstein parser will compile.
 * <p/>
 * The {@link AutoRegister} meta-annotation is ued to make this annotation auto-registering.
 *
 * @author Neil Ellis
 */
@AutoRegister(path = RegisterOperator.PATH, nameAttribute = "name", registerBy = AutoRegisterBy.CLASS)
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface RegisterOperator {

    String PATH = "/transient/runtime/operator";

    /**
     * This is used to register the operator and should be the same name as the instruction if an operator is registered
     * as both an instruction and an operator.
     */
    String name();

    /**
     * The actula symbol used to represent the operation; e.g. ++, - , =
     *
     * @return the symbol that the parser will look for.
     */
    String symbol();

    /**
     * What type of operator can this operator be, e.g. binary, unary and so on.
     *
     * @return the types of operator this operator can be.
     */
    OperatorType[] types();

}