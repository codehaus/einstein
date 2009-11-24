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

package org.cauldron.einstein.provider.counter;

import org.cauldron.einstein.api.provider.facade.*;
import org.cauldron.einstein.api.provider.facade.context.ReadContext;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.message.data.model.DataObject;
import org.cauldron.einstein.ri.core.providers.AbstractFacade;


import java.util.List;
import java.util.ArrayList;

/**
 * @author Neil Ellis
 */

@org.contract4j5.contract.Contract
public class CounterBrowserFacade extends AbstractFacade implements BrowseFacade {
    private final CounterResource counterResource;

    public CounterBrowserFacade(CounterResource counterResource) {
        this.counterResource = counterResource;
    }


    public MessageTuple readNoWait( ReadContext context, boolean all, boolean message) {
        if (all) {
            List<MessageTuple> list = new ArrayList<MessageTuple>();
            int i= counterResource.getCounter();
            while (i != counterResource.getLimit()) {
                DataObject object = context.getActiveProfile().getDataModel().getDataObjectFactory().createDataObject(i);
                list.add(context.getActiveProfile().getMessageModel().createMessage(context.getMessage().getExecutionCorrelation(), object));
                i += counterResource.getForward() ? counterResource.getStep() : -counterResource.getStep();
            }
            return context.getActiveProfile().getMessageModel().createTuple(context.getActiveProfile().getDataModel(), list);
        } else {
            DataObject object = context.getActiveProfile().getDataModel().getDataObjectFactory().createDataObject(counterResource.getCounter());
            return context.getActiveProfile().getMessageModel().createMessage(context.getMessage().getExecutionCorrelation(), object);
        }
    }
}
