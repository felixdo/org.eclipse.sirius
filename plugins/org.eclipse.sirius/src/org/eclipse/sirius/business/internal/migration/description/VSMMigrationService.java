/*******************************************************************************
 * Copyright (c) 2012 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.business.internal.migration.description;

import org.eclipse.sirius.business.api.migration.IMigrationParticipant;
import org.eclipse.sirius.business.internal.migration.AbstractSiriusMigrationService;

/**
 * VSM Implementation of {@link AbstractSiriusMigrationService}.
 * 
 * @author fbarbin
 * 
 */
public final class VSMMigrationService extends AbstractSiriusMigrationService {

    private static VSMMigrationService instance = new VSMMigrationService();

    private VSMMigrationService() {
        loadContributions();
    }

    public static VSMMigrationService getInstance() {
        return instance;
    }

    @Override
    protected String getKind() {
        return IMigrationParticipant.VSM_KIND;
    }

}
