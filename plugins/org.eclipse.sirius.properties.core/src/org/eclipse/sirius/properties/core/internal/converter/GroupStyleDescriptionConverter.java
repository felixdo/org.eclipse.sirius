/*******************************************************************************
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
 *******************************************************************************/
package org.eclipse.sirius.properties.core.internal.converter;

import java.text.MessageFormat;
import java.util.Map;

import org.eclipse.eef.EEFGroupStyle;
import org.eclipse.eef.EEF_TITLE_BAR_STYLE;
import org.eclipse.eef.EEF_TOGGLE_STYLE;
import org.eclipse.eef.EefFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.properties.GroupStyle;
import org.eclipse.sirius.properties.core.api.AbstractDescriptionConverter;
import org.eclipse.sirius.properties.core.api.TransformationCache;
import org.eclipse.sirius.properties.core.internal.Messages;
import org.eclipse.sirius.viewpoint.description.ColorDescription;

/**
 * This class is used to convert the style of a group.
 * 
 * @author sbegaudeau
 */
public class GroupStyleDescriptionConverter extends AbstractDescriptionConverter {

    @Override
    public boolean canHandle(EObject description) {
        return description instanceof GroupStyle;
    }

    @Override
    public EObject convert(EObject description, Map<String, Object> parameters, TransformationCache cache) {
        if (description instanceof GroupStyle) {
            GroupStyle groupStyle = (GroupStyle) description;

            EEFGroupStyle eefGroupStyle = EefFactory.eINSTANCE.createEEFGroupStyle();
            ColorDescription backgroundColorDescription = groupStyle.getBackgroundColor();
            if (backgroundColorDescription != null) {
                String backgroundColorExpression = this.getColorExpression(backgroundColorDescription, parameters);
                if (backgroundColorExpression != null) {
                    eefGroupStyle.setBackgroundColorExpression(backgroundColorExpression);
                }
            }
            ColorDescription foregroundColorDescription = groupStyle.getForegroundColor();
            if (foregroundColorDescription != null) {
                String foregroundColorExpression = this.getColorExpression(foregroundColorDescription, parameters);
                if (foregroundColorExpression != null) {
                    eefGroupStyle.setForegroundColorExpression(foregroundColorExpression);
                }
            }
            eefGroupStyle.setFontNameExpression(groupStyle.getFontNameExpression());
            eefGroupStyle.setFontSizeExpression(groupStyle.getFontSizeExpression());
            eefGroupStyle.setBarStyle(EEF_TITLE_BAR_STYLE.get(groupStyle.getBarStyle().getValue()));
            eefGroupStyle.setToggleStyle(EEF_TOGGLE_STYLE.get(groupStyle.getToggleStyle().getValue()));
            eefGroupStyle.setExpandedByDefault(groupStyle.isExpandedByDefault());

            cache.put(description, eefGroupStyle);

            return eefGroupStyle;
        } else {
            throw new IllegalArgumentException(MessageFormat.format(Messages.IDescriptionConverter_InvalidDescriptionType, description.getClass().getName(), GroupStyle.class.getName()));
        }
    }

}
