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
package org.eclipse.sirius.table.business.internal.migration.description;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.business.api.migration.AbstractVSMMigrationParticipant;
import org.eclipse.sirius.table.metamodel.table.description.DescriptionFactory;
import org.eclipse.sirius.table.metamodel.table.description.LabelEditTool;
import org.eclipse.sirius.table.metamodel.table.description.TableVariable;
import org.eclipse.sirius.table.tools.api.interpreter.IInterpreterSiriusTableVariables;
import org.eclipse.sirius.table.tools.internal.Messages;
import org.osgi.framework.Version;

/**
 * Make sure old VSM get the 2 table variables "table" and "line" properly defined inside LabelEditTool definition.
 * 
 * @author pguilet
 * @see http://eclip.se/466412
 */
public class LabelEditToolVariableMigrationParticipant extends AbstractVSMMigrationParticipant {
    /**
     * The version for which this migration is added.
     */
    public static final Version MIGRATION_VERSION = new Version("12.0.0.2017041100"); //$NON-NLS-1$

    @Override
    public Version getMigrationVersion() {
        return MIGRATION_VERSION;
    }

    @Override
    public EObject updateCreatedObject(EObject newObject, String loadedVersion) {
        if (Version.parseVersion(loadedVersion).compareTo(MIGRATION_VERSION) < 0 && newObject instanceof LabelEditTool) {
            LabelEditTool labelEditTool = (LabelEditTool) newObject;

            final TableVariable tableVar = DescriptionFactory.eINSTANCE.createTableVariable();
            tableVar.setName(IInterpreterSiriusTableVariables.TABLE);
            tableVar.setDocumentation(Messages.TableToolVariables_TableElement);
            labelEditTool.getVariables().add(tableVar);

            final TableVariable lineVar = DescriptionFactory.eINSTANCE.createTableVariable();
            lineVar.setName(IInterpreterSiriusTableVariables.LINE);
            lineVar.setDocumentation(Messages.TableToolVariables_LineElement);
            labelEditTool.getVariables().add(lineVar);
        }
        return newObject;
    }

}
