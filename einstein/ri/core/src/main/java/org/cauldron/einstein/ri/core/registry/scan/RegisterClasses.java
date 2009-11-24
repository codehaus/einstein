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

package org.cauldron.einstein.ri.core.registry.scan;


import org.cauldron.einstein.api.common.annotation.AutoRegister;
import org.cauldron.einstein.api.common.annotation.AutoRegisterBy;
import static org.cauldron.einstein.ri.core.CoreModuleMessages.*;
import org.cauldron.einstein.ri.core.annotation.AnnotationScanner;
import org.cauldron.einstein.ri.core.log.Logger;
import org.cauldron.einstein.ri.core.registry.EinsteinRegistryFactory;
import org.cauldron.einstein.ri.core.registry.exception.RegistryRuntimeException;
import org.contract4j5.contract.Pre;
import org.scannotation.AnnotationDB;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * @author Neil Ellis
 */

@org.contract4j5.contract.Contract
public class RegisterClasses {

    private static boolean registered = false;
    private static final Logger log = Logger.getLogger(RegisterClasses.class);

    /**
     * This method will go through the annotation database returned by {@link AnnotationScanner#getAnnotationDatabase()}
     */
    public synchronized static void register() {
        if (registered) {
            return;
        }
        AnnotationDB db;
        try {
            db = new AnnotationScanner().getAnnotationDatabase();
        } catch (IOException e) {
            throw new RegistryRuntimeException(e);
        }
        Map<String, Set<String>> index = db.getAnnotationIndex();
        Set<String> annotations = index.keySet();
        for (String annotation : annotations) {
            Class<? extends Annotation> annotationClass;
            try {
                annotationClass = (Class<? extends Annotation>) RegisterClasses.class.getClassLoader()
                        .loadClass(annotation);
            }
            catch (ClassNotFoundException e) {
                log.info("Could not locate an instance of " + annotation + " while scanning for auto registerable annotations.");
                continue;
            }
            if (annotationClass.isAnnotationPresent(AutoRegister.class)) {
                Set<String> classes = index.get(annotation);
                for (String clazz : classes) {
                    log.debug("Class: " + clazz);
                    try {
                        Class<?> registerableClass = RegisterClasses.class.getClassLoader().loadClass(clazz);
                        if (!registerableClass.isAnnotation()) {
                            registerClassThroughAnnotation(annotationClass, clazz, registerableClass);
                        } else {
                            log.debug("Skipping annotation: " + clazz);
                        }
                    }
                    catch (InstantiationException e) {
                        throw new RegistryRuntimeException(e, BUNDLE_NAME, INSTANTIATION_EXCEPTION, clazz);
                    }
                    catch (IllegalAccessException e) {
                        throw new RegistryRuntimeException(e, BUNDLE_NAME, ILLEGAL_ACCESS_EXCEPTION, clazz);
                    }
                    catch (ClassNotFoundException e) {
                        throw new RegistryRuntimeException(e, BUNDLE_NAME, CLASS_NOT_FOND_EXCEPTION, clazz);
                    }
                    catch (NoSuchMethodException e) {
                        throw new RegistryRuntimeException(e, BUNDLE_NAME, NO_SUBPATH_EXCEPTION, clazz);
                    }
                    catch (InvocationTargetException e) {
                        throw new RegistryRuntimeException(e.getCause() != null ? e.getCause() : e,
                                                           BUNDLE_NAME,
                                                           INVOCATION_EXCEPTION,
                                                           clazz);
                    }
                    catch (Exception e) {
                        throw new RegistryRuntimeException(e, BUNDLE_NAME, EXCEPTION, clazz);
                    }
                }
            }
        }
        registered = true;
    }


    @Pre("nameOfClassToRegister == /[A-Za-z][A-Za-z0-9.]*/")
    private static void registerClassThroughAnnotation(Class<? extends Annotation> annotationClass,
                                                       String nameOfClassToRegister, Class<?> registerableClass) throws
                                                                                                                 NoSuchMethodException,
                                                                                                                 IllegalAccessException,
                                                                                                                 InvocationTargetException,
                                                                                                                 InstantiationException {
        //Sorry, this is mind bending stuff isn't it.
        //We're looking at classes which have annotations that are themselves annotated by
        //@AutoRegister. The @AutoRegister has an attribute subPathAttribute which determines
        //which attribute from the annotation to use as the sub path when registering the
        //auto registering class.
        final AutoRegister autoRegister = annotationClass.getAnnotation(AutoRegister.class);
        Method declaredMethod = annotationClass.getDeclaredMethod(autoRegister.nameAttribute());

        //We've figured out the method, now we invoke it to get the subPath.
        final Annotation autoRegisterableAnnotation = registerableClass.getAnnotation(annotationClass);
        if (autoRegisterableAnnotation == null) {
            throw new RegistryRuntimeException(BUNDLE_NAME,
                                               COULD_NOT_FIND_ANNOTATION,
                                               annotationClass.getCanonicalName()
                    ,
                                               nameOfClassToRegister,
                                               annotationClass.getCanonicalName());
        }
        String subPath = declaredMethod.invoke(autoRegisterableAnnotation).toString();
        String path = autoRegister.path();

        // Depending on the annotation we're either going to register an actual instance or
        // just the class itself.
        if (autoRegister.registerBy() == AutoRegisterBy.INSTANCE) {
            log.debug("Registering instance of: " + nameOfClassToRegister);
            registerInstance(registerableClass, subPath, path);
        } else if (autoRegister.registerBy() == AutoRegisterBy.CLASS) {
            log.debug("Registering class of: " + nameOfClassToRegister);
            registerClass(registerableClass, subPath, path);
        }
    }


    private static void registerInstance(Class<?> registerableClass, String subPath, String path) throws
                                                                                                  InstantiationException,
                                                                                                  IllegalAccessException {
        Object registerableClassInstance;
        registerableClassInstance = registerableClass.newInstance();
        EinsteinRegistryFactory.getInstance().put(path + "/" + subPath, registerableClassInstance);
    }


    private static void registerClass(Class<?> registerableClass, String subPath, String path) {
        EinsteinRegistryFactory.getInstance().put(path + "/" + subPath, registerableClass);
    }
}
