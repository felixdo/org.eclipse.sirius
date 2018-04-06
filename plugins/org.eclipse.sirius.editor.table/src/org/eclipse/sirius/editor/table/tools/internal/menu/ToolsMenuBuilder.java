/*******************************************************************************
 * Copyright (c) 2009, 2015 THALES GLOBAL SERVICES and others.
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
package org.eclipse.sirius.editor.table.tools.internal.menu;

import org.eclipse.sirius.editor.tools.api.menu.AbstractMenuBuilder;
import org.eclipse.sirius.editor.tools.api.menu.AbstractTypeRestrictingMenuBuilder;

/**
 * The tools menu for tables.
 * 
 * @author cbrun
 * 
 */
public class ToolsMenuBuilder extends AbstractTypeRestrictingMenuBuilder {
    /**
     * Create the menu.
     */
    public ToolsMenuBuilder() {
        super();
        addValidType(org.eclipse.sirius.table.metamodel.table.description.DescriptionPackage.eINSTANCE.getTableTool());
    }

    @Override
    public String getLabel() {
        return "New Tool";
    }

    @Override
    public int getPriority() {
        return AbstractMenuBuilder.TOOL;
    }
}
