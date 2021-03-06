/*******************************************************************************
 * Copyright (c) 2018 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.editor.workflow.internal;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.ui.EclipseUIPlugin;
import org.eclipse.emf.common.util.ResourceLocator;

/**
 * This is the central singleton for the SiriusEditorWorkflow edit plugin.
 * 
 * @author sbegaudeau
 */
public final class SiriusEditorWorkflowPlugin extends EMFPlugin {
    /**
     * The plug-in ID.
     */
    public static final String PLUGIN_ID = "org.eclipse.sirius.editor.workflow"; //$NON-NLS-1$

    /**
     * Singleton instance.
     */
    public static final SiriusEditorWorkflowPlugin INSTANCE = new SiriusEditorWorkflowPlugin();

    private static Implementation plugin;

    /**
     * Create the instance.
     */
    public SiriusEditorWorkflowPlugin() {
        super(new ResourceLocator[0]);
    }

    @Override
    public ResourceLocator getPluginResourceLocator() {
        return plugin;
    }

    /**
     * Returns the singleton instance of the Eclipse plugin.
     * 
     * @return the singleton instance.
     */
    public static Implementation getPlugin() {
        return plugin;
    }

    /**
     * The actual implementation of the Eclipse <b>Plugin</b>.
     */
    public static class Implementation extends EclipseUIPlugin {

        /**
         * Creates an instance.
         */
        public Implementation() {
            plugin = this;
        }

    }
}
