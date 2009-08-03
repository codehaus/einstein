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

package org.cauldron.einstein.ri.core.providers.time;

import org.cauldron.einstein.api.common.euri.EinsteinURI;
import org.cauldron.einstein.api.common.euri.URIDescriptor;
import org.cauldron.einstein.api.message.MessageListener;
import org.cauldron.einstein.api.message.MessageModel;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.message.correlation.Correlation;
import org.cauldron.einstein.api.message.data.model.DataObject;
import org.cauldron.einstein.api.message.data.model.DataObjectFactory;
import org.cauldron.einstein.api.model.lifecycle.LifecycleContext;
import org.cauldron.einstein.api.model.lifecycle.exception.InitializationRuntimeException;
import org.cauldron.einstein.api.model.lifecycle.exception.StartRuntimeException;
import org.cauldron.einstein.api.model.lifecycle.exception.StopRuntimeException;
import org.cauldron.einstein.api.model.profile.Profile;
import org.cauldron.einstein.api.model.resource.ResourceContext;
import org.cauldron.einstein.api.model.resource.ResourceRef;
import org.cauldron.einstein.api.provider.facade.BooleanQueryFacade;
import org.cauldron.einstein.api.provider.facade.ListenFacade;
import org.cauldron.einstein.api.provider.facade.context.ListenContext;
import org.cauldron.einstein.api.provider.facade.exception.ReadFailureRuntimeException;
import org.cauldron.einstein.ri.core.CoreModuleMessages;
import org.cauldron.einstein.ri.core.log.Logger;
import org.cauldron.einstein.ri.core.providers.AbstractFacade;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class TimeListenFacade extends AbstractFacade implements ListenFacade {
    private static final Logger log = Logger.getLogger(TimeListenFacade.class);

    private static final SchedulerFactory SCHEDULER_FACTORY = new StdSchedulerFactory();
    private final EinsteinURI uri;
    private Scheduler sched;
    private final URIDescriptor descriptor;

    public TimeListenFacade(EinsteinURI uri) {
        this.uri = uri;
        descriptor = uri.getDescriptor();
    }


    public void listen(final ListenContext context, final MessageTuple tuple, final ResourceRef filter, boolean message,
                       final MessageListener listener) throws ReadFailureRuntimeException {
        assertStarted();
        try {
            log.debug("Adding listener for {0}.", uri);
            final MessageModel messageModel = context.getActiveProfile().getMessageModel();
            final Correlation executionCorrelation = tuple.getExecutionCorrelation();
            final BooleanQueryFacade booleanQueryFacade;
            if (filter != null) {
                booleanQueryFacade = filter.getResource(new ResourceContext() {
                    public MessageTuple getMessage() {
                        return tuple;
                    }

                    public Profile getActiveProfile() {
                        return context.getActiveProfile();
                    }
                }).getFacade(BooleanQueryFacade.class);
            } else {
                booleanQueryFacade = null;
            }
            final DataObjectFactory dataObjectFactory = context.getActiveProfile()
                    .getDataModel()
                    .getDataObjectFactory();

            String listenerId = UUID.randomUUID().toString();
            final String triggerName = listenerId + ":trigger";
            final String triggerListenerName = listenerId + ":listener";
            
            CronTrigger cronTrigger = new CronTrigger(triggerName, Scheduler.DEFAULT_GROUP, descriptor.asString());
            cronTrigger.setStartTime(new Date());

            sched.addTriggerListener(new EinsteinTriggerListener(triggerListenerName,
                                                                 dataObjectFactory,
                                                                 booleanQueryFacade,
                                                                 listener,
                                                                 messageModel,
                                                                 executionCorrelation,
                                                                 context));

            cronTrigger.addTriggerListener(triggerListenerName);

            sched.scheduleJob(new JobDetail(listenerId + ":job", Scheduler.DEFAULT_GROUP, EmptyJob.class), cronTrigger);
            log.debug("Next trigger fire time {0}.", cronTrigger.getNextFireTime());
            log.debug("Listener added, job scheduled.");
        } catch (SchedulerException e) {
            throw new ReadFailureRuntimeException(e);
        } catch (ParseException e) {
            throw new ReadFailureRuntimeException(e);
        }
    }


    public void init(LifecycleContext ctx) throws InitializationRuntimeException {
        String scheduleValue = (String) uri.getProviderMetadata().asProperties().get("schedule");
        if (scheduleValue == null || !scheduleValue.equals("cron")) {
            throw new InitializationRuntimeException(CoreModuleMessages.BUNDLE_NAME,
                                                     CoreModuleMessages.TIME_SUPPORTS_CRON_ONLY);
        }
        super.init(ctx);
        try {
            sched = SCHEDULER_FACTORY.getScheduler();
        } catch (SchedulerException e) {
            throw new ReadFailureRuntimeException(e);
        }
    }


    public void start(LifecycleContext ctx) {
        super.start(ctx);
        try {
            sched.start();
        } catch (SchedulerException e) {
            throw new StartRuntimeException(e);
        }
    }


    public void stop(LifecycleContext ctx) throws StartRuntimeException {
        super.stop(ctx);
        try {
            sched.shutdown();
        } catch (SchedulerException e) {
            throw new StopRuntimeException(e);
        }
    }

    public void receive(ListenContext context, MessageTuple tuple, boolean payload) {
        //ignore
    }

    private class EinsteinTriggerListener implements TriggerListener {
        private final String triggerListenerName;
        private final DataObjectFactory dataObjectFactory;
        private final BooleanQueryFacade booleanQueryFacade;
        private final MessageListener listener;
        private final MessageModel messageModel;
        private final Correlation executionCorrelation;
        private final ListenContext context;

        public EinsteinTriggerListener(String triggerListenerName, DataObjectFactory dataObjectFactory,
                                       BooleanQueryFacade booleanQueryFacade, MessageListener listener, MessageModel messageModel,
                                       Correlation executionCorrelation, ListenContext context) {
            this.triggerListenerName = triggerListenerName;
            this.dataObjectFactory = dataObjectFactory;
            this.booleanQueryFacade = booleanQueryFacade;
            this.listener = listener;
            this.messageModel = messageModel;
            this.executionCorrelation = executionCorrelation;
            this.context = context;
        }

        public String getName() {
            return triggerListenerName;
        }


        public void triggerFired(Trigger trigger, JobExecutionContext jobExecutionContext) {
            try {
                log.debug("Listener for {0} fired.", uri);
                DataObject object = dataObjectFactory
                        .createDataObject(jobExecutionContext.getFireTime());

                boolean matchedQuery = true;
                if (booleanQueryFacade != null) {
                    matchedQuery = booleanQueryFacade.match(object);
                }
                if (matchedQuery) {
                    listener.handle(messageModel.createMessage(executionCorrelation, object));
                }
            } catch (Exception e) {
                context.getActiveProfile().getExceptionModel().handleRootlessException(e);
            }
        }


        public boolean vetoJobExecution(Trigger trigger, JobExecutionContext jobExecutionContext) {
            return false;
        }


        public void triggerMisfired(Trigger trigger) {
            log.debug("Timer listener trigger misfired for {0}.", trigger);
        }


        public void triggerComplete(Trigger trigger, JobExecutionContext jobExecutionContext, int i) {
        }
    }
}
