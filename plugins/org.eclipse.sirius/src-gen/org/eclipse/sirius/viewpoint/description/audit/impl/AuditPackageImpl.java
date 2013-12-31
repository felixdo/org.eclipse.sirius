/**
 * Copyright (c) 2007, 2013 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Obeo - initial API and implementation
 * 
 */
package org.eclipse.sirius.viewpoint.description.audit.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.sirius.diagram.DiagramPackage;
import org.eclipse.sirius.diagram.description.filter.FilterPackage;
import org.eclipse.sirius.diagram.description.filter.impl.FilterPackageImpl;
import org.eclipse.sirius.diagram.description.style.impl.StylePackageImpl;
import org.eclipse.sirius.diagram.impl.DiagramPackageImpl;
import org.eclipse.sirius.viewpoint.ViewpointPackage;
import org.eclipse.sirius.viewpoint.description.DescriptionPackage;
import org.eclipse.sirius.viewpoint.description.audit.AuditFactory;
import org.eclipse.sirius.viewpoint.description.audit.AuditPackage;
import org.eclipse.sirius.viewpoint.description.audit.InformationSection;
import org.eclipse.sirius.viewpoint.description.audit.TemplateInformationSection;
import org.eclipse.sirius.viewpoint.description.concern.ConcernPackage;
import org.eclipse.sirius.viewpoint.description.concern.impl.ConcernPackageImpl;
import org.eclipse.sirius.viewpoint.description.impl.DescriptionPackageImpl;
import org.eclipse.sirius.viewpoint.description.style.StylePackage;
import org.eclipse.sirius.viewpoint.description.tool.ToolPackage;
import org.eclipse.sirius.viewpoint.description.tool.impl.ToolPackageImpl;
import org.eclipse.sirius.viewpoint.description.validation.ValidationPackage;
import org.eclipse.sirius.viewpoint.description.validation.impl.ValidationPackageImpl;
import org.eclipse.sirius.viewpoint.impl.ViewpointPackageImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!--
 * end-user-doc -->
 * 
 * @generated
 */
public class AuditPackageImpl extends EPackageImpl implements AuditPackage {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass informationSectionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass templateInformationSectionEClass = null;

    /**
     * Creates an instance of the model <b>Package</b>, registered with
     * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the
     * package package URI value.
     * <p>
     * Note: the correct way to create the package is via the static factory
     * method {@link #init init()}, which also performs initialization of the
     * package, or returns the registered package, if one already exists. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.eclipse.emf.ecore.EPackage.Registry
     * @see org.eclipse.sirius.viewpoint.description.audit.AuditPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private AuditPackageImpl() {
        super(eNS_URI, AuditFactory.eINSTANCE);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private static boolean isInited = false;

    /**
     * Creates, registers, and initializes the <b>Package</b> for this model,
     * and for any others upon which it depends.
     * 
     * <p>
     * This method is used to initialize {@link AuditPackage#eINSTANCE} when
     * that field is accessed. Clients should not invoke it directly. Instead,
     * they should simply access that field to obtain the package. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static AuditPackage init() {
        if (isInited)
            return (AuditPackage) EPackage.Registry.INSTANCE.getEPackage(AuditPackage.eNS_URI);

        // Obtain or create and register package
        AuditPackageImpl theAuditPackage = (AuditPackageImpl) (EPackage.Registry.INSTANCE.get(eNS_URI) instanceof AuditPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new AuditPackageImpl());

        isInited = true;

        // Initialize simple dependencies
        EcorePackage.eINSTANCE.eClass();

        // Obtain or create and register interdependencies
        ViewpointPackageImpl theViewpointPackage = (ViewpointPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(ViewpointPackage.eNS_URI) instanceof ViewpointPackageImpl ? EPackage.Registry.INSTANCE
                .getEPackage(ViewpointPackage.eNS_URI) : ViewpointPackage.eINSTANCE);
        DescriptionPackageImpl theDescriptionPackage = (DescriptionPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(DescriptionPackage.eNS_URI) instanceof DescriptionPackageImpl ? EPackage.Registry.INSTANCE
                .getEPackage(DescriptionPackage.eNS_URI) : DescriptionPackage.eINSTANCE);
        org.eclipse.sirius.viewpoint.description.style.impl.StylePackageImpl theStylePackage = (org.eclipse.sirius.viewpoint.description.style.impl.StylePackageImpl) (EPackage.Registry.INSTANCE
                .getEPackage(StylePackage.eNS_URI) instanceof org.eclipse.sirius.viewpoint.description.style.impl.StylePackageImpl ? EPackage.Registry.INSTANCE.getEPackage(StylePackage.eNS_URI)
                : StylePackage.eINSTANCE);
        ToolPackageImpl theToolPackage = (ToolPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(ToolPackage.eNS_URI) instanceof ToolPackageImpl ? EPackage.Registry.INSTANCE
                .getEPackage(ToolPackage.eNS_URI) : ToolPackage.eINSTANCE);
        ValidationPackageImpl theValidationPackage = (ValidationPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(ValidationPackage.eNS_URI) instanceof ValidationPackageImpl ? EPackage.Registry.INSTANCE
                .getEPackage(ValidationPackage.eNS_URI) : ValidationPackage.eINSTANCE);
        ConcernPackageImpl theConcernPackage = (ConcernPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(ConcernPackage.eNS_URI) instanceof ConcernPackageImpl ? EPackage.Registry.INSTANCE
                .getEPackage(ConcernPackage.eNS_URI) : ConcernPackage.eINSTANCE);
        DiagramPackageImpl theDiagramPackage = (DiagramPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(DiagramPackage.eNS_URI) instanceof DiagramPackageImpl ? EPackage.Registry.INSTANCE
                .getEPackage(DiagramPackage.eNS_URI) : DiagramPackage.eINSTANCE);
        org.eclipse.sirius.diagram.description.impl.DescriptionPackageImpl theDescriptionPackage_1 = (org.eclipse.sirius.diagram.description.impl.DescriptionPackageImpl) (EPackage.Registry.INSTANCE
                .getEPackage(org.eclipse.sirius.diagram.description.DescriptionPackage.eNS_URI) instanceof org.eclipse.sirius.diagram.description.impl.DescriptionPackageImpl ? EPackage.Registry.INSTANCE
                .getEPackage(org.eclipse.sirius.diagram.description.DescriptionPackage.eNS_URI) : org.eclipse.sirius.diagram.description.DescriptionPackage.eINSTANCE);
        FilterPackageImpl theFilterPackage = (FilterPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(FilterPackage.eNS_URI) instanceof FilterPackageImpl ? EPackage.Registry.INSTANCE
                .getEPackage(FilterPackage.eNS_URI) : FilterPackage.eINSTANCE);
        StylePackageImpl theStylePackage_1 = (StylePackageImpl) (EPackage.Registry.INSTANCE.getEPackage(org.eclipse.sirius.diagram.description.style.StylePackage.eNS_URI) instanceof StylePackageImpl ? EPackage.Registry.INSTANCE
                .getEPackage(org.eclipse.sirius.diagram.description.style.StylePackage.eNS_URI) : org.eclipse.sirius.diagram.description.style.StylePackage.eINSTANCE);

        // Create package meta-data objects
        theAuditPackage.createPackageContents();
        theViewpointPackage.createPackageContents();
        theDescriptionPackage.createPackageContents();
        theStylePackage.createPackageContents();
        theToolPackage.createPackageContents();
        theValidationPackage.createPackageContents();
        theConcernPackage.createPackageContents();
        theDiagramPackage.createPackageContents();
        theDescriptionPackage_1.createPackageContents();
        theFilterPackage.createPackageContents();
        theStylePackage_1.createPackageContents();

        // Initialize created meta-data
        theAuditPackage.initializePackageContents();
        theViewpointPackage.initializePackageContents();
        theDescriptionPackage.initializePackageContents();
        theStylePackage.initializePackageContents();
        theToolPackage.initializePackageContents();
        theValidationPackage.initializePackageContents();
        theConcernPackage.initializePackageContents();
        theDiagramPackage.initializePackageContents();
        theDescriptionPackage_1.initializePackageContents();
        theFilterPackage.initializePackageContents();
        theStylePackage_1.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theAuditPackage.freeze();

        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(AuditPackage.eNS_URI, theAuditPackage);
        return theAuditPackage;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EClass getInformationSection() {
        return informationSectionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EClass getTemplateInformationSection() {
        return templateInformationSectionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getTemplateInformationSection_TemplatePath() {
        return (EAttribute) templateInformationSectionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public AuditFactory getAuditFactory() {
        return (AuditFactory) getEFactoryInstance();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private boolean isCreated = false;

    /**
     * Creates the meta-model objects for the package. This method is guarded to
     * have no affect on any invocation but its first. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public void createPackageContents() {
        if (isCreated)
            return;
        isCreated = true;

        // Create classes and their features
        informationSectionEClass = createEClass(INFORMATION_SECTION);

        templateInformationSectionEClass = createEClass(TEMPLATE_INFORMATION_SECTION);
        createEAttribute(templateInformationSectionEClass, TEMPLATE_INFORMATION_SECTION__TEMPLATE_PATH);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private boolean isInitialized = false;

    /**
     * Complete the initialization of the package and its meta-model. This
     * method is guarded to have no affect on any invocation but its first. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void initializePackageContents() {
        if (isInitialized)
            return;
        isInitialized = true;

        // Initialize package
        setName(eNAME);
        setNsPrefix(eNS_PREFIX);
        setNsURI(eNS_URI);

        // Obtain other dependent packages
        EcorePackage theEcorePackage = (EcorePackage) EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        templateInformationSectionEClass.getESuperTypes().add(this.getInformationSection());

        // Initialize classes and features; add operations and parameters
        initEClass(informationSectionEClass, InformationSection.class, "InformationSection", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        EOperation op = addEOperation(informationSectionEClass, theEcorePackage.getEString(), "getContent", 0, 1, IS_UNIQUE, IS_ORDERED);
        addEParameter(op, theEcorePackage.getEObject(), "eObj", 0, 1, IS_UNIQUE, IS_ORDERED);

        initEClass(templateInformationSectionEClass, TemplateInformationSection.class, "TemplateInformationSection", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getTemplateInformationSection_TemplatePath(), theEcorePackage.getEString(), "templatePath", null, 0, 1, TemplateInformationSection.class, !IS_TRANSIENT, !IS_VOLATILE,
                IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    }

} // AuditPackageImpl
