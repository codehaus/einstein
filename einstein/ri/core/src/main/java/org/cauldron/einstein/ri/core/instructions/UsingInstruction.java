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
import org.cauldron.einstein.api.assembly.group.InstructionGroup;
import org.cauldron.einstein.api.assembly.instruction.InstructionMetaData;
import org.cauldron.einstein.api.assembly.instruction.MetaInstruction;
import org.cauldron.einstein.api.assembly.instruction.annotation.OperatorType;
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterInstruction;
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterOperator;
import org.cauldron.einstein.api.common.metadata.CoreMetaData;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.model.execution.ExecutionContext;
import org.cauldron.einstein.api.model.lifecycle.LifecycleContext;
import org.cauldron.einstein.ri.core.log.Logger;

import java.util.Map;
import java.util.Set;

/**
 * @author Neil Ellis
 */

@RegisterInstruction(name = "using",
                     metadata = @InstructionMetaData(core = @CoreMetaData(
                             name = "using",
                             shortDescription = "Used to apply meta instructions to an execution group.",
                             description = "Used to apply meta instructions to an execution group.",
                             syntax = "'using' <meta-instruction-list> <instruction-group>",
                             example = "using ( DataModel : XMLDOM ) {\n" +
                                       "                                        read \"camel:http://rss.news.yahoo.com/rss/topstories\";\n" +
                                       "                                        split \"xpath://item/title/text()\" {write \"console:Headline: \"};\n" +
                                       "                                   };"

                     ), result = "The result of executing the instruction group."
                     )
)
@RegisterOperator(name = "using", symbol = "@", types = {OperatorType.TERNARY})
@org.contract4j5.contract.Contract
public class UsingInstruction extends AbstractInstruction {

    private static final Logger log = Logger.getLogger(UsingInstruction.class);

    private final Map<String, MetaInstruction> metaData;
    private final InstructionGroup group;

    public UsingInstruction(CompilationInformation debug, Map metaData, InstructionGroup group) {
        super(debug);
        this.metaData = metaData;
        this.group = group;
        this.group.setParent(this);
    }


    public MessageTuple execute(ExecutionContext context, MessageTuple message) {
        assertStarted();
        logExecution();
        return group.execute(context, message);
    }


    @Override
    public void init(LifecycleContext context) {
        super.init(context);

        Set<Map.Entry<String, MetaInstruction>> entries = metaData.entrySet();
        for (Map.Entry<String, MetaInstruction> entry : entries) {
            log.debug("Executing meta instruction {0}.", entry.getKey());

            MetaInstruction metaInstruction = entry.getValue();
            metaInstruction.init(context);
            metaInstruction.execute(group);
        }
        group.init(context);
    }


    @Override
    public void start(LifecycleContext context) {
        super.start(context);
        group.start(context);
    }


    @Override
    public void stop(LifecycleContext context) {
        super.stop(context);
        group.stop(context);
    }


    @Override
    public void destroy(LifecycleContext context) {
        super.destroy(context);
        group.destroy(context);
    }
}