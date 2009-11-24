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

package org.cauldron.einstein.ri.core.instructions.meta;

import mazz.i18n.Msg;
import org.cauldron.einstein.api.assembly.debug.CompilationInformation;
import org.cauldron.einstein.api.assembly.group.InstructionGroup;
import org.cauldron.einstein.api.assembly.instruction.MetaInstruction;
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterMetaInstruction;
import org.cauldron.einstein.api.message.data.model.DataModel;
import org.cauldron.einstein.api.model.annotation.RegisterDataModel;
import org.cauldron.einstein.api.model.lifecycle.LifecycleContext;
import org.cauldron.einstein.api.model.lifecycle.exception.InitializationRuntimeException;
import static org.cauldron.einstein.ri.core.CoreModuleMessages.*;
import org.cauldron.einstein.ri.core.model.message.simple.SimpleProfile;
import org.cauldron.einstein.ri.core.registry.EinsteinRegistryFactory;
import org.cauldron.einstein.ri.core.log.Logger;

/**
 * @author Neil Ellis
 */
@RegisterMetaInstruction(name = "DataModel")
@org.contract4j5.contract.Contract
public class DataModelMetaInstruction implements MetaInstruction {
    private static final Logger log = Logger.getLogger(DataModelMetaInstruction.class);
    private DataModel model;
    private final CompilationInformation compilationInformation;
    private final String modelName;

    public DataModelMetaInstruction(CompilationInformation compilationInformation, String modelName) throws
                                                                                                     DataModelRuntimeException {
        this.compilationInformation = compilationInformation;
        this.modelName = modelName;
    }


    public void execute(InstructionGroup group) {
        log.debug("Setting group at {0} data model to {1}.", group.getCompilationInformation().getPositionDescription(), model);
        group.setProfile(new SimpleProfile(model, null, null, null, null, null, null, group.getActiveProfile()));
    }


    public CompilationInformation getCompilationInformation() {
        return compilationInformation;
    }


    public Msg getPositionDescription() {
        return compilationInformation.getPositionDescription();
    }


    public void init(LifecycleContext ctx) throws InitializationRuntimeException {
        Object o = EinsteinRegistryFactory.getInstance().get(RegisterDataModel.PATH + "/" + modelName);
        if (o == null) {
            throw new DataModelRuntimeException(BUNDLE_NAME, DATA_MODEL_NOT_IN_REGISTRY, modelName);
        }
        if (!(o instanceof Class)) {
            throw new DataModelRuntimeException(BUNDLE_NAME, DATA_MODEL_NOT_REGISTRERED_AS_CLASS, modelName);
        }
        final Class<? extends DataModel> clazz = (Class<? extends DataModel>) o;
        try {
            model = clazz.newInstance();
        } catch (InstantiationException e) {
            throw new DataModelRuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new DataModelRuntimeException(e);
        }
    }
}
