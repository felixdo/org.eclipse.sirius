/*******************************************************************************
 * Copyright (c) 2012, 2016 THALES GLOBAL SERVICES.
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
package org.eclipse.sirius.ui.tools.internal.views.modelexplorer;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.sirius.ui.tools.internal.views.common.action.RenameRepresentationAction;
import org.eclipse.sirius.ui.tools.internal.views.common.item.RepresentationItemImpl;
import org.eclipse.sirius.viewpoint.DRepresentationDescriptor;

import com.google.common.collect.Iterables;

/**
 * An handler which redirect to the appropriate rename action depending on the
 * selection.
 * 
 * @author lredor
 */
public class RenameActionHandler extends Action {

    private ISelectionProvider selectionProvider;

    /**
     * Create a new instance.
     * 
     * @param selectionProvider
     *            the selection provider
     */
    public RenameActionHandler(ISelectionProvider selectionProvider) {
        this.selectionProvider = selectionProvider;
        this.selectionProvider.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(SelectionChangedEvent event) {

                setEnabled(!getRepresentationDescriptors().isEmpty());
            }

        });
    }

    private Collection<DRepresentationDescriptor> getRepresentationDescriptors() {
        ISelection selection = selectionProvider.getSelection();
        if (selection instanceof IStructuredSelection) {
            Collection<?> selections = ((IStructuredSelection) selection).toList();
            if (selections != null && !selections.isEmpty()) {
                Collection<DRepresentationDescriptor> selectedRepDescriptors = new LinkedHashSet<>();
                Iterables.addAll(selectedRepDescriptors, Iterables.filter(selections, DRepresentationDescriptor.class));
                Iterables.addAll(selectedRepDescriptors, Iterables.transform(Iterables.filter(selections, RepresentationItemImpl.class), RepresentationItemImpl.REPRESENTATION_ITEM_TO_REPRESENTATION));
                return selectedRepDescriptors;
            }
        }
        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        Action renameAction = new RenameRepresentationAction(getRepresentationDescriptors());
        renameAction.run();
    }

}
