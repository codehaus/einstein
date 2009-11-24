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

package org.cauldron.einstein.ri.core.instructions.execution;

import org.cauldron.einstein.api.assembly.debug.CompilationInformation;
import org.cauldron.einstein.api.common.euri.EinsteinURI;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.model.execution.ExecutionContext;
import org.cauldron.einstein.api.model.execution.StatefulExecutable;
import org.cauldron.einstein.api.model.lifecycle.LifecycleContext;
import org.cauldron.einstein.api.model.resource.ResourceRef;
import org.cauldron.einstein.api.provider.facade.ExecuteFacade;
import org.cauldron.einstein.ri.core.instructions.AbstractInstruction;
import org.cauldron.einstein.ri.core.instructions.context.MessageAwareContext;

/**
 * @author Neil Ellis
 */

class ExecuteURIInstruction extends AbstractInstruction {

    private EinsteinURI uri;
    private final ResourceRef ref;


    public ExecuteURIInstruction(CompilationInformation debug, ResourceRef ref) {
        super(debug);
        this.ref = ref;
    }


    public MessageTuple execute(ExecutionContext context, final MessageTuple tuple) {
        StatefulExecutable statefulExecutable = getActiveProfile().getExecutionModel()
                .getSingleInstructionExecutor(ref.getResource(new MessageAwareContext(this,
                                                                                      tuple)).getFacade(ExecuteFacade.class));
        return statefulExecutable.execute(this,
                                          context.getActiveProfile()
                                                  .getMessageModel().createExternalisableTuple(tuple));
    }


    public void init(LifecycleContext ctx) {
        ref.init(ctx);
    }


    public void start(LifecycleContext ctx) {
        ref.start(ctx);
    }


    public void stop(LifecycleContext ctx) {
        ref.stop(ctx);
    }


    public void destroy(LifecycleContext ctx) {
        ref.destroy(ctx);
    }
}