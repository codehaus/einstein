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


package org.cauldron.einstein.ri.parser.debug;

import deesel.parser.ASTNode;
import deesel.parser.com.COMNode;
import deesel.parser.com.nodes.*;
import deesel.parser.com.util.ClassDefFactory;
import org.cauldron.einstein.ri.core.debug.CompilationInformationImpl;

import java.math.BigInteger;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class DebugInfoHelper {
    public static COMNode getDebugCOMNodeFor(ASTNode astNode) {
        PrimaryExpressionNode expressionNode = new PrimaryExpressionNode(astNode);
        ConstructorCall constructorCall = new ConstructorCall(astNode, new CompiledClass(CompilationInformationImpl.class));

        expressionNode.insert(constructorCall);
        constructorCall.insert(new IntegerLiteral(astNode, BigInteger.valueOf(astNode.getFirstToken().beginLine), ClassDefFactory.INTEGER_PRIM));
        constructorCall.insert(new IntegerLiteral(astNode, BigInteger.valueOf(astNode.getFirstToken().beginColumn), ClassDefFactory.INTEGER_PRIM));
        constructorCall.insert(new StringLiteral(astNode, astNode.getBaseName()));
        constructorCall.insert(new StringLiteral(astNode, astNode.toString().replace("\n", "\\n").replace("\"", "\\\"")));

        return expressionNode;

    }
}
