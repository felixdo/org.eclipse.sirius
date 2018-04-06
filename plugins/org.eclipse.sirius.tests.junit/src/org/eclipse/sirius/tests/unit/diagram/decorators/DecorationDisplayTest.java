/*******************************************************************************
 * Copyright (c) 2017 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.tests.unit.diagram.decorators;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DecorationEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecorator;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.DEdge;
import org.eclipse.sirius.diagram.ui.tools.api.editor.DDiagramEditor;
import org.eclipse.sirius.diagram.ui.tools.api.preferences.SiriusDiagramUiPreferencesKeys;
import org.eclipse.sirius.diagram.ui.tools.internal.decoration.SiriusGenericDecorator;
import org.eclipse.sirius.tests.SiriusTestsPlugin;
import org.eclipse.sirius.tests.sample.component.Component;
import org.eclipse.sirius.tests.support.api.TestsUtil;
import org.eclipse.sirius.tests.unit.diagram.GenericTestCase;
import org.eclipse.sirius.ui.business.api.dialect.DialectUIManager;
import org.eclipse.sirius.viewpoint.description.Position;

import com.google.common.collect.Lists;

/**
 * Test display of decoration via Designer Decorations.
 * 
 * @author lfasani
 */
public class DecorationDisplayTest extends GenericTestCase {

    private static final String PATH = "/data/unit/decorators/display/";

    private static final String AIRD_NAME = "representations.aird";

    private static final String SEMANTIC_NAME = "decorationsTest.component";

    private DDiagram diagram;

    private DDiagramEditor editor;

    private static final String CLASS_DIAGRAM_WITH_DECORAIONS = "DiagramWithDecoration";

    private static final String CLASS_DIAGRAM = "Diagram";

    private static Method getDecorators;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        TestsUtil.emptyEventsFromUIThread();
        copyFilesToTestProject(SiriusTestsPlugin.PLUGIN_ID, PATH, SEMANTIC_NAME, AIRD_NAME);
        genericSetUp(Collections.<String> singleton(TEMPORARY_PROJECT_NAME + "/" + SEMANTIC_NAME), Collections.<String> emptyList(), TEMPORARY_PROJECT_NAME + "/" + AIRD_NAME);
        diagram = (DDiagram) getRepresentations(CLASS_DIAGRAM_WITH_DECORAIONS, semanticModel).iterator().next();

        changeDiagramUIPreference(SiriusDiagramUiPreferencesKeys.PREF_AUTHORIZE_DECORATION_OVERLAP.name(), false);
        editor = (DDiagramEditor) DialectUIManager.INSTANCE.openEditor(session, diagram, new NullProgressMonitor());
        TestsUtil.synchronizationWithUIThread();

        getDecorators = DecorationEditPolicy.class.getDeclaredMethod("getDecorators");
        getDecorators.setAccessible(true);
    }

    /**
     * Checks the general display of the decorations.
     */
    public void testDecorationDisplay() {
        List<DDiagramElement> ownedDiagramElements = diagram.getOwnedDiagramElements();
        DDiagramElement decoDisplayDiagElem = getDiagElement(ownedDiagramElements, "decoDisplay");

        // test display in group
        List<Figure> dDiagramElementDecorationFigures = getDDiagramElementDecorationFigures(decoDisplayDiagElem, editor);
        assertEquals("There should be 3 decoration groups", 3, dDiagramElementDecorationFigures.size());

        checkGroupBoundingBox(dDiagramElementDecorationFigures, Position.NORTH_WEST_LITERAL, new Rectangle(326, 206, 49, 49),
                Lists.newArrayList(new Rectangle(326, 206, 16, 16), new Rectangle(343, 206, 32, 32), new Rectangle(326, 223, 32, 32)), false);

        checkGroupBoundingBox(dDiagramElementDecorationFigures, Position.EAST_LITERAL, new Rectangle(505, 265, 10, 10), Lists.newArrayList(new Rectangle(505, 265, 10, 10)), false);

        checkGroupBoundingBox(dDiagramElementDecorationFigures, Position.SOUTH_EAST_LITERAL, new Rectangle(454, 274, 61, 61),
                Lists.newArrayList(new Rectangle(505, 325, 10, 10), new Rectangle(488, 275, 16, 60), new Rectangle(455, 308, 60, 16), new Rectangle(471, 319, 16, 16)), false);

        // test decoration tooltip
        checkDecorationStringTooltip(dDiagramElementDecorationFigures, Position.NORTH_WEST_LITERAL, new Rectangle(326, 206, 49, 49), "NW_1");
    }

    /**
     * Check that no decoration figure is created when there is no decoration.
     */
    public void testNoDecoration() {
        DDiagram diagram = (DDiagram) getRepresentations(CLASS_DIAGRAM, semanticModel).iterator().next();

        changeDiagramUIPreference(SiriusDiagramUiPreferencesKeys.PREF_AUTHORIZE_DECORATION_OVERLAP.name(), false);
        DDiagramEditor editor = (DDiagramEditor) DialectUIManager.INSTANCE.openEditor(session, diagram, new NullProgressMonitor());
        TestsUtil.synchronizationWithUIThread();

        List<DDiagramElement> ownedDiagramElements = diagram.getOwnedDiagramElements();
        DDiagramElement decoDisplayDiagElem = getDiagElement(ownedDiagramElements, "decoDisplay");

        // check
        assertTrue("There should be no decoration figure because there is no decoration", getDDiagramElementDecorationFigures(decoDisplayDiagElem, editor).isEmpty());

        DialectUIManager.INSTANCE.closeEditor(editor, false);
        TestsUtil.synchronizationWithUIThread();
    }

    /**
     * Checks the first tooltip in the decoration group. Checks that the label
     * is correct
     */
    /**
     * @param dDiagramElementDecorationFigures
     *            decoration figures of the DDiagramElement
     * @param position
     *            position of the decoration group
     * @param expectedGroupBounds
     *            the bounds to find the group
     * @param tooltipLabel
     *            the expected tooltip label
     */
    private void checkDecorationStringTooltip(List<Figure> dDiagramElementDecorationFigures, Position position, Rectangle expectedGroupBounds, String tooltipLabel) {
        Figure groupFigure = getGroupFigure(dDiagramElementDecorationFigures, position, expectedGroupBounds);
        List<Figure> children = ((Figure) groupFigure.getChildren().get(0)).getChildren();
        Figure figure = children.get(0);
        assertTrue("Decoration tooltip figure should be an instance of org.eclipse.draw2d.Label in " + position.getName() + " group", figure.getToolTip() instanceof Label);
        assertEquals("Bad decoration tooltip in " + position.getName() + " group", tooltipLabel, ((Label) figure.getToolTip()).getText());
    }

    /**
     * Checks the group decorations. Checks that the given group and its
     * decorations are correctly positioned
     * 
     * @param dDiagramElementDecorationFigures
     *            decoration figures of the dDiagramElement
     * @param position
     *            position of the decoration group
     * @param expectedGroupBounds
     *            must corresponds to one of dDiagramElementDecorationFigures's
     *            bounding box
     * @param expectedDecosBounds
     *            expected bounding box inside the group
     * @param isListDecorator
     *            if false, checks the decorations position in the group
     */
    private void checkGroupBoundingBox(List<Figure> dDiagramElementDecorationFigures, Position position, Rectangle expectedGroupBounds, List<Rectangle> expectedDecosBounds, boolean isListDecorator) {
        Figure groupFigure = getGroupFigure(dDiagramElementDecorationFigures, position, expectedGroupBounds);

        List<Figure> children = ((Figure) groupFigure.getChildren().get(0)).getChildren();
        assertTrue("The decorations at " + position + " should " + (isListDecorator ? "" : "NOT ") + "be merged into a list decorator",
                isListDecorator == children.get(0).getClass().getName().contains("ListDecorationFigure"));

        // check the content of group decorator
        if (!isListDecorator) {
            assertEquals("Bad decoration number in " + position.getName(), expectedDecosBounds.size(), children.size());
            for (Figure figure : children) {
                assertTrue("Bad decoration position in " + position.getName() + "group", expectedDecosBounds.contains(figure.getBounds()));
            }
        }
    }

    /**
     * Gets the group figure corresponding to the expectedGroupBounds bounds.
     */
    private Figure getGroupFigure(List<Figure> dDiagramElementDecorationFigures, Position position, Rectangle expectedGroupBounds) {
        Figure groupFigure = null;
        for (Figure currentGroupFigure : dDiagramElementDecorationFigures) {
            if (currentGroupFigure.getBounds().equals(expectedGroupBounds)) {
                groupFigure = currentGroupFigure;
                break;
            }
        }
        assertNotNull("Bad group decoration position at " + position.getName(), groupFigure);
        return groupFigure;
    }

    /**
     * Gets the {@link DDiagramElement} with the given name among
     * ownedDiagramElements.
     */
    private DDiagramElement getDiagElement(List<DDiagramElement> ownedDiagramElements, String diagElemeName) {
        for (DDiagramElement dDiagramElement : ownedDiagramElements) {
            if (diagElemeName.equals(dDiagramElement.getName())) {
                return dDiagramElement;
            }
        }
        fail(THE_UNIT_TEST_DATA_SEEMS_INCORRECT + ": the DDiagramElement \"diagElemeName\" has not been found.");
        return null;
    }

    /**
     * Check that decorations are merged in a group if they overlap another
     * group
     */
    public void testDecorationMergeInsideAGroup() {
        List<DDiagramElement> ownedDiagramElements = diagram.getOwnedDiagramElements();
        DDiagramElement decoDisplayDiagElem = getDiagElement(ownedDiagramElements, "groupDecoMerge1");

        // test display in group
        List<Figure> dDiagramElementDecorationFigures = getDDiagramElementDecorationFigures(decoDisplayDiagElem, editor);
        assertEquals("There should be 3 decoration groups", 3, dDiagramElementDecorationFigures.size());

        // NORTH_WEST decorations are normally displayed
        checkGroupBoundingBox(dDiagramElementDecorationFigures, Position.NORTH_WEST_LITERAL, new Rectangle(518, 56, 49, 49),
                Lists.newArrayList(new Rectangle(518, 56, 16, 16), new Rectangle(535, 56, 32, 32), new Rectangle(518, 73, 32, 32)), false);

        // EAST decorations should NOT be a list decorator because it is smaller
        // than the list decorator size. A group containing one decoration that
        // is smaller than the list decorator can be changed into a list
        // decorator.
        checkGroupBoundingBox(dDiagramElementDecorationFigures, Position.EAST_LITERAL, new Rectangle(652, 112, 10, 10), Lists.newArrayList(new Rectangle(652, 112, 10, 10)), false);

        // SOUTH_EAST should be a list decorator
        checkGroupBoundingBox(dDiagramElementDecorationFigures, Position.SOUTH_EAST_LITERAL, new Rectangle(646, 163, 16, 16), new ArrayList<>(), true);

        // test decoration tooltip
        Figure groupFigure = getGroupFigure(dDiagramElementDecorationFigures, Position.SOUTH_EAST_LITERAL, new Rectangle(646, 163, 16, 16));
        Figure tooltip = (Figure) ((Figure) (((Figure) groupFigure.getChildren().get(0)).getChildren().get(0))).getToolTip();
        assertTrue("Decoration tooltip figure should be an instance of org.eclipse.draw2d.Label in " + Position.SOUTH_EAST_LITERAL + " group", tooltip instanceof Figure);
        List<Figure> children = tooltip.getChildren();
        assertEquals("The number of figure contained in the " + Position.SOUTH_EAST_LITERAL + " list decorator tooltip is incorrect", 8, children.size());

    }

    /**
     * Check that decorations are merged if they overlap the diagram element.
     */
    public void testDecorationMergeInsideAGroupForTooSmallDDiagElementFigure() {
        List<DDiagramElement> ownedDiagramElements = diagram.getOwnedDiagramElements();
        DDiagramElement decoDisplayDiagElem = getDiagElement(ownedDiagramElements, "groupDecoMerge2");

        // test display in group
        List<Figure> dDiagramElementDecorationFigures = getDDiagramElementDecorationFigures(decoDisplayDiagElem, editor);
        assertEquals("There should be 3 decoration groups", 3, dDiagramElementDecorationFigures.size());

        // check that decoration don't overlap the diagram element
        // NORTH_WEST should be a list decorator because it overlaps the diagram
        // element
        checkGroupBoundingBox(dDiagramElementDecorationFigures, Position.NORTH_WEST_LITERAL, new Rectangle(290, 8, 16, 16), new ArrayList<>(), true);

        // EAST should NOT be a list decorator because it is smaller than the
        // list decorator size
        checkGroupBoundingBox(dDiagramElementDecorationFigures, Position.EAST_LITERAL, new Rectangle(424, 25, 10, 10), Lists.newArrayList(new Rectangle(424, 25, 10, 10)), false);

        // SOUTH_EAST should be a list decorator
        checkGroupBoundingBox(dDiagramElementDecorationFigures, Position.SOUTH_EAST_LITERAL, new Rectangle(418, 37, 16, 16), new ArrayList<>(), true);
    }

    /**
     * Check that groups are merged if they overlap each other
     */
    public void testGroupDecorationMerge() {
        List<DDiagramElement> ownedDiagramElements = diagram.getOwnedDiagramElements();
        DDiagramElement decoDisplayDiagElem = getDiagElement(ownedDiagramElements, "listDecoMerge1");

        // test display in group
        List<Figure> dDiagramElementDecorationFigures = getDDiagramElementDecorationFigures(decoDisplayDiagElem, editor);
        assertEquals("There should be 2 decoration groups", 2, dDiagramElementDecorationFigures.size());

        // NORTH_WEST should be a list decorator
        checkGroupBoundingBox(dDiagramElementDecorationFigures, Position.NORTH_WEST_LITERAL, new Rectangle(92, 32, 16, 16), new ArrayList<>(), true);

        // There should be a list decorator at SOUTH_EAST representing
        // SOUTH_EAST and EAST
        checkGroupBoundingBox(dDiagramElementDecorationFigures, Position.EAST_LITERAL, new Rectangle(220, 40, 16, 16), new ArrayList<>(), true);

        /// =========================

        decoDisplayDiagElem = getDiagElement(ownedDiagramElements, "listDecoMerge2");

        // test display in group
        dDiagramElementDecorationFigures = getDDiagramElementDecorationFigures(decoDisplayDiagElem, editor);
        assertEquals("There should be 1 decoration group", 1, dDiagramElementDecorationFigures.size());

        // All decorators should be merged in SOUTH_EAST list decorator
        checkGroupBoundingBox(dDiagramElementDecorationFigures, Position.EAST_LITERAL, new Rectangle(155, 103, 16, 16), new ArrayList<>(), true);

    }

    /**
     * Check that decorations are correctly displayed when the image or the
     * figure is provided by a service
     */
    public void testDecorationImageAndTooltipProvidedByService() {
        List<DDiagramElement> ownedDiagramElements = diagram.getOwnedDiagramElements();
        DDiagramElement decoDisplayDiagElem = getDiagElement(ownedDiagramElements, "Root");

        List<Figure> dDiagramElementDecorationFigures = getDDiagramElementDecorationFigures(decoDisplayDiagElem, editor);

        // There should be a single composite decoration at NORTH
        // NORTH should display the composite figure created in
        // org.eclipse.sirius.tests.sample.component.service.ComponentServices.getDecorationFigure(EObject)
        checkGroupBoundingBox(dDiagramElementDecorationFigures, Position.NORTH_LITERAL, new Rectangle(153, 182, 36, 20), Lists.newArrayList(new Rectangle(153, 182, 36, 20)), false);

        // test decoration tooltip
        checkDecorationStringTooltip(dDiagramElementDecorationFigures, Position.NORTH_LITERAL, new Rectangle(153, 182, 36, 20), "Tooltip_Root");

        // There should be a single decoration at CENTER whose image is provided
        // by DecorationServices.getDecorationImage
        checkGroupBoundingBox(dDiagramElementDecorationFigures, Position.CENTER_LITERAL, new Rectangle(166, 232, 11, 11), Lists.newArrayList(new Rectangle(166, 232, 11, 11)), false);

        // test decoration tooltip
        checkDecorationStringTooltip(dDiagramElementDecorationFigures, Position.CENTER_LITERAL, new Rectangle(166, 232, 11, 11), "Tooltip_Root");

        session.getTransactionalEditingDomain().getCommandStack().execute(new RecordingCommand(session.getTransactionalEditingDomain()) {
            @Override
            protected void doExecute() {
                ((Component) decoDisplayDiagElem.getTarget()).setName("Root_2");
            }
        });

        // After update NORTH decoration should be "Tooltip_Root_2" with the
        // image and tooltip should be updated with the name
        ownedDiagramElements = diagram.getOwnedDiagramElements();
        dDiagramElementDecorationFigures = getDDiagramElementDecorationFigures(getDiagElement(ownedDiagramElements, "Root_2"), editor);
        checkGroupBoundingBox(dDiagramElementDecorationFigures, Position.NORTH_LITERAL, new Rectangle(0, 0, 44, 20), Lists.newArrayList(new Rectangle(0, 0, 44, 20)), false);
        checkDecorationStringTooltip(dDiagramElementDecorationFigures, Position.NORTH_LITERAL, new Rectangle(0, 0, 44, 20), "Tooltip_Root_2");
    }

    /**
     * Check that decorations are displayed correctly on edge
     */
    public void testDecorationOnEdge() {
        List<DDiagramElement> ownedDiagramElements = diagram.getOwnedDiagramElements();
        DDiagramElement decoDisplayDiagElem = ownedDiagramElements.stream().filter(v -> v instanceof DEdge).iterator().next();

        // test display in group
        List<Figure> dDiagramElementDecorationFigures = getDDiagramElementDecorationFigures(decoDisplayDiagElem, editor);
        assertEquals("There should be 1 decoration group", 1, dDiagramElementDecorationFigures.size());

        // decorations are normally displayed at CENTER
        checkGroupBoundingBox(dDiagramElementDecorationFigures, Position.CENTER_LITERAL, new Rectangle(350, 97, 82, 32),
                Lists.newArrayList(new Rectangle(350, 97, 16, 16), new Rectangle(367, 97, 32, 32), new Rectangle(400, 97, 32, 32)), false);
    }

    private List<Figure> getDDiagramElementDecorationFigures(DDiagramElement diagElement, DDiagramEditor editor) {
        List<Figure> decorationFigures = new ArrayList<>();
        IGraphicalEditPart editPart = getEditPart(diagElement, editor);
        final DecorationEditPolicy policy = (DecorationEditPolicy) editPart.getEditPolicy(EditPolicyRoles.DECORATION_ROLE);
        final Map<Object, IDecorator> decorators = getDecorators(policy);
        for (IDecorator decorator : decorators.values()) {
            if (decorator instanceof SiriusGenericDecorator) {
                decorationFigures.addAll((Collection<? extends Figure>) ((SiriusGenericDecorator) decorator).getDecorations());
                break;
            }
        }
        return decorationFigures;
    }

    @SuppressWarnings("unchecked")
    private Map<Object, IDecorator> getDecorators(final DecorationEditPolicy policy) {
        try {
            return (Map<Object, IDecorator>) getDecorators.invoke(policy);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        }
        return null;
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
