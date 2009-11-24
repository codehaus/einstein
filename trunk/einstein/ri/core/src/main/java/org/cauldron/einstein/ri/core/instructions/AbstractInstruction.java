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

import mazz.i18n.Msg;
import org.cauldron.einstein.api.assembly.debug.CompilationInformation;
import org.cauldron.einstein.api.assembly.instruction.Instruction;
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterInstruction;
import org.cauldron.einstein.api.message.MessageModel;
import org.cauldron.einstein.api.model.execution.Executable;
import org.cauldron.einstein.api.model.execution.ExecutionContext;
import org.cauldron.einstein.api.model.lifecycle.LifecycleContext;
import org.cauldron.einstein.api.model.profile.Profile;
import org.cauldron.einstein.api.provider.ProviderContext;
import org.cauldron.einstein.api.provider.facade.context.QueryContext;
import org.cauldron.einstein.api.provider.facade.context.RoutingContext;
import org.cauldron.einstein.api.provider.facade.context.URIExecutionContext;
import org.cauldron.einstein.api.provider.facade.context.WriteContext;
import org.cauldron.einstein.ri.core.lifecycle.LifecycleStateMachine;
import org.cauldron.einstein.ri.core.log.Logger;

import java.util.Arrays;
import java.util.List;

/**
 * @author Neil Ellis
 */

public abstract class AbstractInstruction implements RoutingContext, ProviderContext, ExecutionContext, WriteContext,
                                                     URIExecutionContext, QueryContext, Instruction, Executable {

    private static final Logger log = Logger.getLogger(AbstractInstruction.class);
    private static final Msg.BundleBaseName USAGE_BUNDLE = new Msg.BundleBaseName("instruction_usage");
    private Instruction parent;
    private final CompilationInformation compilationInformation;

    private final LifecycleStateMachine state = new LifecycleStateMachine();


    protected AbstractInstruction(CompilationInformation compilationInformation) {
        this.compilationInformation = compilationInformation;
    }


    public static Msg.BundleBaseName getUsageBundle() {
        return USAGE_BUNDLE;
    }

    protected void logExecution(Object... args) {

        final List<Object> list = Arrays.asList(args);
        log.debug("{0}({1}:{2}):{3}{4}",
                  compilationInformation.getFileName(),
                  compilationInformation.getLineNumber(),
                  compilationInformation.getColumnNumber(),
                  getClass().getAnnotation(
                          RegisterInstruction.class).name(),
                  list);

    }


    public void init(LifecycleContext ctx) {
        getState().init();
        log.debug("{0} init() started.", getClass());
    }


    public void start(LifecycleContext ctx) {
        getState().start();
        log.debug("{0} start() started.", getClass());
    }


    public void stop(LifecycleContext ctx) {
        getState().stop();
        log.debug("{0} stop() started.", getClass());
    }


    public void destroy(LifecycleContext ctx) {
        getState().destroy();
        log.debug("{0} destroy() started.", getClass());
    }


    public Profile getActiveProfile() {
        return getParent().getActiveProfile();
    }


    public Instruction getParent() {
        return parent;
    }


    public void setParent(Instruction parent) {
        this.parent = parent;
    }


    public CompilationInformation getCompilationInformation() {
        return compilationInformation;
    }


    public Msg getPositionDescription() {
        return getCompilationInformation().getPositionDescription();
    }


    public MessageModel getMessageFactory() {
        return getActiveProfile().getMessageModel();
    }


    protected void assertStarted() {
        getState().assertStarted();
    }


    protected final boolean hasQualifier(String qualifier, String... qualifiers) {
        return Arrays.asList(qualifiers).contains(qualifier);
    }


    protected LifecycleStateMachine getState() {
        return state;
    }
}

