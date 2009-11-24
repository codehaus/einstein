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
import org.cauldron.einstein.api.assembly.group.InstructionGroup;
import org.cauldron.einstein.api.assembly.instruction.InstructionMetaData;
import org.cauldron.einstein.api.assembly.instruction.annotation.OperatorType;
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterInstruction;
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterOperator;
import org.cauldron.einstein.api.assembly.instruction.annotation.ResourceReference;
import org.cauldron.einstein.api.common.metadata.CoreMetaData;
import org.cauldron.einstein.api.message.MessageListener;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.message.correlation.Correlation;
import org.cauldron.einstein.api.model.execution.ExecutionContext;
import org.cauldron.einstein.api.model.lifecycle.LifecycleContext;
import org.cauldron.einstein.api.model.resource.ResourceRef;
import org.cauldron.einstein.api.provider.facade.ListenFacade;
import org.cauldron.einstein.ri.core.instructions.AbstractInstruction;
import org.cauldron.einstein.ri.core.instructions.context.MessageAwareContext;
import org.cauldron.einstein.ri.core.log.Logger;
import org.cauldron.einstein.ri.core.model.correlation.SimpleCorrelation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * See annotations for description of this instruction.
 *
 * @author Neil Ellis
 */
@RegisterInstruction(name = "listen", qualifiers = {"payload", "to"},
                     resources = {@ResourceReference(facadesUsed = ListenFacade.class), @ResourceReference()},
                     metadata = @InstructionMetaData(
                             core = @CoreMetaData(
                                     name = "listen",
                                     description = "Listens for messages on a resource, that match the query supplied - if any, and executes the supplied " +
                                                   "execution group with the message when it replies. Once the listener is registered execution continues immediately " +
                                                   "with the next instruction. Once registered messages will be passed to the execution group until the resource is " +
                                                   "stopped.",
                                     shortDescription = "Listens for messages on a resource, that match the (optional) query.",
                                     syntax = " ( 'listen'  [ 'payload' ] [ 'to' ] <resource> [ <query-resource> ] <instruction-group> ) | ( <instruction-group> '<!' <resource> ':' <instruction-group> )",
                                     example = "listen esper \"text:select avg(value) from org.cauldron.einstein.ri.examples.esper.TessaractWidget.win:time(30 sec)\" {\n" +
                                               "    ( -> \"xpath:/underlying\" ) >> \"console:Tesseract average value is: \";\n};"

                             ),
                             qualifiers = {"payload - the resource stored is a complete payload not just the payload."},
                             result = "The current message."
                     ))

@RegisterOperator(name = "listen", symbol = "<!", types = {OperatorType.TERNARY})

@org.contract4j5.contract.Contract
public class ListenInstruction extends AbstractInstruction {

    private static final Logger log = Logger.getLogger(ListenInstruction.class);
    private final ResourceRef ref;
    private ResourceRef query;
    private final InstructionGroup group;
    private boolean payload;
    private List<Correlation> correlations = Collections.synchronizedList(new ArrayList<Correlation>());

    public ListenInstruction(CompilationInformation debug, ResourceRef ref, InstructionGroup group) {
        super(debug);
        this.ref = ref;
        this.group = group;
        group.setParent(this);
    }


    public ListenInstruction(CompilationInformation debug, ResourceRef ref, InstructionGroup group,
                             String... qualifiers) {
        super(debug);
        this.ref = ref;
        this.group = group;
        group.setParent(this);
        payload = hasQualifier("payload", qualifiers);
    }


    public ListenInstruction(CompilationInformation debug, ResourceRef ref, ResourceRef query, InstructionGroup group) {
        this(debug, ref, group);
        this.query = query;
        group.setParent(this);
    }


    public ListenInstruction(CompilationInformation debug, ResourceRef ref, ResourceRef query, InstructionGroup group,
                             String... qualifiers) {
        this(debug, ref, group, qualifiers);
        this.query = query;
        group.setParent(this);
    }


    public MessageTuple execute(final ExecutionContext context, MessageTuple tuple) {
        assertStarted();
        logExecution(ref.getURI().asString());

        log.debug("Listening to {0}.", ref.getURI().asString());
        final Correlation correlation = tuple.getExecutionCorrelation();
        MessageAwareContext messageAwareContext = new MessageAwareContext(this,
                                                                          context.getActiveProfile()
                                                                                  .getMessageModel().createExternalisableTuple(
                                                                                  tuple));

        final ListenFacade listenFacade = ref.getResource(messageAwareContext).getFacade(ListenFacade.class);
        final SimpleCorrelation childCorrelation = new SimpleCorrelation(correlation);

        if (isListeningTo(correlation)) {
            log.debug("Already listening.");
        } else {
            log.debug("Adding new listener.");
            listenFacade
                    .listen(messageAwareContext, tuple, query, payload, new MessageListener() {

                        public void handle(MessageTuple message) {
                            log.debug("Listener received {0}", message);
                            //We need to change the correlation id because only one correlation id can be in one scope.
                            //but make sure it has the original inbound payload as a parent
                            //so we can access the original scope.
                            message.setExecutionCorrelation(childCorrelation);
                            group.execute(context, message);
                        }
                    });
            correlations.add(correlation);
        }
        listenFacade.receive(messageAwareContext, tuple, payload);
        return tuple;
    }

    private boolean isListeningTo(Correlation executionCorrelation) {
        return correlations.contains(executionCorrelation);
    }


    @Override
    public void init(LifecycleContext context) {
        super.init(context);
        ref.init(context);
        if (query != null) {
            query.init(context);
        }
        group.init(context);
    }


    @Override
    public void start(LifecycleContext context) {
        super.start(context);
        ref.start(context);
        if (query != null) {
            query.start(context);
        }
        group.start(context);
    }


    @Override
    public void stop(LifecycleContext context) {
        super.stop(context);
        ref.stop(context);
        if (query != null) {
            query.stop(context);
        }
        group.stop(context);
    }


    @Override
    public void destroy(LifecycleContext context) {
        super.destroy(context);
        ref.destroy(context);
        if (query != null) {
            query.destroy(context);
        }
        group.destroy(context);
    }
}