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


package org.cauldron.einstein.ri.parser.builder;

import deesel.parser.ASTNode;
import org.cauldron.einstein.api.assembly.instruction.Instruction;
import org.cauldron.einstein.api.assembly.instruction.annotation.OperatorType;
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterOperator;
import org.cauldron.einstein.ri.core.runtime.OperatorLookup;
import org.cauldron.einstein.ri.parser.EinsteinParseVisitor;
import static org.cauldron.einstein.ri.parser.ParserModuleMessages.*;
import org.cauldron.einstein.ri.parser.exception.EinsteinParseException;
import org.cauldron.einstein.ri.parser.tree.BinaryExpressionNode;
import org.cauldron.einstein.ri.parser.tree.EinsteinNode;

import java.util.Arrays;
import java.util.List;

/**
 * @author Neil Ellis
 */

@org.contract4j5.contract.Contract
public class BinaryExpressionBuilder extends AbstractEinsteinNodeBuilder {


    public EinsteinNode build(EinsteinParseVisitor visitor, ASTNode astNode, EinsteinNode parent) {

        if (!visitor.lazyLoading()) {
            astNode.setVisited(true);
        }

        String operatorSymbol = astNode.getSecondChild().toString().trim();
        Class<? extends Instruction> operator = OperatorLookup.getInstance().getClassForOperator(operatorSymbol);
        if (operator == null) {
            throw new EinsteinParseException(BUNDLE_NAME, astNode, UNRECOGNIZED_OPERATOR, operatorSymbol);
        }

        RegisterOperator annotation = operator.getAnnotation(RegisterOperator.class);
        List<OperatorType> operatorTypes = Arrays.asList(annotation.types());
        OperatorType opType;

        if (operatorTypes.contains(OperatorType.N_ARY)) {
            opType = OperatorType.N_ARY;
        } else if (operatorTypes.contains(OperatorType.BINARY)) {
            opType = OperatorType.BINARY;
        } else if (operatorTypes.contains(OperatorType.BINARY_REVERSED)) {
            opType = OperatorType.BINARY_REVERSED;
        } else {
            throw new EinsteinParseException(BUNDLE_NAME, astNode, OPERATOR_NOT_BINARY, operatorSymbol);
        }

        BinaryExpressionNode node = new BinaryExpressionNode(astNode, opType, operatorSymbol, operator);
        parent.addChild(node);
        visitor.yieldToChildren(astNode, node);
        return node;
    }
}