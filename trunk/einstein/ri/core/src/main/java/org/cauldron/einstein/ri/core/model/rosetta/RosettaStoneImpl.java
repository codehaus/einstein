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

package org.cauldron.einstein.ri.core.model.rosetta;

import org.cauldron.einstein.api.message.data.exception.DataObjectConversionRuntimeException;
import org.cauldron.einstein.api.message.data.model.DataModel;
import org.cauldron.einstein.api.message.data.model.DataObject;
import org.cauldron.einstein.api.message.data.rosetta.ConversionStrategy;
import org.cauldron.einstein.api.message.data.rosetta.RegisterConverter;
import org.cauldron.einstein.api.message.data.rosetta.RosettaStone;
import static org.cauldron.einstein.ri.core.CoreModuleMessages.BUNDLE_NAME;
import static org.cauldron.einstein.ri.core.CoreModuleMessages.CONVERTER_INSTATIATION_EXCEPTION;
import org.cauldron.einstein.ri.core.registry.EinsteinRegistryFactory;
import org.cauldron.einstein.ri.core.registry.exception.RegistryRuntimeException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class RosettaStoneImpl implements RosettaStone {

    private final Map<String, ConversionStrategy> lookup;


    public RosettaStoneImpl() {
        List all = EinsteinRegistryFactory.getInstance().getAll("/transient/runtime/datamodel/converters/");
        lookup = new HashMap<String, ConversionStrategy>();
        for (Object o : all) {
            Class<ConversionStrategy> strategy = (Class<ConversionStrategy>) o;
            RegisterConverter converter = strategy.getAnnotation(RegisterConverter.class);
            try {
                lookup.put(converter.from().getCanonicalName() + converter.to().getCanonicalName(),
                           strategy.newInstance());
            } catch (InstantiationException e) {
                throw new RegistryRuntimeException(e,
                                                   BUNDLE_NAME,
                                                   CONVERTER_INSTATIATION_EXCEPTION,
                                                   strategy.getCanonicalName());
            } catch (IllegalAccessException e) {
                throw new RegistryRuntimeException(e,
                                                   BUNDLE_NAME,
                                                   CONVERTER_INSTATIATION_EXCEPTION,
                                                   strategy.getCanonicalName());
            }
        }
    }


    public DataObject convert(Class<? extends DataModel> to, DataObject object) {
        if (to.equals(object.getDataModel().getClass())) {
            return object;
        }
        Class<? extends DataModel> fromClass = object.getDataModel().getClass();

        ConversionStrategy strategy = lookup.get(fromClass.getName() + to.getName());
        if (strategy == null) {
            throw new DataObjectConversionRuntimeException(fromClass, to);
        }
        return strategy.convert(object);
    }
}
