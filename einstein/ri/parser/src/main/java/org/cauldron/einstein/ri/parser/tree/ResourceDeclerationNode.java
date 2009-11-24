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
import org.cauldron.einstein.ri.core.instructions.ResourceDeclerationInstruction;
import org.cauldron.einstein.ri.core.log.Logger;
import org.cauldron.einstein.ri.parser.debug.DebugInfoHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class ResourceDeclerationNode extends AbstractEinsteinNode implements StatementNode {
    private static final Logger log = Logger.getLogger(ResourceDeclerationNode.class);

    private final String uri;
    private final String name;
    private final List<FacadeNode> facades = new ArrayList<FacadeNode>();

    public ResourceDeclerationNode(ASTNode astNode, String uri, String name) {
        super(astNode);
        this.uri = uri;
        this.name = name;
    }

    public COMNode toCOMNode(COMNode parent) {

        PrimaryExpressionNode expressionNode = new PrimaryExpressionNode(astNode);
        ConstructorCall constructorCall = new ConstructorCall(astNode, new CompiledClass(ResourceDeclerationInstruction.class));
        parent.insert(expressionNode).insert(constructorCall);
        constructorCall.insert(DebugInfoHelper.getDebugCOMNodeFor(astNode));

        //remove quotation marks
        constructorCall.insert(new StringLiteral(astNode, uri));
        constructorCall.insert(new StringLiteral(astNode, name));
        NodeUtil.addFacadeList(constructorCall, astNode, uri, facades);

        return expressionNode;
    }

    public String getName() {
        return name;
    }

    public String getURI() {
        return uri;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public void addChildInternal(FacadeNode node) {
        log.debug("addChild facade {0}.", node);
        facades.add(node);

    }
}