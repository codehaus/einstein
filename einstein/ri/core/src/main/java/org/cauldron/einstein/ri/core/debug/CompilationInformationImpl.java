/******************************************************************************
 *                                                                            *
 *                                                                            *
 *     All works are (C) 2008 - Mangala Solutions Ltd and Paremus Ltd.
 *                                                                            *
 *     Jointly liicensed to Mangala Solutions and Paremus under one           *
 *     or more contributor license agreements.  See the NOTICE file           *
 *     distributed with this work for additional information                  *
 *     regarding copyright ownership.  Mangala Solutions and Paremus          *
 *     licenses this file to you under the Apache License, Version            *
 *     2.0 (the "License"); you may not use this file except in               *
 *     compliance with the License.  You may obtain a copy of the             *
 *     License at                                                             *
 *                                                                            *
 *             http://www.apache.org/licenses/LICENSE-2.0                     *
 *                                                                            *
 *     Unless required by applicable law or agreed to in writing,             *
 *     software distributed under the License is distributed on an            *
 *     "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY                 *
 *     KIND, either express or implied.  See the License for the              *
 *     specific language governing permissions and limitations                *
 *     under the License.                                                     *
 *                                                                            *
 ******************************************************************************/

package org.cauldron.einstein.ri.core.debug;

import mazz.i18n.Msg;
import mazz.i18n.annotation.I18NResourceBundle;
import org.cauldron.einstein.api.assembly.debug.CompilationInformation;
import static org.cauldron.einstein.ri.core.CoreModuleMessages.BUNDLE_NAME;
import static org.cauldron.einstein.ri.core.CoreModuleMessages.POSITION_DESCRIPTION;

/**
 * @author Neil Ellis
 */
@I18NResourceBundle(baseName = BUNDLE_NAME)
@org.contract4j5.contract.Contract
public class CompilationInformationImpl implements CompilationInformation {

    private final int line;
    private final int col;
    private final String sourceText;
    private final String fileName;

    public CompilationInformationImpl(int line, int col, String fileName, String sourceText) {
        this.line = line;
        this.col = col;
        this.sourceText = sourceText;
        this.fileName = fileName;
    }


    public int getColumnNumber() {
        return col;
    }


    public String getFileName() {
        return fileName;
    }


    public int getLineNumber() {
        return line;
    }


    public Msg getPositionDescription() {
        return Msg.createMsg(new Msg.BundleBaseName(BUNDLE_NAME),
                             POSITION_DESCRIPTION,
                             line,
                             col,
                             fileName,
                             sourceText);
    }


    public String getSourceText() {
        return sourceText;
    }

    @Override public String toString() {
        return getPositionDescription().toString();
    }
}
