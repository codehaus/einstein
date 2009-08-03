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
import org.cauldron.einstein.api.assembly.instruction.annotation.OperatorType;
import org.cauldron.einstein.ri.core.group.FlowInstructionGroup;
import org.cauldron.einstein.ri.core.group.TupleInstructionGroup;
import static org.cauldron.einstein.ri.parser.ParserModuleMessages.*;
import org.cauldron.einstein.ri.parser.debug.DebugInfoHelper;
import org.cauldron.einstein.ri.parser.exception.EinsteinParseException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class BinaryExpressionNode extends AbstractEinsteinNode implements ExpressionNode, OperatorNode {
    private final OperatorType operatorType;
    private final Class<? extends Instruction> operator;
    private final String operatorSymbol;
    private final List<ExpressionNode> expressions = new ArrayList<ExpressionNode>();

    public BinaryExpressionNode(ASTNode astNode, OperatorType operatorType, String operatorSymbol, Class<? extends Instruction> operator) {
        super(astNode);
        this.operatorType = operatorType;
        this.operatorSymbol = operatorSymbol;
        this.operator = operator;
    }

    public COMNode toCOMNode(COMNode parent) {
        if (operatorType == OperatorType.BINARY) {
            expressions.get(0).toCOMNode(parent);
            for (int i = 1; i < expressions.size(); i++) {
                PrimaryExpressionNode primaryExpressionNode = new PrimaryExpressionNode(astNode);
                ConstructorCall constructorCall = parent.insert(primaryExpressionNode)
                        .insert(new ConstructorCall(astNode, new CompiledClass(operator)));

                constructorCall.insert(DebugInfoHelper.getDebugCOMNodeFor(astNode));
                ExpressionNode expressionNode = expressions.get(i);
                expressionNode.toCOMNode(constructorCall);
                primaryExpressionNode.resolve();
            }
            return null;

        } else if (operatorType == OperatorType.BINARY_REVERSED) {
            if (astNode.getNumberOfChildren() > 3) {
                throw new EinsteinParseException(BUNDLE_NAME, astNode, BINARY_REVERED_NO_CHAINING, operatorSymbol);
            }
            PrimaryExpressionNode expressionNode = new PrimaryExpressionNode(astNode);
            ConstructorCall constructorCall = parent.insert(expressionNode)
                    .insert(new ConstructorCall(astNode, new CompiledClass(operator)));
            constructorCall.insert(DebugInfoHelper.getDebugCOMNodeFor(astNode));
            expressions.get(1).toCOMNode(constructorCall);
            expressions.get(0).toCOMNode(parent);
            expressionNode.resolve();
            return null;

        } else if (operatorType == OperatorType.N_ARY) {
            PrimaryExpressionNode listNode = new PrimaryExpressionNode(astNode);
            ConstructorCall constructorCall = parent.insert(listNode)
                    .insert(new ConstructorCall(astNode, new CompiledClass(TupleInstructionGroup.class)));
            constructorCall.insert(DebugInfoHelper.getDebugCOMNodeFor(astNode));
            for (ExpressionNode expressionNode : expressions) {
                //We need to insert these extra flow nodes to make sure operators work correctly.
                ConstructorCall insertedFlowNode = constructorCall
                        .insert(new ConstructorCall(astNode, new CompiledClass(FlowInstructionGroup.class)));
                insertedFlowNode.insert(DebugInfoHelper.getDebugCOMNodeFor(astNode));
                expressionNode.toCOMNode(insertedFlowNode);
            }
            listNode.resolve();
            PrimaryExpressionNode expressionNode = new PrimaryExpressionNode(astNode);
            ConstructorCall instructionContstructor = new ConstructorCall(astNode, new CompiledClass(operator));
            instructionContstructor.insert(DebugInfoHelper.getDebugCOMNodeFor(astNode));
            instructionContstructor.insert(listNode);
            parent.insert(expressionNode).insert(instructionContstructor);
            return null;

        } else {
            throw new EinsteinParseException(BUNDLE_NAME, astNode, OPERATOR_NOT_BINARY, operatorSymbol);
        }

    }

    @SuppressWarnings({"UnusedDeclaration"})
    public void addChildInternal(ExpressionNode node) {
        expressions.add(node);
    }

    public Class<? extends Instruction> getInstructionClass() {
        return operator;
    }

    public int getResourceRefPosition(EinsteinNode node) {
        return 0;
    }
}