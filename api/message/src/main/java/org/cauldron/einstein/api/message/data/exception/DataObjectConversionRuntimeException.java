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

package org.cauldron.einstein.api.message.data.exception;

import mazz.i18n.annotation.I18NMessage;
import mazz.i18n.annotation.I18NResourceBundle;
import org.cauldron.einstein.api.common.exception.EinsteinRuntimeException;
import org.cauldron.einstein.api.message.data.model.DataModel;

/**
 * @author Neil Ellis
 */

@I18NResourceBundle(baseName = "message_api")
@org.contract4j5.contract.Contract
public class DataObjectConversionRuntimeException extends EinsteinRuntimeException {

    @I18NMessage("Could not convert {0} to {1}.")
    public static final String MSG = "data.object.conversion.exception.exception";

    public DataObjectConversionRuntimeException(Class<? extends DataModel> fromClass,
                                                Class<? extends DataModel> toClass) {

        super("message_api", MSG, fromClass.getName(), toClass.getName());

    }
}
