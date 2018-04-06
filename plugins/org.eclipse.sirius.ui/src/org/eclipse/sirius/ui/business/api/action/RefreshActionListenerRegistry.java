/*******************************************************************************
 * Copyright (c) 2011 THALES GLOBAL SERVICES.
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
package org.eclipse.sirius.ui.business.api.action;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.sirius.viewpoint.DRepresentation;

/**
 * A registry listing all the listeners that will be notified any time a user
 * executes a Refresh action.
 * 
 * @author <a href="mailto:alex.lagarde@obeo.fr">Alex Lagarde</a>
 * 
 */
public final class RefreshActionListenerRegistry {

    /**
     * The registry instance.
     */
    public static final RefreshActionListenerRegistry INSTANCE = new RefreshActionListenerRegistry();

    private Set<IRefreshActionListener> listeners = new LinkedHashSet<>();

    /**
     * Private constructor.
     */
    private RefreshActionListenerRegistry() {

    }

    /**
     * Registers the given listener.
     * 
     * @param listener
     *            the {@link IRefreshActionListener} to register
     */
    public void addListener(IRefreshActionListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes the given listener from the registered listeners.
     * 
     * @param listener
     *            the {@link IRefreshActionListener} to remove
     */
    public void removeListener(IRefreshActionListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies the listeners, indicating that a user is about to execute a
     * Refresh action on a given representation.
     * 
     * @param refreshedRepresentation
     *            the representation that is about to be refreshed
     */
    public void notifyRepresentationIsAboutToBeRefreshed(DRepresentation refreshedRepresentation) {
        for (IRefreshActionListener listener : listeners) {
            listener.notifyRepresentationIsAboutToBeRefreshed(refreshedRepresentation);
        }
    }
}
