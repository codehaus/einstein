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

package org.cauldron.einstein.ri.core.model.message.simple;

import org.cauldron.einstein.api.message.MessageModel;
import org.cauldron.einstein.api.message.data.model.DataModel;
import org.cauldron.einstein.api.model.*;
import org.cauldron.einstein.api.model.profile.Profile;
import org.cauldron.einstein.ri.core.log.Logger;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class SimpleProfile implements Profile {

    private static final Logger log = Logger.getLogger(SimpleProfile.class);
    private final ExecutionModel executionModel;
    private final MessageModel messageModel;
    private final DataModel dataModel;
    private Profile parentProfile;

    public SimpleProfile(Profile parentProfile) {
        executionModel = parentProfile.getExecutionModel();
        messageModel = parentProfile.getMessageModel();
        dataModel = parentProfile.getDataModel();
    }


    public SimpleProfile(DataModel dataModel, ExecutionModel executionModel,
                         MessageModel messageModel, ExceptionModel exceptionModel,
                         DeploymentModel deploymentModel, ProvisioningModel provisioningModel,
                         InvocationModel invocationModel, Profile parentProfile) {
        if (executionModel == null && parentProfile != null) {
            this.executionModel = parentProfile.getExecutionModel();
        } else {
            this.executionModel = executionModel;
        }
        if (messageModel == null && parentProfile != null) {
            this.messageModel = parentProfile.getMessageModel();
        } else {
            this.messageModel = messageModel;
        }
        if (dataModel == null && parentProfile != null) {
            this.dataModel = parentProfile.getDataModel();
        } else {
            this.dataModel = dataModel;
        }
    }


    public DataModel getDataModel() {
        return dataModel;
    }


    public DeploymentModel getDeploymentModel() {
        return null; //TODO
    }


    public ExceptionModel getExceptionModel() {
        return new ExceptionModel() {

            public void handleRootlessException(Exception e) {
                log.fatal(e);
                e.printStackTrace(System.err);
            }
        };
    }


    public ExecutionModel getExecutionModel() {
        return executionModel;
    }


    public InvocationModel getInvocationModel() {
        return null; //TODO
    }


    public MessageModel getMessageModel() {
        return messageModel;
    }


    public ProvisioningModel getProvisioningModel() {
        return null; //TODO
    }
}
