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

package org.cauldron.einstein.api.assembly.debug;

import mazz.i18n.Msg;
import org.contract4j5.contract.Contract;
import org.contract4j5.contract.Post;


/**
 * CompilationInformation stores debugging information for Instructions so that when exceptions occur the source of the
 * error can be easily traced. It is also useful if the runtime wishes to provide tracing capabilities.
 *
 * @author Neil Ellis
 */
@Contract
public interface CompilationInformation {

    @Post("$return >= 0") int getLineNumber();

    @Post("$return >= 0") int getColumnNumber();

    @Post String getSourceText();

    @Post("$return =~ /[a-zA-Z0-9]+/") String getFileName();

    @Post Msg getPositionDescription();
}
