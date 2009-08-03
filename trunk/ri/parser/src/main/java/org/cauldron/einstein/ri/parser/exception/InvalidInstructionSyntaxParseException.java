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


package org.cauldron.einstein.ri.parser.exception;

import deesel.parser.ASTNode;
import deesel.parser.com.nodes.DeeselClass;
import deesel.parser.visitors.UnparseVisitor;
import mazz.i18n.Msg;
import mazz.i18n.annotation.I18NMessage;
import mazz.i18n.annotation.I18NResourceBundle;
import org.cauldron.einstein.ri.core.instructions.AbstractInstruction;

/**
 * @author Neil Ellis
 */
@I18NResourceBundle(baseName = "parser")
@org.contract4j5.contract.Contract
public class InvalidInstructionSyntaxParseException extends EinsteinParseException {
    @I18NMessage("Error while processing '{2}' the correct usage for {0} is '{1}'.")
    public static final String INSTRUCTION_SYNTAX_ERROR = "instruction.syntax.error";

    public InvalidInstructionSyntaxParseException(ASTNode astNode, String instructionName, DeeselClass[] suppliedTypes) {
        super(astNode,
                Msg.createMsg(new Msg.BundleBaseName("parser"), INSTRUCTION_SYNTAX_ERROR, instructionName, Msg.createMsg(AbstractInstruction.getUsageBundle(), instructionName).toString(), astNode.jjtAccept(new UnparseVisitor(), null), suppliedTypes));


    }

}
