/*******************************************************************************
 * Copyright (c) 2009, 2009 THALES GLOBAL SERVICES.
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
package org.eclipse.sirius.table.tools.api.interpreter;

import org.eclipse.sirius.common.tools.api.interpreter.IInterpreterSiriusVariables;

/**
 * Sirius variables strings (specific for table).
 * 
 * @author <a href="mailto:laurent.redor@obeo.fr">Laurent Redor</a>
 * @since 0.9.0
 */
public interface IInterpreterSiriusTableVariables extends IInterpreterSiriusVariables {
    /**
     * the line element.
     */
    String LINE = "line"; //$NON-NLS-1$

    /**
     * The column element.
     */
    String COLUMN = "column"; //$NON-NLS-1$

    /**
     * the line semantic element.
     */
    String LINE_SEMANTIC = "lineSemantic"; //$NON-NLS-1$

    /**
     * The column semantic element.
     */
    String COLUMN_SEMANTIC = "columnSemantic"; //$NON-NLS-1$
}
