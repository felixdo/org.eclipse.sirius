/*******************************************************************************
 * Copyright (c) 2018 Obeo
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.diagram.ui.internal.layout;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.AbstractLayoutEditPartProvider;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.description.CustomLayoutConfiguration;
import org.eclipse.sirius.diagram.description.DiagramDescription;
import org.eclipse.sirius.diagram.description.Layout;
import org.eclipse.sirius.diagram.ui.provider.DiagramUIPlugin;
import org.eclipse.sirius.diagram.ui.tools.api.layout.provider.DefaultLayoutProvider;
import org.eclipse.sirius.diagram.ui.tools.api.layout.provider.LayoutProvider;
import org.eclipse.sirius.diagram.ui.tools.internal.layout.provider.ArrangeSelectionLayoutProvider;

/**
 * A generic layout provider that will allow to use a layout algorithm specified in a Sirius VSM.
 * 
 * @author <a href="mailto:pierre.guilet@obeo.fr">Pierre Guilet</a>
 *
 */
public class GenericLayoutProvider implements LayoutProvider {

    @Override
    public AbstractLayoutEditPartProvider getLayoutNodeProvider(final IGraphicalEditPart container) {
        DefaultLayoutProvider layoutProvider = getGenericLayoutProvider(container);
        if (layoutProvider != null) {
            return new ArrangeSelectionLayoutProvider(layoutProvider);
        }
        return null;
    }

    @Override
    public boolean provides(final IGraphicalEditPart container) {
        return getGenericLayoutProvider(container) != null;
    }

    @Override
    public boolean isDiagramLayoutProvider() {
        return true;
    }

    /**
     * Returns the {@link CustomLayoutConfiguration} associated to the description of the {@link DDiagram} related to
     * the given part.
     * 
     * @param container
     * @return the {@link CustomLayoutConfiguration} associated to the description of the {@link DDiagram} related to
     *         the given part. Null if no such element exists.
     */
    private CustomLayoutConfiguration getCustomLayoutConfiguration(final IGraphicalEditPart container) {
        final View view = container.getNotationView();
        final EObject modelElement = view.getElement();
        if (view instanceof Diagram && modelElement instanceof DDiagram) {
            final DDiagram vp = (DDiagram) modelElement;
            final DiagramDescription desc = vp.getDescription();
            if (desc != null) {
                final Layout layout = desc.getLayout();
                if (layout instanceof CustomLayoutConfiguration) {
                    return (CustomLayoutConfiguration) layout;
                }
            }
        }
        return null;
    }

    /**
     * Returns the generic layout provider associated to the description of the {@link DDiagram} related to the given
     * part.
     * 
     * @param container
     * @return the generic layout provider associated to the description of the {@link DDiagram} related to the given
     *         part. Null if no such element exists.
     */
    private DefaultLayoutProvider getGenericLayoutProvider(final IGraphicalEditPart container) {
        CustomLayoutConfiguration customLayoutConfiguration = getCustomLayoutConfiguration(container);
        if (customLayoutConfiguration != null) {
            return DiagramUIPlugin.getPlugin().getLayoutAlgorithms().get(customLayoutConfiguration.getId()).getLayoutAlgorithmInstance();
        }
        return null;
    }

}
