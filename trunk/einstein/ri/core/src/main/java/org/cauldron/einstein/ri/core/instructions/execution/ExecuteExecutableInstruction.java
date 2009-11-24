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

package org.cauldron.einstein.ri.core.instructions.execution;

import org.cauldron.einstein.api.assembly.debug.CompilationInformation;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.message.action.context.ActionCallbackContext;
import org.cauldron.einstein.api.message.action.types.ReadAction;
import org.cauldron.einstein.api.message.data.model.DataObject;
import org.cauldron.einstein.api.message.view.composites.ReadOnlyView;
import org.cauldron.einstein.api.model.execution.ExecutionContext;
import org.cauldron.einstein.api.model.execution.PayloadOnlyExecutable;
import org.cauldron.einstein.ri.core.instructions.AbstractInstruction;

/**
 * @author Neil Ellis
 */

class ExecuteExecutableInstruction extends AbstractInstruction {

    private final PayloadOnlyExecutable executable;


    public ExecuteExecutableInstruction(CompilationInformation debug, PayloadOnlyExecutable executable) {
        super(debug);
        this.executable = executable;
    }


    public MessageTuple execute(final ExecutionContext context, final MessageTuple message) {
        final MessageTuple[] result = new MessageTuple[1];
        message.realiseAsOne().execute(ReadAction.class, new ReadAction() {

            public void handle(ActionCallbackContext<ReadOnlyView, Object> readOnlyViewActionCallbackContext) {
                Object payload = readOnlyViewActionCallbackContext.getMessageHistory()
                        .getCurrentEntry()
                        .getNewValue()
                        .getPayload()
                        .getValue();
                Object resultObject = executable.execute(context, payload);
                DataObject dataObject = context.getActiveProfile()
                        .getDataModel()
                        .getDataObjectFactory()
                        .createDataObject(resultObject);
                result[0] = context.getActiveProfile()
                        .getMessageModel()
                        .createMessage(message.getExecutionCorrelation(), dataObject);
            }
        });
        return result[0];
    }
}