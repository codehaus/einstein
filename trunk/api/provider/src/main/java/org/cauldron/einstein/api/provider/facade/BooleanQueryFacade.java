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

package org.cauldron.einstein.api.provider.facade;

import org.cauldron.einstein.api.message.data.model.query.BooleanQuery;
import org.cauldron.einstein.api.model.resource.Facade;

/**
 * The QueryFacade is used to hide the implementation of something that is able to query a {@link
 * org.cauldron.einstein.api.message.data.model.DataObject}. An example of a query in action is: <br/>
 * <pre>
 * get "java:org.cauldron.einstein.ri.examples.MyBean";
 * split "xpath:/myOtherBean/name";
 * send "console:Split:";
 * </pre>
 * <p/>
 * In the above example the split command will use the {@link org.cauldron.einstein.api.provider.facade.QueryFacade} interface of the XPathProvider.
 *
 * @author Neil Ellis
 */
public interface BooleanQueryFacade extends Facade, BooleanQuery {

}