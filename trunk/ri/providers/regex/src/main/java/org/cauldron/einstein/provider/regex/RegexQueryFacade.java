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

package org.cauldron.einstein.provider.regex;

import org.contract4j5.contract.*;

import org.cauldron.einstein.api.message.data.model.DataObject;
import org.cauldron.einstein.api.message.data.model.DataObjectFactory;
import org.cauldron.einstein.api.message.data.model.DataList;
import org.cauldron.einstein.api.provider.facade.BooleanQueryFacade;
import org.cauldron.einstein.api.provider.facade.QueryFacade;
import org.cauldron.einstein.ri.core.providers.AbstractFacade;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Neil Ellis
 */
@Contract
public class RegexQueryFacade extends AbstractFacade implements QueryFacade, BooleanQueryFacade {
    private final Pattern pattern;

    public RegexQueryFacade(Pattern pattern) {
        this.pattern = pattern;
    }

    @Pre
    @Post
    public DataObject selectSingle(DataObject o) {
        Matcher matcher = pattern.matcher(o.asString());
        DataObjectFactory dataObjectFactory = o.getDataModel().getDataObjectFactory();
        matcher.find();
        return dataObjectFactory.createDataObject(matcher.group());
    }

    @Pre
    @Post
    public DataList selectMultiple( DataObject o) {
        DataObjectFactory dataObjectFactory = o.getDataModel().getDataObjectFactory();
        Matcher matcher = pattern.matcher(o.asString());
        List<DataObject> results = new ArrayList<DataObject>();
        while (matcher.find()) {
            if (matcher.groupCount() == 0) {
                results.add(dataObjectFactory.createDataObject(matcher.group()));
            } else {
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    results.add(dataObjectFactory.createDataObject(matcher.group(i)));
                }
            }

        }
        return dataObjectFactory.createDataList(results);
    }

    public boolean match(@Pre DataObject o) {
        return pattern.matcher(o.asString()).matches();
    }
}
