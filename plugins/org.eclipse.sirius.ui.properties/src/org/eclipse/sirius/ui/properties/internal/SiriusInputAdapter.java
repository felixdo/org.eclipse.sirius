/*******************************************************************************
 * Copyright (c) 2016, 2017 Obeo.
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
package org.eclipse.sirius.ui.properties.internal;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.eef.core.api.InputDescriptor;
import org.eclipse.sirius.properties.core.api.SiriusInputDescriptor;

/**
 * An adapter factory to convert an element selected in Sirius into a suitable
 * {@link InputDescriptor}.
 * 
 * @author Pierre-Charles David <pierre-charles.david@obeo.fr>
 */
public class SiriusInputAdapter implements IAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public Object getAdapter(final Object adaptableObject, @SuppressWarnings("rawtypes") final Class adapterType) {
        if (adapterType != null) {
            return new SiriusInputDescriptor(adaptableObject);
        } else {
            return null;
        }
    }

    @Override
    public Class<?>[] getAdapterList() {
        return new Class<?>[] { InputDescriptor.class };
    }
}
