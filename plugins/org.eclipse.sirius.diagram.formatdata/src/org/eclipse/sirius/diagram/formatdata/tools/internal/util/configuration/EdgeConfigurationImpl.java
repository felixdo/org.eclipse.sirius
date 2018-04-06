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
package org.eclipse.sirius.diagram.formatdata.tools.internal.util.configuration;

import java.text.MessageFormat;

import org.eclipse.sirius.diagram.formatdata.tools.Messages;
import org.eclipse.sirius.diagram.formatdata.tools.api.util.configuration.EdgeConfiguration;

/**
 * Implementation of
 * {@link org.eclipse.sirius.diagram.formatdata.tools.api.util.configuration.EdgeConfiguration}
 * .
 * 
 * @author dlecan
 */
public class EdgeConfigurationImpl implements EdgeConfiguration {

    private static final double DEFAULT_DISTANCE_AROUND = 0;

    private double distance = DEFAULT_DISTANCE_AROUND;

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDistanceAroundPointsOfEdgeBendpointsList(double pDistance) {
        this.distance = pDistance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final double getDistanceAroundPointsOfEdgeBendpointsList() {
        return distance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MessageFormat.format(Messages.EdgeConfigurationImpl_toString, getDistanceAroundPointsOfEdgeBendpointsList());
    }

}
