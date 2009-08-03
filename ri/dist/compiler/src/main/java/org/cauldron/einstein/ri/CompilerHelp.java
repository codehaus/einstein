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

import org.apache.commons.cli.HelpFormatter;
import org.cauldron.einstein.api.assembly.instruction.InstructionMetaData;
import org.cauldron.einstein.api.model.resource.Facade;
import org.cauldron.einstein.api.provider.ProviderMetaData;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
class CompilerHelp {

    static void displayProviderInfo(ProviderMetaData metadata) {
        System.out.println("");
        System.out.printf("Provider '%s'\n", metadata.core().name());
        System.out.println("");
        System.out.println("Description");
        System.out.println("-----------");
        System.out.println("");
        System.out.printf("  %s\n", metadata.core().description());
        System.out.println("");
        System.out.println("Syntax");
        System.out.println("------");
        System.out.println("");
        System.out.printf("  %s\n", metadata.core().syntax());
        System.out.println("");
        System.out.println("Example");
        System.out.println("-------");
        System.out.println("");
        System.out.printf("  %s\n", metadata.core().example());
        System.out.println("");
        System.out.println("Facades");
        System.out.println("-------");
        System.out.println("");
        if (metadata.supportsAll()) {
            System.out.println("This provider supports all Facades.");
        } else {
            Class<? extends Facade>[] always = metadata.alwaysSupported();
            if (always.length == 0) {
                System.out.println("No Facades are guaranteed to be implemented by this provider.");
            } else {
                System.out.println("These Facades will always be honored by the provider:");
                System.out.println("");
                for (Class<? extends Facade> alwaysClass : always) {
                    System.out.println(alwaysClass.getName());

                }
            }
            if (metadata.optionallySupportsAll()) {
                System.out.println("");
                System.out.println("This provider can support all Facades, but it depends on the URI.");
            } else {
                Class<? extends Facade>[] optionally = metadata.optionallySupported();
                if (optionally.length == 0) {
                } else {
                    System.out.println("These Facades may also be honored by the provider, depending on the URI:");
                    System.out.println("");
                    for (Class<? extends Facade> alwaysClass : always) {
                        System.out.println(alwaysClass.getName());

                    }
                }

            }
        }
        System.out.println("");
    }


    static void displayInstructionInfo(InstructionMetaData metadata) {
        System.out.println("");
        System.out.printf("Instruction '%s'\n", metadata.core().name());
        System.out.println("");
        System.out.println("Description");
        System.out.println("-----------");
        System.out.println("");
        System.out.printf("  %s\n", metadata.core().description());
        System.out.println("");
        System.out.println("Syntax");
        System.out.println("------");
        System.out.println("");
        System.out.printf("  %s\n", metadata.core().syntax());
        System.out.println("");
        System.out.println("Example");
        System.out.println("-------");
        System.out.println("");
        System.out.printf("  %s\n", metadata.core().example());
        System.out.println("");
        String[] qualifiers = metadata.qualifiers();
        if (qualifiers.length != 0) {
            System.out.println("Qualifier Help");
            System.out.println("--------------");
            System.out.println("");
            for (String qualifier : qualifiers) {
                System.out.printf("%-80s\n", qualifier);
            }
        }
        System.out.println("");
        System.out.println("Result");
        System.out.println("-------");
        System.out.println("");
        System.out.printf("%s\n", metadata.result());
        System.out.println("");
        System.out.println("");

    }

    static void displayOptions() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("ec", CompilerOptionsFactory.getOptions());
    }
}
