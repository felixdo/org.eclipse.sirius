/*******************************************************************************
 * Copyright (c) 2011, 2015 THALES GLOBAL SERVICES and others.
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
package org.eclipse.sirius.ui.tools.internal.operations;

import java.util.Collection;
import java.util.LinkedHashSet;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.tools.api.command.semantic.AddSemanticResourceCommand;
import org.eclipse.sirius.viewpoint.provider.Messages;

/**
 * Operation to add a semantic resource to a session.
 *
 * @author mchauvin
 */
public class SemanticResourceAdditionOperation implements IRunnableWithProgress {

    private Collection<Session> sessions;

    private Collection<URI> uris;

    private Collection<Object> results;

    /**
     * Creates a new instance.
     *
     * @param sessions
     *            the sessions
     * @param uris
     *            the uris
     */
    public SemanticResourceAdditionOperation(final Collection<Session> sessions, final Collection<URI> uris) {
        this.sessions = sessions;
        this.uris = uris;
    }

    @Override
    public void run(IProgressMonitor monitor) throws java.lang.reflect.InvocationTargetException, InterruptedException {
        try {
            monitor.beginTask(Messages.SemanticResourceAdditionOperation_semanticResourceAdditionTask, uris.size() * sessions.size());
            results = new LinkedHashSet<>();
            for (Session session : sessions) {
                TransactionalEditingDomain transDomain = session.getTransactionalEditingDomain();
                CompoundCommand command = new CompoundCommand();
                for (URI semanticModelURI : uris) {
                    RecordingCommand cmd = new AddSemanticResourceCommand(session, semanticModelURI, new SubProgressMonitor(monitor, 1));
                    command.append(cmd);
                }
                transDomain.getCommandStack().execute(command);
                results.addAll(command.getResult());
            }
        } finally {
            monitor.done();
        }
    }

    public Collection<Object> getResults() {
        return results;
    }
}
