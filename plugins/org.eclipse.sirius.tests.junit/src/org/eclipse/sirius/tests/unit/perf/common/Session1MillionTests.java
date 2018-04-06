/*******************************************************************************
 * Copyright (c) 2016, 2017 Obeo.
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
package org.eclipse.sirius.tests.unit.perf.common;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.BinaryResourceImpl;
import org.eclipse.emf.ecore.resource.impl.BinaryResourceImpl.BinaryIO.Version;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryRegistryImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.tests.SiriusTestsPlugin;
import org.eclipse.sirius.tests.support.api.SiriusDiagramTestCase;
import org.eclipse.sirius.tests.support.api.TestsUtil;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Iterators;

/**
 * Tests working with a Session of almost 1 million model elements.
 * 
 * @author cbrun
 */
public class Session1MillionTests extends SiriusDiagramTestCase {
    /**
     * limit is set empirically.
     */
    private static final long MAX_TIME_TO_OPEN_SECONDS = 20;

    private static final long MAX_TIME_TO_CLOSE_SECONDS = 15;

    private static final int NUMBER_Of_ELEMENTS = 966220;

    private static final String[] SEMANTIC_ROOTS = IntStream.range(1, 21).mapToObj(i -> String.format("/reverse%d.ecorebin", i)).collect(Collectors.toList()).toArray(new String[0]);
    
    private static final String AIRD_ROOT = "/representations.aird";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ResourceFactoryRegistryImpl.INSTANCE.getExtensionToFactoryMap().put("ecorebin", new EcoreBinResourceFactory());
        copyFilesToTestProject(SiriusTestsPlugin.PLUGIN_ID, "/data/unit/session/1Modeling", SEMANTIC_ROOTS);
        copyFilesToTestProject(SiriusTestsPlugin.PLUGIN_ID, "/data/unit/session/1Modeling", AIRD_ROOT);
    }

    private void createAndOpenSession() throws Exception {
        Collection<String> rootsInWorkspace = new LinkedHashSet<>();
        for (String semanticRoot : SEMANTIC_ROOTS) {
            rootsInWorkspace.add(TEMPORARY_PROJECT_NAME + semanticRoot);
        }
        genericSetUp(rootsInWorkspace, Collections.<String> emptySet(), TEMPORARY_PROJECT_NAME + AIRD_ROOT);
    }

    /**
     * Measure the creation and opening of a session instance with a lot of
     * model elements and make sure this time is not bigger than expected.
     * 
     * @throws Exception
     */
    public void testSessionCreationTime() throws Exception {
        if (TestsUtil.shouldSkipUnreliableTests()) {
            // On some IC server the time is very variable and exceeds the
            // expected time.
            return;
        }
        Stopwatch openingtimer = Stopwatch.createStarted();
        createAndOpenSession();
        openingtimer.stop();
        long elapsedTime = openingtimer.elapsed(TimeUnit.SECONDS);
        int semanticElementsCount = 0;
        Stopwatch eAllContentsTimer = Stopwatch.createStarted();
        for (Resource r : session.getSemanticResources()) {
            if (r.getURI().isPlatformResource()) {
                Iterator<EObject> it = Iterators.filter(EcoreUtil.getAllContents(r, false), EObject.class);
                while (it.hasNext()) {
                    it.next();
                    semanticElementsCount++;
                }
            }
        }
        System.out.println("[PERFO] Loading a project with " + semanticElementsCount + " semantic elements in " + elapsedTime + " seconds.");
        eAllContentsTimer.stop();
        System.out.println("[PERFO] Iterating a project with " + semanticElementsCount + " semantic elements in " + eAllContentsTimer.elapsed(TimeUnit.MILLISECONDS) + " milliseconds.");

        assertEquals("The number of semantic elements in the project is not the expected one", NUMBER_Of_ELEMENTS, semanticElementsCount);
        assertTrue("The time required to open the session (" + elapsedTime + " secs) is bigger than expected (" + MAX_TIME_TO_OPEN_SECONDS + ").", elapsedTime < MAX_TIME_TO_OPEN_SECONDS);

        Stopwatch closingtimer = Stopwatch.createStarted();
        session.close(new NullProgressMonitor());
        closingtimer.stop();
        System.out.println("[PERFO] Closing a project with " + semanticElementsCount + " semantic elements in " + closingtimer.elapsed(TimeUnit.SECONDS) + " seconds.");
        assertFalse("The session should have been closed and is not reporting it is", session.isOpen());
        elapsedTime = closingtimer.elapsed(TimeUnit.SECONDS);
        assertTrue("The time required to close the session (" + elapsedTime + " secs) is bigger than expected (" + MAX_TIME_TO_CLOSE_SECONDS + ").", elapsedTime < MAX_TIME_TO_CLOSE_SECONDS);

    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        ResourceFactoryRegistryImpl.INSTANCE.getExtensionToFactoryMap().remove("ecorebin");
    }

    class EcoreBinResourceFactory extends ResourceFactoryImpl {

        @Override
        public Resource createResource(URI uri) {
            EcoreBinResourceImpl result = new EcoreBinResourceImpl(uri);
            return result;
        }
    }

    class EcoreBinResourceImpl extends BinaryResourceImpl {

        public EcoreBinResourceImpl(URI uri) {
            super(uri);
            this.defaultLoadOptions = new HashMap<Object, Object>();
            this.defaultLoadOptions.put(OPTION_VERSION, Version.VERSION_1_1);
            this.defaultSaveOptions = new HashMap<Object, Object>();
            this.defaultSaveOptions.put(OPTION_VERSION, Version.VERSION_1_1);
        }

    }

}
