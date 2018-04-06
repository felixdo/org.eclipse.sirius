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
package org.eclipse.sirius.properties.core.internal.preprocessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.common.interpreter.api.IInterpreter;
import org.eclipse.sirius.common.interpreter.api.IVariableManager;
import org.eclipse.sirius.properties.AbstractGroupDescription;
import org.eclipse.sirius.properties.GroupValidationSetDescription;
import org.eclipse.sirius.properties.PropertiesFactory;
import org.eclipse.sirius.properties.PropertiesPackage;
import org.eclipse.sirius.properties.PropertyValidationRule;
import org.eclipse.sirius.properties.core.api.OverridesProvider;
import org.eclipse.sirius.properties.core.api.PreconfiguredPreprocessor;
import org.eclipse.sirius.properties.core.api.TransformationCache;
import org.eclipse.sirius.viewpoint.description.validation.SemanticValidationRule;

/**
 * Preprocessor for {@link AbstractGroupDescription}.
 * <ul>
 * <li>The {@code filterConditionalStylesFromExtendedGroupExpression} attribute is ignored.</li>
 * <li>The {@code filterControlsFromExtendedGroupExpression} attribute is ignored.</li>
 * <li>The {@code filterValidationRulesFromExtendedGroupExpression} attribute is ignored.</li>
 * <li>The {@code validationSet} containment is handled manually.</li>
 * <li>The {@code controls} containment is handled manually.</li>
 * <li>The {@code style} containment value is copied.</li>
 * <li>The {@code conditionalStyles} containment value is copied.</li>
 * </ul>
 * 
 * @author flatombe
 * @author mbats
 */
public class GroupDescriptionPreprocessor extends PreconfiguredPreprocessor<AbstractGroupDescription> {
    /**
     * The validation set feature is handled separately.
     */
    protected static final EReference VALIDATIONSET_FEATURE = PropertiesPackage.Literals.ABSTRACT_GROUP_DESCRIPTION__VALIDATION_SET;

    /**
     * The constructor.
     */
    public GroupDescriptionPreprocessor() {
        super(AbstractGroupDescription.class, PropertiesPackage.Literals.GROUP_DESCRIPTION);
    }

    @Override
    protected void processMonoValuedEReference(EReference eReference, AbstractGroupDescription processedDescription, AbstractGroupDescription currentDescription, TransformationCache cache,
            IInterpreter interpreter, IVariableManager variableManager, OverridesProvider overridesProvider) {
        if (!eReference.equals(VALIDATIONSET_FEATURE)) {
            super.processMonoValuedEReference(eReference, processedDescription, currentDescription, cache, interpreter, variableManager, overridesProvider);
        } else {
            processValidationSet(processedDescription, currentDescription, cache, interpreter, variableManager, overridesProvider);
        }
    }

    /**
     * Special case for the validation set. A new set is created if need be. The rules of the parent description are
     * copied into the set.
     * 
     * @param processedDescription
     *            the resulting description.
     * @param currentDescription
     *            the original or parent description.
     * @param cache
     *            the processing cache.
     * @param interpreter
     *            the interpreter.
     * @param variableManager
     *            the variable manager.
     * @param overridesProvider
     *            Utility class used to provide the override descriptions.
     */
    private void processValidationSet(AbstractGroupDescription processedDescription, AbstractGroupDescription currentDescription, TransformationCache cache, IInterpreter interpreter,
            IVariableManager variableManager, OverridesProvider overridesProvider) {
        if (currentDescription.eIsSet(VALIDATIONSET_FEATURE)) {
            GroupValidationSetDescription validationSet = Optional.ofNullable(processedDescription.getValidationSet()).orElse(PropertiesFactory.eINSTANCE.createGroupValidationSetDescription());
            processedDescription.setValidationSet(validationSet);

            // Maintain the order: first the rules of the extended description,
            // then those contributed by the current description.
            // Copy all the semantic validation rules
            List<SemanticValidationRule> newSemanticValidationRules = new ArrayList<>();
            currentDescription.getValidationSet().getSemanticValidationRules().forEach(rule -> {
                if (!this.isFiltered(PropertiesPackage.eINSTANCE.getGroupValidationSetDescription_SemanticValidationRules(), processedDescription, rule, cache, interpreter, variableManager,
                        overridesProvider)) {
                    newSemanticValidationRules.add(EcoreUtil.copy(rule));
                }
            });
            newSemanticValidationRules.addAll(processedDescription.getValidationSet().getSemanticValidationRules());
            processedDescription.getValidationSet().getSemanticValidationRules().clear();
            processedDescription.getValidationSet().getSemanticValidationRules().addAll(newSemanticValidationRules);

            // Copy all the property validation rules
            List<PropertyValidationRule> newPropertyValidationRules = new ArrayList<>();
            currentDescription.getValidationSet().getPropertyValidationRules().forEach(rule -> {
                if (!this.isFiltered(PropertiesPackage.eINSTANCE.getGroupValidationSetDescription_PropertyValidationRules(), processedDescription, rule, cache, interpreter, variableManager,
                        overridesProvider)) {
                    newPropertyValidationRules.add(EcoreUtil.copy(rule));
                }
            });
            newPropertyValidationRules.addAll(processedDescription.getValidationSet().getPropertyValidationRules());
            processedDescription.getValidationSet().getPropertyValidationRules().clear();
            processedDescription.getValidationSet().getPropertyValidationRules().addAll(newPropertyValidationRules);
        }
    }
}
