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
import deesel.parser.com.nodes.PrimaryExpressionNode;
import deesel.parser.com.nodes.StringLiteral;
import org.cauldron.einstein.ri.core.log.Logger;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class QualifierNode extends AbstractEinsteinNode {
    private static final Logger log = Logger.getLogger(QualifierNode.class);

    public String getQualifier() {
        return qualifier;
    }

    private final String qualifier;

    public QualifierNode(ASTNode astNode, String qualifier) {
        super(astNode);
        this.qualifier = qualifier;
    }

    public COMNode toCOMNode(COMNode parent) {

        PrimaryExpressionNode expressionNode = new PrimaryExpressionNode(astNode);
        expressionNode.insert(new StringLiteral(astNode, qualifier));
        parent.insert(expressionNode);
        log.debug("Qualifier added was {0}.", qualifier);
        return expressionNode;

    }
}