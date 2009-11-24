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


package org.cauldron.einstein.ri.parser.builder;

import deesel.parser.ASTNode;
import org.cauldron.einstein.api.assembly.instruction.MetaInstruction;
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterMetaInstruction;
import org.cauldron.einstein.ri.core.registry.EinsteinRegistryFactory;
import org.cauldron.einstein.ri.parser.EinsteinParseVisitor;
import static org.cauldron.einstein.ri.parser.ParserModuleMessages.*;
import org.cauldron.einstein.ri.parser.exception.EinsteinParseException;
import org.cauldron.einstein.ri.parser.tree.*;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class MetaDataBuilder extends AbstractEinsteinNodeBuilder {


    public EinsteinNode build(EinsteinParseVisitor visitor, ASTNode astNode, EinsteinNode parent) {


        if (!visitor.lazyLoading()) {
            astNode.setVisited(true);
        }

        MetaDataNode node = new MetaDataNode(astNode);
        if (parent != null) {
            parent.addChild(node);
        }

        ASTNode[] keyValueNodes = astNode.getChildren();
        for (ASTNode keyValueNode : keyValueNodes) {

            if (keyValueNode.getNumberOfChildren() != 2) {
                throw new EinsteinParseException(BUNDLE_NAME, keyValueNode, META_DATA_BLOCK_SYNTAX);
            }

            //We treat the meta instruction @ as a special case, the data is no longer pure meta data now
            //the top level is treated as a set of instructions.
            if (parentIsTheUsingOperator(parent) || parentIsTheUsingInstruction(parent)) {
                buildMetaInstructionNode(visitor, astNode, node, keyValueNode);

            } else {
                buildNameValueNode(visitor, node, keyValueNode);

            }
        }
        return node;
    }

    private boolean parentIsTheUsingOperator(EinsteinNode parent) {
        return (parent instanceof TernaryNode) && ((TernaryNode) parent).getOperatorSymbol().equals("@");
    }

    private boolean parentIsTheUsingInstruction(EinsteinNode parent) {
        return (parent instanceof InstructionNode) && ((InstructionNode) parent).getInstructionName().equals("using");
    }

    private void buildMetaInstructionNode(EinsteinParseVisitor visitor, ASTNode astNode, MetaDataNode node, ASTNode keyValueNode) {
        //top level meta  nodes actually produce meta instructions
        final String instructionName = keyValueNode.getFirstTokenImage();
        final Object o = EinsteinRegistryFactory.getInstance().get(RegisterMetaInstruction.PATH + "/" + instructionName);
        if (o == null) {
            throw new EinsteinParseException(BUNDLE_NAME, astNode, META_INSTRUCTION_NOT_IN_REGISTRY, instructionName);
        }
        if (!(o instanceof Class)) {
            throw new EinsteinParseException(BUNDLE_NAME, astNode, META_INSTRUCTION_REGISTERED_AS_CLASS, instructionName);
        }
        final Class<? extends MetaInstruction> clazz = (Class<? extends MetaInstruction>) o;
        MetaInstructionNode nameValueNode = new MetaInstructionNode(keyValueNode, clazz);
        if (hasNestedMetaDataNode(keyValueNode)) {
            visitor.yield(keyValueNode.getSecondChild(), nameValueNode);
        }
        node.addChild(nameValueNode);
    }

    private void buildNameValueNode(EinsteinParseVisitor visitor, MetaDataNode node, ASTNode keyValueNode) {
        NameValueNode nameValueNode = new NameValueNode(keyValueNode);
        if (hasNestedMetaDataNode(keyValueNode)) {
            visitor.yield(keyValueNode.getSecondChild(), nameValueNode);
        }
        node.addChild(nameValueNode);
    }

    private boolean hasNestedMetaDataNode(ASTNode keyValueNode) {
        return keyValueNode.getSecondChild().getNumberOfChildren() != 1 || keyValueNode.getSecondChild().getFirstTokenImage().equals("(");
    }
}