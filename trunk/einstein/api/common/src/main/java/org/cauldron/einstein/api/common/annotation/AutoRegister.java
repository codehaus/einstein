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

package org.cauldron.einstein.api.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Meta-annotation to say that an annotation causes automatic registration.
 *
 * @author Neil Ellis
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(java.lang.annotation.ElementType.ANNOTATION_TYPE)
public @interface AutoRegister {
    /**
     * The path to the area of the registry for storing this auto-registered type.
     *
     * @return a / seperated path.
     */
    String path();

    /**
     * This attribute from the annotation that this annotates which contains the name to which the auto-registered
     * object is bound.
     *
     * @return the name.
     */
    String nameAttribute();

    /**
     * This denotes what is actually registered e.g. an instance or the class.
     *
     * @return what is registered.
     */
    AutoRegisterBy registerBy();
}
