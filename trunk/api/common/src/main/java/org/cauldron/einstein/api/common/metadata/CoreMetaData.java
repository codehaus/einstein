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

package org.cauldron.einstein.api.common.metadata;

import java.lang.annotation.Documented;

/**
 * @author Neil Ellis
 */
@Documented
public @interface CoreMetaData {
    /**
     * Returns the name of the language elemnt, i.e. for providers this forms the first part of a URI e.g. the camel in
     * 'camel:http://mywebsite.com'.
     *
     * @return the name of the provider.
     */
    String name();

    /**
     * Returns a short description of the language element, this should be made available so that a user of the compiler
     * can request to see what elements are loaded and what they do.
     *
     * @return a short textual description (non HTML) of what the element does.
     */
    String shortDescription();

    /**
     * Returns a description of the language element, this should be made available so that a user of the compiler can
     * request to see what elements are loaded and what they do.
     *
     * @return textual description (non HTML) of what the element does.
     */
    String description();

    /**
     * Returns the syntax of the language element, this should be made available so that a user of the compiler can
     * request to see what elements are loaded and how to use them.
     *
     * @return textual description (non HTML) of the syntax of the element.
     */
    String syntax();

    /**
     * Returns an exampe usage of the language element, this should be made available so that a user of the compiler can
     * request to see what elements are loaded and how to use them.
     *
     * @return textual example (non HTML) of the element.
     */
    String example();
}
