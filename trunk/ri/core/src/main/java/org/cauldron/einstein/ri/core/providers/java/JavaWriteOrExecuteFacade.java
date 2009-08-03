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

package org.cauldron.einstein.ri.core.providers.java;


import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.message.action.context.ActionCallbackContext;
import org.cauldron.einstein.api.message.action.types.WriteAction;
import org.cauldron.einstein.api.message.data.model.DataObject;
import org.cauldron.einstein.api.message.view.composites.WriteView;
import org.cauldron.einstein.api.model.execution.ExecutionContext;
import org.cauldron.einstein.api.model.execution.StatefulExecutable;
import org.cauldron.einstein.api.model.lifecycle.LifecycleContext;
import org.cauldron.einstein.api.model.lifecycle.exception.StartRuntimeException;
import org.cauldron.einstein.api.model.lifecycle.exception.StopRuntimeException;
import org.cauldron.einstein.api.model.profile.Profile;
import org.cauldron.einstein.api.model.profile.ProfileAware;
import org.cauldron.einstein.api.provider.facade.ExecuteFacade;
import org.cauldron.einstein.api.provider.facade.WriteFacade;
import org.cauldron.einstein.api.provider.facade.context.WriteContext;
import org.cauldron.einstein.api.provider.facade.exception.WriteFailureRuntimeException;
import static org.cauldron.einstein.ri.core.CoreModuleMessages.BUNDLE_NAME;
import static org.cauldron.einstein.ri.core.CoreModuleMessages.COULD_NOT_FIND_MATCHING_METHOD;
import org.cauldron.einstein.ri.core.providers.AbstractFacade;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class JavaWriteOrExecuteFacade extends AbstractFacade implements WriteFacade, ExecuteFacade {

    private final Object instance;
    private final List<Method> methods = new ArrayList<Method>();

    public JavaWriteOrExecuteFacade(Object instance) {
        this.instance = instance;
    }


    public MessageTuple write(WriteContext context, MessageTuple tuple, boolean message) throws
                                                                                         WriteFailureRuntimeException {
        return executeJavaObject(context, tuple);
    }


    private MessageTuple executeJavaObject(final ProfileAware context, MessageTuple message) {
        final Object payloadValue;
        if (message.isEmpty()) {
            payloadValue = null;
        } else {
            final DataObject[] dataObject = new DataObject[]{null};
            message.realiseAsOne().execute(WriteAction.class, new WriteAction() {

                public void handle(ActionCallbackContext<WriteView, Object> callbackContext) {
                    dataObject[0] = callbackContext.getMessageHistory().getCurrentEntry().getNewValue().getPayload();
                }
            });

            payloadValue = dataObject[0] == null ? null : dataObject[0].getValue();
        }

        StatefulExecutable executable = null;
        for (final Method method : methods) {
            if (payloadValue == null || method.getParameterTypes()[0].isAssignableFrom(payloadValue.getClass())) {
                executable = new StatefulExecutable() {

                    public MessageTuple execute(ExecutionContext context, MessageTuple message) {
                        try {
                            return context.getActiveProfile()
                                    .getMessageModel()
                                    .createMessage(message.getExecutionCorrelation(),
                                                   context.getActiveProfile()
                                                           .getDataModel()
                                                           .getDataObjectFactory().createDataObject(
                                                           method.invoke(instance, payloadValue)));
                        } catch (IllegalAccessException e) {
                            throw new WriteFailureRuntimeException(e);
                        } catch (InvocationTargetException e) {
                            throw new WriteFailureRuntimeException(e);
                        }
                    }


                    public void stop(LifecycleContext ctx) throws StartRuntimeException {
                    }


                    public void start(LifecycleContext ctx) throws StopRuntimeException {
                    }
                };
            }
        }
        if (executable == null) {
            throw new WriteFailureRuntimeException(BUNDLE_NAME,
                                                   COULD_NOT_FIND_MATCHING_METHOD,
                                                   instance.getClass().getCanonicalName(),
                                                   payloadValue == null ? "null" : payloadValue.getClass()
                                                           .getCanonicalName());
        }
        return context.getActiveProfile()
                .getExecutionModel()
                .getSingleInstructionExecutor(executable)
                .execute(new ExecutionContext() {

                    public Profile getActiveProfile() {
                        return context.getActiveProfile();
                    }
                }, message);
    }


    public MessageTuple write(WriteContext context, MessageTuple tuple, boolean message, long timeout) throws
                                                                                                       WriteFailureRuntimeException {
        return executeJavaObject(context, tuple);
    }


    public void start(LifecycleContext ctx) {
        super.start(ctx);
        Method[] classMethods = instance.getClass().getDeclaredMethods();
        for (Method method : classMethods) {
            if (Modifier.isPublic(method.getModifiers()) && method.getParameterTypes().length == 1) {
                methods.add(method);
            }
        }
    }


    public MessageTuple execute(ExecutionContext context, MessageTuple message) {
        return executeJavaObject(context, message);
    }


    public MessageTuple write(WriteContext context, MessageTuple message) {
        return executeJavaObject(context, message);
    }
}
