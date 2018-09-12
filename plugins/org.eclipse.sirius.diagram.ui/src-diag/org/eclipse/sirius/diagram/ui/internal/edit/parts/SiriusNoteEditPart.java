/*******************************************************************************
 * Copyright (c) 2015, 2018 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *    Felix Dorner <felix.dorner@gmail.com> - Bug 533002
 *******************************************************************************/
package org.eclipse.sirius.diagram.ui.internal.edit.parts;

import java.util.List;
import java.util.Optional;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.Request;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DescriptionCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.NoteEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.DiagramNameCompartmentEditPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DiagramPackage;
import org.eclipse.sirius.diagram.ui.business.api.query.ViewQuery;
import org.eclipse.sirius.diagram.ui.business.internal.view.ShowingViewUtil;
import org.eclipse.sirius.diagram.ui.graphical.edit.policies.OpenDRepresentationEditPolicy;
import org.eclipse.sirius.diagram.ui.tools.internal.ui.SnapToAllDragEditPartsTracker;
import org.eclipse.sirius.viewpoint.DRepresentationDescriptor;
import org.eclipse.sirius.viewpoint.ViewpointPackage;

import com.google.common.collect.Iterables;

/**
 * A specific NoteEditPart to handle the snapToAllShapes.
 * 
 * @author <a href="mailto:laurent.redor@obeo.fr">Laurent Redor</a>
 */
public class SiriusNoteEditPart extends NoteEditPart {

    /**
     * Annotation source constant for Diagram Link Note EAnnotations
     */
    public static final String LINK_NOTE_SOURCE = "http://www.eclipse.org/sirius/annotations/diagramlink"; //$NON-NLS-1$

    /**
     * Default constructor.
     * 
     * @param view
     *            the view controlled by this edit part
     */
    public SiriusNoteEditPart(View view) {
        super(view);
    }

    @Override
    public DragTracker getDragTracker(Request request) {
        return new SnapToAllDragEditPartsTracker(this);
    }

    /**
     * Redirect the direct edit request to the {@link DescriptionCompartmentEditPart}.
     * 
     * @param request
     *            the direct edit request
     */
    @Override
    protected void performDirectEditRequest(Request request) {
        Iterable<DescriptionCompartmentEditPart> descriptionCompartmentEditPartsfilter = Iterables.filter(this.getChildren(), DescriptionCompartmentEditPart.class);
        if (Iterables.size(descriptionCompartmentEditPartsfilter) == 1) {
            DescriptionCompartmentEditPart descriptionCompartmentEditPart = Iterables.getOnlyElement(descriptionCompartmentEditPartsfilter);
            descriptionCompartmentEditPart.performRequest(request);
        }
    }

    @Override
    protected List getModelChildren() {
        return ShowingViewUtil.getModelChildren(getModel());
    }

    @Override
    protected void addNotationalListeners() {
        super.addNotationalListeners();
        if (hasNotationView()) {
            ViewQuery viewQuery = new ViewQuery((View) getModel());
            Optional<DDiagram> diagram = viewQuery.getDDiagram();
            if (diagram.isPresent()) {
                addListenerFilter("ShowingMode", this, diagram.get(), DiagramPackage.eINSTANCE.getDDiagram_IsInShowingMode()); //$NON-NLS-1$
            }
            if (isLinkNote() && !isDangling()) {
                DRepresentationDescriptor linkedDescriptor = (DRepresentationDescriptor) getNotationView().getElement();
                if (linkedDescriptor != null) {
                    addListenerFilter("DiagramLink_RepresentationNameChange", this, linkedDescriptor); //$NON-NLS-1$
                }
            }
        }
    }

    @Override
    protected void removeNotationalListeners() {
        super.removeNotationalListeners();
        removeListenerFilter("ShowingMode"); //$NON-NLS-1$
        removeListenerFilter("DiagramLink_RepresentationNameChange"); //$NON-NLS-1$
    }

    @Override
    protected List getModelSourceConnections() {
        return ShowingViewUtil.getSourceConnectionsConnectingVisibleViews((View) getModel());
    }

    @Override
    protected List getModelTargetConnections() {
        return ShowingViewUtil.getTargetConnectionsConnectingVisibleViews((View) getModel());
    }

    @Override
    protected void setConnectionsVisibility(boolean visibility) {
        ShowingViewUtil.setConnectionsVisibility(this, (View) getModel(), SELECTED_NONE, visibility);
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#setVisibility(boolean)
     */
    @Override
    protected void setVisibility(boolean vis) {
        ShowingViewUtil.setVisibility(this, vis, SELECTED_NONE, getFlag(FLAG__AUTO_CONNECTIONS_VISIBILITY));
    }

    @Override
    protected void handleNotificationEvent(Notification notification) {
        super.handleNotificationEvent(notification);

        // this handles link note label refresh when the linked representation name changes
        if (notification.getNotifier() == getNotationView().getElement() && notification.getFeature() == ViewpointPackage.Literals.DREPRESENTATION_DESCRIPTOR__NAME) {
            Iterable<DiagramNameCompartmentEditPart> diagramNameCompartmentEditPartsfilter = Iterables.filter(this.getChildren(), DiagramNameCompartmentEditPart.class);
            if (Iterables.size(diagramNameCompartmentEditPartsfilter) == 1) {
                DiagramNameCompartmentEditPart diagramNameCompartmentEditPart = Iterables.getOnlyElement(diagramNameCompartmentEditPartsfilter);
                refreshChild(diagramNameCompartmentEditPart);
            }
        }
    }

    @Override
    protected void createDefaultEditPolicies() {
        super.createDefaultEditPolicies();

        // replace the default open policy with one that handles DRepresentationDescriptors
        if (isLinkNote()) {
            removeEditPolicy(EditPolicyRoles.OPEN_ROLE);
            installEditPolicy(EditPolicyRoles.OPEN_ROLE, new OpenDRepresentationEditPolicy());
        }
    }

    /**
     * Is this a link note or is it a 'simple' note?
     * 
     * @return true if this is a link note, false otherwise
     */
    public boolean isLinkNote() {
        return getLinkAnnotation() != null;
    }

    /**
     * If this is a link note, does it refer to a deleted representation descriptor? Invocations should be guarded by
     * {@link #isLinkNote()}.
     */
    public boolean isDangling() {
        EObject element = getNotationView().getElement();
        return element == null || !(element instanceof DRepresentationDescriptor) || element.eIsProxy() || element.eResource() == null;
    }

    private EAnnotation getLinkAnnotation() {
        EAnnotation result = null;
        if (getNotationView() != null) {
            result = getNotationView().getEAnnotation(SiriusNoteEditPart.LINK_NOTE_SOURCE);
        }
        return result;
    }

}
