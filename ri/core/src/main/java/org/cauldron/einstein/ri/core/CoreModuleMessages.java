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

package org.cauldron.einstein.ri.core;

import mazz.i18n.annotation.I18NMessage;
import mazz.i18n.annotation.I18NResourceBundle;

/**
 * @author Neil Ellis
 */
@I18NResourceBundle(baseName = CoreModuleMessages.BUNDLE_NAME)
public interface CoreModuleMessages {

    public static final String BUNDLE_NAME = "core";


    @I18NMessage(
            "Could not find annotation: {0} on {1}. Have you made sure the annotation {2} has @Retention(java.lang.annotation.RetentionPolicy.RUNTIME) set?")
    public static final String COULD_NOT_FIND_ANNOTATION = "could.not.find.annotation";

    @I18NMessage("Failed to instantiate an annotation registered class {0}.")
    public static final String INSTANTIATION_EXCEPTION = "register.classes.instatiation.exception";

    @I18NMessage("Illegal access exception while trying to register {0}.")
    public static final String ILLEGAL_ACCESS_EXCEPTION = "register.classes.illegal.access.exception";

    @I18NMessage("Failed to find an annotation registered class {0}.")
    public static final String CLASS_NOT_FOND_EXCEPTION = "register.classes.class.not.found.exception";

    @I18NMessage("Failed to find the subPathAttribute on the annotation registered class $0}.")
    public static final String NO_SUBPATH_EXCEPTION = "register.classes.subpath.exception";

    @I18NMessage("Exception occured while invoking the subPathAttributeMethod on the annotation registered class {0}.")
    public static final String INVOCATION_EXCEPTION = "register.classes.invocation.exception";

    @I18NMessage("Error occured while trying to autoregister {0}.")
    public static final String EXCEPTION = "register.classes.exception";


    @I18NMessage("Exception occured wile trying to match an XPath query {0}.")
    public static final String MATCH_EXCEPTION = "xmldom.xpath.match.exception";

    @I18NMessage("Exception occured wile trying to match an XPath query {0}.")
    public static final String SELECT_SINGLE_EXCEPTION = "xmldom.xpath.select.single.exception";

    @I18NMessage("Exception occured wile trying to match an XPath query {0}.")
    public static final String SELECT_MULTIPLE_EXCEPTION = "xmldom.xpath.select.multiple.exception";

    @I18NMessage("Runtime not initialized.")
    public static final String RUNTIME_NOT_INITIALIZED = "runtime.not.initialized";

    @I18NMessage("Could not create an instance of a class called {0}.")
    public static final String COULD_NOT_CREATE_INSTANCE = "could.not.create.instance";

// --Commented out by Inspection START (5/28/08 11:59 AM):
//    @I18NMessage("Cannot execute a collection group.")
//    public static final String CANNOT_EXECUTE_A_COLLECTION_GROUP = "cannot.execute.collection.group";
// --Commented out by Inspection STOP (5/28/08 11:59 AM)

    @I18NMessage("Could not find a matching method for {0} payloads was of type {1}.")
    public static final String COULD_NOT_FIND_MATCHING_METHOD = "could.not.find.matching.method";

    @I18NMessage("More than one ? in the URI is not allowed.")
    public static final String MULTIPLE_QUERIES_IN_URL = "multiple.queries.in.url";

    @I18NMessage("Could not parse the port number {0} in {1}.")
    public static final String COULD_NOT_PARSE_THE_PORT = "could.not.parse.the.port";

    @I18NMessage("Malformed URI {0}.")
    public static final String MALFORMED_URI = "malformed.uri";

    @I18NMessage("Too many = in query string {0}.")
    public static final String TOO_MANY_EQUALS = "too.many.equals.in.query.string";

    @I18NMessage("{2}: Line {0}, Col {1}: {3}")
    public static final String POSITION_DESCRIPTION = "debug.info.position.description";

    @I18NMessage("No label for instruction at {0} - the parser should have caught this!")
    public static final String NO_LABEL_FOR_INSTRUCTION = "no.label.for.instruction";

    @I18NMessage("Expected instruction, instead found {0}.")
    public static final String EXPECTED_INSTRUCTION = "expected.instruction";

    @I18NMessage("The operation {0} is not supported on {1}.")
    public static final String INVALID_OPERATION_EXCEPTION = "invalid.operation.exeception";

    @I18NMessage("Could not find a profile.")
    public static final String PROFILE_NOT_FOUND = "profile.not.found";

    @I18NMessage("A URI with a nested URI cannot have the nested URI accessed as a location, the nested URI was {0}.")
    public static final String CANNOT_CONVERT_NESTED_URI_TO_LOCATION = "cannot.convert.nested.uri.to.location";

    @I18NMessage("A URI with a nested URI cannot have the nested URI accessed as a path, the nested URI was {0}.")
    public static final String CANNOT_CONVERT_NESTED_URI_TO_PATH = "cannot.convert.nested.uri.to.path";

    @I18NMessage(
            "The action {0} is paramaterized by an unsupported message view, please make sure you only use message views that are parents of CombinedView.")
    public static final String UNSUPPORTED_MESSAGE_VIEW = "unsupported.message.view";

    @I18NMessage("The converter {0} had an error when instantiated for the Rosetta Stone.")
    public static final String CONVERTER_INSTATIATION_EXCEPTION = "converter.instantiation.exception";


    @I18NMessage("Invalid lifecycle state, expected state(s) \"{0}\" but was in state \"{1}\".")
    public static final String INVALID_LIFECYCLE_STATE = "invalid.lifecycle.state";

    @I18NMessage("Currently only the cron scheduler is supported, and it must be explicitly specified.")
    public static final String TIME_SUPPORTS_CRON_ONLY = "time.supports.cron.only";

// --Commented out by Inspection START (5/28/08 11:59 AM):
//    @I18NMessage("Einstein has entered into an invalid state, there is already an entry in the scope map for this execution correlation ({0}), no execution flow can have the same execution correlation as another. This needs urgent attention.")
//    public static final String CORRELATION_ALREADY_HAS_SCOPE = "correlation.already.has.scope";
// --Commented out by Inspection STOP (5/28/08 11:59 AM)

    @I18NMessage(
            "Einstein has entered into an invalid state, there is no execution correlation on a message, all messages must have one, the scope ID was {0}. This needs urgent attention.")
    public static final String NO_EXECUTION_CORRELATION = "no.execution.correlation";


    @I18NMessage(
            "The data model {0} was not in the registry, is this the correct spelling and is the model's jar on the classpath?")
    public static final String DATA_MODEL_NOT_IN_REGISTRY = "data.model.not.in.registry";

    @I18NMessage("The data model {0} was not registered as a class, this is an error in Einstein not in the model.")
    public static final String DATA_MODEL_NOT_REGISTRERED_AS_CLASS = "data.model.not.registered.as.class";

    @I18NMessage(
            "Exception occured executing '{0}' in compilation file {1} at line {2} column {3} ({4}) the error was:\\n{5}")
    public static final String INSTRUCTION_EXECUTION_EXCEPTION = "instruction.execution.exception";

    @I18NMessage("All instructions failed while executing a competing execution block, a sample error is:\n {0}.")
    public static final String ALL_INSTRUCTIONS_FAILED_IN_COMPETING_BLOCK = "all.instructions.failed.competing.block";
}
