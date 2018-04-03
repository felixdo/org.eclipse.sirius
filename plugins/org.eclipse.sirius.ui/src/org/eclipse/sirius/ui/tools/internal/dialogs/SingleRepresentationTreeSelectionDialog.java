/*******************************************************************************
 * Copyright (c) 2018 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Felix Dorner <felix.dorner@gmail.com> - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.ui.tools.internal.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.ui.tools.internal.graphicalcomponents.GraphicalRepresentationHandler;
import org.eclipse.sirius.ui.tools.internal.graphicalcomponents.GraphicalRepresentationHandler.GraphicalRepresentationHandlerBuilder;
import org.eclipse.sirius.ui.tools.internal.views.common.item.RepresentationItemImpl;
import org.eclipse.sirius.ui.tools.internal.views.common.navigator.SiriusCommonContentProvider;
import org.eclipse.sirius.viewpoint.DRepresentationDescriptor;
import org.eclipse.sirius.viewpoint.provider.Messages;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * Dialog with OK and Cancel buttons, showing representations of a given session in a tree viewer. The OK button is
 * enabled if exactly one representation is selected.
 */
public class SingleRepresentationTreeSelectionDialog extends Dialog {

    private final Session session;

    private DRepresentationDescriptor selected;

    private TreeViewer viewer;

    /**
     * Create a new Dialog for the given session.
     * 
     * @param parentShell
     *            The parent shell
     * @param session
     *            The session from which the dialog contents will be derived
     */
    public SingleRepresentationTreeSelectionDialog(Shell parentShell, Session session) {
        super(parentShell);
        this.session = session;
    }

    @Override
    protected Point getInitialSize() {
        return new Point(500, 340);
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.SingleRepresentationTreeSelectionDialog_Title);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite compo = (Composite) super.createDialogArea(parent);
        GraphicalRepresentationHandlerBuilder builder = new GraphicalRepresentationHandlerBuilder(session);
        builder.activateHideBrowser().customizeContentAndLabel(new SiriusCommonContentProvider(session, null), null).filterEmptyViewpoints().filterEmptyRepresentationDescriptors();
        GraphicalRepresentationHandler handler = builder.build();
        handler.createControl(compo);
        handler.initInput();
        viewer = handler.getTreeViewer();
        return compo;
    }

    @Override
    protected Control createContents(Composite parent) {
        Control result = super.createContents(parent);
        getButton(IDialogConstants.OK_ID).setEnabled(false);
        viewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                if (selection.size() == 1 && selection.getFirstElement() instanceof RepresentationItemImpl) {
                    selected = ((RepresentationItemImpl) selection.getFirstElement()).getDRepresentationDescriptor();
                } else {
                    selected = null;
                }
                getButton(IDialogConstants.OK_ID).setEnabled(selected != null);
            }
        });
        return result;
    }

    /**
     * Get the selected representation.
     * 
     * @return the selected representation
     */
    public DRepresentationDescriptor getResult() {
        return selected;
    }

}
