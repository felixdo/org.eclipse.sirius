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
package org.eclipse.sirius.diagram.ui.graphical.edit.policies;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.common.core.command.AbstractCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.OpenDiagramEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.ui.business.api.dialect.DialectUIManager;
import org.eclipse.sirius.viewpoint.DRepresentationDescriptor;

public class OpenDRepresentationEditPolicy extends OpenDiagramEditPolicy {

    @Override
    protected Command getOpenCommand(Request request) {
        EditPart targetEditPart = getTargetEditPart(request);
        if (targetEditPart instanceof IGraphicalEditPart) {
            IGraphicalEditPart editPart = (IGraphicalEditPart) targetEditPart;
            View view = editPart.getNotationView();
            if (view != null) {
                EObject element = ViewUtil.resolveSemanticElement(view);
                if (element instanceof DRepresentationDescriptor) {
                    return new ICommandProxy(new OpenSiriusDiagramCommand((DRepresentationDescriptor) element));
                }
            }
        }
        return null;
    }

    public static class OpenSiriusDiagramCommand extends AbstractCommand {

        /** Remember the element to be opened. */
        private DRepresentationDescriptor element;

        /**
         * Create an instance.
         * 
         * @param element
         *            to be opened.
         */
        public OpenSiriusDiagramCommand(DRepresentationDescriptor element) {
            super(DiagramUIMessages.Command_openDiagram);
            this.element = element;
        }

        /**
         * returns the element associated with that command.
         * 
         * @return the element associated with that command
         */
        protected DRepresentationDescriptor getElement() {
            return element;
        }

        @Override
        protected CommandResult doExecuteWithResult(IProgressMonitor progressMonitor, IAdaptable info) throws ExecutionException {
            Session session = SessionManager.INSTANCE.getSession(EcoreUtil.getRootContainer(getElement()).eResource().getURI(), new NullProgressMonitor());
            DialectUIManager.INSTANCE.openEditor(session, getElement().getRepresentation(), new NullProgressMonitor());
            return CommandResult.newOKCommandResult();
        }

        @Override
        public boolean canUndo() {
            return false;
        }

        @Override
        public boolean canRedo() {
            return false;
        }

        @Override
        protected CommandResult doRedoWithResult(IProgressMonitor progressMonitor, IAdaptable info) throws ExecutionException {
            return null;
        }

        @Override
        protected CommandResult doUndoWithResult(IProgressMonitor progressMonitor, IAdaptable info) throws ExecutionException {
            return null;
        }

    }

}
