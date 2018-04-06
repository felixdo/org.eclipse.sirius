/*******************************************************************************
 * Copyright (c) 2016 Obeo.
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
package org.eclipse.sirius.diagram.internal.description.provider;

import java.util.Collection;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.sirius.common.tools.api.util.MessageTranslator;
import org.eclipse.sirius.diagram.description.CustomLayoutConfiguration;
import org.eclipse.sirius.diagram.description.provider.DiagramDescriptionItemProvider;
import org.eclipse.sirius.viewpoint.description.IdentifiedElement;

/**
 * Specific implementation of {@link DiagramDescriptionItemProvider}.
 * 
 * @author <a href="mailto:steve.monnier@obeo.fr">Steve Monnier</a>
 */
public class DiagramDescriptionItemProviderSpec extends DiagramDescriptionItemProvider {

    /**
     * Default constructor.
     * 
     * @param adapterFactory
     *            current {@link AdapterFactory}
     */
    public DiagramDescriptionItemProviderSpec(AdapterFactory adapterFactory) {
        super(adapterFactory);
    }

    /**
     * {@inheritDoc}
     * 
     * This method has been overridden in order to return a localized text if available.
     */
    @Override
    public String getText(Object object) {
        return MessageTranslator.INSTANCE.getMessage((IdentifiedElement) object, super.getText(object));
    }

    @Override
    public String getCreateChildText(Object owner, Object feature, Object child, Collection<?> selection) {
        if (child instanceof CustomLayoutConfiguration) {
            return ((CustomLayoutConfiguration) child).getLabel();
        }

        return super.getCreateChildText(owner, feature, child, selection);
    }

}
