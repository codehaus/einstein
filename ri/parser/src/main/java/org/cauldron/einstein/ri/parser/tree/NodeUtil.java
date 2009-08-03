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
import deesel.parser.com.util.ClassDefFactory;
import deesel.parser.com.nodes.ArrayAllocationExpression;
import deesel.parser.com.nodes.ArrayInitializer;
import deesel.parser.ASTNode;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.cauldron.einstein.api.common.euri.EinsteinURI;
import org.cauldron.einstein.api.provider.ResourceProvider;
import org.cauldron.einstein.api.provider.ProviderMetaData;
import org.cauldron.einstein.api.provider.annotation.RegisterProvider;
import org.cauldron.einstein.api.model.resource.Facade;
import org.cauldron.einstein.ri.core.euri.EinsteinURIFactory;
import org.cauldron.einstein.ri.core.providers.factory.ProviderFactory;
import org.cauldron.einstein.ri.parser.exception.EinsteinParseException;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
class NodeUtil {
    static void addFacadeList(COMNode constructorCall, ASTNode astNode, String uri, List<FacadeNode> facades) {
        ArrayAllocationExpression array = constructorCall.insert(new ArrayAllocationExpression(astNode));
        array.addChild(ClassDefFactory.CLASS);


        ArrayInitializer initializer = array.insert(new ArrayInitializer(astNode));
        EinsteinURI einsteinURI = EinsteinURIFactory.createURI(uri);
        //Now we need to make sure the declared resource knows about the facades that will be used.
        ResourceProvider registeredProvider = ProviderFactory.getInstance().getRegisteredProvider(einsteinURI.getProviderName());
        ProviderMetaData metaData = registeredProvider.getClass().getAnnotation(RegisterProvider.class).metadata();
        List<Class<? extends Facade>> supportedFacades= new ArrayList<Class<? extends Facade>>();
        supportedFacades.addAll(Arrays.asList(metaData.optionallySupported()));
        supportedFacades.addAll(Arrays.asList(metaData.alwaysSupported()));
        boolean supportsAll = metaData.supportsAll();
        boolean optionallySupportsAll = metaData.optionallySupportsAll();
        for (FacadeNode facadeNode : facades) {
            if(!supportsAll && !optionallySupportsAll) {
                if(!supportedFacades.contains(facadeNode.getFacadeClass())) {
                    throw new EinsteinParseException(org.cauldron.einstein.ri.parser.ParserModuleMessages.BUNDLE_NAME, facadeNode.getASTNode(), org.cauldron.einstein.ri.parser.ParserModuleMessages.FACADE_NOT_SUPPORTED, metaData.core().name(), facadeNode.getFacadeClass());
                }
            }
            facadeNode.toCOMNode(initializer);
        }
    }
}
