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

package org.cauldron.einstein.ri.core.instructions.io;

import org.cauldron.einstein.api.assembly.debug.CompilationInformation;
import org.cauldron.einstein.api.assembly.instruction.InstructionMetaData;
import org.cauldron.einstein.api.assembly.instruction.annotation.OperatorType;
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterInstruction;
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterOperator;
import org.cauldron.einstein.api.assembly.instruction.annotation.ResourceReference;
import org.cauldron.einstein.api.common.metadata.CoreMetaData;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.model.execution.ExecutionContext;
import org.cauldron.einstein.api.model.lifecycle.LifecycleContext;
import org.cauldron.einstein.api.model.resource.ResourceRef;
import org.cauldron.einstein.api.provider.facade.ReadFacade;
import org.cauldron.einstein.api.provider.facade.WriteFacade;
import org.cauldron.einstein.ri.core.instructions.AbstractInstruction;
import org.cauldron.einstein.ri.core.instructions.context.MessageAwareContext;


/**
 * See annotations for description of this instruction.
 *
 * @author Neil Ellis
 */
@RegisterInstruction(name = "swap", qualifiers = {"with"},
                     resources = @ResourceReference(facadesUsed = {ReadFacade.class, WriteFacade.class}),
                     metadata = @InstructionMetaData(
                             core = @CoreMetaData(
                                     name = "poll",
                                     description = "Takes a message from a resource using a blocking read, writes the current message to the resource, " +
                                                   "ignores any response and makes the taken message the current message.",
                                     shortDescription = "Swaps the current message with a resource.",
                                     syntax = " ( 'swap'  [ 'with' ] <resource> ) | ( [ <instruction-group> ] '<->' <resource> [ ':' <resource> ] )",
                                     example = "swap \"jms:queue\";"

                             ),
                             result = "A message from the specified resource."
                     ))

@RegisterOperator(name = "swap",
                  symbol = "<->",
                  types = {OperatorType.BINARY_REVERSED, OperatorType.UNARY, OperatorType.TERNARY})

@org.contract4j5.contract.Contract
public class SwapInstruction extends AbstractInstruction {

    private final ResourceRef ref;

    public SwapInstruction(CompilationInformation debug, ResourceRef ref) {
        super(debug);
        this.ref = ref;
    }


    public MessageTuple execute(ExecutionContext context, MessageTuple tuple) {
        assertStarted();
        logExecution(ref.getURI().asString());

        MessageAwareContext messageAwareContext = new MessageAwareContext(this, tuple);
        ReadFacade readFacade = ref.getResource(messageAwareContext).getFacade(ReadFacade.class);
        WriteFacade writeFacade = ref.getResource(messageAwareContext).getFacade(WriteFacade.class);

        MessageTuple readMessage = readFacade.read(messageAwareContext, false, false);
        readMessage.setExecutionCorrelation(tuple.getExecutionCorrelation());
        writeFacade.write(messageAwareContext,
                          context.getActiveProfile().getMessageModel().createExternalisableTuple(tuple),
                          false);
        return readMessage;
    }


    @Override
    public void init(LifecycleContext context) {
        super.init(context);
        ref.init(context);
    }


    @Override
    public void start(LifecycleContext context) {
        super.start(context);
        ref.start(context);
    }


    @Override
    public void stop(LifecycleContext context) {
        super.stop(context);
        ref.stop(context);
    }


    @Override
    public void destroy(LifecycleContext context) {
        super.destroy(context);
        ref.destroy(context);
    }
}