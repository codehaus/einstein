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

package org.cauldron.einstein.ri.parser;

import deesel.parser.*;
import deesel.parser.com.COMNode;
import deesel.parser.parslet.Parslet;
import deesel.parser.visitors.RecurseVisitor;
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterInstruction;
import org.cauldron.einstein.api.model.resource.Facade;
import org.cauldron.einstein.ri.core.log.Logger;
import org.cauldron.einstein.ri.core.registry.EinsteinRegistryFactory;
import org.cauldron.einstein.ri.parser.builder.*;
import org.cauldron.einstein.ri.parser.tree.*;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class EinsteinParseVisitor extends RecurseVisitor {

    private static final Logger log = Logger.getLogger(EinsteinParseVisitor.class);

    private static final IntegerBuilder INTEGER_BUILDER = new IntegerBuilder();
    private static final UnaryExpressionBuilder UNARY_EXPRESSION_BUILDER = new UnaryExpressionBuilder();
    private static final FlowGroupBuilder FLOW_GROUP_BUILDER = new FlowGroupBuilder();
    private static final UnSequencedGroupBuilder UN_SEQUENCED_GROUP_BUILDER = new UnSequencedGroupBuilder();
    private static final BinaryExpressionBuilder BINARY_EXPRESSION_BUILDER = new BinaryExpressionBuilder();
    private static final TernaryExpressionBuilder TERNARY_EXPRESSION_BUILDER = new TernaryExpressionBuilder();
    private static final OperatorOnlyExpressionBuilder OPERATOR_ONLY_EXPRESSION_BUILDER = new OperatorOnlyExpressionBuilder();

    private static final ResourceDeclerationBuilder RESOURCE_DECLERATION_BUILDER = new ResourceDeclerationBuilder();
    private static final InstructionBuilder INSTRUCTION_BUILDER = new InstructionBuilder();
    private static final TupleGroupBuilder TUPLE_GROUP_BUILDER = new TupleGroupBuilder();
    private static final StaticResourceRefBuilder STATIC_RESOURCE_REF_BUILDER = new StaticResourceRefBuilder();
    private static final LabelledExpressionBuilder LABELLED_EXPRESSION_BUILDER = new LabelledExpressionBuilder();
    private static final String INSTRUCTION_REGISTRY_PREFIX = RegisterInstruction.PATH + "/";

    private boolean lazyLoading;
    private static final QualifierBuilder QUALIFIER_BUILDER = new QualifierBuilder();
    private static final MetaDataBuilder META_DATA_BUILDER = new MetaDataBuilder();
    private static final TuplePositionReferenceBuilder TUPLE_POSITION_REFERENCE_BUILDER = new TuplePositionReferenceBuilder();

    public EinsteinParseVisitor(Parslet parslet) {
        Parslet parslet1 = parslet;
    }

    public Object visit(ASTDialectBlockBody node, Object o) {
        if (o instanceof COMNode) {
            //Top level
            EinsteinNode parentNode = FLOW_GROUP_BUILDER.build(this, node, null);
            return parentNode.toCOMNode((COMNode) o);
        } else {
            return FLOW_GROUP_BUILDER.build(this, node, (EinsteinNode) o);
        }
    }

    public Object visit(ASTIntegerLiteral node, Object o) {
        return INTEGER_BUILDER.build(this, node, (EinsteinNode) o);
    }

    public Object visit(ASTDialectUnaryExpression node, Object o) {
        return UNARY_EXPRESSION_BUILDER.build(this, node, (EinsteinNode) o);
    }

    public Object visit(ASTDialectBinaryExpression node, Object o) {
        return BINARY_EXPRESSION_BUILDER.build(this, node, (EinsteinNode) o);
    }


    public Object visit(ASTDialectIdentifier node, Object o) {
        if (node.alreadyVisited()) {
            //Instructions mark the first identifier as already visited.
            return o;
        }
        EinsteinNode parentNode = (EinsteinNode) o;
        String name = node.getFirstTokenImage();
        if (parentNode instanceof InstructionNode && ((InstructionNode) parentNode).isQualifierSupported(name)) {
            log.debug("Qualifier {0}.", name);
            return QUALIFIER_BUILDER.build(this, node, (EinsteinNode) o);
        }
        EinsteinNode declarationNode = parentNode.findDeclarationNode(name);
        //TODO: Cleanup and add a proper builder.
        if (declarationNode instanceof ResourceDeclerationNode) {
            DynamicResourceReferenceNode dynamicResourceReferenceNode = new DynamicResourceReferenceNode(node, name, ((ResourceDeclerationNode) declarationNode).getURI());
            parentNode.addChild(dynamicResourceReferenceNode);
            log.debug("Dynamic ref {0} ", name);
            if (parentNode instanceof InstructionNode || parentNode instanceof OperatorNode) {
                InstructionAwareNode awareNode = (InstructionAwareNode) parentNode;
                int pos= awareNode.getResourceRefPosition(dynamicResourceReferenceNode);
                Class<? extends Facade>[] facades = awareNode.getInstructionClass().getAnnotation(RegisterInstruction.class).resources()[pos].facadesUsed();
                for (Class<? extends Facade> facade : facades) {
                    declarationNode.addChild(new FacadeNode(node, facade));
                }
            }
            yieldToChildren(node, parentNode);
            return dynamicResourceReferenceNode;
        }
        //TODO:FIXME: Proper error handling with i18n
        throw new ParseException(node, "Unrecognized identifier " + name);

    }


    public Object visit(ASTDialectTernaryExpression node, Object o) {
        return TERNARY_EXPRESSION_BUILDER.build(this, node, (EinsteinNode) o);
    }

    public Object visit(ASTDialectOperatorOnlyExpression node, Object o) {
        return OPERATOR_ONLY_EXPRESSION_BUILDER.build(this, node, (EinsteinNode) o);
    }

    public Object visit(ASTLabelledDialectExpression node, Object o) {
        //Note we make sure that all expressions are part of a flow group to make sure that operators work correctly.
        if (node.getFirstChild() instanceof ASTDialectIdentifier && node.getNumberOfChildren() == 2 && node.getFirstToken().next.image.equals(":")) {
            return LABELLED_EXPRESSION_BUILDER.build(this, node, (EinsteinNode) o);
        } else {
            if (!(o instanceof FlowGroupNode)) {
                return FLOW_GROUP_BUILDER.build(this, node, (EinsteinNode) o);
            } else {
                return super.visit(node, o);
            }
        }
    }


    public Object visit(ASTDialectExpressionElement node, Object o) {
        if (node.getFirstChild() != null && node.getFirstChild() instanceof ASTDialectIdentifier) {
            Object instructionObject = EinsteinRegistryFactory.getInstance().get(INSTRUCTION_REGISTRY_PREFIX + node.getFirstTokenImage());
            if (node.getFirstTokenImage().equals("resource")) {
                return RESOURCE_DECLERATION_BUILDER.build(this, node, (EinsteinNode) o);
            } else if (instructionObject != null) {
                return INSTRUCTION_BUILDER.build(this, node, (EinsteinNode) o);
            }

        } else
        if (o instanceof GroupNode && node.getFirstChild() != null && node.getFirstChild().getFirstChild() instanceof ASTIntegerLiteral) {
            return TUPLE_POSITION_REFERENCE_BUILDER.build(this, node, (EinsteinNode) o);
        }
        return super.visit(node, o);
    }

    public Object visit(ASTDialectBracketedExpression node, Object o) {
        return TUPLE_GROUP_BUILDER.build(this, node, (EinsteinNode) o);
    }

    public Object visit(ASTDialectParenthesisExpression node, Object o) {
        ASTNode childNode = node.getFirstChild();
        if (childNode.getFirstChild() instanceof ASTDialectIdentifier && childNode.getNumberOfChildren() == 2 && childNode.getFirstToken().next.image.equals(":")) {
            //So the child is actually a labelled expression, which means this is a name value block
            return META_DATA_BUILDER.build(this, node, (EinsteinNode) o);
        } else {
            return UN_SEQUENCED_GROUP_BUILDER.build(this, node, (EinsteinNode) o);

        }
    }

    public Object visit(ASTTemplateString node, Object o) {
        return STATIC_RESOURCE_REF_BUILDER.build(this, node, (EinsteinNode) o);
    }


    public void yieldToChildren(ASTNode astNode, EinsteinNode parentNode) {
        ASTNode[] children = astNode.getChildren();
        if (children != null) {
            for (ASTNode child : children) {
                child.jjtAccept(this, parentNode);
            }
        }
    }


    public void yield
            (ASTNode
                    node, EinsteinNode
                    parentNode) {

        node.jjtAccept(this, parentNode);
    }


    public boolean lazyLoading
            () {
        return lazyLoading;
    }
}
