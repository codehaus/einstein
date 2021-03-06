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
import org.cauldron.einstein.ri.parser.EinsteinParseVisitor;
import org.cauldron.einstein.ri.parser.tree.EinsteinNode;
import org.cauldron.einstein.ri.parser.tree.FlowGroupNode;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class FlowGroupBuilder extends AbstractEinsteinNodeBuilder {


    public EinsteinNode build(EinsteinParseVisitor visitor, ASTNode astNode, EinsteinNode parent) {


        if (!visitor.lazyLoading()) {
            astNode.setVisited(true);
        }

        FlowGroupNode node = new FlowGroupNode(astNode);
        if (parent != null) {
            parent.addChild(node);
        }
        visitor.yieldToChildren(astNode, node);
        return node;
    }
}
