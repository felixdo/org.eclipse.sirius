/*******************************************************************************
 * Copyright (c) 2017 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.properties.editor.properties.sections.properties.dialogbutton;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.sirius.editor.properties.sections.common.AbstractCheckBoxPropertySection;
import org.eclipse.sirius.properties.PropertiesPackage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * A section for the close dialog on click property of a Dialog Button object.
 */
public class DialogButtonCloseDialogOnClickPropertySectionSpec extends AbstractCheckBoxPropertySection {
    @Override
    protected String getDefaultLabelText() {
        return "CloseDialogOnClick"; //$NON-NLS-1$
    }

    @Override
    protected String getLabelText() {
        String labelText;
        labelText = super.getLabelText() + ":"; //$NON-NLS-1$
        // Start of user code get label text

        // End of user code get label text
        return labelText;
    }

    @Override
    protected EAttribute getFeature() {
        return PropertiesPackage.eINSTANCE.getDialogButton_CloseDialogOnClick();
    }

    @Override
    protected String getDefaultFeatureAsText() {
        String value = new String();
        if (eObject.eGet(getFeature()) != null) {
            value = toBoolean(eObject.eGet(getFeature()).toString()).toString();
        }
        return value;
    }

    @Override
    protected Object getFeatureValue(String newText) {
        return toBoolean(newText);
    }

    @Override
    protected boolean isEqual(String newText) {
        boolean equal = true;
        if (toBoolean(newText) != null) {
            equal = getFeatureAsText().equals(toBoolean(newText).toString());
        } else {
            refresh();
        }
        return equal;
    }

    /**
     * Converts the given text to the boolean it represents if applicable.
     *
     * @return The boolean the given text represents if applicable, <code>null</code> otherwise.
     */
    private Boolean toBoolean(String text) {
        Boolean booleanValue = null;
        if (text.toLowerCase().matches("true|false")) {
            booleanValue = Boolean.parseBoolean(text);
        }
        return booleanValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createControls(Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage) {
        super.createControls(parent, tabbedPropertySheetPage);
    }
}
