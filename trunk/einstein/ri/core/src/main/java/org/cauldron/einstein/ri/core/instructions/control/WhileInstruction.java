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

package org.cauldron.einstein.ri.core.instructions.control;

import org.cauldron.einstein.api.assembly.debug.CompilationInformation;
import org.cauldron.einstein.api.assembly.group.InstructionGroup;
import org.cauldron.einstein.api.assembly.instruction.InstructionMetaData;
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterInstruction;
import org.cauldron.einstein.api.assembly.instruction.annotation.ResourceReference;
import org.cauldron.einstein.api.common.metadata.CoreMetaData;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.message.action.context.ActionCallbackContext;
import org.cauldron.einstein.api.message.action.types.QueryAction;
import org.cauldron.einstein.api.message.view.composites.QueryView;
import org.cauldron.einstein.api.model.execution.ExecutionContext;
import org.cauldron.einstein.api.model.lifecycle.LifecycleContext;
import org.cauldron.einstein.api.model.resource.ResourceRef;
import org.cauldron.einstein.api.provider.facade.BooleanQueryFacade;
import org.cauldron.einstein.ri.core.instructions.AbstractInstruction;
import org.cauldron.einstein.ri.core.instructions.context.MessageAwareContext;

/**
 * See annotations for description of this instruction.
 *
 * @author Neil Ellis
 */
@RegisterInstruction(name = "while", qualifiers = "not",
                     resources = @ResourceReference(facadesUsed = BooleanQueryFacade.class),
                     metadata = @InstructionMetaData(
                             core = @CoreMetaData(
                                     name = "while",
                                     shortDescription = "Conditional looped execution.",
                                     description = "If the boolean query supplied evalautes to true then the first supplied group is executed, the result of the group execution is the input for the condition and so on.",
                                     syntax = "'while' <resource> <instruction-group>",
                                     example = "while \"xpath://thing[size > 5]\" {write to bigThings; read from thingResource};"
                             ),
                             result = "The result of the last loops execution, or the inbound message if it is never executed."
                     )

)
@org.contract4j5.contract.Contract
public class WhileInstruction extends AbstractInstruction {

    private final ResourceRef queryRef;
    private final InstructionGroup group;
    private boolean not;

    public WhileInstruction(CompilationInformation debug, ResourceRef queryRef, InstructionGroup group) {
        super(debug);
        this.queryRef = queryRef;
        this.group = group;
        this.group.setParent(this);
    }


    public WhileInstruction(CompilationInformation debug, ResourceRef queryRef, InstructionGroup group,
                            String... qualifiers) {
        this(debug, queryRef, group);
        not = hasQualifier("not", qualifiers);
    }


    public MessageTuple execute(final ExecutionContext context, MessageTuple inboundTuple) {
        final MessageTuple[] currentMessageTuple = new MessageTuple[]{inboundTuple};
        final boolean[] cont = new boolean[1];
        do {
            if (currentMessageTuple[0].isVoid()) {
                break;
            } else if (currentMessageTuple[0].isEmpty()) {
                cont[0] = queryRef.getResource(new MessageAwareContext(this, currentMessageTuple[0]))
                        .getFacade(BooleanQueryFacade.class)
                        .match(context.getActiveProfile().getDataModel().getDataObjectFactory().createNullDataObject());
            } else {
                currentMessageTuple[0].realiseAsOne().execute(QueryAction.class, new QueryAction() {

                    public void handle(ActionCallbackContext<QueryView, Object> queryCallback) {
                        cont[0] = queryRef.getResource(new MessageAwareContext(WhileInstruction.this,
                                                                               currentMessageTuple[0]))
                                .getFacade(BooleanQueryFacade.class)
                                .match(queryCallback.getMessageHistory().getCurrentEntry().getNewValue().getPayload());
                    }
                });
            }
            if (cont[0] ^ not) {
                currentMessageTuple[0] = group.execute(context, currentMessageTuple[0]);
            }
        } while (cont[0] ^ not);
        return currentMessageTuple[0];
    }


    @Override
    public void init(LifecycleContext ctx) {
        queryRef.init(ctx);
        if (group != null) {
            group.init(ctx);
        }
    }


    @Override
    public void start(LifecycleContext ctx) {
        queryRef.start(ctx);
        if (group != null) {
            group.start(ctx);
        }
    }


    @Override
    public void stop(LifecycleContext ctx) {
        queryRef.stop(ctx);
        if (group != null) {
            group.stop(ctx);
        }
    }


    @Override
    public void destroy(LifecycleContext ctx) {
        queryRef.destroy(ctx);
        if (group != null) {
            group.destroy(ctx);
        }
    }
}