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

package org.cauldron.einstein.ri.core.instructions.tuple;

import org.cauldron.einstein.api.assembly.debug.CompilationInformation;
import org.cauldron.einstein.api.assembly.instruction.InstructionMetaData;
import org.cauldron.einstein.api.assembly.instruction.annotation.OperatorType;
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterInstruction;
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterOperator;
import org.cauldron.einstein.api.common.metadata.CoreMetaData;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.model.execution.ExecutionContext;
import org.cauldron.einstein.ri.core.instructions.AbstractInstruction;

import java.util.ArrayList;
import java.util.List;


/**
 * See annotations for description of this instruction.
 *
 * @author Neil Ellis
 */
@RegisterInstruction(name = "flatten",
                     metadata = @InstructionMetaData(
                             core = @CoreMetaData(
                                     name = "flatten",
                                     shortDescription = "Flatten a tuple.",
                                     description = "Flattens a tuple into a tuple that comtaines no tuples. I.e. [a,[b,c]] becomes [a,b,c].",
                                     syntax = "( 'flatten'  | (  '|-' <instruction-group>  ) ",
                                     example = "[ read \"text:A\",[ read \"text:B\", read \"text:C\" ] ]; flatten;"
                             ),
                             result = "A tuple which does not contain tuples."
                     ))

@RegisterOperator(name = "flatten",
                  symbol = "|-",
                  types = {OperatorType.UNARY_REVERSED, OperatorType.OPERATOR_ONLY, OperatorType.N_ARY})
@org.contract4j5.contract.Contract
public class FlattenInstruction extends AbstractInstruction {

    public FlattenInstruction(CompilationInformation debug) {
        super(debug);
    }


    public MessageTuple execute(ExecutionContext context, MessageTuple message) {
        assertStarted();
        logExecution();
        
        List<MessageTuple> list = new ArrayList<MessageTuple>();
        flatten(message, list);
        return getParent().getActiveProfile()
                .getMessageModel()
                .createTuple(context.getActiveProfile().getDataModel(), list);
    }


    private void flatten(MessageTuple message, List<MessageTuple> result) {
        List<MessageTuple> messages = message.realiseAsList();
        for (MessageTuple messageTuple : messages) {
            if (messageTuple.isOne()) {
                result.add(messageTuple);
            } else {
                flatten(messageTuple, result);
            }
        }
    }
}