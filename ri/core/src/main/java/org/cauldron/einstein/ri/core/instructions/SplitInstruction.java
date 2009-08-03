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
import org.cauldron.einstein.api.assembly.instruction.annotation.OperatorType;
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterInstruction;
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterOperator;
import org.cauldron.einstein.api.assembly.instruction.annotation.ResourceReference;
import org.cauldron.einstein.api.common.metadata.CoreMetaData;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.message.action.context.ActionCallbackContext;
import org.cauldron.einstein.api.message.action.types.SplitAction;
import org.cauldron.einstein.api.message.data.model.DataList;
import org.cauldron.einstein.api.message.data.model.DataObject;
import org.cauldron.einstein.api.message.view.composites.SplitView;
import org.cauldron.einstein.api.model.execution.ExecutionContext;
import org.cauldron.einstein.api.model.execution.Executor;
import org.cauldron.einstein.api.model.lifecycle.LifecycleContext;
import org.cauldron.einstein.api.model.resource.ResourceRef;
import org.cauldron.einstein.api.provider.facade.QueryFacade;
import org.cauldron.einstein.ri.core.instructions.context.MessageAwareContext;
import org.cauldron.einstein.ri.core.log.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * See annotations for description of this instruction.
 *
 * @author Neil Ellis
 */
@RegisterInstruction(name = "split", qualifiers = {"with"},
                     resources = @ResourceReference(facadesUsed = QueryFacade.class),
                     metadata = @InstructionMetaData(
                             core = @CoreMetaData(
                                     name = "split",
                                     shortDescription = "Splits a message and (optionally) iterates over the result.",
                                     description = "Merges the current tuple, splits the resulting messages payload using the supplied query, and optionally iterates over the result and aggregates the result of execution into the result.",
                                     syntax = "( 'route' [ 'through' ] <resource> [ 'to' ] <instruction-group> ) | ( [ <instruction-group> ] '=>' <resource> ':' <instruction-group>  ) ",
                                     example = "route through \"java:org.me.my.Router\" to [ red : << \"text:Roses\", blue : << \"text:Violets\""
                             ),
                             result = "Either the split tuple, or if an instruction group is supplied the result of iterating over the split tuple with the instruction group."
                     )

)
@RegisterOperator(name = "split", symbol = "%", types = {OperatorType.BINARY, OperatorType.TERNARY, OperatorType.UNARY})
@org.contract4j5.contract.Contract
public class SplitInstruction extends AbstractInstruction {

    private static final Logger log = Logger.getLogger(SplitInstruction.class);

    private ResourceRef ref;
    private InstructionGroup group;
    private Executor executor;

    public SplitInstruction(CompilationInformation debug) {
        super(debug);
    }


    public SplitInstruction(CompilationInformation debug, ResourceRef ref) {
        super(debug);
        this.ref = ref;
    }


    public SplitInstruction(CompilationInformation debug, String... ignore) {
        super(debug);
    }


    public SplitInstruction(CompilationInformation debug, ResourceRef ref, InstructionGroup group) {
        super(debug);
        this.ref = ref;
        this.group = group;
        this.group.setParent(this);
    }


    public SplitInstruction(CompilationInformation debug, ResourceRef ref, String... ignore) {
        this(debug, ref);
    }


    public SplitInstruction(CompilationInformation debug, ResourceRef ref, InstructionGroup group, String... ignore) {
        this(debug, ref, group);
    }


    public MessageTuple execute(final ExecutionContext context, final MessageTuple inboundTuple) {
        logExecution(ref == null ? "" : ref.getURI().asString());
        log.debug("Data Model is {0}.", context.getActiveProfile().getDataModel());

        if (inboundTuple.isEmpty()) {
            return inboundTuple;
        }
        final List<MessageTuple> splitGroup = new ArrayList<MessageTuple>();

        inboundTuple.realiseAsOne().execute(SplitAction.class, new SplitAction() {

            public void handle(ActionCallbackContext<SplitView, Object> splitViewActionCallbackContext) {
                DataObject payload = splitViewActionCallbackContext.getMessageHistory()
                        .getCurrentEntry()
                        .getNewValue()
                        .getPayload();

                DataList dataList= null;

                if (ref == null) {
                    log.debug("Splitting with null reference so will apply a 'natural' split only.");
                    if (payload != null && payload.isList()) {
                        log.debug("Dividing the list.");
                        dataList = payload.asList();
                    } else {
                        log.debug("Not a list.");
                        splitGroup.add(getActiveProfile().getMessageModel().createMessage(inboundTuple.getExecutionCorrelation(), payload));

                    }
                } else {
                    log.debug("Splitting with {0} reference.", ref);
                    dataList = ref.getResource(new MessageAwareContext(SplitInstruction.this, inboundTuple))
                            .getFacade(QueryFacade.class)
                            .selectMultiple(payload);
                }

                if (dataList != null ) {
                    log.debug("Dividing the list.");
                    final Iterator<DataObject> dataObjectIterator = dataList.iterator();
                    while (dataObjectIterator.hasNext()) {
                        DataObject individualObject = dataObjectIterator.next();
                        MessageTuple message = getActiveProfile().getMessageModel()
                                .createMessage(inboundTuple.getExecutionCorrelation(),
                                               individualObject);
                        if (group != null) {
                            log.debug("Group specified so applying group to message.");
                            message = executor.execute(context, message);
                        } else {
                            log.debug("Group not specified just adding message.");
                        }
                        splitGroup.add(message);
                    }
                }
            }
        });

        return getParent().getActiveProfile()
                .getMessageModel()
                .createTuple(context.getActiveProfile().getDataModel(), splitGroup);
    }


    @Override
    public void init(LifecycleContext context) {
        if (ref != null) {
            ref.init(context);
        }
        if (group != null) {
            group.init(context);
            executor = getActiveProfile().getExecutionModel().getIteratingExecutor(group);
            executor.init(context);
        }
    }


    @Override
    public void start(LifecycleContext context) {
        if (ref != null) {
            ref.start(context);
        }
        if (group != null) {
            group.start(context);
            executor.start(context);
        }
    }


    @Override
    public void stop(LifecycleContext context) {
        if (ref != null) {
            ref.stop(context);
        }
        if (group != null) {
            group.stop(context);
            executor.stop(context);
        }
    }


    @Override
    public void destroy(LifecycleContext context) {
        if (ref != null) {
            ref.destroy(context);
        }
        if (group != null) {
            group.destroy(context);
            executor.destroy(context);
        }
    }
}
