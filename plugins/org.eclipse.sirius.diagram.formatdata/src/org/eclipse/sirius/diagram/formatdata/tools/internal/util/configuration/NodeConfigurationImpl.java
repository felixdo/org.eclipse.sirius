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
import org.eclipse.sirius.diagram.formatdata.tools.api.util.configuration.NodeConfiguration;

/**
 * Implementation of {@link NodeConfiguration}.
 * 
 * @author dlecan
 */
public class NodeConfigurationImpl implements NodeConfiguration {

    private static final double DEFAULT_DISTANCE_AROUND_POINT = 0;

    private double distanceAroundPoint = DEFAULT_DISTANCE_AROUND_POINT;

    /**
     * {@inheritDoc}
     */
    @Override
    public final double getDistanceAroundPoint() {
        return distanceAroundPoint;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setDistanceAroundPoint(double distanceAroundPoint) {
        this.distanceAroundPoint = distanceAroundPoint;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MessageFormat.format(Messages.NodeConfigurationImpl_distanceAroundPoint, getDistanceAroundPoint());
    }

}
