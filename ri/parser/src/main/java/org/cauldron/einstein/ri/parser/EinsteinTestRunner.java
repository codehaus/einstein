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

import junit.framework.TestCase;
import org.cauldron.einstein.api.assembly.group.InstructionGroup;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.message.action.context.ActionCallbackContext;
import org.cauldron.einstein.api.message.action.types.ReadAction;
import org.cauldron.einstein.api.message.data.model.DataObject;
import org.cauldron.einstein.api.message.view.composites.ReadOnlyView;
import org.cauldron.einstein.api.model.profile.Profile;
import org.cauldron.einstein.ri.core.instructions.context.ProfileAwareContext;
import org.cauldron.einstein.ri.core.model.correlation.SimpleCorrelation;
import org.cauldron.einstein.ri.core.model.data.object.ObjectGraphDataModel;
import org.cauldron.einstein.ri.core.model.execution.direct.DirectExecutionModel;
import org.cauldron.einstein.ri.core.model.message.simple.EmptySimpleMessage;
import org.cauldron.einstein.ri.core.model.message.simple.SimpleMessageModel;
import org.cauldron.einstein.ri.core.model.message.simple.SimpleProfile;
import org.cauldron.einstein.ri.core.runtime.EinsteinRIRuntimeFactory;
import org.cauldron.einstein.ri.core.runtime.EinsteinStandaloneRuntime;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class EinsteinTestRunner extends TestCase {
    private final Profile pojoProfile = new SimpleProfile(new ObjectGraphDataModel(), new DirectExecutionModel(), new SimpleMessageModel(), null, null, null, null, null);

    private final ProfileAwareContext context = new ProfileAwareContext(pojoProfile);

    public EinsteinTestRunner(String s) {
        super(s);
        EinsteinStandaloneRuntime runtime = new EinsteinStandaloneRuntime();
        EinsteinRIRuntimeFactory.getInstance().setRuntime(runtime);

    }


    public void assertResultEquals(InstructionGroup group, final String compare) {

        group.init(context);
        group.start(context);
        MessageTuple result = group.execute(context, new EmptySimpleMessage(new SimpleCorrelation(null)));
        result.realiseAsOne().execute(ReadAction.class, new ReadAction() {
            public void handle(ActionCallbackContext<ReadOnlyView, Object> ctx) {
                DataObject payload = ctx.getMessageHistory().getCurrentEntry().getEvent().getNewState().getPayload();
                if (payload == null) {
                    assertEquals(null, compare);
                    return;
                }
                assertEquals(compare, payload.asString());
            }
        });
        group.stop(context);
        group.destroy(context);
    }

    public void assertSuccess(InstructionGroup group) {

        group.init(context);
        group.start(context);
        group.execute(context, new EmptySimpleMessage(new SimpleCorrelation(null)));
        group.stop(context);
        group.destroy(context);
    }

    public void assertEmpty(InstructionGroup group) {
        group.init(context);
        group.start(context);
        MessageTuple result = group.execute(context, new EmptySimpleMessage(new SimpleCorrelation(null)));
        assertTrue("Expected empty tuple.", result.isEmpty());
        group.stop(context);
        group.destroy(context);
    }

    public void assertVoid(InstructionGroup group) {
        group.init(context);
        group.start(context);
        MessageTuple result = group.execute(context, new EmptySimpleMessage(new SimpleCorrelation(null)));
        assertTrue("Expected void.", result.isVoid());
        group.stop(context);
        group.destroy(context);
    }

    @SuppressWarnings({"EmptyCatchBlock"})
    public void assertFailure(InstructionGroup group) {

        group.init(context);
        group.start(context);
        try {
            group.execute(context, new EmptySimpleMessage(new SimpleCorrelation(null)));
            fail("Expected an exception to occur but none did.");
        } catch (Exception e) {

        }
        group.stop(context);
        group.destroy(context);
    }


    public void assertResultEquals(InstructionGroup group, final InstructionGroup group2) {

        group.init(context);
        group.start(context);
        MessageTuple result = group.execute(context, new EmptySimpleMessage(new SimpleCorrelation(null)));
        final Object[] comparables = new Object[]{null, null};
        result.realiseAsOne().execute(ReadAction.class, new ReadAction() {
            public void handle(ActionCallbackContext<ReadOnlyView, Object> ctx) {
                DataObject payload = ctx.getMessageHistory().getCurrentEntry().getEvent().getNewState().getPayload();
                if (payload == null) {
                    return;
                }
                comparables[0] = payload;
            }
        });
        group.stop(context);
        group.destroy(context);
        group2.init(context);
        group2.start(context);
        MessageTuple result2 = group2.execute(context, new EmptySimpleMessage(new SimpleCorrelation(null)));
        result2.realiseAsOne().execute(ReadAction.class, new ReadAction() {
            public void handle(ActionCallbackContext<ReadOnlyView, Object> ctx) {
                DataObject payload = ctx.getMessageHistory().getCurrentEntry().getEvent().getNewState().getPayload();
                if (payload == null) {
                    return;
                }
                comparables[1] = payload;
            }
        });
        group2.stop(context);
        group2.destroy(context);
        assertEquals(comparables[0], comparables[1]);
    }
}
