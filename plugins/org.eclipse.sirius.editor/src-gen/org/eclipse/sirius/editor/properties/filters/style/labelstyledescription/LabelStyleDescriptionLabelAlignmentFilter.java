/*******************************************************************************
 * Copyright (c) 2007, 2018 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.editor.properties.filters.style.labelstyledescription;

// Start of user code specific imports

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.sirius.editor.properties.filters.common.ViewpointPropertyFilter;
import org.eclipse.sirius.viewpoint.description.style.StylePackage;

// End of user code specific imports

/**
 * A filter for the labelAlignment property section.
 */
public class LabelStyleDescriptionLabelAlignmentFilter extends ViewpointPropertyFilter {

    /**
     * {@inheritDoc}
     */
    @Override
    protected EStructuralFeature getFeature() {
        return StylePackage.eINSTANCE.getLabelStyleDescription_LabelAlignment();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isRightInputType(Object arg0) {
        return arg0 instanceof org.eclipse.sirius.viewpoint.description.style.LabelStyleDescription;
    }

    // Start of user code user methods
    @Override
    public boolean select(Object arg0) {
        if (isRightInputType(arg0) && !(arg0.getClass().getName().contains("TreeItemStyleDescription"))) {
            EStructuralFeature feature = getFeature();
            if (feature != null && isVisible(feature)) {
                return true;
            }
        }
        return false;
    }
    // End of user code user methods

}
