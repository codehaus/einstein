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
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.message.data.model.DataObject;
import org.cauldron.einstein.api.provider.facade.BrowseFacade;
import org.cauldron.einstein.api.provider.facade.ReadFacade;
import org.cauldron.einstein.api.provider.facade.context.ReadContext;
import org.cauldron.einstein.api.provider.facade.exception.ReadFailureRuntimeException;
import org.cauldron.einstein.ri.core.providers.AbstractFacade;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class TimeReadFacade extends AbstractFacade implements ReadFacade, BrowseFacade {

    private final DateTimeFormatter formatter;

    public TimeReadFacade(EinsteinURI uri) {
        formatter = DateTimeFormat.forPattern(uri.getDescriptor().asString());
    }


    public MessageTuple read(ReadContext context, boolean all, boolean payload) throws ReadFailureRuntimeException {
        return readNoWait(context, all, payload);
    }


    public MessageTuple readNoWait(ReadContext context, boolean all, boolean payload) throws
                                                                                      ReadFailureRuntimeException {
        DataObject object = context.getActiveProfile()
                .getDataModel()
                .getDataObjectFactory()
                .createDataObject(formatter.print(System.currentTimeMillis()));
        return context.getActiveProfile()
                .getMessageModel()
                .createMessage(context.getMessage().getExecutionCorrelation(), object);
    }


    public MessageTuple read(ReadContext context, boolean all, boolean payload, long timeout) throws
                                                                                              ReadFailureRuntimeException {
        return readNoWait(context, all, payload);
    }
}
