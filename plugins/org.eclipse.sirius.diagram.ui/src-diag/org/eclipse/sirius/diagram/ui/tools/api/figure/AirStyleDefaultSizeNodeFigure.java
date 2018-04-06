/*******************************************************************************
 * Copyright (c) 2007, 2015 THALES GLOBAL SERVICES.
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
package org.eclipse.sirius.diagram.ui.tools.api.figure;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.sirius.diagram.ui.tools.internal.figure.AlphaBasedSlidableImageAnchor;
import org.eclipse.sirius.ext.gmf.runtime.gef.ui.figures.SiriusDefaultSizeNodeFigure;

/**
 * The default size node for styles.
 * 
 * @author ymortier
 */
public class AirStyleDefaultSizeNodeFigure extends SiriusDefaultSizeNodeFigure {

    /**
     * Constructor.
     * 
     * @param defSize
     *            the default size.
     */
    public AirStyleDefaultSizeNodeFigure(final Dimension defSize) {
        super(defSize);
    }

    /**
     * Constructor.
     * 
     * @param width
     *            the initial width to initialize the default size with
     * @param height
     *            the initial height to initialize the default size with
     */
    public AirStyleDefaultSizeNodeFigure(final int width, final int height) {
        super(width, height);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.draw2d.Figure#setBounds(org.eclipse.draw2d.geometry.Rectangle)
     */
    @Override
    public void setBounds(final Rectangle rect) {
        if (getParent() != null) {
            super.setBounds(getParent().getBounds());
        } else {
            super.setBounds(rect);
        }
    }

    @Override
    protected ConnectionAnchor createDefaultAnchor() {
        return new AlphaBasedSlidableImageAnchor(this);
    }

    @Override
    protected ConnectionAnchor createAnchor(PrecisionPoint p) {
        if (p == null) {
            // If the old terminal for the connection anchor cannot be resolved
            // (by SlidableAnchor) a null PrecisionPoint will passed in - this
            // is handled here
            return createDefaultAnchor();
        }
        return new AlphaBasedSlidableImageAnchor(this, p);
    }
}
