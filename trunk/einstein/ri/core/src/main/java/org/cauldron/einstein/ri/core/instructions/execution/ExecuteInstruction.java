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
import org.cauldron.einstein.api.assembly.instruction.InstructionMetaData;
import org.cauldron.einstein.api.assembly.instruction.annotation.OperatorType;
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterInstruction;
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterOperator;
import org.cauldron.einstein.api.assembly.instruction.annotation.ResourceReference;
import org.cauldron.einstein.api.common.metadata.CoreMetaData;
import org.cauldron.einstein.api.model.execution.PayloadOnlyExecutable;
import org.cauldron.einstein.api.model.resource.ResourceRef;
import org.cauldron.einstein.api.provider.facade.ExecuteFacade;
import org.cauldron.einstein.ri.core.instructions.DelegatingInstruction;

/**
 * @author Neil Ellis
 */

/**
 * See annotations for description of this instruction.
 *
 * @author Neil Ellis
 */

//todo: add 'async' as an optional qualifier
@RegisterInstruction(name = "execute",
                     resources = @ResourceReference(facadesUsed = ExecuteFacade.class),
                     metadata = @InstructionMetaData(
                             core = @CoreMetaData(
                                     name = "execute",
                                     shortDescription = "Execute a resource.",
                                     description = "Executes the supplied resource or the resource identified by the payload of the message created by executing the supplied instruction group. ",
                                     syntax = "( 'execute'  ( <resource> | <instruction-group> ) | ( '**'( <resource> | <instruction-group> ) ) ",
                                     example = "execute { read from \"text:jms:myqueue\" }; execute \"java:org.me.MyService\";"

                             ),
                             result = "The result of executing the resource."
                     )

)
@RegisterOperator(name = "execute", symbol = "**", types = {OperatorType.UNARY})
@org.contract4j5.contract.Contract
public class ExecuteInstruction extends DelegatingInstruction {

    public ExecuteInstruction(CompilationInformation debug, ResourceRef resourceRef) {
        super(debug);

        implementation = new ExecuteURIInstruction(debug, resourceRef);
    }


    public ExecuteInstruction(CompilationInformation debug, InstructionGroup group) {
        super(debug);

        implementation = new ExecuteGroupInstruction(debug, group);
    }


    public ExecuteInstruction(CompilationInformation debug, PayloadOnlyExecutable executable) {
        super(debug);
        implementation = new ExecuteExecutableInstruction(debug, executable);
    }
}