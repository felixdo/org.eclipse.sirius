/*******************************************************************************
 * Copyright (c) 2010, 2018 THALES GLOBAL SERVICES and others.
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
package org.eclipse.sirius.tests.unit.api.session;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.sirius.business.api.componentization.ViewpointRegistry;
import org.eclipse.sirius.ui.business.api.viewpoint.ViewpointSelection;
import org.eclipse.sirius.viewpoint.description.DescriptionFactory;
import org.eclipse.sirius.viewpoint.description.Viewpoint;

import junit.framework.TestCase;

public class ViewpointSelectionTests extends TestCase {

    /**
     * {@inheritDoc}
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        /* init all */
        ViewpointSelection.getViewpoints("*");
    }

    public void testFileExtensionWithoutPattern() {
        Set<Viewpoint> createdViewpoints = new HashSet<Viewpoint>();
        createdViewpoints.add(createViewpoint("lolita"));

        ViewpointRegistry.getInstance().registerFromWorkspace(createdViewpoints);

        Set<Viewpoint> viewpoints = ViewpointSelection.getViewpoints("lolita");
        assertFalse(viewpoints.isEmpty());
    }

    public void testMultipleFileExtension() {
        Set<Viewpoint> createdViewpoints = new HashSet<Viewpoint>();
        createdViewpoints.add(createViewpoint("lolita tatata       tutu"));

        ViewpointRegistry.getInstance().registerFromWorkspace(createdViewpoints);

        Set<Viewpoint> viewpoints = ViewpointSelection.getViewpoints("lolita");
        assertFalse(viewpoints.isEmpty());
        StringBuilder sb = new StringBuilder("ViewpointSelection.getViewpoints('lolita')\n");
        viewpoints.stream().forEach(vp -> sb.append(vp + "\n"));
        assertEquals(sb.toString(), 1, viewpoints.size());
        
        viewpoints = ViewpointSelection.getViewpoints("tatata");
        assertFalse(viewpoints.isEmpty());
        assertEquals(1, viewpoints.size());
        
        viewpoints = ViewpointSelection.getViewpoints("tutu");
        assertFalse(viewpoints.isEmpty());
        assertEquals(1, viewpoints.size());
        
        viewpoints = ViewpointSelection.getViewpoints("toto");
        assertTrue(viewpoints.isEmpty());
    }

    public void testFileExtensionWithPattern() {
        Set<Viewpoint> createdViewpoints = new HashSet<Viewpoint>();
        createdViewpoints.add(createViewpoint("*.treztattaaaazzz"));

        ViewpointRegistry.getInstance().registerFromWorkspace(createdViewpoints);

        Set<Viewpoint> viewpoints = ViewpointSelection.getViewpoints("treztattaaaazzz");
        assertFalse(viewpoints.isEmpty());
    }

    public void testMultipleFileExtensionWithPattern() {
        Set<Viewpoint> createdViewpoints = new HashSet<Viewpoint>();
        createdViewpoints.add(createViewpoint("*.treztattaaaazzz toto *.tutu   tata  "));

        ViewpointRegistry.getInstance().registerFromWorkspace(createdViewpoints);

        Set<Viewpoint> viewpoints = ViewpointSelection.getViewpoints("treztattaaaazzz");
        StringBuilder sb = new StringBuilder("ViewpointSelection.getViewpoints('treztattaaaazzz')\n");
        viewpoints.stream().forEach(vp -> sb.append(vp + "\n"));

        assertFalse(viewpoints.isEmpty());
        assertEquals(sb.toString(), 1, viewpoints.size());
        
        viewpoints = ViewpointSelection.getViewpoints("toto");
        assertFalse(viewpoints.isEmpty());
        assertEquals(1, viewpoints.size());
        
        viewpoints = ViewpointSelection.getViewpoints("tutu");
        assertFalse(viewpoints.isEmpty());
        assertEquals(1, viewpoints.size());
        
        viewpoints = ViewpointSelection.getViewpoints("tata");
        assertFalse(viewpoints.isEmpty());
        assertEquals(1, viewpoints.size());
        
        viewpoints = ViewpointSelection.getViewpoints("titi");
        assertTrue(viewpoints.isEmpty());
    }

    private Viewpoint createViewpoint(String modelExtension) {
        Viewpoint vp = DescriptionFactory.eINSTANCE.createViewpoint();
        vp.setModelFileExtension(modelExtension);
        return vp;
    }

}
