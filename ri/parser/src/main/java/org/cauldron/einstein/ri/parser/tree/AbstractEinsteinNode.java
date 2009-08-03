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
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.cauldron.einstein.api.assembly.debug.CompilationInformation;
import static org.cauldron.einstein.ri.parser.ParserModuleMessages.*;
import org.cauldron.einstein.ri.parser.exception.EinsteinParseException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.ArrayList;

/**
 * @author Neil Ellis
 */
public abstract class AbstractEinsteinNode implements EinsteinNode {

    private CompilationInformation compilationInformation;
    private EinsteinNode parent;
    private final List<EinsteinNode> allChidren= new ArrayList<EinsteinNode>();

    final ASTNode astNode;

    AbstractEinsteinNode(ASTNode astNode) {
        this.astNode = astNode;
    }


    public CompilationInformation getCompilationInfo() {
        return compilationInformation;
    }

    public EinsteinNode getParent() {
        return parent;
    }

    public final void addChild(EinsteinNode child) {
        Method[] methods = getClass().getMethods();
        Class best = COMNode.class;
        for (Method method : methods) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (method.getName().equals("addChildInternal") && parameterTypes.length == 1 && parameterTypes[0].isAssignableFrom(child.getClass()) && !parameterTypes[0].isAssignableFrom(best)) {
                best = parameterTypes[0];
            }
        }

        if (best.equals(EinsteinNode.class)) {
            throw new EinsteinParseException(BUNDLE_NAME, astNode, INVALID_CHILD_NODE, child.getClass(), getClass());
        }
        try {
            getClass().getMethod("addChildInternal", best).invoke(this, child);
            allChidren.add(child);
            child.setParent(this);

        } catch (IllegalAccessException e) {
            throw new EinsteinParseException(e, BUNDLE_NAME, astNode, ADD_CHILD_SECURITY_FAILURE, child.getClass().getName(), getClass().getName());
        } catch (NoSuchMethodException e) {
            throw new EinsteinParseException(e, BUNDLE_NAME, astNode, ADD_CHILD_FAILURE, child.getClass().getName(), getClass().getName());
        } catch (InvocationTargetException e) {
            throw new EinsteinParseException(e.getTargetException(), BUNDLE_NAME, astNode, CHILD_NODE_FAILURE);
        }

    }


    public void setParent(EinsteinNode parent) {
        this.parent = parent;
    }

    public EinsteinNode findDeclarationNode(String name) {
        if (getParent() != null) {
            return getParent().findDeclarationNode(name);
        } else {
            return null;
        }
    }

    public int getPosition(EinsteinNode node) {
        return allChidren.indexOf(node);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toStringExclude(this, "parent");
    }
}
