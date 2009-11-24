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

package org.cauldron.einstein.ri.core.runtime;

import org.cauldron.einstein.api.assembly.instruction.Instruction;
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterOperator;
import org.cauldron.einstein.ri.core.registry.EinsteinRegistryFactory;

import java.util.HashMap;
import java.util.List;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class OperatorLookup {

    private static final OperatorLookup instance = new OperatorLookup();
    private HashMap<String, Class<? extends Instruction>> lookup;

    private OperatorLookup() {
        init();
    }


    void init() {
        List all = EinsteinRegistryFactory.getInstance().getAll(RegisterOperator.PATH);
        lookup = new HashMap<String, Class<? extends Instruction>>();
        for (Object o : all) {
            Class<? extends Instruction> clazz = (Class<? extends Instruction>) o;
            RegisterOperator registerOperator = clazz.getAnnotation(RegisterOperator.class);
            lookup.put(registerOperator.symbol(), clazz);
        }
    }


    public static OperatorLookup getInstance() {
        return instance;
    }


    public Class<? extends Instruction> getClassForOperator(String symbol) {
        return lookup.get(symbol);
    }
}

