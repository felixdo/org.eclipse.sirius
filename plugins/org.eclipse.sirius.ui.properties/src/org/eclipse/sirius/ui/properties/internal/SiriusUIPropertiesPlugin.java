/*******************************************************************************
 * Copyright (c) 2015, 2017 Obeo.
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

import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.eef.common.api.AbstractEEFEclipsePlugin;
import org.eclipse.eef.core.api.EditingContextAdapter;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.ext.ide.api.AbstractRegistryEventListener;
import org.eclipse.sirius.ext.ide.api.DescriptorRegistryEventListener;
import org.eclipse.sirius.ext.ide.api.IItemDescriptor;
import org.eclipse.sirius.ext.ide.api.IItemRegistry;
import org.eclipse.sirius.ext.ide.api.ItemRegistry;
import org.eclipse.sirius.ui.properties.api.IEditingContextAdapterProvider;
import org.osgi.framework.BundleContext;

/**
 * The plugin for <code>org.eclipse.sirius.ui.properties</code>.
 * 
 * @author pcdavid
 * @author sbegaudeau
 */
public class SiriusUIPropertiesPlugin extends EMFPlugin {
    /**
     * The identifier of the plugin.
     */
    public static final String PLUGIN_ID = "org.eclipse.sirius.ui.properties"; //$NON-NLS-1$

    /**
     * The sole instance of the plugin.
     */
    public static final SiriusUIPropertiesPlugin INSTANCE = new SiriusUIPropertiesPlugin();

    /**
     * The sole instance of the bundle activator.
     */
    private static Implementation plugin;

    /**
     * The constructor.
     */
    public SiriusUIPropertiesPlugin() {
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
     * The bundle activator.
     * 
     * @author sbegaudeau
     */
    public static class Implementation extends AbstractEEFEclipsePlugin {
        /**
         * The name of the extension point for the {@link IEditingContextAdapterProvider}.
         */
        private static final String CONTEXT_ADAPTER_PROVIDER_EXTENSION_POINT = "contextAdapterProvider"; //$NON-NLS-1$

        /**
         * The {@link IItemRegistry} used to retrieve the {@link IEditingContextAdapterProvider}.
         */
        private IItemRegistry<IEditingContextAdapterProvider> contextAdapterProviderRegistry;

        /**
         * The extension registry listener for the {@link IEditingContextAdapterProvider}.
         */
        private AbstractRegistryEventListener contextAdapterProviderListener;

        /**
         * The constructor.
         */
        public Implementation() {
            super(PLUGIN_ID);
            SiriusUIPropertiesPlugin.plugin = this;
        }

        @Override
        public void start(BundleContext context) throws Exception {
            super.start(context);

            IExtensionRegistry registry = Platform.getExtensionRegistry();
            this.contextAdapterProviderRegistry = new ItemRegistry<>();
            this.contextAdapterProviderListener = new DescriptorRegistryEventListener<>(PLUGIN_ID, CONTEXT_ADAPTER_PROVIDER_EXTENSION_POINT, this.contextAdapterProviderRegistry);
            registry.addListener(this.contextAdapterProviderListener, PLUGIN_ID + '.' + CONTEXT_ADAPTER_PROVIDER_EXTENSION_POINT);
            this.contextAdapterProviderListener.readRegistry(registry);

        }

        @Override
        public void stop(BundleContext context) throws Exception {
            IExtensionRegistry registry = Platform.getExtensionRegistry();
            registry.removeListener(this.contextAdapterProviderListener);

            this.contextAdapterProviderListener = null;
            this.contextAdapterProviderRegistry = null;

            super.stop(context);
        }

        /**
         * Returns the appropriate {@link EditingContextAdapter} to use for the specified session, taking registered
         * providers into account.
         * 
         * @param session
         *            the Sirius session.
         * @return the {@link EditingContextAdapter} to use for this session.
         */
        public EditingContextAdapter getEditingContextAdapter(Session session) {
            for (IItemDescriptor<IEditingContextAdapterProvider> itemDescriptor : this.contextAdapterProviderRegistry.getItemDescriptors()) {
                IEditingContextAdapterProvider provider = itemDescriptor.getItem();
                if (provider != null) {
                    EditingContextAdapter provided = provider.createEditingContextAdapter(session);
                    if (provided != null) {
                        return provided;
                    }
                }
            }
            return new TransactionalEditingDomainContextAdapter(session.getTransactionalEditingDomain());
        }
    }
}
