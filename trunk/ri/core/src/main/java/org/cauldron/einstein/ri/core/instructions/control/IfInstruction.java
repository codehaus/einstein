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
import org.cauldron.einstein.api.assembly.instruction.annotation.OperatorType;
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterInstruction;
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterOperator;
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
@RegisterInstruction(name = "if", qualifiers = {"else", "not"},
                     resources = @ResourceReference(facadesUsed = BooleanQueryFacade.class),
                     metadata = @InstructionMetaData(
                             core = @CoreMetaData(
                                     name = "if",
                                     shortDescription = "Conditional execution.",
                                     description = "If the boolean query supplied evalautes to true then the first supplied group is executed, otherwise if a second one is supplied use that.",
                                     syntax = "( 'if'  <resource> [ <instruction-group> [ 'else' ] [ <instruction-group> ] ]) | ( [ <instruction-group> ] '?' <resource> [ ':' <instruction-group> ] ) ",
                                     example = "if \"xpath://thing[size > 5]\" {write to bigThings} else { write to smallThings };"
                             ),
                             result = "The result of executing either selected block, or a void message if neither were executed."
                     )

)
@RegisterOperator(name = "if", symbol = "?", types = {OperatorType.TERNARY, OperatorType.BINARY, OperatorType.UNARY})
@org.contract4j5.contract.Contract
public class IfInstruction extends AbstractInstruction {

    private InstructionGroup group;
    private InstructionGroup elseGroup;
    private final ResourceRef ref;
    private boolean not;

    public IfInstruction(CompilationInformation debug, ResourceRef ref) {
        super(debug);
        this.ref = ref;
    }


    public IfInstruction(CompilationInformation debug, ResourceRef ref, InstructionGroup group) {
        super(debug);
        this.group = group;
        this.group.setParent(this);
        this.ref = ref;
    }


    public IfInstruction(CompilationInformation debug, ResourceRef ref, String... qualifiers) {
        this(debug, ref);
        not = hasQualifier("not", qualifiers);
    }


    public IfInstruction(CompilationInformation debug, ResourceRef ref, InstructionGroup group, String... qualifiers) {
        this(debug, ref, group);
        not = hasQualifier("not", qualifiers);
    }


    public IfInstruction(CompilationInformation debug, ResourceRef ref, InstructionGroup group,
                         InstructionGroup elseGroup) {
        super(debug);
        this.group = group;
        this.elseGroup = elseGroup;
        this.group.setParent(this);
        this.elseGroup.setParent(this);
        this.ref = ref;
    }


    public IfInstruction(CompilationInformation debug, ResourceRef ref, InstructionGroup group,
                         InstructionGroup elseGroup, String... qualifiers) {
        this(debug, ref, group, elseGroup);
        not = hasQualifier("not", qualifiers);
    }


    public MessageTuple execute(final ExecutionContext context, final MessageTuple inboundTuple) {
        assertStarted();
        final MessageTuple[] result = new MessageTuple[]{null};
        final boolean[] match = new boolean[1];
        if (inboundTuple.isVoid()) {
            return inboundTuple;
        } else if (inboundTuple.isEmpty()) {
            match[0] = ref.getResource(new MessageAwareContext(IfInstruction.this, inboundTuple))
                    .getFacade(BooleanQueryFacade.class)
                    .match(context.getActiveProfile().getDataModel().getDataObjectFactory().createNullDataObject());
        } else {
            inboundTuple.realiseAsOne().execute(QueryAction.class, new QueryAction() {

                public void handle(ActionCallbackContext<QueryView, Object> queryCallback) {
                    match[0] = ref.getResource(new MessageAwareContext(IfInstruction.this, inboundTuple))
                            .getFacade(BooleanQueryFacade.class)
                            .match(queryCallback.getMessageHistory().getCurrentEntry().getNewValue().getPayload());
                }
            });
        }

        if (match[0] ^ not) {
            //true
            if (group == null) {
                result[0] = inboundTuple;
            } else {
                result[0] = group.execute(context, inboundTuple);
            }
        } else {
            //false
            if (elseGroup == null) {
                result[0] = inboundTuple;
                //context.getActiveProfile()
                //        .getMessageModel()
                //        .createVoidMessage(inboundTuple.getExecutionCorrelation());
            } else {
                result[0] = elseGroup.execute(context, inboundTuple);
            }
        }

        return result[0];
    }


    @Override
    public void init(LifecycleContext ctx) {
        super.init(ctx);
        ref.init(ctx);
        if (group != null) {
            group.init(ctx);
        }
        if (elseGroup != null) {
            elseGroup.init(ctx);
        }
    }


    @Override
    public void start(LifecycleContext ctx) {
        super.start(ctx);
        ref.start(ctx);
        if (group != null) {
            group.start(ctx);
        }
        if (elseGroup != null) {
            elseGroup.start(ctx);
        }
    }


    @Override
    public void stop(LifecycleContext ctx) {
        super.stop(ctx);
        ref.stop(ctx);
        if (group != null) {
            group.stop(ctx);
        }
        if (elseGroup != null) {
            elseGroup.stop(ctx);
        }
    }


    @Override
    public void destroy(LifecycleContext ctx) {
        super.destroy(ctx);
        ref.destroy(ctx);
        if (group != null) {
            group.destroy(ctx);
        }
        if (elseGroup != null) {
            elseGroup.destroy(ctx);
        }
    }
}