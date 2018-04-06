/*******************************************************************************
 * Copyright (c) 2010, 2017 THALES GLOBAL SERVICES.
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
package org.eclipse.sirius.tests.unit.api.vsm.editor;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.sirius.editor.tools.internal.assist.TypeAssistant;

import junit.framework.TestCase;

/**
 * Standalone test for {@link TypeAssistant}. See VP-2811.
 * 
 * @author <a href="mailto:esteban.dugueperoux@obeo.fr">Esteban Dugueperoux</a>
 */
public class TypeAssistantTests extends TestCase {

    private Registry registryStub;

    private TypeAssistant typeAssistant;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        registryStub = new RegistryStub();
        typeAssistant = new TypeAssistant(registryStub);
    }

    /**
     * Test that {@link TypeAssistant} doesn't return proposals for a empty
     * {@link Registry}.
     */
    public void testProposalsWithEmptyEPackageRegistry() {
        assertTrue("If the registry is empty we souldn't have proposals", typeAssistant.proposal("EClass").isEmpty());
    }

    /**
     * Test that {@link TypeAssistant} return 2 proposals for a "ECla" prefix
     * having the EcorePackage in the {@link Registry}.
     */
    public void testProposalsWithoutEPackageNamePrefix() {
        registryStub.put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
        List<EClassifier> proposals = typeAssistant.proposal("ECla");
        assertEquals("with ECla as beginning we should have 2 proposals", 2, proposals.size());
        assertEquals("with ECla as beginning we should have EClass proposal", EcorePackage.Literals.ECLASS, proposals.get(0));
        assertEquals("with ECla as beginning we should have EClassifier proposal", EcorePackage.Literals.ECLASSIFIER, proposals.get(1));
    }

    /**
     * Test that {@link TypeAssistant} return 2 proposals for a "ecore.ECla"
     * prefix having the EcorePackage in the {@link Registry}.
     */
    public void testProposalsWithEPackageNamePrefix() {
        registryStub.put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
        List<EClassifier> proposals = typeAssistant.proposal("ecore.ECla");
        assertEquals("with ECla as beginning we should have 2 proposals", 2, proposals.size());
        assertEquals("with ECla as beginning we should have EClass proposal", EcorePackage.Literals.ECLASS, proposals.get(0));
        assertEquals("with ECla as beginning we should have EClassifier proposal", EcorePackage.Literals.ECLASSIFIER, proposals.get(1));
    }

    /**
     * Test that {@link TypeAssistant} return 53 proposals for a "ecore:" prefix
     * having the EcorePackage in the {@link Registry}.
     */
    public void testAddProposalsWithAQLSyntaxSeparator() {
        registryStub.put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
        List<EClassifier> proposals = typeAssistant.proposal("ecore:");
        assertEquals("with ':' as beginning we should have 20 proposals", 20, proposals.size());
    }

    /**
     * Test that {@link TypeAssistant} return 53 proposals for a "ecore::"
     * prefix having the EcorePackage in the {@link Registry}.
     */
    public void testAddProposalsWithAQLSyntaxSeparator2() {
        registryStub.put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
        List<EClassifier> proposals = typeAssistant.proposal("ecore::");
        assertEquals("with ':' as beginning we should have 20 proposals", 20, proposals.size());
    }

    /**
     * Test that {@link TypeAssistant} return 1 proposal for a "OneT" prefix
     * having the EPackage with name to null in the {@link Registry}.
     */
    public void testProposalsWithEPackageNameNull() {
        EPackage ePackageWithNullName = EcoreFactory.eINSTANCE.createEPackage();
        ePackageWithNullName.setNsURI("http://www.example.com/dsl/viewpoint/tests/TypeAssistantTests/0.1.0");
        EClass eClass = EcoreFactory.eINSTANCE.createEClass();
        eClass.setName("OneType");
        ePackageWithNullName.getEClassifiers().add(eClass);
        registryStub.put(ePackageWithNullName.getNsURI(), ePackageWithNullName);

        List<EClassifier> proposals = typeAssistant.proposal("OneT");
        assertEquals("with OneT as beginning we should have 1 proposals", 1, proposals.size());
        assertEquals("with OneT as beginning we should have OneType proposal", eClass, proposals.get(0));
    }

    /**
     * Test that {@link EEnum} are not proposed from {@link TypeAssistant}
     * because Sirius does not handle it.
     */
    public void testProposalsWithEEnum() {
        EPackage ePackage = EcoreFactory.eINSTANCE.createEPackage();
        ePackage.setName("EPackage");
        ePackage.setNsURI("http://www.example.com/dsl/viewpoint/tests/TypeAssistantTests/0.1.0");
        EEnum eEnum = EcoreFactory.eINSTANCE.createEEnum();
        eEnum.setName("OneType");
        ePackage.getEClassifiers().add(eEnum);
        registryStub.put(ePackage.getNsURI(), ePackage);

        List<EClassifier> proposals = typeAssistant.proposal("OneT");
        assertEquals("EEnum should not be proposed.", 0, proposals.size());
    }

    /**
     * Test that {@link EDatatype} are not proposed from {@link TypeAssistant}
     * because Sirius does not handle it.
     */
    public void testProposalsWithEDatatype() {
        EPackage ePackage = EcoreFactory.eINSTANCE.createEPackage();
        ePackage.setName("EPackage");
        ePackage.setNsURI("http://www.example.com/dsl/viewpoint/tests/TypeAssistantTests/0.1.0");
        EDataType eDatatype = EcoreFactory.eINSTANCE.createEDataType();
        eDatatype.setName("OneType");
        ePackage.getEClassifiers().add(eDatatype);
        registryStub.put(ePackage.getNsURI(), ePackage);

        List<EClassifier> proposals = typeAssistant.proposal("OneT");
        assertEquals("EEnum should not be proposed.", 0, proposals.size());
    }

    @Override
    protected void tearDown() throws Exception {
        registryStub = null;
        typeAssistant = null;
        super.tearDown();
    }
}
