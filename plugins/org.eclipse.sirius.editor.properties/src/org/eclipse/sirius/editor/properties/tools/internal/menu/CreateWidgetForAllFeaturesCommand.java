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

import java.text.MessageFormat;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.editor.properties.api.DefaultWidgetDescription;
import org.eclipse.sirius.editor.properties.api.IDefaultWidgetDescriptionFactory;
import org.eclipse.sirius.editor.properties.internal.Messages;
import org.eclipse.sirius.editor.properties.internal.SiriusEditorPropertiesPlugin;
import org.eclipse.sirius.editor.tools.api.menu.AbstractUndoRecordingCommand;
import org.eclipse.sirius.properties.ContainerDescription;
import org.eclipse.sirius.properties.GroupDescription;
import org.eclipse.sirius.properties.core.internal.EditSupportSpec;

/**
 * Creates all the widget descriptions with values for each structural feature of the selected domain class.
 * 
 * @author sbegaudeau
 * @author arichard
 */
public class CreateWidgetForAllFeaturesCommand extends AbstractUndoRecordingCommand {

    /**
     * The descriptor.
     */
    private CreateWidgetForAllFeaturesDescriptor descriptor;

    /**
     * Create a new command.
     * 
     * @param resourceSet
     *            The current resourceSet.
     * @param descriptor
     *            The descriptor
     */
    public CreateWidgetForAllFeaturesCommand(ResourceSet resourceSet, CreateWidgetForAllFeaturesDescriptor descriptor) {
        super(resourceSet);
        this.descriptor = descriptor;
    }

    @Override
    protected void doExecute() {
        this.descriptor.getDomainClass().getEAllStructuralFeatures().stream().filter(EditSupportSpec::shouldAppearInPropertySheet).forEach(eStructuralFeature -> {
            List<IDefaultWidgetDescriptionFactory> factories = SiriusEditorPropertiesPlugin.getPlugin().getDefaultWidgetDescriptionFactory(this.descriptor.getDomainClass(), eStructuralFeature);
            if (factories.size() >= 1) {
                IDefaultWidgetDescriptionFactory factory = factories.get(0);
                DefaultWidgetDescription defaultWidgetDescription = factory.create(this.descriptor.getDomainClass(), eStructuralFeature);

                EObject controlsContainerDescription = this.descriptor.getControlsContainerDescription();
                if (controlsContainerDescription instanceof GroupDescription) {
                    ((GroupDescription) controlsContainerDescription).getControls().add(defaultWidgetDescription.getWidgetDescription());
                } else if (controlsContainerDescription instanceof ContainerDescription) {
                    ((ContainerDescription) controlsContainerDescription).getControls().add(defaultWidgetDescription.getWidgetDescription());
                }
            }
        });
    }

    @Override
    protected String getText() {
        return MessageFormat.format(Messages.CreateWidgetForAllFeaturesCommand_text, this.descriptor.getDomainClass().getEPackage().getName(), this.descriptor.getDomainClass().getName());
    }

}
