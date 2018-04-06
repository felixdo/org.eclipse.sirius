/*******************************************************************************
 * Copyright (c) 2018 Obeo
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.diagram.internal.description.provider;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.diagram.description.CustomLayoutConfiguration;
import org.eclipse.sirius.diagram.description.EnumLayoutOption;
import org.eclipse.sirius.diagram.description.EnumLayoutValue;
import org.eclipse.sirius.diagram.description.EnumSetLayoutOption;
import org.eclipse.sirius.diagram.description.LayoutOption;
import org.eclipse.sirius.diagram.description.provider.EnumSetLayoutOptionItemProvider;
import org.eclipse.sirius.diagram.ui.api.layout.CustomLayoutAlgorithm;
import org.eclipse.sirius.diagram.ui.provider.DiagramUIPlugin;

/**
 * Customize the label of {@link EnumLayoutOption} items in VSM editor. Also override child descriptor to propose
 * available {@link EnumLayoutValue} with their attributes already filled.
 * 
 * @author <a href="mailto:pierre.guilet@obeo.fr">Pierre Guilet</a>
 *
 */
public class EnumSetLayoutOptionItemProviderSpec extends EnumSetLayoutOptionItemProvider {

    /**
     * Default constructor.
     * 
     * @param adapterFactory
     *            factory to use.
     */
    public EnumSetLayoutOptionItemProviderSpec(AdapterFactory adapterFactory) {
        super(adapterFactory);
    }

    @Override
    public String getText(Object object) {
        LayoutOption layout = (LayoutOption) object;
        return layout.getLabel();
    }

    @Override
    protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
        Map<String, CustomLayoutAlgorithm> layoutProviderRegistry = DiagramUIPlugin.getPlugin().getLayoutAlgorithms();
        EnumSetLayoutOption layoutOption = (EnumSetLayoutOption) object;
        CustomLayoutConfiguration layout = (CustomLayoutConfiguration) layoutOption.eContainer();
        CustomLayoutAlgorithm genericLayoutProviderSupplier = layoutProviderRegistry.get(layout.getId());
        Map<String, LayoutOption> layoutOptions = genericLayoutProviderSupplier.getLayoutOptions();
        EnumSetLayoutOption layoutOptionTemplate = (EnumSetLayoutOption) layoutOptions.get(layoutOption.getId());
        EList<EnumLayoutValue> choices = layoutOptionTemplate.getChoices();
        for (EnumLayoutValue enumLayoutValue : choices) {
            newChildDescriptors.add(createChildParameter(org.eclipse.sirius.diagram.description.DescriptionPackage.Literals.ENUM_SET_LAYOUT_OPTION__VALUES, EcoreUtil.copy(enumLayoutValue)));
        }
    }

    @Override
    public String getCreateChildText(Object owner, Object feature, Object child, Collection<?> selection) {
        if (child instanceof EnumLayoutValue) {
            return ((EnumLayoutValue) child).getName();
        }

        return super.getCreateChildText(owner, feature, child, selection);
    }

}
