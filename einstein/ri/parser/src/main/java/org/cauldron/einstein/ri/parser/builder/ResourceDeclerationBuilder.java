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

import deesel.parser.ASTDialectIdentifier;
import deesel.parser.ASTNode;
import deesel.parser.ASTTemplateString;
import org.cauldron.einstein.ri.parser.EinsteinParseVisitor;
import static org.cauldron.einstein.ri.parser.ParserModuleMessages.*;
import org.cauldron.einstein.ri.parser.exception.EinsteinParseException;
import org.cauldron.einstein.ri.parser.tree.EinsteinNode;
import org.cauldron.einstein.ri.parser.tree.ResourceDeclerationNode;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class ResourceDeclerationBuilder extends AbstractEinsteinNodeBuilder {


    public EinsteinNode build(EinsteinParseVisitor visitor, ASTNode astNode, EinsteinNode parent) {

        if (!visitor.lazyLoading()) {
            astNode.setVisited(true);
        }

        astNode.getFirstChild().setVisited(true);
        if (!(astNode.getSecondChild().getFirstChild() instanceof ASTTemplateString)) {
            throw new EinsteinParseException(BUNDLE_NAME, astNode, FIRST_ARGUMENT_TO_RESOURCE_NOT_A_TEMPLATE_STRING);
        }
        if (!(astNode.getThirdChild() instanceof ASTDialectIdentifier)) {
            throw new EinsteinParseException(BUNDLE_NAME, astNode, FIRST_ARGUMENT_TO_RESOURCE_NOT_AN_IDENTIFIER);
        }

        String uri = astNode.getSecondChild().getFirstTokenImage();
        uri = uri.substring(1, uri.length() - 1);

        ResourceDeclerationNode node = new ResourceDeclerationNode(astNode, uri, astNode.getThirdChild().getFirstTokenImage());
        parent.addChild(node);
        return node;
    }
}
