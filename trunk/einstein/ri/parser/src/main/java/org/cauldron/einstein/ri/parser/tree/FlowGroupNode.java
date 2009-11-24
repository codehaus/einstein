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
import org.cauldron.einstein.ri.core.group.FlowInstructionGroup;
import org.cauldron.einstein.ri.parser.debug.DebugInfoHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class FlowGroupNode extends AbstractEinsteinNode implements ExpressionNode, GroupNode {

    private final List<StatementNode> statements = new ArrayList<StatementNode>();
    private final List<MetaDataNode> metaDatas = new ArrayList<MetaDataNode>();

    public FlowGroupNode(ASTNode astNode) {
        super(astNode);
    }

    public COMNode toCOMNode(COMNode parent) {

        PrimaryExpressionNode expressionNode = new PrimaryExpressionNode(astNode);
        ConstructorCall constructorCall = new ConstructorCall(astNode, new CompiledClass(FlowInstructionGroup.class));

        parent.insert(expressionNode)
                .insert(constructorCall);

        constructorCall.insert(DebugInfoHelper.getDebugCOMNodeFor(astNode));

        for (MetaDataNode metaData : metaDatas) {
            metaData.toCOMNode(constructorCall);
        }

        for (StatementNode expression : statements) {
            expression.toCOMNode(constructorCall);
        }


        return expressionNode;
    }

    public void addChildInternal(StatementNode node) {
        statements.add(node);

    }


    public void addChildInternal(MetaDataNode node) {
        metaDatas.add(node);
    }

    public EinsteinNode findDeclarationNode(String name) {
        for (StatementNode statementNode : statements) {
            if (statementNode instanceof ResourceDeclerationNode) {
                ResourceDeclerationNode declerationNode = (ResourceDeclerationNode) statementNode;
                if (declerationNode.getName().equals(name)) {
                    return declerationNode;
                }
            }
        }
        return super.findDeclarationNode(name);
    }
}
