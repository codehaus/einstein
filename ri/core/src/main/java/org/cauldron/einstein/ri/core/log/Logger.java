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

package org.cauldron.einstein.ri.core.log;

import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.Priority;
import org.apache.log4j.PropertyConfigurator;
import org.contract4j5.contract.Contract;

import java.net.URL;
import java.text.MessageFormat;

/**
 * @author Neil Ellis
 */
@SuppressWarnings({"UnusedDeclaration"})
@Contract
public class Logger {

    private final org.apache.log4j.Logger logger;

    private Logger(org.apache.log4j.Logger logger) {
        this.logger = logger;
    }


    static {
        System.setProperty(org.apache.log4j.LogManager.DEFAULT_INIT_OVERRIDE_KEY, "true");
        URL resource = Logger.class.getResource("/einstein-log4j.properties");
        if (resource != null) {
            PropertyConfigurator.configure(resource);
        }
    }

    public static Logger getLogger(String name) {
        return new Logger(org.apache.log4j.Logger.getLogger(name));
    }


    public static Logger getLogger(Class clazz) {
        return new Logger(org.apache.log4j.Logger.getLogger(clazz));
    }


    public void assertLog(boolean assertion, String msg) {
        logger.assertLog(assertion, msg);
    }


    public void debug(String message, Object... params) {
        if (logger.isDebugEnabled()) {
            logger.debug(getPrefix() + MessageFormat.format(message, params));
        }
    }


    @SuppressWarnings({"ThrowableInstanceNeverThrown"})
    private static String getPrefix() {
        StackTraceElement[] stackTraceElements = new Throwable().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            String className = stackTraceElement.getClassName();
            if (!className.equals(Logger.class.getCanonicalName())) {
                String name = className.substring(className.lastIndexOf('.') + 1);
                return MessageFormat.format("{0}:{1}({2}) ",
                                            name,
                                            stackTraceElement.getMethodName(),
                                            stackTraceElement.getLineNumber());
            }
        }
        return "NO PREFIX FOUND : ";
    }


    public void debug(Throwable t, String message, Object... params) {
        if (logger.isDebugEnabled()) {
            logger.debug(t);
            logger.debug(getPrefix() + MessageFormat.format(message, params));
        }
    }


    public void error(Throwable t) {
        if (logger.isEnabledFor(Priority.ERROR)) {
            logger.error(t, t);
        }
    }


    public void error(String message, Object... params) {
        if (logger.isEnabledFor(Priority.ERROR)) {
            logger.error(getPrefix() + MessageFormat.format(message, params));
        }
    }


    public void error(Throwable t, String message, Object... params) {
        if (logger.isEnabledFor(Priority.ERROR)) {
            logger.error(t);
            logger.error(getPrefix() + MessageFormat.format(message, params));
        }
    }


    public void fatal(Throwable t) {
        if (logger.isEnabledFor(Priority.FATAL)) {
            logger.fatal(t, t);
        }
    }


    public void fatal(String message, Object... params) {
        if (logger.isEnabledFor(Priority.FATAL)) {
            logger.fatal(getPrefix() + MessageFormat.format(message, params));
        }
    }


    public void fatal(Throwable t, String message, Object... params) {
        if (logger.isEnabledFor(Priority.FATAL)) {
            logger.fatal(t);
            logger.fatal(getPrefix() + MessageFormat.format(message, params));
        }
    }


    public Level getLevel() {
        return logger.getLevel();
    }


    public String getName() {
        return logger.getName();
    }


    public Category getParent() {
        return logger.getParent();
    }


    public void info(String message, Object... params) {
        if (logger.isEnabledFor(Priority.INFO)) {
            logger.info(getPrefix() + MessageFormat.format(message, params));
        }
    }


    public void info(Throwable t, String message, Object... params) {
        if (logger.isEnabledFor(Priority.INFO)) {
            logger.info(t);
            logger.info(getPrefix() + MessageFormat.format(message, params));
        }
    }


    public void warn(String message, Object... params) {
        if (logger.isEnabledFor(Priority.WARN)) {
            logger.warn(getPrefix() + MessageFormat.format(message, params));
        }
    }


    public void warn(Throwable t, String message, Object... params) {
        if (logger.isEnabledFor(Priority.WARN)) {
            logger.warn(t);
            logger.warn(getPrefix() + MessageFormat.format(message, params));
        }
    }
}
