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
import org.cauldron.einstein.ri.core.resource.StaticResourceRef;
import org.cauldron.einstein.ri.core.log.Logger;
import org.cauldron.einstein.ri.parser.debug.DebugInfoHelper;
import org.cauldron.einstein.api.model.resource.Facade;
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterInstruction;

import java.util.List;
import java.util.ArrayList;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class StaticResourceReferenceNode extends AbstractEinsteinNode implements ExpressionNode {
     private static final Logger log = Logger.getLogger(StaticResourceReferenceNode.class);

    public StaticResourceReferenceNode(ASTNode astNode) {
        super(astNode);
    }

    public COMNode toCOMNode(COMNode parent) {
        List<FacadeNode> facades= new ArrayList<FacadeNode>();

        PrimaryExpressionNode expressionNode = new PrimaryExpressionNode(astNode);
        ConstructorCall constructorCall = new ConstructorCall(astNode, new CompiledClass(StaticResourceRef.class));
        constructorCall.insert(DebugInfoHelper.getDebugCOMNodeFor(astNode));
        String firstTokenImage = astNode.getFirstTokenImage();
        String uri = firstTokenImage.substring(1, firstTokenImage.length() - 1);
        constructorCall.insert(new StringLiteral(astNode, uri));
        InstructionAwareNode awareNode = (InstructionAwareNode) getParent();
        int pos= awareNode.getResourceRefPosition(this);
        Class<? extends Facade>[] facadeClasses = awareNode.getInstructionClass().getAnnotation(RegisterInstruction.class).resources()[pos].facadesUsed();
        for (Class<? extends Facade> facade : facadeClasses) {
            facades.add(new FacadeNode(astNode, facade));
        }
        NodeUtil.addFacadeList(constructorCall, astNode, uri, facades);
        parent.insert(expressionNode).insert(constructorCall);        
        return expressionNode;

    }


}