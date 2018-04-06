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
package org.eclipse.sirius.ext.emf;

import java.util.Collections;
import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;

/**
 * Adapter class to treat an <code>EObject</code> as an <code>Iterable</code> on
 * its direct contents.
 * <p>
 * Usage:
 * 
 * <pre>
 * for (EObject obj : Contents.of(root)) {
 *     // code
 * }
 * </pre>
 * 
 * @author pcdavid
 */
public final class Contents implements Iterable<EObject> {
    private final EObject root;

    /**
     * Constructor.
     * 
     * @param root
     *            the root element.
     */
    public Contents(EObject root) {
        this.root = root;
    }

    /**
     * Factory method.
     * 
     * @param obj
     *            the root element.
     * @return an iterable on all the direct sub-elements of the root.
     */
    public static Iterable<EObject> of(EObject obj) {
        return new Contents(obj);
    }

    /**
     * {@inheritDoc}
     */
    public Iterator<EObject> iterator() {
        return (root != null) ? root.eContents().iterator() : Collections.<EObject> emptyIterator();
    }
}
