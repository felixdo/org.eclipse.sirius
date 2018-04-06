/*******************************************************************************
 * Copyright (c) 2008, 2017 THALES GLOBAL SERVICES and others.
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
package org.eclipse.sirius.ui.tools.api.views;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.sirius.ui.tools.internal.views.ViewHelperImpl;

/**
 * An helper to provide facilities to view which extends viewpoint. This
 * interface is not intended to be subclassed.
 * 
 * @author mchauvin
 */
public interface ViewHelper {

    /**
     * The shared instance.
     */
    ViewHelper INSTANCE = ViewHelperImpl.init();

    /**
     * Create a composed adapter factory with the right factories inside.
     * 
     * @return the composed adapter factory
     */
    AdapterFactory createAdapterFactory();

    /**
     * Get the content provider for a session.
     * 
     * @return the content provider
     */
    ITreeContentProvider createContentProvider();
}
