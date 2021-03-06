/**
 * Copyright (c) 2007, 2017 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *
 */
package org.eclipse.sirius.diagram.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.diagram.DiagramPackage;
import org.eclipse.sirius.diagram.GraphicalFilter;
import org.eclipse.sirius.viewpoint.impl.IdentifiedElementImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Graphical Filter</b></em>'. <!-- end-user-doc
 * -->
 *
 * @generated
 */
public abstract class GraphicalFilterImpl extends IdentifiedElementImpl implements GraphicalFilter {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected GraphicalFilterImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return DiagramPackage.Literals.GRAPHICAL_FILTER;
    }

} // GraphicalFilterImpl
