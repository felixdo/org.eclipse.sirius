/*******************************************************************************
 * Copyright (c) 2017, 2018 THALES GLOBAL SERVICES and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.diagram.ui.tools.internal.editor.tabbar;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.sirius.common.ui.tools.api.util.EclipseUIUtil;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.ui.provider.DiagramUIPlugin;
import org.eclipse.sirius.diagram.ui.provider.Messages;
import org.eclipse.sirius.diagram.ui.tools.api.image.DiagramImagesPath;
import org.eclipse.sirius.diagram.ui.tools.api.ui.actions.ActionIds;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.EditorActionBarContributor;

/**
 * An action that switches the showing mode of the given {@link DDiagram}.
 * 
 * @author <a href="mailto:pierre.guilet@obeo.fr">Pierre Guilet</a>
 *
 */
public class ShowingModeSwitchingAction extends DiagramAction {

    /**
     * Icon used in the tabbar to allow end-user to activate the showing mode. It is also used in the editor's status
     * line to indicate that showing mode is active.
     */
    private static final ImageDescriptor ACTIVATE_SHOW_HIDE_MODE_IMAGE_DESCRIPTOR = DiagramUIPlugin.Implementation.getBundledImageDescriptor(DiagramImagesPath.SHOWING_MODE_ACTIVE_ICON);

    /**
     * The {@link DDiagram} on witch the showing mode should be switched.
     */
    private DDiagram ddiagram;

    /**
     * Default constructor.
     *
     * @param iWorkbenchPart
     *            The workbench part associated with this action
     * @param editorDiagram
     *            the {@link DDiagram} on witch the showing mode should be switched
     */
    public ShowingModeSwitchingAction(IWorkbenchPart iWorkbenchPart, DDiagram editorDiagram) {
        super(iWorkbenchPart);
        setId(ActionIds.SWITCH_SHOWING_MODE);
        this.ddiagram = editorDiagram;
        if (ddiagram != null) {
            setImageDescriptor(ACTIVATE_SHOW_HIDE_MODE_IMAGE_DESCRIPTOR);
            setTextAndStatusAccordingToShowingMode();
        }
    }

    /**
     * Switches the text associated to this Action according to the current ShowingMode status (activated or not) and
     * updates this editor's statusLine.
     */
    private void setTextAndStatusAccordingToShowingMode() {
        // Step 1 : updating action's text and image
        setText(Messages.ShowingModeSwitchingAction_label);

        // Step 2 : updating the editor's status bar
        IEditorPart activeEditor = EclipseUIUtil.getActiveEditor();
        if (activeEditor != null) {
            // Step 2.1 : trying to get the status line manager
            IEditorSite site = (IEditorSite) activeEditor.getSite();
            EditorActionBarContributor actionBarContributor = null;
            IStatusLineManager statusLineManager = null;
            if (site != null && site.getActionBarContributor() instanceof EditorActionBarContributor) {
                actionBarContributor = (EditorActionBarContributor) site.getActionBarContributor();
            }
            if (actionBarContributor != null && actionBarContributor.getActionBars() != null) {
                statusLineManager = actionBarContributor.getActionBars().getStatusLineManager();
            }

            // Step 2.2 : if statusLineManager can be found
            if (statusLineManager != null) {
                // we update it according to the DDiagram showing mode status
                if (this.ddiagram != null && this.ddiagram.isIsInShowingMode()) {
                    String statusMessage = Messages.ShowingModeSwitchingAction_statusOn;
                    statusLineManager.setMessage(DiagramUIPlugin.getPlugin().getImage(ACTIVATE_SHOW_HIDE_MODE_IMAGE_DESCRIPTOR), statusMessage);
                } else {
                    statusLineManager.setMessage(""); //$NON-NLS-1$
                }
            }
        }
    }

    @Override
    public int getStyle() {
        return AS_UNSPECIFIED;
    }

    @Override
    protected boolean isSelectionListener() {
        return false;
    }

    @Override
    protected Command getCommand() {
        Command returnedCommand = UnexecutableCommand.INSTANCE;
        returnedCommand = switchShowingModeCommand(ddiagram);

        return returnedCommand;
    }

    /**
     * Returns a command that deactivate the showing mode of the given {@link DDiagram}.
     *
     * @param diagram
     *            the {@link DDiagram} on witch the showing mode should be switched
     * @return a command that deactivate the showing mode of the given {@link DDiagram}
     */
    private Command switchShowingModeCommand(DDiagram diagram) {
        Command returnedCommand = UnexecutableCommand.INSTANCE;
        TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(diagram);
        if (editingDomain != null) {
            String commandLabel = diagram.isIsInShowingMode() ? Messages.SetShowingModeCommandAndUpdateActionImage_deactivateLabel : Messages.SetShowingModeCommandAndUpdateActionImage_activateLabel;
            returnedCommand = new ICommandProxy(new SetLayoutingModeCommand(TransactionUtil.getEditingDomain(ddiagram), ddiagram, false))
                    .chain(new ICommandProxy(new SetShowingModeCommand(editingDomain, diagram, commandLabel, true) {

                        @Override
                        protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
                            CommandResult doExecuteWithResult = super.doExecuteWithResult(monitor, info);
                            if (IStatus.OK == doExecuteWithResult.getStatus().getCode()) {
                                setTextAndStatusAccordingToShowingMode();
                            }
                            return doExecuteWithResult;
                        }

                    }));
        }
        return returnedCommand;
    }

    @Override
    public void dispose() {
        ddiagram = null;
        super.dispose();
    }

    @Override
    protected Request createTargetRequest() {
        return null;
    }

}
