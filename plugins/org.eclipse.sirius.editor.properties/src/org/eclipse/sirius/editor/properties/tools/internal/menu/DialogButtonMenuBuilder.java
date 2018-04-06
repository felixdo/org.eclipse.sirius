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
package org.eclipse.sirius.editor.properties.tools.internal.menu;

import org.eclipse.sirius.editor.properties.internal.Messages;
import org.eclipse.sirius.editor.tools.api.menu.AbstractTypeRestrictingMenuBuilder;
import org.eclipse.sirius.properties.PropertiesPackage;

/**
 * The menu builder for the dialog buttons.
 * 
 * @author sbegaudeau
 */
public class DialogButtonMenuBuilder extends AbstractTypeRestrictingMenuBuilder {

    /**
     * Create the menu.
     */
    public DialogButtonMenuBuilder() {
        this.addValidType(PropertiesPackage.eINSTANCE.getDialogButton());
    }

    @Override
    public String getLabel() {
        return Messages.DialogButtonMenuBuilder_label;
    }

    @Override
    public int getPriority() {
        return PropertiesMenuBuilderConstants.DIALOG_BUTTON;
    }

}
