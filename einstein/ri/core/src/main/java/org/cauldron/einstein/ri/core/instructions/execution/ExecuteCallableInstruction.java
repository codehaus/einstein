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
import org.cauldron.einstein.api.model.execution.ExecutionFailureRuntimeException;
import org.cauldron.einstein.api.model.execution.StatefulExecutable;
import org.cauldron.einstein.api.provider.facade.ExecuteFacade;
import org.cauldron.einstein.ri.core.euri.EinsteinURIFactory;
import org.cauldron.einstein.ri.core.instructions.AbstractInstruction;
import org.cauldron.einstein.ri.core.providers.factory.ProviderFactory;

import java.util.concurrent.Callable;

/**
 * @author Neil Ellis
 */

class ExecuteCallableInstruction extends AbstractInstruction {

    private final Callable callable;


    public ExecuteCallableInstruction(CompilationInformation debug, Callable callable) {
        super(debug);
        this.callable = callable;
    }


    public MessageTuple execute(ExecutionContext context, final MessageTuple tuple) {
        try {
            String name = (String) callable.call();
            EinsteinURI tempURI = EinsteinURIFactory.createURI(name);
            ExecuteFacade executeFacade = ProviderFactory.getInstance()
                    .getRegisteredProvider(tempURI.getProviderName())
                    .getLocalResource(tempURI, new Class[]{ExecuteFacade.class})
                    .getFacade(ExecuteFacade.class);
            StatefulExecutable tempStatefulExecutable = getActiveProfile().getExecutionModel()
                    .getSingleInstructionExecutor(executeFacade);
            return tempStatefulExecutable.execute(this,
                                                  context.getActiveProfile()
                                                          .getMessageModel().createExternalisableTuple(tuple));
        } catch (Exception e) {
            throw new ExecutionFailureRuntimeException(e);
        }
    }
}