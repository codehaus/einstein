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
import deesel.parser.com.COMNodeValidator;
import deesel.parser.com.exceptions.COMValidationException;
import deesel.parser.com.nodes.*;
import deesel.parser.com.util.ClassDefFactory;
import org.cauldron.einstein.api.assembly.instruction.Instruction;
import org.cauldron.einstein.api.assembly.instruction.annotation.RegisterInstruction;
import org.cauldron.einstein.ri.core.log.Logger;
import static org.cauldron.einstein.ri.parser.ParserModuleMessages.BUNDLE_NAME;
import static org.cauldron.einstein.ri.parser.ParserModuleMessages.QUALIFIERS_NOT_SUPPORTED;
import org.cauldron.einstein.ri.parser.debug.DebugInfoHelper;
import org.cauldron.einstein.ri.parser.exception.EinsteinParseException;
import org.cauldron.einstein.ri.parser.exception.InvalidInstructionSyntaxParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class InstructionNode extends AbstractEinsteinNode implements ExpressionNode, InstructionAwareNode {
    private static final Logger log = Logger.getLogger(InstructionNode.class);

    private final Class<? extends Instruction> instructionClass;
    private final List<ExpressionNode> arguments = new ArrayList<ExpressionNode>();
    private final String instructionName;
    private final List<String> supportedQualifiers;
    private final List<QualifierNode> suppliedQualifiers = new ArrayList<QualifierNode>();

    public InstructionNode(ASTNode astNode, String instructionName, Class<? extends Instruction> instructionClass) {
        super(astNode);
        this.instructionName = instructionName;
        this.instructionClass = instructionClass;
        supportedQualifiers = Arrays.asList(instructionClass.getAnnotation(RegisterInstruction.class).qualifiers());
    }

    public COMNode toCOMNode(COMNode parent) {

        PrimaryExpression primaryExpression = new PrimaryExpressionNode(astNode);
        final CompiledClass instructionClass = new CompiledClass(this.instructionClass);
        final ConstructorCall constructorCall = new ConstructorCall(astNode, instructionClass);
        parent.insert(primaryExpression).insert(constructorCall);
        constructorCall.insert(DebugInfoHelper.getDebugCOMNodeFor(astNode));

        for (ExpressionNode expressionNode : arguments) {
            expressionNode.toCOMNode(constructorCall);

        }
        if (supportedQualifiers.size() == 0 && suppliedQualifiers.size() > 0) {
            throw new EinsteinParseException(BUNDLE_NAME, astNode, QUALIFIERS_NOT_SUPPORTED, suppliedQualifiers);
        }

        if (supportedQualifiers.size() > 0) {
            log.debug("[{0}]: {1} supports qualifiers {2} and has qualifiers {3} supplied.", astNode, instructionName, supportedQualifiers, suppliedQualifiers);
            ArrayAllocationExpression array = constructorCall.insert(new ArrayAllocationExpression(astNode));
            array.addChild(ClassDefFactory.STRING);
            ArrayInitializer initializer = array.insert(new ArrayInitializer(astNode));

            for (QualifierNode qualifier : suppliedQualifiers) {
                log.debug("Converting qualifier {0}.", qualifier.getQualifier());
                qualifier.toCOMNode(initializer);
            }

        }
        constructorCall.addCustomValidation(new COMNodeValidator() {
            public void validate(COMNode comNode) throws COMValidationException {
                DeeselClass[] classes = ((ConstructorCall) comNode).getParameterTypes();
                try {
                    instructionClass.getConstructor(classes);
                } catch (NoSuchMethodException e) {
                    throw new InvalidInstructionSyntaxParseException(astNode, instructionName, classes);
                }
            }
        });
        constructorCall.resolve();


        return primaryExpression;
    }


    @SuppressWarnings({"UnusedDeclaration"})
    public void addChildInternal(ExpressionNode node) {
        log.debug("addChild expression {0}.", node);
        arguments.add(node);

    }

    @SuppressWarnings({"UnusedDeclaration"})
    public void addChildInternal(QualifierNode node) {
        log.debug("addChild qualifier {0}.", node);
        suppliedQualifiers.add(node);

    }


    public boolean isQualifierSupported(String name) {
        return supportedQualifiers.contains(name);
    }

    public String getInstructionName() {
        return instructionName;
    }

    public Class<? extends Instruction> getInstructionClass() {
        return instructionClass;
    }

    @SuppressWarnings({"SuspiciousMethodCalls"})
    public int getResourceRefPosition(EinsteinNode node) {
        return arguments.indexOf(node);
    }
}
