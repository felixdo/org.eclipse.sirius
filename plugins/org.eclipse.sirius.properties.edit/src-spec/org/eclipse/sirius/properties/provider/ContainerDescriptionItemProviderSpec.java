/**
 * Copyright (c) 2016, 2017 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *
 */
package org.eclipse.sirius.properties.provider;

import java.util.Collection;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.provider.StyledString;
import org.eclipse.sirius.properties.DynamicMappingForDescription;
import org.eclipse.sirius.properties.PropertiesFactory;
import org.eclipse.sirius.properties.PropertiesPackage;

/**
 * Subclass used to not have to modify the generated code.
 *
 * @author sbegaudeau
 */
public class ContainerDescriptionItemProviderSpec extends ContainerDescriptionItemProvider {

    /**
     * The constructor.
     *
     * @param adapterFactory
     *            The adapter factory
     */
    public ContainerDescriptionItemProviderSpec(AdapterFactory adapterFactory) {
        super(adapterFactory);
    }

    @Override
    public String getText(Object object) {
        Object styledText = this.getStyledText(object);
        if (styledText instanceof StyledString) {
            return ((StyledString) styledText).getString();
        }
        return super.getText(object);
    }

    @Override
    public Object getStyledText(Object object) {
        return Utils.computeLabel(this, object, "_UI_ContainerDescription_type"); //$NON-NLS-1$
    }

    @Override
    protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
        newChildDescriptors.add(createChildParameter(PropertiesPackage.Literals.ABSTRACT_CONTAINER_DESCRIPTION__CONTROLS, PropertiesFactory.eINSTANCE.createContainerDescription()));

        newChildDescriptors.add(createChildParameter(PropertiesPackage.Literals.ABSTRACT_CONTAINER_DESCRIPTION__CONTROLS, PropertiesFactory.eINSTANCE.createTextDescription()));

        newChildDescriptors.add(createChildParameter(PropertiesPackage.Literals.ABSTRACT_CONTAINER_DESCRIPTION__CONTROLS, PropertiesFactory.eINSTANCE.createButtonDescription()));

        newChildDescriptors.add(createChildParameter(PropertiesPackage.Literals.ABSTRACT_CONTAINER_DESCRIPTION__CONTROLS, PropertiesFactory.eINSTANCE.createLabelDescription()));

        newChildDescriptors.add(createChildParameter(PropertiesPackage.Literals.ABSTRACT_CONTAINER_DESCRIPTION__CONTROLS, PropertiesFactory.eINSTANCE.createCheckboxDescription()));

        newChildDescriptors.add(createChildParameter(PropertiesPackage.Literals.ABSTRACT_CONTAINER_DESCRIPTION__CONTROLS, PropertiesFactory.eINSTANCE.createSelectDescription()));

        DynamicMappingForDescription dynamicFor = PropertiesFactory.eINSTANCE.createDynamicMappingForDescription();
        dynamicFor.getIfs().add(PropertiesFactory.eINSTANCE.createDynamicMappingIfDescription());
        newChildDescriptors.add(createChildParameter(PropertiesPackage.Literals.ABSTRACT_CONTAINER_DESCRIPTION__CONTROLS, dynamicFor));

        newChildDescriptors.add(createChildParameter(PropertiesPackage.Literals.ABSTRACT_CONTAINER_DESCRIPTION__CONTROLS, PropertiesFactory.eINSTANCE.createTextAreaDescription()));

        newChildDescriptors.add(createChildParameter(PropertiesPackage.Literals.ABSTRACT_CONTAINER_DESCRIPTION__CONTROLS, PropertiesFactory.eINSTANCE.createRadioDescription()));

        newChildDescriptors.add(createChildParameter(PropertiesPackage.Literals.ABSTRACT_CONTAINER_DESCRIPTION__CONTROLS, PropertiesFactory.eINSTANCE.createListDescription()));

        newChildDescriptors.add(createChildParameter(PropertiesPackage.Literals.ABSTRACT_CONTAINER_DESCRIPTION__CONTROLS, PropertiesFactory.eINSTANCE.createCustomDescription()));

        newChildDescriptors.add(createChildParameter(PropertiesPackage.Literals.ABSTRACT_CONTAINER_DESCRIPTION__CONTROLS, PropertiesFactory.eINSTANCE.createHyperlinkDescription()));

        newChildDescriptors.add(createChildParameter(PropertiesPackage.Literals.ABSTRACT_CONTAINER_DESCRIPTION__LAYOUT, PropertiesFactory.eINSTANCE.createFillLayoutDescription()));

        newChildDescriptors.add(createChildParameter(PropertiesPackage.Literals.ABSTRACT_CONTAINER_DESCRIPTION__LAYOUT, PropertiesFactory.eINSTANCE.createGridLayoutDescription()));
    }

    @Override
    protected CommandParameter createChildParameter(Object feature, Object child) {
        Utils.addNoopNavigationOperations(child);
        return super.createChildParameter(feature, child);
    }
}
