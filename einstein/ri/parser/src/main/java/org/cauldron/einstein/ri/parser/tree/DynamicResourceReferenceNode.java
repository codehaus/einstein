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
import deesel.parser.com.nodes.StringLiteral;
import org.cauldron.einstein.ri.core.resource.DynamicResourceRef;
import org.cauldron.einstein.ri.parser.debug.DebugInfoHelper;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class DynamicResourceReferenceNode extends AbstractEinsteinNode implements ExpressionNode {
    private final String name;
    private final String uri;


    public DynamicResourceReferenceNode(ASTNode astNode, String name, String uri) {
        super(astNode);
        this.name = name;
        this.uri = uri;
    }

    public COMNode toCOMNode(COMNode parent) {

        PrimaryExpressionNode expressionNode = new PrimaryExpressionNode(astNode);
        ConstructorCall constructorCall = new ConstructorCall(astNode, new CompiledClass(DynamicResourceRef.class));
        constructorCall.insert(DebugInfoHelper.getDebugCOMNodeFor(astNode));
        constructorCall.insert(new StringLiteral(astNode, name));
        constructorCall.insert(new StringLiteral(astNode, uri));
        parent.insert(expressionNode).insert(constructorCall);
        return expressionNode;

    }
}