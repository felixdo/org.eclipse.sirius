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
package org.eclipse.sirius.business.api.resource.strategy;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.business.api.resource.LoadEMFResource;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.common.tools.api.resource.ResourceSetFactory;
import org.eclipse.sirius.tools.internal.resource.ModelingProjectFileQuery;
import org.eclipse.sirius.viewpoint.Messages;
import org.eclipse.sirius.viewpoint.SiriusPlugin;

/**
 * An abstract implementation of {@link ResourceStrategy} that does nothing.
 * 
 * @author <a href="mailto:laurent.redor@obeo.fr">Laurent Redor</a>
 */
public abstract class AbstractResourceStrategyImpl implements ResourceStrategy {

    @Override
    public boolean canHandle(URI resourceURI, ResourceStrategyType resourceStrategyType) {
        // Handle nothing by default
        return false;
    }

    @Override
    public boolean canHandle(Resource resource, ResourceStrategyType resourceStrategyType) {
        // Handle nothing by default
        return false;
    }

    @Override
    public IStatus releaseResourceAtResourceSetDispose(Resource resource, IProgressMonitor monitor) {
        // Just return an ERROR status if this methid is called.
        return new Status(IStatus.ERROR, SiriusPlugin.ID, 1, Messages.AbstractResourceStrategyImpl_methodReleaseNotHandleMsg, null);
    }

    /**
     * Corresponding to default Sirius implementation (
     * {@link org.eclipse.sirius.business.internal.resource.strategy.DefaultResourceStrategyImpl}
     * ). It is not used in this abstract ResourceStrategy as canHandle methods
     * return false. It is in this abstract class to be easily used by sub
     * classes.
     */
    @Override
    public boolean isPotentialSemanticResource(URI uri) {
        if (uri != null) {
            IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(uri.toPlatformString(true)));
            return new ModelingProjectFileQuery(file).isPotentialSemanticResource();
        }
        return false;
    }

    /**
     * Corresponding to default Sirius implementation (
     * {@link org.eclipse.sirius.business.internal.resource.strategy.DefaultResourceStrategyImpl}
     * ). It is not used in this abstract ResourceStrategy as canHandle methods
     * return false. It is in this abstract class to be easily used by sub
     * classes.
     */
    @Override
    public boolean isLoadableModel(URI uri, Session session) {
        if (uri != null) {
            IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(uri.toPlatformString(true)));
            if (file.exists()) {
                final ResourceSet set = ResourceSetFactory.createFactory().createResourceSet(session.getSessionResource().getURI());
                LoadEMFResource runnable = new LoadEMFResource(set, file);
                runnable.run();
                return runnable.getLoadedResource() != null;
            }
        }
        return false;
    }
}
