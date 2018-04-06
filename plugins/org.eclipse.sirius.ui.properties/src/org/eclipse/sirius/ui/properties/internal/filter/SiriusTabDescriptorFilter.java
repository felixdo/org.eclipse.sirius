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
package org.eclipse.sirius.ui.properties.internal.filter;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.eef.properties.ui.api.IEEFTabDescriptor;
import org.eclipse.eef.properties.ui.api.IEEFTabDescriptorFilter;
import org.eclipse.sirius.properties.core.api.preferences.SiriusPropertiesCorePreferences;

/**
 * The {@link IEEFTabDescriptorFilter} for Eclipse Sirius.
 * 
 * @author mbats
 */
public class SiriusTabDescriptorFilter implements IEEFTabDescriptorFilter {

    /**
     * Ids of the default tab. For historical reasons the ids used by the different dialects (diagrams, tables & trees)
     * are not the same, so we must consider all of them.
     */
    private static final String DEFAULT_TAB_IDS = "org.eclipse.sirius.ui.tools.views.model.explorer.tab"; //$NON-NLS-1$

    /**
     * Id of the semantic tab.
     */
    private static final Collection<String> SEMANTIC_TAB_ID = Arrays.asList("property.tab.semantic", //$NON-NLS-1$
            "org.eclipse.sirius.table.ui.tab.semantic", "org.eclipse.sirius.tree.ui.tab.semantic"); //$NON-NLS-1$ //$NON-NLS-2$

    @Override
    public boolean filter(IEEFTabDescriptor tabDescriptor) {
        boolean result = true;
        // Filter the default tab existing in the properties view when an
        // element is selected from the model explorer and the semantic tab when
        // an element is selected from a Sirius editor
        if (SEMANTIC_TAB_ID.contains(tabDescriptor.getId())) {
            result = SiriusPropertiesCorePreferences.INSTANCE.isSemanticTabFiltered();
        } else if (DEFAULT_TAB_IDS.equals(tabDescriptor.getId())) {
            result = SiriusPropertiesCorePreferences.INSTANCE.isDefaultTabFiltered();
        }

        return result;
    }

}
