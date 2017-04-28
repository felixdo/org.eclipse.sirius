/**
 * Copyright (c) 2016, 2017 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *
 */
package org.eclipse.sirius.properties.provider;

import java.text.MessageFormat;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.provider.StyledString;
import org.eclipse.sirius.properties.GridLayoutDescription;

/**
 * Subclass used to not have to modify the generated code.
 *
 * @author sbegaudeau
 */
public class GridLayoutDescriptionItemProviderSpec extends GridLayoutDescriptionItemProvider {

    /**
     * The constructor.
     *
     * @param adapterFactory
     *            The adapter factory
     */
    public GridLayoutDescriptionItemProviderSpec(AdapterFactory adapterFactory) {
        super(adapterFactory);
    }

    @Override
    public String getText(Object object) {
        Object styledText = this.getStyledText(object);
        if (styledText instanceof StyledString) {
            return ((StyledString) styledText).getString();
        }
        return super.getText(object);
    }

    @Override
    public Object getStyledText(Object object) {
        int numberOfColumns = ((GridLayoutDescription) object).getNumberOfColumns();
        String label = MessageFormat.format(Messages.GridLayoutDescription__label, numberOfColumns);
        return new StyledString(label);
    }

}