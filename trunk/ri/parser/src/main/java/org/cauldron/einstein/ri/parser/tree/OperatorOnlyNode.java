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

package org.cauldron.einstein.ri.parser.tree;

import deesel.parser.ASTNode;
import deesel.parser.com.COMNode;
import deesel.parser.com.nodes.CompiledClass;
import deesel.parser.com.nodes.ConstructorCall;
import deesel.parser.com.nodes.PrimaryExpressionNode;
import org.cauldron.einstein.api.assembly.instruction.Instruction;
import org.cauldron.einstein.ri.parser.debug.DebugInfoHelper;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class OperatorOnlyNode extends AbstractEinsteinNode implements ExpressionNode, OperatorNode {
    private final Class<? extends Instruction> operator;

    public OperatorOnlyNode(ASTNode astNode, Class<? extends Instruction> operator) {
        super(astNode);
        this.operator = operator;
    }

    public COMNode toCOMNode(COMNode parent) {
        PrimaryExpressionNode expressionNode = new PrimaryExpressionNode(astNode);
        ConstructorCall constructorCall = parent.insert(expressionNode)
                .insert(new ConstructorCall(astNode, new CompiledClass(operator)));
        constructorCall.insert(DebugInfoHelper.getDebugCOMNodeFor(astNode));
        expressionNode.resolve();
        return constructorCall;
    }

    public Class<? extends Instruction> getInstructionClass() {
        return operator;
    }

    public int getResourceRefPosition(EinsteinNode node) {
        return 0;
    }
}