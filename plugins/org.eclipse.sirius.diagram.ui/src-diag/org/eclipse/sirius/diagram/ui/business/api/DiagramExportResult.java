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
package org.eclipse.sirius.diagram.ui.business.api;

import java.util.Collection;

import org.eclipse.core.runtime.IPath;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.ui.business.api.dialect.ExportResult;

/**
 * An {@link ExportResult} with additional diagram-specific metadata.
 *
 * @author pcdavid
 */
public class DiagramExportResult extends ExportResult {
    private final double scalingFactor;

    /**
     * The blank margin that was used to generate the image.
     */
    private int margin;

    /**
     * Create a new DiagramExportResult.
     *
     * @param diagram
     *            the diagram the was exported.
     * @param scalingFactor
     *            the scaling factor that was used when exporting the diagram.
     * @param exportedFiles
     *            the files produced by this export.
     */
    public DiagramExportResult(DDiagram diagram, double scalingFactor, Collection<IPath> exportedFiles) {
        super(diagram, exportedFiles);
        this.scalingFactor = scalingFactor;
        margin = -1;
    }

    /**
     * Create a new DiagramExportResult.
     *
     * @param diagram
     *            the diagram the was exported.
     * @param scalingFactor
     *            the scaling factor that was used when exporting the diagram.
     * @param margin
     *            the blank margin that was used to generate the image.
     * @param exportedFiles
     *            the files produced by this export.
     */
    public DiagramExportResult(DDiagram diagram, double scalingFactor, int margin, Collection<IPath> exportedFiles) {
        this(diagram, scalingFactor, exportedFiles);
        this.margin = margin;
    }

    /**
     * Returns the scaling factor that was used when exporting the diagram. 1.0 represents a 100% zoom level, 2.0 is
     * 200% zoom level, etc.
     *
     * @return the scaling factor that was used when exporting the diagram.
     */
    public double getScalingFactor() {
        return scalingFactor;
    }

    /**
     * Returns the blank margin that was used to generate the image.
     * 
     * @return the blank margin that was used to generate the image. -1 if information about margin is unavailable.
     */
    public int getMargin() {
        return margin;
    }
}
