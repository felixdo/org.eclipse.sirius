/*******************************************************************************
 * Copyright (c) 2015 THALES GLOBAL SERVICES and others.
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
package org.eclipse.sirius.tree.business.internal.dialect.common.tree;

import org.eclipse.sirius.synchronizer.CreatedOutput;
import org.eclipse.sirius.tree.business.internal.dialect.common.viewpoint.GlobalContext;

/**
 * An abstract {@link CreatedOutput}.
 * 
 * @author cbrun
 */
abstract class AbstractCreatedDTreeItemContainer implements CreatedOutput {

    protected GlobalContext ctx;

    /**
     * A default constructor.
     * 
     * @param ctx
     *            a {@link GlobalContext}
     */
    AbstractCreatedDTreeItemContainer(GlobalContext ctx) {
        this.ctx = ctx;
    }

    public GlobalContext getGlobalContext() {
        return ctx;
    }
}
