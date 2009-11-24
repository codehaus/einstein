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
import org.cauldron.einstein.api.assembly.group.NamedTupleInstructionGroup;
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
import org.cauldron.einstein.api.provider.facade.RoutingFacade;
import org.cauldron.einstein.ri.core.instructions.context.MessageAwareContext;

/**
 * See annotations for description of this instruction.
 *
 * @author Neil Ellis
 */
@RegisterInstruction(name = "route", qualifiers = {"through", "to"},
                     resources = @ResourceReference(facadesUsed = RoutingFacade.class),
                     metadata = @InstructionMetaData(
                             core = @CoreMetaData(
                                     name = "route",
                                     shortDescription = "Routes to instructions in a named tuple.",
                                     description = "Passes control to the specified router to choose how the message should be routed to one or more of the named tuple entries.",
                                     syntax = "( 'route' [ 'through' ] <resource> [ 'to' ] <instruction-group> ) | ( [ <instruction-group> ] '=>' <resource> ':' <instruction-group>  ) ",
                                     example = "route through \"java:org.me.my.Router\" to [ red : << \"text:Roses\", blue : << \"text:Violets\""
                             ),
                             result = "The result of executing the choice(s) - the result is determined by the router as this is complete inversion of control."
                     )

)

@RegisterOperator(name = "route", symbol = "=>", types = {OperatorType.TERNARY})
@org.contract4j5.contract.Contract
public class RouteInstruction extends AbstractInstruction {

    private final ResourceRef ref;
    private final NamedTupleInstructionGroup group;

    public RouteInstruction(CompilationInformation debug, ResourceRef ref, NamedTupleInstructionGroup group) {
        super(debug);
        this.ref = ref;
        this.group = group;
        this.group.setParent(this);
    }


    public RouteInstruction(CompilationInformation debug, ResourceRef ref, NamedTupleInstructionGroup group,
                            String... ignore) {
        this(debug, ref, group);
    }


    public MessageTuple execute(final ExecutionContext context, MessageTuple tuple) {
        logExecution(ref.getURI().asString());        
        final MessageTuple result = ref.getResource(new MessageAwareContext(this, tuple))
                .getFacade(RoutingFacade.class)
                .route(this, tuple, group);
        result.setExecutionCorrelation(tuple.getExecutionCorrelation());
        return result;
    }


    @Override
    public void init(LifecycleContext context) {
        group.init(context);
        ref.init(context);
    }


    @Override
    public void start(LifecycleContext context) {
        group.start(context);
        ref.start(context);
    }


    @Override
    public void stop(LifecycleContext context) {
        group.stop(context);
        ref.stop(context);
    }


    @Override
    public void destroy(LifecycleContext context) {
        group.destroy(context);
        ref.destroy(context);
    }
}