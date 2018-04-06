/*******************************************************************************
 * Copyright (c) 2009, 2009 THALES GLOBAL SERVICES.
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
package org.eclipse.sirius.diagram.ui.tools.internal.layout.semantic;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.diagram.ui.tools.internal.layout.NodeLayoutDataKey;

/**
 * Kind of key use to store the layout data corresponding to a semantic element
 * represented by a {@link org.eclipse.sirius.viewpoint.AbstractDNode} or a
 * {@link org.eclipse.sirius.diagram.DDiagram}.
 * 
 * @author <a href="mailto:laurent.redor@obeo.fr">Laurent Redor</a>
 * 
 */
public class SemanticNodeLayoutDataKey extends AbstractSemanticLayoutDataKey implements NodeLayoutDataKey {

    /**
     * Constructor.
     * 
     * @param uriFragment
     *            String to use.
     */
    public SemanticNodeLayoutDataKey(final String uriFragment) {
        super(uriFragment);
    }

    /**
     * Default constructor.
     * 
     * @param semanticElement
     *            The element to build the key
     */
    public SemanticNodeLayoutDataKey(final EObject semanticElement) {
        super(semanticElement);
    }
}
