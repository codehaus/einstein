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
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterInstruction;
import org.cauldron.einstein.api.common.metadata.CoreMetaData;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.model.execution.ExecutionContext;
import org.cauldron.einstein.api.model.profile.Profile;
import org.cauldron.einstein.ri.core.instructions.AbstractInstruction;
import org.cauldron.einstein.ri.core.log.Logger;

import java.util.ArrayList;
import java.util.List;


/**
 * See annotations for description of this instruction.
 *
 * @author Neil Ellis
 */
@RegisterInstruction(name = "element", qualifiers = {"end", "to"},
                     metadata = @InstructionMetaData(
                             core = @CoreMetaData(
                                     name = "element",
                                     shortDescription = "Extracts a tuple entry by position.",
                                     description = "Extracts a tuple entry by position.",
                                     syntax = "['element'] <integer> ( 'to' ( <integer> | 'end' ) )",
                                     example = "[current, read \"time:hh:mm:ss\",  read \"text:Note\"]; 1 to 2;"
                             ),
                             result = "The selected tuple element or an empty message."
                     ))
@org.contract4j5.contract.Contract
public class ElementInstruction extends AbstractInstruction {

    private static final Logger log = Logger.getLogger(ElementInstruction.class);
    private final int position;
    private boolean end;
    private Integer endValue;

    public ElementInstruction(CompilationInformation debug, int position) {
        super(debug);
        this.position = position;
    }


    public ElementInstruction(CompilationInformation debug, int position, String... qualifiers) {
        super(debug);
        this.position = position;
        if (hasQualifier("end", qualifiers)) {
            end = true;
        }
    }


    public ElementInstruction(CompilationInformation debug, int position, Integer end, String... qualifiers) {
        this(debug, position, qualifiers);
        endValue = end;
    }


    public MessageTuple execute(ExecutionContext context, MessageTuple message) {
        assertStarted();
        logExecution(position, end ? "end" : endValue);

        List<MessageTuple> list = message.realiseAsList();
        log.debug("Tuple before element extraction is {0}.", list);
        final Profile profile = context.getActiveProfile();
        if (end) {
            log.debug("To End");
            List<MessageTuple> result = new ArrayList<MessageTuple>();
            for (int i = position; i < list.size(); i++) {
                result.add(list.get(i));
            }
            log.debug("Tuple after element extraction is {0}.", result);
            return profile.getMessageModel().createTuple(profile.getDataModel(), result);
        } else if (endValue != null) {
            List<MessageTuple> result = new ArrayList<MessageTuple>();
            for (int i = position; i <= endValue; i++) {
                result.add(list.get(i));
            }
            log.debug("Tuple after element extraction is {0}.", result);
            return profile.getMessageModel().createTuple(profile.getDataModel(), result);
        }
        MessageTuple result = list.get(position);
        log.debug("Single element extraction is {0}.", result);
        return result;
    }
}