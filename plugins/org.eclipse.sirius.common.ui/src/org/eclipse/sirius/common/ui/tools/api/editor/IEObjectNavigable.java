/*******************************************************************************
 * Copyright (c) 2007, 2009 THALES GLOBAL SERVICES.
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
package org.eclipse.sirius.common.ui.tools.api.editor;

import org.eclipse.emf.common.util.URI;

/**
 * Editor able to reveal a given EObject.
 * 
 * @author cbrun
 * @since 0.9.0
 */
public interface IEObjectNavigable {
    /**
     * Reveal an EObject corresponding to the URI.
     * 
     * @param uri
     *            uri of the EObject to reveal.
     * @return true of the EObject has been revealed, false otherwise.
     */
    boolean navigateToEObject(URI uri);
}
