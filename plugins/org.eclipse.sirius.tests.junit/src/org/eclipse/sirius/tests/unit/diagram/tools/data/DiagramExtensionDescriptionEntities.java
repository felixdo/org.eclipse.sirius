/*******************************************************************************
 * Copyright (c) 2010, 2014 THALES GLOBAL SERVICES.
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
package org.eclipse.sirius.tests.unit.diagram.tools.data;

import org.eclipse.sirius.diagram.description.AdditionalLayer;
import org.eclipse.sirius.diagram.description.DiagramExtensionDescription;

public class DiagramExtensionDescriptionEntities {

    private DiagramExtensionDescription val;

    public DiagramExtensionDescriptionEntities(DiagramExtensionDescription val) {
        this.val = val;
    }

    public AdditionalLayerDocumentation documentation() {
        return new AdditionalLayerDocumentation((AdditionalLayer) val.getLayers().get(0));
    }

    public DiagramExtensionDescription object() {
        return this.val;
    }
}
