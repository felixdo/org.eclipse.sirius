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
 * Configuration for edges.
 * 
 * @author dlecan
 */
public interface EdgeConfiguration {

    /**
     * Set the distance to use to compute edge bendpoints list format.
     * 
     * @param distance
     *            Number such as distance (with
     *            {@link org.eclipse.draw2d.geometry.Point#getDistance(org.eclipse.draw2d.geometry.Point)}
     *            ) between original point and new point will be less or equal
     *            than the provided distance.
     */
    void setDistanceAroundPointsOfEdgeBendpointsList(double distance);

    /**
     * Returns the distance.
     * 
     * @return The distance.
     */
    double getDistanceAroundPointsOfEdgeBendpointsList();

}
