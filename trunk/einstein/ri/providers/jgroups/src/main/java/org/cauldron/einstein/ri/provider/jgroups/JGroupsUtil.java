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

package org.cauldron.einstein.ri.provider.jgroups;

import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.message.action.context.ActionCallbackContext;
import org.cauldron.einstein.api.message.action.types.ReadAction;
import org.cauldron.einstein.api.message.correlation.Correlation;
import org.cauldron.einstein.api.message.data.model.DataObject;
import org.cauldron.einstein.api.message.view.composites.ReadOnlyView;
import org.cauldron.einstein.api.model.profile.Profile;
import org.cauldron.einstein.api.runtime.Serializer;
import org.cauldron.einstein.ri.core.runtime.EinsteinRIRuntimeFactory;
import org.contract4j5.contract.Contract;

/**
 * @author Neil Ellis
 */
@Contract
public class JGroupsUtil {

    public static MessageTuple convert(Profile profile, Correlation executionCorrelation, org.jgroups.Message jgroupsMessage, boolean payload) {
        final byte[] rawBuffer = jgroupsMessage.getRawBuffer();
        final Serializer serializer = EinsteinRIRuntimeFactory.getInstance().getRuntime().getSerializer();
        if (payload) {
            final DataObject object = profile.getDataModel().getDataObjectFactory().createDataObject(serializer.deSerializeObject(rawBuffer));
            return profile.getMessageModel().createMessage(executionCorrelation, object);
        } else {
            return serializer.deSerializeTuple(rawBuffer);
        }


    }


    public static org.jgroups.Message convert(MessageTuple message, final boolean payload) {
        final org.jgroups.Message[] result = new org.jgroups.Message[1];
        if (payload) {
            message.realiseAsOne().execute(ReadAction.class, new ReadAction() {
                public void handle(ActionCallbackContext<ReadOnlyView, Object> ctx) {
                    final Serializer serializer = EinsteinRIRuntimeFactory.getInstance().getRuntime().getSerializer();
                    result[0] = new org.jgroups.Message(null, null, serializer.serialize(ctx.getMessageHistory().getCurrentEntry().getNewValue().getPayload().getValue()));
                }
            });
        } else {
            result[0] = new org.jgroups.Message(null, null, EinsteinRIRuntimeFactory.getInstance().getRuntime().getSerializer().serialize(message));

        }
        return result[0];

    }

}
