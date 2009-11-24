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


package namespaced.einstein.asm;

import deesel.parser.ASTNode;
import deesel.parser.ParseException;
import deesel.parser.com.COMNode;
import deesel.parser.parslet.AbstractParslet;
import deesel.util.logging.Logger;
import org.cauldron.einstein.ri.core.registry.scan.RegisterClasses;
import org.cauldron.einstein.ri.parser.EinsteinParseVisitor;


/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class NamespacedParslet extends AbstractParslet {
    Logger log = Logger.getLogger(NamespacedParslet.class);


    public NamespacedParslet() {
        super();
        RegisterClasses.register();
    }

    protected NamespacedParslet(AbstractParslet parslet) {
        super(parslet);
        RegisterClasses.register();
    }


    public COMNode parse(ASTNode node, COMNode parent) throws ParseException {
        return (COMNode) node.jjtAccept(new EinsteinParseVisitor(this), parent);
    }
}
