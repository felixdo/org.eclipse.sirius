/*******************************************************************************
 * Copyright (c) 2007, 2013 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.diagram.editor.properties.sections.description.edgemapping;

// Start of user code imports
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.sirius.diagram.description.DescriptionPackage;
import org.eclipse.sirius.editor.properties.sections.common.AbstractCheckBoxPropertySection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

// End of user code imports

/**
 * A section for the useDomainElement property of a EdgeMapping object.
 */
public class EdgeMappingUseDomainElementPropertySection extends AbstractCheckBoxPropertySection {
    /**
     * @see org.eclipse.sirius.diagram.editor.properties.sections.AbstractCheckBoxPropertySection#getDefaultLabelText()
     */
    protected String getDefaultLabelText() {
        return "UseDomainElement"; //$NON-NLS-1$
    }

    /**
     * @see org.eclipse.sirius.diagram.editor.properties.sections.AbstractCheckBoxPropertySection#getLabelText()
     */
    protected String getLabelText() {
        String labelText;
        labelText = super.getLabelText() + ":"; //$NON-NLS-1$
        // Start of user code get label text

        // End of user code get label text
        return labelText;
    }

    /**
     * @see org.eclipse.sirius.diagram.editor.properties.sections.AbstractCheckBoxPropertySection#getFeature()
     */
    protected EAttribute getFeature() {
        return DescriptionPackage.eINSTANCE.getEdgeMapping_UseDomainElement();
    }

    /**
     * @see org.eclipse.sirius.diagram.editor.properties.sections.AbstractCheckBoxPropertySection#getFeatureAsInteger()
     */
    protected String getDefaultFeatureAsText() {
        String value = new String();
        if (eObject.eGet(getFeature()) != null)
            value = toBoolean(eObject.eGet(getFeature()).toString()).toString();
        return value;
    }

    /**
     * @see org.eclipse.sirius.diagram.editor.properties.sections.AbstractCheckBoxPropertySection#getFeatureValue(int)
     */
    protected Object getFeatureValue(String newText) {
        return toBoolean(newText);
    }

    /**
     * @see org.eclipse.sirius.diagram.editor.properties.sections.AbstractCheckBoxPropertySection#isEqual(int)
     */
    protected boolean isEqual(String newText) {
        boolean equal = true;
        if (toBoolean(newText) != null)
            equal = getFeatureAsText().equals(toBoolean(newText).toString());
        else
            refresh();
        return equal;
    }

    /**
     * Converts the given text to the boolean it represents if applicable.
     * 
     * @return The boolean the given text represents if applicable,
     *         <code>null</code> otherwise.
     */
    private Boolean toBoolean(String text) {
        Boolean booleanValue = null;
        if (text.toLowerCase().matches("true|false"))
            booleanValue = Boolean.parseBoolean(text);
        return booleanValue;
    }

    /**
     * {@inheritDoc}
     */
    public void createControls(Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage) {
        super.createControls(parent, tabbedPropertySheetPage);
        checkbox.setToolTipText(
                "Trigger usage of a domain class representing an Edge or just a reference. If you set it to true, the Edge research will start from the domain instances of the given type and look for target/source nodes accordingly, if you set to false, the research will start from the source nodes and look for corresponding target nodes.");

        CLabel help = getWidgetFactory().createCLabel(composite, "");
        FormData data = new FormData();
        data.top = new FormAttachment(checkbox, 0, SWT.TOP);
        data.left = new FormAttachment(nameLabel);
        help.setLayoutData(data);
        help.setImage(getHelpIcon());
        help.setToolTipText(
                "Trigger usage of a domain class representing an Edge or just a reference. If you set it to true, the Edge research will start from the domain instances of the given type and look for target/source nodes accordingly, if you set to false, the research will start from the source nodes and look for corresponding target nodes.");
    }
}
