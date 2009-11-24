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

package org.cauldron.einstein.ri.core.instructions;

import org.cauldron.einstein.api.assembly.debug.CompilationInformation;
import org.cauldron.einstein.api.assembly.instruction.InstructionMetaData;
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterInstruction;
import org.cauldron.einstein.api.common.euri.EinsteinURI;
import org.cauldron.einstein.api.common.metadata.CoreMetaData;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.model.execution.ExecutionContext;
import org.cauldron.einstein.api.model.execution.ExecutionScope;
import org.cauldron.einstein.api.model.execution.ExecutionScopeManager;
import org.cauldron.einstein.api.model.resource.Resource;
import org.cauldron.einstein.ri.core.euri.EinsteinURIFactory;
import org.cauldron.einstein.ri.core.providers.factory.ProviderFactory;

/**
 * See annotations for description of this instruction.
 *
 * @author Neil Ellis
 */
@RegisterInstruction(name = "resource",
                     metadata = @InstructionMetaData(
                             core = @CoreMetaData(
                                     name = "resource",
                                     shortDescription = "Declare a resource based on a URI.",
                                     description = "Declare a resource based on a URI.",
                                     syntax = "'resource' <uri> <identifier>",
                                     example = "resource \"text:Hello World!\""
                             ),
                             result = "The current message."
                     ))
@org.contract4j5.contract.Contract
public class ResourceDeclerationInstruction extends AbstractInstruction {

    private final String identifier;
    private final Class[] facades;
    private final EinsteinURI uri;

    public ResourceDeclerationInstruction(CompilationInformation debug, String uri, String identifier,
                                          Class[] facades) {
        super(debug);
        this.identifier = identifier;
        this.facades = facades;
        this.uri = EinsteinURIFactory.createURI(uri);
    }


    public MessageTuple execute(ExecutionContext context, MessageTuple message) {
        assertStarted();
        logExecution(uri.asString(), identifier);

        Resource resource = ProviderFactory.getInstance()
                .getRegisteredProvider(uri.getProviderName())
                .getLocalResource(uri, facades);
        ExecutionScopeManager scopeManager = context.getActiveProfile().getExecutionModel().getScopeManager();
        ExecutionScope scope = scopeManager.getScope(message.getExecutionCorrelation());
        scope.addResource(context, identifier, resource);
        return message;
    }
}