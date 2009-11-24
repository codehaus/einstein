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

package org.cauldron.einstein.ri;

import org.apache.commons.cli.Options;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
class CompilerOptionsFactory {

    public static Options getOptions() {
        Options options = new Options();
        options.addOption("cp", "classpath", true, "Specify where to find user class files");
        options.addOption("sp", "sourcepath", true, "Specify where to find input source files");
        options.addOption("d", "directory", true, "Specify where to place generated class files");
        options.addOption("t", "temppath", true, "Specify where to place temporary intermediate files");
        options.addOption("T", "notemp", false, "Do not create temporary intermediate files");
        options.addOption("C", "noclass", false, "Do not compile to class files");
        options.addOption("ph", "provider-help", true, "Provides information on how to use a given provider e.g. -ph text .");
        options.addOption("ih", "instruction-help", true, "Provides information on how to use a given provider e.g. -ph text .");
        options.addOption("lp", "list-providers", false, "Lists all available providers.");
        options.addOption("li", "list-instructions", false, "Lists all available providers.");
        options.addOption("X", "stacktrace", true, "Stacktraces are displayed when errors occur.");
        return options;
    }


}
