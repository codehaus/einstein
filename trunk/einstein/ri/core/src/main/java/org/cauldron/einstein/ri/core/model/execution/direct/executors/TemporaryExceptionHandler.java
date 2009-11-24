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

package org.cauldron.einstein.ri.core.model.execution.direct.executors;

import org.cauldron.einstein.api.assembly.debug.CompilationInformation;
import org.cauldron.einstein.api.assembly.instruction.Instruction;
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterInstruction;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.model.execution.Executable;
import org.cauldron.einstein.api.model.execution.ExecutionContext;
import org.cauldron.einstein.ri.core.CoreModuleMessages;
import org.cauldron.einstein.ri.core.exception.InstructionExecutionRuntimeException;

/**
 * @author Neil Ellis
 * @TODO remove this as soon as execption models are added.
 */
@org.contract4j5.contract.Contract class TemporaryExceptionHandler {

    public static MessageTuple executeWithExceptionHandling(Executable executable, ExecutionContext context,
                                                            MessageTuple tuple) {
        try {
            return executable.execute(context, tuple);
        } catch (InstructionExecutionRuntimeException e) {
            //Bubble up ones that have already been caught
            throw e;
        } catch (RuntimeException e) {
            handle(executable, e);
            throw e;

        } catch (Error e) {
            handle(executable, e);
            throw e;
        }
    }

    private static void handle(Executable executable, Throwable e) {
        RegisterInstruction annotation = executable.getClass().getAnnotation(RegisterInstruction.class);
        if (annotation != null) {
            if (executable instanceof Instruction) {
                Instruction instruction = (Instruction) executable;
                CompilationInformation compilationInformation = instruction.getCompilationInformation();
                throw new InstructionExecutionRuntimeException(e, CoreModuleMessages.BUNDLE_NAME,
                                                               CoreModuleMessages.INSTRUCTION_EXECUTION_EXCEPTION,
                                                               annotation.metadata().core().name(),
                                                               compilationInformation.getFileName(),
                                                               compilationInformation.getLineNumber(),
                                                               compilationInformation.getColumnNumber(),
                                                               compilationInformation.getSourceText()
                                                                       .substring(0,
                                                                                  Math.min(20,
                                                                                           compilationInformation.getSourceText().length()))
                                                                       .replace('\n', ' ').trim(),
                                                               e.getMessage());
            }
        }
    }
}
