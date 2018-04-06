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
package org.eclipse.sirius.diagram.business.internal.metamodel.description.spec;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.diagram.business.internal.metamodel.description.operations.ConditionalStyleSpecOperations;
import org.eclipse.sirius.diagram.description.impl.ConditionalContainerStyleDescriptionImpl;

/**
 * Implementation of ConditionalContainerStyleDescription.
 * 
 * @author cbrun
 */
public class ConditionalContainerStyleDescriptionSpec extends ConditionalContainerStyleDescriptionImpl {

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.sirius.viewpoint.description.impl.ConditionalStyleDescriptionImpl#checkPredicate(org.eclipse.emf.ecore.EObject,
     *      org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject)
     */
    @Override
    public boolean checkPredicate(final EObject modelElement, final EObject viewVariable, final EObject containerVariable) {
        return ConditionalStyleSpecOperations.checkPredicate(this, modelElement, viewVariable, containerVariable);
    }

}
