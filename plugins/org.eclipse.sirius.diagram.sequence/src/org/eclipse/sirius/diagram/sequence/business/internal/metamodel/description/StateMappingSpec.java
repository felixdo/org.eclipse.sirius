/*******************************************************************************
 * Copyright (c) 2010 THALES GLOBAL SERVICES.
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
package org.eclipse.sirius.diagram.sequence.business.internal.metamodel.description;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.BasicEList.UnmodifiableEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.common.tools.api.interpreter.IInterpreter;
import org.eclipse.sirius.common.tools.api.util.EObjectCouple;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.DNode;
import org.eclipse.sirius.diagram.DNodeListElement;
import org.eclipse.sirius.diagram.business.internal.metamodel.description.extensions.INodeMappingExt;
import org.eclipse.sirius.diagram.business.internal.metamodel.description.operations.AbstractNodeMappingSpecOperations;
import org.eclipse.sirius.diagram.business.internal.metamodel.description.operations.SiriusElementMappingSpecOperations;
import org.eclipse.sirius.diagram.business.internal.metamodel.helper.NodeMappingHelper;
import org.eclipse.sirius.diagram.description.DiagramElementMapping;
import org.eclipse.sirius.diagram.description.NodeMapping;
import org.eclipse.sirius.diagram.sequence.description.impl.StateMappingImpl;
import org.eclipse.sirius.viewpoint.DMappingBased;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;
import org.eclipse.sirius.viewpoint.SiriusPlugin;

/**
 * Implementation of <code>StateMapping</code>.
 * 
 * @author smonnier
 */
public class StateMappingSpec extends StateMappingImpl implements INodeMappingExt {

    private final Map<EObject, EList<DSemanticDecorator>> viewNodesDone = new HashMap<EObject, EList<DSemanticDecorator>>();

    private final Map<EObjectCouple, EList<EObject>> candidatesCache = new WeakHashMap<EObjectCouple, EList<EObject>>();

    @Override
    public Map<EObject, EList<DSemanticDecorator>> getViewNodesDone() {
        return viewNodesDone;
    }

    @Override
    public Map<EObjectCouple, EList<EObject>> getCandidatesCache() {
        return candidatesCache;
    }

    @Override
    public EList<EObject> getNodesCandidates(final EObject semanticOrigin, final EObject container) {
        return NodeMappingHelper.getNodesCandidates(this, semanticOrigin, container);
    }

    @Override
    public EList<EObject> getNodesCandidates(final EObject semanticOrigin, final EObject container, final EObject containerView) {
        return NodeMappingHelper.getNodesCandidates(this, semanticOrigin, container, containerView);
    }

    @Override
    public DNode createNode(final EObject modelElement, final EObject container, final DDiagram diagram) {
        IInterpreter interpreter = SiriusPlugin.getDefault().getInterpreterRegistry().getInterpreter(modelElement);
        return new NodeMappingHelper(interpreter).createNode(this, modelElement, container, diagram);
    }

    @Override
    public void updateNode(final DNode node) {
        IInterpreter interpreter = SiriusPlugin.getDefault().getInterpreterRegistry().getInterpreter(node);
        new NodeMappingHelper(interpreter).updateNode(this, node);
    }

    @Override
    public void updateListElement(final DNodeListElement listElement) {
        IInterpreter interpreter = SiriusPlugin.getDefault().getInterpreterRegistry().getInterpreter(listElement);
        new NodeMappingHelper(interpreter).updateListElement(this, listElement);
    }

    @Override
    public void clearDNodesDone() {
        NodeMappingHelper.clearDNodesDone(this);
    }

    @Override
    public EList<DDiagramElement> findDNodeFromEObject(final EObject object) {
        return NodeMappingHelper.findDNodeFromEObject(this, object);
    }

    /*
     * Here we add the behavior we should inherit from AbstractNodeMapping
     */

    @Override
    public void createBorderingNodes(final EObject modelElement, final DDiagramElement vpElement, final Collection filterSemantic, final DDiagram viewPoint) {
        AbstractNodeMappingSpecOperations.createBorderingNodes(this, modelElement, vpElement, filterSemantic, viewPoint);
    }

    /*
     * Behavior inherited from DiagramElementMapping
     */

    @Override
    public boolean checkPrecondition(final EObject modelElement, final EObject container, final EObject containerView) {
        return SiriusElementMappingSpecOperations.checkPrecondition(this, modelElement, container, containerView);
    }

    @Override
    public EList<DiagramElementMapping> getAllMappings() {
        final BasicEList<DiagramElementMapping> allMappings = new BasicEList<DiagramElementMapping>();
        allMappings.addAll(this.getAllBorderedNodeMappings());
        return new UnmodifiableEList<DiagramElementMapping>(allMappings.size(), allMappings.toArray());
    }

    @Override
    public boolean isFrom(final DMappingBased element) {
        return SiriusElementMappingSpecOperations.isFrom(this, element);
    }

    @Override
    public void addDoneNode(final DSemanticDecorator node) {
        NodeMappingHelper.addDoneNode(this, node);
    }

    @Override
    public String toString() {
        return new StringBuffer(getClass().getName()).append(" ").append(getName()).toString(); //$NON-NLS-1$
    }

    @Override
    public EList<NodeMapping> getAllBorderedNodeMappings() {
        return AbstractNodeMappingSpecOperations.getAllBorderedNodeMappings(this);
    }

}
