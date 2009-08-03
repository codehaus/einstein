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
import org.cauldron.einstein.api.provider.facade.BrowseFacade;
import org.cauldron.einstein.ri.core.instructions.AbstractInstruction;
import org.cauldron.einstein.ri.core.instructions.context.MessageAwareContext;

/*
 * See annotations for description of this instruction.
 *
 * @author Neil Ellis
 */
@RegisterInstruction(name = "browse", qualifiers = {"all", "payload", "of", "from"},
                     resources = @ResourceReference(facadesUsed = BrowseFacade.class),
                     metadata = @InstructionMetaData(
                             core = @CoreMetaData(
                                     name = "browse",
                                     shortDescription = "Non-destructive read from a resource.",
                                     description = "Requests messages from a resource returns an empty message if nothing available yet, read is non-destructtve.",
                                     example = "browse \"jms://in\";",
                                     syntax = "browse” [ 'all' ] [ 'payload' ] ['of']  ['from'] <uri> |  [ <instruction-group> ] '<?'"

                             )
                             , qualifiers = {"all - browse all messages currently on the resource",
                             "payload - the resource stored is just the payload."},
                               result = "Tuple of messages or an empty tuple."

                     ))
@RegisterOperator(name = "browse", symbol = "<?", types = {OperatorType.BINARY_REVERSED, OperatorType.UNARY})

@org.contract4j5.contract.Contract
public class BrowseInstruction extends AbstractInstruction {

    private final ResourceRef ref;
    private boolean all;
    private boolean payload;

    public BrowseInstruction(CompilationInformation debug, ResourceRef ref) {
        super(debug);
        this.ref = ref;
    }


    public BrowseInstruction(CompilationInformation debug, ResourceRef ref, String... qualifiers) {
        super(debug);
        this.ref = ref;
        if (hasQualifier("all", qualifiers)) {
            all = true;
        }
        if (hasQualifier("payload", qualifiers)) {
            payload = true;
        }
    }


    public MessageTuple execute(ExecutionContext context, MessageTuple tuple) {
        assertStarted();
        logExecution(ref.getURI().asString());
        MessageAwareContext newContext = new MessageAwareContext(this,
                                                                 context.getActiveProfile()
                                                                         .getMessageModel().createExternalisableTuple(
                                                                         tuple));
        final MessageTuple result = ref.getResource(newContext)
                .getFacade(BrowseFacade.class)
                .readNoWait(newContext, all, payload);
        result.setExecutionCorrelation(tuple.getExecutionCorrelation());
        return result;
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