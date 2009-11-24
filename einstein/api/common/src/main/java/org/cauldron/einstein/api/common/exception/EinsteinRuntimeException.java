/******************************************************************************
 *                                                                            *
 *                                                                            *
 *     All works are (C) 2008- Mangala Solutions Ltd and Paremus Ltd.         *
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

package org.cauldron.einstein.api.common.exception;

import mazz.i18n.Msg;
import org.contract4j5.contract.Contract;
import org.contract4j5.contract.Pre;


/**
 * The parent class for all non-parsing exception is Einstein.
 *
 * @author Neil Ellis
 */
@Contract
public abstract class EinsteinRuntimeException extends RuntimeException {

    /**
     * We don't throw meaningless exceptions in Einstein.
     */
    private EinsteinRuntimeException() {
    }

    //TODO: To be useful all sub classes should actually specify the conditions ... a job for another day.
    //    @Pre ("message != null && bundle != null && message ==~ /[a-z0-9_\\-.]+/ && bundle ==~ /[a-z0-9_\\-.]+/")
    public EinsteinRuntimeException(Throwable t, String bundle, String message, Object... args) {

        super(Msg.createMsg(new Msg.BundleBaseName(bundle), message, args).toString(), t);
    }

    //    @Pre ("message != null && bundle != null && message ==~ /[a-z0-9_\\-.]+/ && bundle ==~ /[a-z0-9_\\-.]+/")
    public EinsteinRuntimeException(String bundle, String message, Object... args) {

        super(Msg.createMsg(new Msg.BundleBaseName(bundle), message, args).toString());
    }

    @Pre
    public EinsteinRuntimeException(Throwable throwable) {
        super(throwable);
    }
}
