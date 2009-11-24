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
import org.cauldron.einstein.api.assembly.group.InstructionGroup;
import org.cauldron.einstein.api.assembly.instruction.Instruction;
import org.cauldron.einstein.api.common.euri.EinsteinURI;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.message.action.context.ActionCallbackContext;
import org.cauldron.einstein.api.message.action.types.ReadAction;
import org.cauldron.einstein.api.message.view.composites.ReadOnlyView;
import org.cauldron.einstein.api.model.execution.ExecutionContext;
import org.cauldron.einstein.api.model.execution.StatefulExecutable;
import org.cauldron.einstein.api.model.lifecycle.LifecycleContext;
import org.cauldron.einstein.api.provider.facade.ExecuteFacade;
import org.cauldron.einstein.ri.core.euri.EinsteinURIFactory;
import org.cauldron.einstein.ri.core.instructions.AbstractInstruction;
import org.cauldron.einstein.ri.core.providers.factory.ProviderFactory;

/**
 * @author Neil Ellis
 */

class ExecuteGroupInstruction extends AbstractInstruction {

    private InstructionGroup group;

    private Instruction implementation;


    public ExecuteGroupInstruction(CompilationInformation debug, InstructionGroup group) {
        super(debug);
        this.group = group;
        this.group.setParent(this);
        
    }


    public MessageTuple execute(final ExecutionContext context, final MessageTuple tuple) {
        final MessageTuple[] result = new MessageTuple[1];
        group.execute(context, tuple).realiseAsOne().execute(ReadAction.class, new ReadAction() {

            public void handle(ActionCallbackContext<ReadOnlyView, Object> readOnlyViewActionCallbackContext) {
                EinsteinURI tempURI = EinsteinURIFactory.createURI(readOnlyViewActionCallbackContext.getMessageHistory()
                        .getCurrentEntry()
                        .getNewValue()
                        .getPayload().asString());
                ExecuteFacade executeFacade = ProviderFactory.getInstance()
                        .getRegisteredProvider(tempURI.getProviderName())
                        .getLocalResource(tempURI, new Class[]{ExecuteFacade.class})
                        .getFacade(ExecuteFacade.class);
                StatefulExecutable tempStatefulExecutable = getActiveProfile().getExecutionModel()
                        .getSingleInstructionExecutor(executeFacade);
                result[0] = tempStatefulExecutable.execute(ExecuteGroupInstruction.this,
                                                           context.getActiveProfile()
                                                                   .getMessageModel().createExternalisableTuple(tuple));
            }
        });
        return result[0];
    }


    @Override
    public void init(LifecycleContext context) {
        group.init(context);
    }


    @Override
    public void start(LifecycleContext ctx) {
        group.start(ctx);
    }


    @Override
    public void stop(LifecycleContext ctx) {
        group.start(ctx);
    }


    @Override
    public void destroy(LifecycleContext context) {
        group = null;
    }
}