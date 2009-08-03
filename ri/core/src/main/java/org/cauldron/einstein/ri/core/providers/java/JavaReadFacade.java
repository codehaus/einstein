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

import org.cauldron.einstein.api.message.Message;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.provider.facade.ReadFacade;
import org.cauldron.einstein.api.provider.facade.context.ReadContext;
import org.cauldron.einstein.api.provider.facade.exception.ReadFailureRuntimeException;
import org.cauldron.einstein.ri.core.providers.AbstractFacade;
import org.contract4j5.contract.Contract;
import org.contract4j5.contract.Pre;

/**
 * @author Neil Ellis
 */
@Contract class JavaReadFacade extends AbstractFacade implements ReadFacade {

    private final Object instance;

    public JavaReadFacade(Object instance) {
        this.instance = instance;
    }


    @Pre
    public MessageTuple read(@Pre ReadContext context, boolean all, boolean payload) throws
                                                                                     ReadFailureRuntimeException {
        return createMessage(context);
    }


    private Message createMessage(ReadContext context) {
        return context.getActiveProfile()
                .getMessageModel()
                .createMessage(context.getMessage().getExecutionCorrelation(),
                               context.getActiveProfile().getDataModel().getDataObjectFactory().createDataObject(
                                       instance));
    }


    @Pre
    public MessageTuple read(@Pre ReadContext context, boolean all, boolean payload, long timeout) throws
                                                                                                   ReadFailureRuntimeException {
        return createMessage(context);
    }


    @Pre
    public MessageTuple readNoWait(@Pre ReadContext context, boolean all, boolean payload) throws
                                                                                           ReadFailureRuntimeException {
        return createMessage(context);
    }
}
