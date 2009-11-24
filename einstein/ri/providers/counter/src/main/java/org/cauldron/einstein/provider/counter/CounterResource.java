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

import org.cauldron.einstein.ri.core.providers.base.AbstractResource;
import org.cauldron.einstein.ri.core.log.Logger;
import org.cauldron.einstein.api.common.euri.EinsteinURI;
import org.cauldron.einstein.api.model.resource.Resource;
import org.cauldron.einstein.api.provider.facade.*;

import java.util.List;
import java.util.ArrayList;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
class CounterResource extends AbstractResource implements Resource {
    private static final Logger log = Logger.getLogger(CounterResource.class);
    private boolean expired;
    private boolean forward;
    private int counter;
    private int limit;
    private int step;
    final List<CounterFacade.CounterListener> listeners = new ArrayList<CounterFacade.CounterListener>();

    public CounterResource(EinsteinURI uri) {
        super(uri);
        String[] args = uri.getDescriptor().asString().split(":");
        setCounter(Integer.parseInt(args[0]));
        if(args.length >= 2) {
            setLimit(Integer.parseInt(args[1]));
        } else {
            setLimit(Integer.MAX_VALUE);
        }
        if (args.length == 3) {
            setStep(Integer.parseInt(args[2]));
        } else {
            setStep(1);
        }
        setForward(limit > counter);
        log.debug("Counter starting at {0} going to {1} step {2} direction {3}.", counter, limit, step, forward ? "forward" : "backward");
        addMapping(BooleanQueryFacade.class, new CounterFacade(this));
        addMapping(ReadFacade.class, new CounterFacade(this));
        addMapping(ListenFacade.class, new CounterFacade(this));
        addMapping(WriteFacade.class, new CounterFacade(this));
        addMapping(BrowseFacade.class, new CounterBrowserFacade(this));

    }


    public synchronized void count() {
        if (!isExpired()) {
            if (isForward()) {
                setCounter(getCounter() + step);
                setExpired(counter >= limit);
                log.debug("Incrementing counter, new value is {0}.", counter);
            } else {
                setCounter(getCounter() - step);
                setExpired(counter <= limit);
                log.debug("Decrementing counter, new value is {0}.", counter);
            }
            for (CounterFacade.CounterListener listener : listeners) {
                listener.handle(getCounter());

            }
        } else {
            log.debug("Count has expired in isCount().");
        }
    }

    public int getCounter() {
        return counter;
    }

    public int getLimit() {
        return limit;
    }

    public boolean getForward() {
        return isForward();
    }

    public int getStep() {
        return step;
    }

    public boolean isExpired() {
        return expired;
    }

    void setExpired(boolean expired) {
        if(expired) {
            log.debug("Expiring counter.");
        }
        this.expired = expired;
    }

    boolean isForward() {
        return forward;
    }

    void setForward(boolean forward) {
        this.forward = forward;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    void setLimit(int limit) {
        this.limit = limit;
    }

    void setStep(int step) {
        this.step = step;
    }
}
