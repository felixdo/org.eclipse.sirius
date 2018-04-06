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
package org.eclipse.sirius.tests.api.tools;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;
import org.eclipse.sirius.tree.DTreeItem;
import org.eclipse.sirius.tree.business.internal.helper.TreeHelper;

/**
 * Stub class for testing external java actions.
 * 
 * @author alagarde
 */
public class TreeStubJavaAction implements IExternalJavaAction {

    /**
     * {@inheritDoc}
     */
    public void execute(Collection<? extends EObject> selections, Map<String, Object> parameters) {
        EObject next = selections.iterator().next();
        EObject target = ((DTreeItem) next).getTarget();
        EStructuralFeature nameFeature = (target.eClass().getEStructuralFeature("name"));
        target.eSet(nameFeature, target.eGet(nameFeature) + "_RENAMMED");
        TreeHelper.getTree(next).refresh();
    }

    /**
     * {@inheritDoc}
     */
    public boolean canExecute(Collection<? extends EObject> selections) {
        return true;
    }

}
