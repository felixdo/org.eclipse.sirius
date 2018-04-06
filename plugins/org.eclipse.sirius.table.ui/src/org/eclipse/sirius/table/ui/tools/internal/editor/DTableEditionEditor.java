/*******************************************************************************
 * Copyright (c) 2007, 2008, 2009 THALES GLOBAL SERVICES.
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
package org.eclipse.sirius.table.ui.tools.internal.editor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.sirius.table.metamodel.table.provider.TableUIPlugin;
import org.eclipse.swt.graphics.Image;

/**
 * Editor for a DTable with a description of type EditionTable.
 * 
 * @author <a href="mailto:laurent.redor@obeo.fr">Laurent Redor</a>
 */
public class DTableEditionEditor extends AbstractDTableEditor {
    /**
     * The editor ID.
     */
    public static final String ID = "org.eclipse.sirius.table.ui.EditionTableEditorID"; //$NON-NLS-1$

    /** Initial title image descriptor **/
    private static final ImageDescriptor INITIAL_TITLE_IMAGE_DESCRIPTOR = TableUIPlugin.Implementation.getBundledImageDescriptor("icons/full/obj16/EditionTableDescription.gif"); //$NON-NLS-1$

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.sirius.ui.tools.internal.editor.AbstractDTreeEditor#getInitialImage()
     */
    public Image getInitialImage() {
        if (initialTitleImage == null || initialTitleImage.isDisposed()) {
            initialTitleImage = TableUIPlugin.Implementation.getImage(INITIAL_TITLE_IMAGE_DESCRIPTOR);
        }
        return initialTitleImage;
    }
}
