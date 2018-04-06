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
package org.eclipse.sirius.tests.swtbot;

import org.eclipse.draw2d.geometry.Point;

/**
 * Same tests as {@link DNodeListCreationTest} but with snapToGrid enabled.
 * 
 * @author <a href="mailto:laurent.redor@obeo.fr">Laurent Redor</a>
 */
public class DNodeListCreationWithSnapToGridTest extends DNodeListCreationTest {

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        editor.setSnapToGrid(true, 100, 2);
    }

    @Override
    protected Point adaptLocation(Point expectedAbsoluteLocation) {
        return editor.adaptLocationToSnap(expectedAbsoluteLocation);
    }
}
