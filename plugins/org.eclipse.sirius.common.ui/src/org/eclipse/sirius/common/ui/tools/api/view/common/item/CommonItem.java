/*******************************************************************************
 * Copyright (c) 2009, 2010 THALES GLOBAL SERVICES.
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
package org.eclipse.sirius.common.ui.tools.api.view.common.item;

import java.util.Collection;

/**
 * An interface to get the wrapped object.
 * 
 * @author mporhel
 */
public interface CommonItem {

    /**
     * Get the children items.
     * 
     * @return the children
     */
    Collection<?> getChildren();

    /**
     * Get the parent item.
     * 
     * @return the parent
     */
    Object getParent();
}
