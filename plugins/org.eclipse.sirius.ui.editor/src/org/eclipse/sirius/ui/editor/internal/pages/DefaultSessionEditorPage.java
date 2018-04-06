/*******************************************************************************
 * Copyright (c) 2017 Obeo.
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
package org.eclipse.sirius.ui.editor.internal.pages;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.sirius.business.api.modelingproject.ModelingProject;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionListener;
import org.eclipse.sirius.business.api.session.SessionStatus;
import org.eclipse.sirius.ui.editor.Messages;
import org.eclipse.sirius.ui.editor.SessionEditor;
import org.eclipse.sirius.ui.editor.SessionEditorPlugin;
import org.eclipse.sirius.ui.editor.api.pages.AbstractSessionEditorPage;
import org.eclipse.sirius.ui.editor.api.pages.PageProviderRegistry.PositioningKind;
import org.eclipse.sirius.ui.editor.api.pages.PageUpdateCommandBuilder.PageUpdateCommand;
import org.eclipse.sirius.ui.editor.internal.graphicalcomponents.GraphicalSemanticModelsHandler;
import org.eclipse.sirius.ui.tools.internal.actions.session.CloseSessionsAction;
import org.eclipse.sirius.ui.tools.internal.graphicalcomponents.GraphicalRepresentationHandler;
import org.eclipse.sirius.ui.tools.internal.graphicalcomponents.GraphicalRepresentationHandler.GraphicalRepresentationHandlerBuilder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.handlers.CollapseAllHandler;
import org.eclipse.ui.internal.navigator.actions.CollapseAllAction;
import org.eclipse.ui.internal.navigator.filters.FilterActionGroup;
import org.eclipse.ui.internal.navigator.filters.SelectFiltersAction;
import org.eclipse.ui.navigator.CommonViewer;
import org.eclipse.ui.navigator.INavigatorViewerDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Class used to create the main page of the session editor which describe
 * Viewpoints used, model and representations.
 * 
 * @author jmallet
 * @author <a href="mailto:pierre.guilet@obeo.fr">Pierre Guilet</a>
 *
 */
public class DefaultSessionEditorPage extends AbstractSessionEditorPage implements SessionListener {

    /**
     * Delimiter used to separate text part for the page title.
     */
    private static final String DELIMITER = "/"; //$NON-NLS-1$

    /**
     * Current editor.
     */
    protected SessionEditor editor;

    /**
     * Session to describe and edit.
     */
    protected Session session;

    /**
     * This graphical component provides a viewer showing all semantic models
     * loaded in the given session.
     */
    private GraphicalSemanticModelsHandler graphicalSemanticModelsHandler;

    /**
     * Label used to provides information regarding editor's context.
     */
    private FormText informativeLabel;

    /**
     * The graphical component providing a viewer showing all representations
     * belonging to the given session under corresponding viewpoints objects
     */
    private GraphicalRepresentationHandler graphicalRepresentationHandler;

    /**
     * Allow to collapse tree item of the models block viewer.
     */
    private CollapseAllHandler collapseAllHandler;

    /**
     * Filter group for CNF filtering functionality on models block viewer.
     */
    private FilterActionGroup filterActionGroup;

    /**
     * Constructor.
     * 
     * @param theEditor
     *            the editor.
     */
    public DefaultSessionEditorPage(SessionEditor theEditor) {
        this(theEditor, SessionEditorPlugin.DEFAULT_PAGE_ID, Messages.UI_SessionEditor_default_page_tab_label);
    }

    /**
     * A constructor that creates the page and initializes it with the editor.
     *
     * @param editor
     *            the parent editor
     * @param id
     *            the unique identifier
     * @param title
     *            the page title
     */
    public DefaultSessionEditorPage(SessionEditor editor, String id, String title) {
        super(editor, id, title);
        this.session = editor.getSession();
        this.editor = editor;
    }

    @Override
    protected void createFormContent(final IManagedForm managedForm) {
        super.createFormContent(managedForm);
        final ScrolledForm scrolledForm = managedForm.getForm();

        if (ResourcesPlugin.getWorkspace() != null && ResourcesPlugin.getWorkspace().getRoot() != null && session.getSessionResource() != null && session.getSessionResource().getURI() != null) {
            IProject sessionProject = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(session.getSessionResource().getURI().toPlatformString(true))).getProject();
            if (!ModelingProject.hasModelingProjectNature(sessionProject)) {
                initEditorHeaderToolbar(managedForm);
            }
        }

        FormToolkit toolkit = managedForm.getToolkit();

        int sessionUriSegmentSize = session.getSessionResource().getURI().segmentsList().size();
        scrolledForm.setText(MessageFormat.format(Messages.UI_SessionEditor_header_title,
                session.getSessionResource().getURI().segmentsList().subList(1, sessionUriSegmentSize).stream().collect(Collectors.joining(DELIMITER)))); // $NON-NLS-1$
        toolkit.decorateFormHeading(scrolledForm.getForm());

        Composite body = managedForm.getForm().getBody();
        body.setLayout(GridLayoutFactory.swtDefaults().create());

        informativeLabel = toolkit.createFormText(body, false);
        informativeLabel.addHyperlinkListener(new HyperlinkAdapter() {
            @Override
            public void linkActivated(HyperlinkEvent e) {
                try {
                    PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser().openURL(new URL(e.getHref().toString()));
                } catch (PartInitException | MalformedURLException ex) {
                    SessionEditorPlugin.getPlugin().error("An error occured while opening the external web browser.", ex); //$NON-NLS-1$
                }
            }

        });
        informativeLabel.setForeground(body.getDisplay().getSystemColor(SWT.COLOR_RED));

        Composite subBody = toolkit.createComposite(body);
        subBody.setLayout(GridLayoutFactory.swtDefaults().numColumns(2).equalWidth(false).create());
        subBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        createModelsControl(toolkit, subBody);

        Composite rightComposite = toolkit.createComposite(subBody);
        rightComposite.setLayout(GridLayoutFactory.swtDefaults().margins(5, 0).create());
        rightComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        createRepresentationsControl(toolkit, rightComposite);

        session.addListener(this);
        // needed when opening editor from explorer views or scrollbar is not
        // visible if needed.
        scrolledForm.reflow(true);

    }

    /**
     * Add the button allowing to close the session and thus the aird editor in
     * the editor's header.
     * 
     * @param managedForm
     *            the form from which we retrieve the toolbar manager to add the
     *            button.
     */
    private void initEditorHeaderToolbar(IManagedForm managedForm) {
        CloseSessionsAction closeSessionsAction = new CloseSessionsAction(Messages.DefaultSessionEditorPage_closeSession_action_label, session);
        closeSessionsAction.setToolTipText(Messages.DefaultSessionEditorPage_closeSession_action_tooltip);
        managedForm.getForm().getToolBarManager().add(closeSessionsAction);
        managedForm.getForm().getToolBarManager().update(true);
    }

    /**
     * Create the control allowing to view the semantic models of the session.
     * 
     * @param toolkit
     *            the tool allowing to create form UI component.
     * @param subBody
     *            the composite containing the viewpoint selection control.
     */
    protected void createModelsControl(FormToolkit toolkit, Composite subBody) {
        Section modelSection = toolkit.createSection(subBody, Section.TITLE_BAR);
        modelSection.setLayout(GridLayoutFactory.swtDefaults().create());
        modelSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

        modelSection.setText(Messages.UI_SessionEditor_models_title);

        Composite modelSectionClient = toolkit.createComposite(modelSection, SWT.NONE);
        modelSectionClient.setLayout(GridLayoutFactory.swtDefaults().numColumns(2).create());
        modelSectionClient.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        modelSection.setClient(modelSectionClient);

        graphicalSemanticModelsHandler = createGraphicalSemanticModelsHandler(toolkit);
        graphicalSemanticModelsHandler.createControl(modelSectionClient);
        getSite().setSelectionProvider(graphicalSemanticModelsHandler.getTreeViewer());

        initModelSectionToolbar(modelSection, graphicalSemanticModelsHandler.getTreeViewer());
    }

    /**
     * Initialize the {@link GraphicalSemanticModelsHandler} used for the aird
     * editor.
     * 
     * @param toolkit
     *            the tool allowing to create form UI component.
     * @return the {@link GraphicalSemanticModelsHandler} used for the aird
     *         editor.
     */
    protected GraphicalSemanticModelsHandler createGraphicalSemanticModelsHandler(FormToolkit toolkit) {
        return new GraphicalSemanticModelsHandler(session, toolkit, ((IEditorSite) editor.getSite()).getActionBars(), ((IEditorSite) editor.getSite()).getSelectionProvider(), this.getSite());
    }

    /**
     * Init the collapse all and customize view(CNF filter and content provider)
     * buttons in the section toolbar.
     * 
     * @param section
     *            the section that will have the buttons initialized.
     * @param treeViewer
     *            {@link TreeViewer} impacted by collapsing and CNF buttons.
     */
    private void initModelSectionToolbar(Section section, CommonViewer treeViewer) {
        ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT);
        ToolBar toolbar = toolBarManager.createControl(section);
        final Cursor handCursor = Display.getCurrent().getSystemCursor(SWT.CURSOR_HAND);
        toolbar.setCursor(handCursor);

        INavigatorViewerDescriptor viewerDescriptor = treeViewer.getNavigatorContentService().getViewerDescriptor();

        boolean hideCollapseAllAction = viewerDescriptor.getBooleanConfigProperty(INavigatorViewerDescriptor.PROP_HIDE_COLLAPSE_ALL_ACTION);
        if (!hideCollapseAllAction) {
            Action collapseAllAction = new CollapseAllAction(treeViewer);
            collapseAllAction.setToolTipText(Messages.DefaultSessionEditorPage_collapseAllAction_tooltip);
            ImageDescriptor collapseAllIcon = getImageDescriptor("collapseall.png"); //$NON-NLS-1$
            collapseAllAction.setImageDescriptor(collapseAllIcon);
            collapseAllAction.setHoverImageDescriptor(collapseAllIcon);
            collapseAllHandler = new CollapseAllHandler(treeViewer);

            toolBarManager.add(collapseAllAction);
        }

        filterActionGroup = new FilterActionGroup(treeViewer);
        Action selectFiltersAction = new SelectFiltersAction(treeViewer, filterActionGroup);
        selectFiltersAction.setToolTipText(Messages.DefaultSessionEditorPage_selectFilterAction_tooltip);
        ImageDescriptor selectFiltersIcon = getImageDescriptor("filter_ps.png"); //$NON-NLS-1$
        selectFiltersAction.setImageDescriptor(selectFiltersIcon);
        selectFiltersAction.setHoverImageDescriptor(selectFiltersIcon);
        toolBarManager.add(selectFiltersAction);

        toolBarManager.update(true);
        section.setTextClient(toolbar);
    }

    /**
     * Returns the image descriptor with the given relative path.
     * 
     * @param relativePath
     *            the path to the icon.
     * @return the image descriptor with the given relative path.
     */
    protected final ImageDescriptor getImageDescriptor(String relativePath) {
        return AbstractUIPlugin.imageDescriptorFromPlugin(SessionEditorPlugin.ID, "icons/" + relativePath); //$NON-NLS-1$
    }

    /**
     * Create the control allowing to view/create/remove representations of the
     * session.
     * 
     * @param toolkit
     *            the tool allowing to create form UI component.
     * @param rightComposite
     *            the composite containing the viewpoint selection control.
     */
    protected void createRepresentationsControl(FormToolkit toolkit, Composite rightComposite) {
        Section representationSection = toolkit.createSection(rightComposite, Section.TITLE_BAR);
        representationSection.setLayout(GridLayoutFactory.swtDefaults().create());
        representationSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        representationSection.setText(MessageFormat.format(Messages.UI_SessionEditor_representation_title, new Object[0])); // $NON-NLS-1$

        Composite representationSectionClient = toolkit.createComposite(representationSection, SWT.NONE);
        representationSectionClient.setLayout(GridLayoutFactory.swtDefaults().numColumns(2).create());
        representationSectionClient.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        representationSection.setClient(representationSectionClient);
        GraphicalRepresentationHandlerBuilder graphicalRepresentationHandlerBuilder = new GraphicalRepresentationHandler.GraphicalRepresentationHandlerBuilder(session);
        graphicalRepresentationHandler = graphicalRepresentationHandlerBuilder.activateBrowserWithViewpointAndRepresentationDescriptionInformation().activateGroupingByCheckbox()
                .activateRepresentationAndViewpointControls().useToolkitToCreateGraphicComponents(toolkit).activateShowDisabledViewpointsCheckbox().build();
        graphicalRepresentationHandler.createControl(representationSectionClient);
        graphicalRepresentationHandler.initInput();

    }

    @Override
    public void doSave(IProgressMonitor monitor) {
        if (session != null) {
            session.getTransactionalEditingDomain().getCommandStack().execute(new RecordingCommand(session.getTransactionalEditingDomain()) {
                @Override
                protected void doExecute() {
                    session.save(monitor);
                }
            });
        }
    }

    @Override
    public boolean isDirty() {
        return session != null && session.getStatus() == SessionStatus.DIRTY;
    }

    @Override
    public void notify(int changeKind) {
        switch (changeKind) {
        case SessionListener.SYNC:
            PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
                @Override
                public void run() {
                    getManagedForm().commit(true);
                    getManagedForm().dirtyStateChanged();
                }
            });
            break;
        case SessionListener.DIRTY:
            PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
                @Override
                public void run() {
                    getManagedForm().dirtyStateChanged();
                }
            });
            break;
        default:
            break;
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (session != null) {
            session.removeListener(this);
            session = null;
        }
        if (graphicalSemanticModelsHandler != null) {
            graphicalSemanticModelsHandler.dispose();
            graphicalSemanticModelsHandler = null;
        }
        if (graphicalRepresentationHandler != null) {
            graphicalRepresentationHandler.dispose();
            graphicalRepresentationHandler = null;
        }
        if (collapseAllHandler != null) {
            collapseAllHandler.dispose();
            collapseAllHandler = null;
        }
        if (filterActionGroup != null) {
            filterActionGroup.dispose();
            filterActionGroup = null;
        }
    }

    @Override
    public Optional<String> getLocationId() {
        return Optional.empty();
    }

    @Override
    public Optional<PositioningKind> getPositioning() {
        return Optional.empty();
    }

    @Override
    public Optional<PageUpdateCommand> resourceSetChanged(ResourceSetChangeEvent resourceSetChangeEvent) {
        return Optional.empty();
    }

    @Override
    public Optional<PageUpdateCommand> pageChanged(boolean isVisible) {
        return Optional.empty();
    }

}
