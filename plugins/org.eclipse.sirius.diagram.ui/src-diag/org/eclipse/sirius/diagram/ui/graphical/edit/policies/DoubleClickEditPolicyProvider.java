/*******************************************************************************
 * Copyright (c) 2007, 2017 THALES GLOBAL SERVICES.
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
package org.eclipse.sirius.diagram.ui.graphical.edit.policies;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.IProviderChangeListener;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.services.editpolicy.CreateEditPoliciesOperation;
import org.eclipse.gmf.runtime.diagram.ui.services.editpolicy.IEditPolicyProvider;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.ui.edit.api.part.IAbstractDiagramNodeEditPart;
import org.eclipse.sirius.diagram.ui.edit.api.part.IDiagramEdgeEditPart;
import org.eclipse.sirius.diagram.ui.edit.api.part.IDiagramNameEditPart;

/**
 * Provides Edit Policy for double clicking on Diagram elements.
 * 
 * @author smonnier
 * 
 */
public class DoubleClickEditPolicyProvider implements IEditPolicyProvider {

    /** the property change support. */
    private final List<IProviderChangeListener> listeners;

    /**
     * Create a new {@link DoubleClickEditPolicyProvider}.
     */
    public DoubleClickEditPolicyProvider() {
        this.listeners = new ArrayList<IProviderChangeListener>();
    }

    @Override
    public void createEditPolicies(EditPart editPart) {
        if (editPart instanceof IAbstractDiagramNodeEditPart || editPart instanceof IDiagramEdgeEditPart || editPart instanceof IDiagramNameEditPart) {
            editPart.installEditPolicy(EditPolicyRoles.OPEN_ROLE, new DoubleClickEditPolicy());
        }
    }

    @Override
    public void addProviderChangeListener(IProviderChangeListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public boolean provides(IOperation operation) {
        if (operation instanceof CreateEditPoliciesOperation) {
            CreateEditPoliciesOperation castedOperation = (CreateEditPoliciesOperation) operation;
            EditPart editPart = castedOperation.getEditPart();
            Object model = editPart.getModel();
            if (model instanceof View) {
                EObject element = ((View) model).getElement();
                if (element instanceof DDiagramElement) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void removeProviderChangeListener(IProviderChangeListener listener) {
        this.listeners.remove(listener);
    }

}
