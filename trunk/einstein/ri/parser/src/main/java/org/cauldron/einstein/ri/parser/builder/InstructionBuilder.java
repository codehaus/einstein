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
import org.cauldron.einstein.api.assembly.instruction.Instruction;
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterInstruction;
import org.cauldron.einstein.ri.core.registry.EinsteinRegistryFactory;
import org.cauldron.einstein.ri.parser.EinsteinParseVisitor;
import static org.cauldron.einstein.ri.parser.ParserModuleMessages.*;
import org.cauldron.einstein.ri.parser.exception.EinsteinParseException;
import org.cauldron.einstein.ri.parser.tree.EinsteinNode;
import org.cauldron.einstein.ri.parser.tree.InstructionNode;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class InstructionBuilder extends AbstractEinsteinNodeBuilder {

    private static final String REGISTRY_PREFIX = RegisterInstruction.PATH + "/";


    public EinsteinNode build(EinsteinParseVisitor
            visitor, ASTNode astNode, EinsteinNode
            parent) {

        final ASTNode identifierNode = astNode.getFirstChild();
        identifierNode.setVisited(true);

        ASTDialectIdentifier identifier = (ASTDialectIdentifier) identifierNode;
        if (!visitor.lazyLoading()) {
            astNode.setVisited(true);
        }

        final String instructionName = identifier.getFirstTokenImage();
        final Object o = EinsteinRegistryFactory.getInstance().get(REGISTRY_PREFIX + instructionName);
        if (o == null) {
            throw new EinsteinParseException(BUNDLE_NAME, astNode, INSTRUCTION_NOT_IN_REGISTRY, instructionName);
        }
        if (!(o instanceof Class)) {
            throw new EinsteinParseException(BUNDLE_NAME, astNode, INSTRUCTION_REGISTERED_AS_CLASS, instructionName);
        }
        final Class<? extends Instruction> clazz = (Class<? extends Instruction>) o;
        InstructionNode node = new InstructionNode(astNode, instructionName, clazz);
        parent.addChild(node);
        visitor.yieldToChildren(astNode, node);
        return node;
    }


}
