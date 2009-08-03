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

import org.cauldron.einstein.api.assembly.group.InstructionGroup;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.model.profile.Profile;
import org.cauldron.einstein.ri.core.instructions.context.ProfileAwareContext;
import org.cauldron.einstein.ri.core.model.correlation.SimpleCorrelation;
import org.cauldron.einstein.ri.core.model.data.object.ObjectGraphDataModel;
import org.cauldron.einstein.ri.core.model.execution.direct.DirectExecutionModel;
import org.cauldron.einstein.ri.core.model.message.simple.EmptySimpleMessage;
import org.cauldron.einstein.ri.core.model.message.simple.SimpleMessageModel;
import org.cauldron.einstein.ri.core.model.message.simple.SimpleProfile;
import org.cauldron.einstein.ri.core.runtime.EinsteinRIRuntimeFactory;
import org.cauldron.einstein.ri.core.runtime.EinsteinStandaloneRuntime;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class EinsteinScriptRunner {

    protected final Profile pojoProfile = new SimpleProfile(new ObjectGraphDataModel(),
                                                            new DirectExecutionModel(),
                                                            new SimpleMessageModel(),
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            null);

    protected final ProfileAwareContext lifecycleContext = new ProfileAwareContext(pojoProfile);
    private final InstructionGroup group;

    public EinsteinScriptRunner(InstructionGroup group) {
        this.group = group;
    }


    public void run() {
        call();
    }


    public MessageTuple call() {
        EinsteinStandaloneRuntime runtime = new EinsteinStandaloneRuntime();
        EinsteinRIRuntimeFactory.getInstance().setRuntime(runtime);
        group.init(lifecycleContext);
        group.start(lifecycleContext);
        MessageTuple messageTuple;
        try {
            messageTuple = group.execute(lifecycleContext,
                                         new EmptySimpleMessage(new SimpleCorrelation(null)));
        } finally {
            group.stop(lifecycleContext);
            group.destroy(lifecycleContext);
        }
        return messageTuple;
    }
}