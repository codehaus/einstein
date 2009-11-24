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
import deesel.parser.com.nodes.*;

import org.cauldron.einstein.api.model.resource.Facade;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class FacadeNode extends AbstractEinsteinNode implements ExpressionNode {
    private final Class<? extends Facade> facadeClass;

    public FacadeNode(ASTNode astNode, Class<? extends Facade> facadeClass
    ) {
        super(astNode);
        this.facadeClass = facadeClass;
    }

    public COMNode toCOMNode(COMNode parent) {

        PrimaryExpressionNode expressionNode = new PrimaryExpressionNode(astNode);
        expressionNode.insert(new ClassNameReference(astNode, new CompiledClass(facadeClass)));
        expressionNode.insert(new Operator(astNode, "."));
        expressionNode.insert(new ClassKeywordReference(astNode));
        parent.insert(expressionNode);
        return expressionNode;

    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public Class<? extends Facade> getFacadeClass() {
        return facadeClass;
    }

    public ASTNode getASTNode() {
        return astNode;
    }
}