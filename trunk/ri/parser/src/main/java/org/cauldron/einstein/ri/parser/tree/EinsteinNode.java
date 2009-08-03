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

import deesel.parser.com.COMNode;
import org.contract4j5.contract.*;

import org.cauldron.einstein.api.assembly.debug.CompilationInformation;

/**
 * @author Neil Ellis
 */
@Contract
public interface EinsteinNode {

    @Pre
    COMNode toCOMNode(@Pre COMNode parent);

    @Pre
    EinsteinNode getParent();

    void addChild(@Pre EinsteinNode child);

    void setParent(@Pre EinsteinNode parent);

    @Pre
    EinsteinNode findDeclarationNode(@Pre String name);

    int getPosition(EinsteinNode node);

    CompilationInformation getCompilationInfo();
}
