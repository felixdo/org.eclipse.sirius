/*******************************************************************************
 * Copyright (c) 2018 Obeo.
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
package org.eclipse.sirius.server.backend.internal;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.sirius.server.api.ISiriusServerConfigurator;
import org.eclipse.sirius.server.backend.internal.servlets.APIServlet;

/**
 * The entry point of the back-end used to configure the Sirius server.
 *
 * @author sbegaudeau
 */
public class SiriusServerBackendConfigurator implements ISiriusServerConfigurator {

	/**
	 * The context path of the Sirius back-end.
	 */
	private static final String CONTEXT_PATH = "/api"; //$NON-NLS-1$

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.sirius.server.api.ISiriusServerConfigurator#configure(org.eclipse.sirius.server.api.Server)
	 */
	@Override
	public void configure(Server server) {
		ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS | ServletContextHandler.GZIP);
		servletContextHandler.setContextPath(CONTEXT_PATH);
		servletContextHandler.addServlet(APIServlet.class, APIServlet.PATH);

		Handler handler = server.getHandler();
		if (handler instanceof HandlerCollection) {
			HandlerCollection handlerCollection = (HandlerCollection) handler;
			handlerCollection.addHandler(servletContextHandler);
		}
	}

}
