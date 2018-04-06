/*******************************************************************************
 * Copyright (c) 2010, 2016 THALES GLOBAL SERVICES.
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
package org.eclipse.sirius.diagram.formatdata.tools.api.util.configuration;

/**
 * Configuration for nodes.
 * 
 * @author dlecan
 */
public interface NodeConfiguration {

    /**
     * Returns the distanceAroundPoint.
     * 
     * @return The distanceAroundPoint.
     */
    double getDistanceAroundPoint();

    /**
     * Sets the value of distanceAroundPoint to distanceAroundPoint.
     * 
     * @param distanceAroundPoint
     *            The distanceAroundPoint to set.
     */
    void setDistanceAroundPoint(double distanceAroundPoint);

}
