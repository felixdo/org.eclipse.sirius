/*******************************************************************************
 * Copyright (c) 2017 Obeo.
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
package org.eclipse.sirius.properties.core.internal.preprocessor;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.common.interpreter.api.IInterpreter;
import org.eclipse.sirius.common.interpreter.api.IVariableManager;
import org.eclipse.sirius.properties.AbstractPageDescription;
import org.eclipse.sirius.properties.core.api.OverridesProvider;
import org.eclipse.sirius.properties.core.api.TransformationCache;

/**
 * Preprocessor for
 * {@link org.eclipse.sirius.properties.PageOverrideDescription}.
 * <ul>
 * <li>The {@code overrides} containment is ignored.</li>
 * </ul>
 * 
 * @author mbats
 */
public class PageOverrideDescriptionPreprocessor extends PageDescriptionPreprocessor {
    @Override
    protected void processMonoValuedEReference(EReference eReference, AbstractPageDescription processedDescription, AbstractPageDescription currentDescription, TransformationCache cache,
            IInterpreter interpreter, IVariableManager variableManager, OverridesProvider overridesProvider) {
        if (!eReference.equals(currentDescription.eClass().getEStructuralFeature("overrides"))) { //$NON-NLS-1$
            super.processMonoValuedEReference(eReference, processedDescription, currentDescription, cache, interpreter, variableManager, overridesProvider);
        }
    }
}
