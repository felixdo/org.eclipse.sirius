/*******************************************************************************
 * Copyright (c) 2017 THALES GLOBAL SERVICES.
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
package org.eclipse.sirius.diagram.ui.internal.providers;

import org.eclipse.gmf.runtime.diagram.ui.services.decorator.AbstractDecorator;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorTarget;

/**
 * Decorator that do nothing (just replace GMF
 * {@link org.eclipse.gmf.runtime.diagram.ui.providers.internal.UnresolvedViewDecorator}
 * ).
 * 
 * @author <a href="mailto:laurent.redor@obeo.fr">Laurent Redor</a>
 */
@SuppressWarnings("restriction")
public class SiriusUnresolvedViewDecorator extends AbstractDecorator {

    /**
     * Default constructor.
     * 
     * @param decoratorTarget
     *            the object to be decorated
     */
    public SiriusUnresolvedViewDecorator(IDecoratorTarget decoratorTarget) {
        super(decoratorTarget);
    }

    @Override
    public void activate() {
        // Do nothing
    }

    @Override
    public void refresh() {
        // Do nothing
    }

}
