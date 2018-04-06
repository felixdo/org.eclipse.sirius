/*******************************************************************************
 * Copyright (c) 2016, 2017 Obeo.
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
package org.eclipse.sirius.properties.core.api;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.eef.core.api.InputDescriptor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.ext.base.Option;
import org.eclipse.sirius.properties.core.internal.SiriusContext;

/**
 * An EEF InputDescriptor for elements selected in a Sirius context.
 * 
 * @author Pierre-Charles David <pierre-charles.david@obeo.fr>
 */
public class SiriusInputDescriptor implements InputDescriptor {
    /**
     * The full context, determined from the original input.
     */
    private final SiriusContext context;

    /**
     * Creates a SiriusInputDescriptor from the specified input.
     * 
     * @param input
     *            the original input.
     */
    public SiriusInputDescriptor(Object input) {
        this.context = SiriusContext.from(input);
    }

    @Override
    public Object getOriginalSelection() {
        return context.getInput();
    }

    @Override
    public EObject getSemanticElement() {
        Option<EObject> obj = context.getMainSemanticElement();
        if (obj.some()) {
            return obj.get();
        } else {
            return null;
        }
    }

    /**
     * Returns all the semantic model element associated with the current selection, including secondary associated
     * elements if any.
     * 
     * @return all the semantic model element associated with the current selection.
     */
    public Set<EObject> getAllSemanticElements() {
        Set<EObject> result = new LinkedHashSet<>();
        result.add(getSemanticElement());
        Option<List<EObject>> additional = context.getAdditionalSemanticElements();
        if (additional.some()) {
            result.addAll(additional.get());
        }
        return result;
    }

    /**
     * Returns the full Sirius context determined from the original input, which may include addition Sirius-specific
     * information in addition to what can be exposed through the generic {@link InputDescriptor} API.
     * 
     * @return the full Sirius context determined from the original input.
     */
    public SiriusContext getFullContext() {
        return context;
    }

}
