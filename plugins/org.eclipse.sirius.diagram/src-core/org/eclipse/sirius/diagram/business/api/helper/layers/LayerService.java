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
package org.eclipse.sirius.diagram.business.api.helper.layers;

import org.eclipse.sirius.diagram.description.DiagramDescription;

/**
 * A temporary service.
 * 
 * @author mchauvin
 */
public final class LayerService {

    /**
     * Avoid instantiation.
     */
    private LayerService() {

    }

    /**
     * Check if are in the mode without layers.
     * 
     * @param diagramDescription
     *            the diagram description to check
     * @return <code>true</code> if we are in the without layer mode,
     *         <code>false</code> otherwise
     */
    public static boolean withoutLayersMode(final DiagramDescription diagramDescription) {
        return diagramDescription.getDefaultLayer() == null;
    }
}
