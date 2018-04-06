/*******************************************************************************
 * Copyright (c) 2018 THALES GLOBAL SERVICES and others.
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
package org.eclipse.sirius.diagram.ui.tools.internal.figure;

import org.eclipse.draw2d.IFigure;

/**
 * 
 * A fixed figure is a figure that has always the same position in a Sirius Diagram like {@link SynchronizeStatusFigure}
 * and {@link DiagramSemanticElementLockedNotificationFigure}. This interface provides methods to update those figures.
 * 
 * @author <a href=mailto:pierre.guilet@obeo.fr>Pierre Guilet</a>
 *
 */
public interface IFixedFigure extends IFigure {
    /**
     * Update this figure location regarding its graphic context.
     */
    void updateLocation();
}
