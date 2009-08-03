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


package org.cauldron.einstein.ri.parser.exception;

import deesel.parser.ASTNode;
import deesel.parser.ParseException;
import mazz.i18n.Msg;

/**
 * @author Neil Ellis
 */

@org.contract4j5.contract.Contract
public class EinsteinParseException extends ParseException {

    private EinsteinParseException() {
    }

    public EinsteinParseException(Throwable e, ASTNode node, Msg message) {
        super(e, node, message.toString());
    }

    public EinsteinParseException(Throwable e, String bundle, ASTNode node, String message, Object... args) {
        super(e, node, Msg.createMsg(new Msg.BundleBaseName(bundle), message, args).toString());
    }

    public EinsteinParseException(String bundle, ASTNode node, String message, Object... args) {
        super(node, Msg.createMsg(new Msg.BundleBaseName(bundle), message, args).toString());
    }

    public EinsteinParseException(ASTNode node, Msg message) {
        super(node, message.toString());
    }
}
