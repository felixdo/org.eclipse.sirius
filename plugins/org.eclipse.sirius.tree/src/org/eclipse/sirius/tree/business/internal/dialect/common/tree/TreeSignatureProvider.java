/*******************************************************************************
 * Copyright (c) 2015 THALES GLOBAL SERVICES and others.
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
package org.eclipse.sirius.tree.business.internal.dialect.common.tree;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.synchronizer.IntegerProvider;
import org.eclipse.sirius.synchronizer.MappingHiearchy;
import org.eclipse.sirius.synchronizer.MappingHiearchyTable;
import org.eclipse.sirius.synchronizer.OutputDescriptor;
import org.eclipse.sirius.synchronizer.Signature;
import org.eclipse.sirius.synchronizer.SignatureProvider;
import org.eclipse.sirius.synchronizer.StringSignature;
import org.eclipse.sirius.tree.tools.internal.Messages;

/**
 * A {@link SignatureProvider}.
 * 
 * @author cbrun
 */
class TreeSignatureProvider implements SignatureProvider {

    private Map<String, Signature> allSignatures = new HashMap<>();

    private MappingHiearchyTable hierarchyTable;

    /**
     * Default constructor.
     * 
     * @param hierarchyTable
     *            a {@link MappingHiearchyTable}
     */
    TreeSignatureProvider(MappingHiearchyTable hierarchyTable) {
        this.hierarchyTable = hierarchyTable;
    }

    @Override
    public Signature getSignature(OutputDescriptor descriptor) {
        if (descriptor instanceof OutputTreeItemDescriptor) {
            return doGetSignature((OutputTreeItemDescriptor) descriptor);
        } else if (descriptor instanceof OutputDTreeDescriptor) {
            return doGetSignature((OutputDTreeDescriptor) descriptor);
        }
        throw new RuntimeException(MessageFormat.format(Messages.DTreeRefresh_unknownDescriptor, descriptor));

    }

    private Signature doGetSignature(OutputDTreeDescriptor desc) {
        return getOrCreate("tree " + getURI(desc.getSourceElement()) + desc.getMapping().toString()); //$NON-NLS-1$
    }

    private Signature getOrCreate(String string) {
        Signature existing = allSignatures.get(string);
        if (existing == null) {
            existing = new StringSignature(string);
            allSignatures.put(string, existing);
        }
        return existing;
    }

    private String getURI(EObject sourceElement) {
        return EcoreUtil.getURI(sourceElement).toString();
    }

    private Signature doGetSignature(OutputTreeItemDescriptor desc) {
        String sourceID = desc.getSourceElement() == null ? String.valueOf(desc.hashCode()) : IntegerProvider.getInteger(desc.getSourceElement()).toString();
        String containerID = IntegerProvider.getInteger(desc.getViewContainer()).toString();
        Collection<MappingHiearchy> hierarchy = hierarchyTable.getHierarchy(desc.getMapping());
        return getOrCreate(hierarchy + sourceID + containerID);
    }

}
