/*******************************************************************************
 * Copyright (c) 2007, 2008 THALES GLOBAL SERVICES.
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
package org.eclipse.sirius.diagram.ui.internal.edit.commands;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.emf.type.core.commands.CreateElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.sirius.diagram.DNode;
import org.eclipse.sirius.diagram.DiagramPackage;

/**
 * @was-generated
 */
public class WorkspaceImageCreateCommand extends CreateElementCommand {

    /**
     * @was-generated
     */
    public WorkspaceImageCreateCommand(CreateElementRequest req) {
        super(req);
    }

    /**
     * @was-generated
     */
    protected EObject getElementToEdit() {
        EObject container = ((CreateElementRequest) getRequest()).getContainer();
        if (container instanceof View) {
            container = ((View) container).getElement();
        }
        return container;
    }

    /**
     * @was-generated
     */
    protected EClass getEClassToEdit() {
        return DiagramPackage.eINSTANCE.getDNode();
    }

    /**
     * @was-generated
     */
    public boolean canExecute() {
        DNode container = (DNode) getElementToEdit();
        if (container.getOwnedStyle() != null) {
            return false;
        }
        return true;
    }
}
