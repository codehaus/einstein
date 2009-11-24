/******************************************************************************
 *  All works are (C) 2008 - Mangala Solutions Ltd and Paremus Ltd.           *
 *                                                                            *
 *  Jointly liicensed to Mangala Solutions and Paremus under one              *
 *  or more contributor license agreements.  See the NOTICE file              *
 *  distributed with this work for additional information                     *
 *  regarding copyright ownership.                                            *
 *                                                                            *
 *  This program is free software: you can redistribute it and/or modify      *
 *  it under the terms of the GNU Affero General Public License as published  *
 *  by the Free Software Foundation, either version 3 of the License, or      *
 *  (at your option) any later version.                                       *
 *                                                                            *
 *  This program is distributed in the hope that it will be useful,           *
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of            *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the             *
 *  GNU Affero General Public License for more details.                       *
 *                                                                            *
 *  You should have received a copy of the GNU Affero General Public License  *
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.     *
 *                                                                            *
 ******************************************************************************/


package org.cauldron.einstein.ri.parser;

import mazz.i18n.annotation.I18NMessage;
import mazz.i18n.annotation.I18NResourceBundle;

/**
 * @author Neil Ellis
 */
@I18NResourceBundle(baseName = ParserModuleMessages.BUNDLE_NAME)
public interface ParserModuleMessages {

    String BUNDLE_NAME = "parser";

// --Commented out by Inspection START (5/28/08 12:14 PM):
//    @I18NMessage("Could not find variable {0}.")
//    String VARIABLE_NOT_FOUND = "variable.not.found";
// --Commented out by Inspection STOP (5/28/08 12:14 PM)


    @I18NMessage("Operator {0} is not a unary operator.")
    String OPERATOR_NOT_UNARY = "operator.not.unary";

    @I18NMessage("Operator {0} is not a binary operator.")
    String OPERATOR_NOT_BINARY = "operator.not.binary";

    @I18NMessage("Operator {0} is not a ternary operator.")
    String OPERATOR_NOT_TERNARY = "operator.not.ternary";

    @I18NMessage("Operator {0} is not a an 'operator only' operator.")
    String OPERATOR_NOT_OPONLY = "operator.not.oponly";

    @I18NMessage("Binary reversed operators such as {0} cannot be chained, please use explicit parenthesis not chaining.")
    String BINARY_REVERED_NO_CHAINING = "binary.reveresed.cannot.be.chained";

    @I18NMessage("Unrecognized operator {0}.")
    String UNRECOGNIZED_OPERATOR = "unrecognized.operator";

// --Commented out by Inspection START (5/28/08 12:14 PM):
//    @I18NMessage("Invalid syntax for map based instruction group, ':' should be used to seperate the label from the instruction and should be specified for all entries.")
//    public static final String MAPPED_INSTRUCTION_GROUP_SYNTAX_EXCEPTION = "mapped.instruction.group.syntax.exception";
// --Commented out by Inspection STOP (5/28/08 12:14 PM)


    @I18NMessage("Invalid instruction {0}, it was not found in the registry.")
    public static final String INSTRUCTION_NOT_IN_REGISTRY = "instruction.not.in.registry";

    @I18NMessage("The meta-instruction {0} was in the registry, but is not stored in the registry as a Class.")
    public static final String META_INSTRUCTION_REGISTERED_AS_CLASS = "meta.instruction.registered.as.class";

    @I18NMessage("Invalid meta-instruction {0}, it was not found in the registry.")
    public static final String META_INSTRUCTION_NOT_IN_REGISTRY = "meta.instruction.not.in.registry";

    @I18NMessage("The instruction {0} was in the registry, but is not stored in the registry as a Class.")
    public static final String INSTRUCTION_REGISTERED_AS_CLASS = "instruction.registered.as.class";

    @I18NMessage("The first argument to resource should be a quoted URI, e.g. \"http://myserver.com/\".")
    public static final String FIRST_ARGUMENT_TO_RESOURCE_NOT_A_TEMPLATE_STRING = "first.argument.to.resourece.keyword.not.a.template.string";

    @I18NMessage("The second argument to resource should be an identifier, e.g. myResourceName.")
    public static final String FIRST_ARGUMENT_TO_RESOURCE_NOT_AN_IDENTIFIER = "second.argument.to.resourece.keyword.not.an.identifier";

    @I18NMessage("Invalid child node {0} for {1}.")
    public static final String INVALID_CHILD_NODE = "invalid.child.node";

    @I18NMessage("Failed to add child node to {0}.")
    public static final String CHILD_NODE_FAILURE = "child.node.failure";

    @I18NMessage("No suitable method to add child node {0} to {1}.")
    public static final String ADD_CHILD_FAILURE = "add.child.failure";

    @I18NMessage("No suitable method to add child node {0} to {1}, the appropriate method does not have public access.")
    public static final String ADD_CHILD_SECURITY_FAILURE = "add.child.security.failure";

    @I18NMessage("Qualifiers {0} specified for an instruction that does not support qualifiers.")
    public static final String QUALIFIERS_NOT_SUPPORTED = "qualifiers.not.supported";

    @I18NMessage("Provider {0} deos not support facade {1}.")
    public static final String FACADE_NOT_SUPPORTED = "facade.not.supported";

    @I18NMessage("Each entry into a metadata block must be of the form key:value.")
    public static final String META_DATA_BLOCK_SYNTAX = "metadata.block.syntax";
}
