/*******************************************************************************
 * Copyright (c) 2010 THALES GLOBAL SERVICES.
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
package org.eclipse.sirius.diagram.business.internal.repair.resource.session.diagram.data;

import org.eclipse.sirius.viewpoint.description.RepresentationElementMapping;

/**
 * Lost element data with mapping.
 * 
 * @author dlecan
 */
public interface ILostElementDataWithMapping extends ILostElementDataWithTarget {

    /**
     * Sets the value of mapping to mapping.
     * 
     * @param mapping
     *            The mapping to set.
     */
    void setMapping(RepresentationElementMapping mapping);

    /**
     * Returns the mapping.
     * 
     * @return The mapping.
     */
    RepresentationElementMapping getMapping();

}
