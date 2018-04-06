/*******************************************************************************
 * Copyright (c) 2017 Obeo.
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
package org.eclipse.sirius.ui.editor.internal.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.sirius.ui.tools.internal.preference.SessionEditorUIPreferencesKeys;
import org.eclipse.sirius.viewpoint.provider.SiriusEditPlugin;

/**
 * This class is in charge of setting the default value for preferences
 * regarding the session's editor.
 * 
 * @author <a href="mailto:pierre.guilet@obeo.fr">Pierre Guilet</a>
 *
 */
public class SessionEditorPreferencesInitializer extends AbstractPreferenceInitializer {

    @Override
    public void initializeDefaultPreferences() {
        final IPreferenceStore uiPreferenceStore = SiriusEditPlugin.getPlugin().getPreferenceStore();
        uiPreferenceStore.setDefault(SessionEditorUIPreferencesKeys.PREF_OPEN_SESSION_EDITOR_ON_SESSION_OPEN.name(), false);
    }

}
