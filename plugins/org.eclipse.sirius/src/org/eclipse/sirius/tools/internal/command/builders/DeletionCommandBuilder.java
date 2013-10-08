/*******************************************************************************
 * Copyright (c) 2009, 2012 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.tools.internal.command.builders;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import org.eclipse.sirius.common.tools.api.interpreter.EvaluationException;
import org.eclipse.sirius.common.tools.api.interpreter.IInterpreter;
import org.eclipse.sirius.common.tools.api.interpreter.IInterpreterSiriusVariables;
import org.eclipse.sirius.common.tools.api.util.Option;
import org.eclipse.sirius.common.tools.api.util.StringUtil;
import org.eclipse.sirius.business.api.helper.SiriusUtil;
import org.eclipse.sirius.business.api.helper.task.AbstractCommandTask;
import org.eclipse.sirius.business.api.helper.task.InitInterpreterVariablesTask;
import org.eclipse.sirius.business.api.helper.task.RemoveDanglingReferencesTask;
import org.eclipse.sirius.business.api.helper.task.RemoveSemanticDanglingReferenceTask;
import org.eclipse.sirius.business.api.helper.task.UnexecutableTask;
import org.eclipse.sirius.business.api.logger.RuntimeLoggerManager;
import org.eclipse.sirius.business.api.preferences.SiriusPreferencesKeys;
import org.eclipse.sirius.business.api.query.EObjectQuery;
import org.eclipse.sirius.business.api.query.IEdgeMappingQuery;
import org.eclipse.sirius.business.api.query.IdentifiedElementQuery;
import org.eclipse.sirius.business.internal.helper.task.DeleteDDiagramElementTask;
import org.eclipse.sirius.business.internal.helper.task.DeleteDDiagramTask;
import org.eclipse.sirius.business.internal.helper.task.DeleteSeveralDDiagramElementsTask;
import org.eclipse.sirius.tools.api.command.DCommand;
import org.eclipse.sirius.tools.api.command.NoNullResourceCommand;
import org.eclipse.sirius.tools.api.command.semantic.RemoveDanglingReferences;
import org.eclipse.sirius.tools.api.interpreter.InterpreterUtil;
import org.eclipse.sirius.viewpoint.DDiagram;
import org.eclipse.sirius.viewpoint.DDiagramElement;
import org.eclipse.sirius.viewpoint.DEdge;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;
import org.eclipse.sirius.viewpoint.EdgeTarget;
import org.eclipse.sirius.viewpoint.SiriusPlugin;
import org.eclipse.sirius.viewpoint.description.DiagramElementMapping;
import org.eclipse.sirius.viewpoint.description.EdgeMapping;
import org.eclipse.sirius.viewpoint.description.tool.AbstractVariable;
import org.eclipse.sirius.viewpoint.description.tool.DeleteElementDescription;
import org.eclipse.sirius.viewpoint.description.tool.ToolPackage;

/**
 * .
 * 
 * @author mchauvin
 */
public class DeletionCommandBuilder extends AbstractCommandBuilder {

    /** The default label for the enclosing command. */
    protected static final String DELETE = "Delete";

    /** The label for delete from diagram. */
    protected static final String DELETE_FROM_DIAGRAM_LABEL = "Delete from diagram";

    /** The label for delete from model without tool. */
    protected static final String DELETE_FROM_MODEL = "Delete from model ";

    /** The label for delete diagram. */
    protected static final String DELETE_DIAGRAM_LABEL = "Delete diagram";

    /**
     * The diagram to delete.
     */
    private DDiagram diagram;

    /**
     * The diagram element to delete.
     */
    private DDiagramElement diagramElement;

    /**
     * The tool if there is one.
     */
    private DeleteElementDescription tool;

    private boolean deleteFromDiagram;

    /**
     * Create a deletion command builder not to create a command, but only to
     * add delete diagram tasks.
     */
    public DeletionCommandBuilder() {
    }

    /**
     * Create a deletion command builder to delete a diagram.
     * 
     * @param diagram
     *            the diagram to delete
     */
    public DeletionCommandBuilder(final DDiagram diagram) {
        this.diagram = diagram;
    }

    /**
     * Create a deletion command builder to delete a diagram element.
     * 
     * @param diagramElement
     *            the diagram element to delete
     * @param deleteFromDiagram
     *            define if the delete should be a graphical one, or a semantic
     *            one.
     */
    public DeletionCommandBuilder(final DDiagramElement diagramElement, final boolean deleteFromDiagram) {
        this.diagramElement = diagramElement;
        this.deleteFromDiagram = deleteFromDiagram;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.sirius.tools.internal.command.builders.CommandBuilder#buildCommand()
     */
    public Command buildCommand() {
        Command command = UnexecutableCommand.INSTANCE;
        if (diagram != null) {
            command = buildDeleteDiagram();
        } else if (diagramElement != null) {
            command = deleteFromDiagram ? buildDeleteDiagramElementFromDiagram() : buildDeleteDiagramElement();
        }
        return command;
    }

    private Command buildDeleteDiagram() {
        Command cmd = UnexecutableCommand.INSTANCE;
        if (permissionAuthority.canEditInstance(diagram) && permissionAuthority.canEditInstance(diagram.eContainer())) {
            /*
             * If the viewpoint is a root viewpoint then we should not delete it
             * !
             */
            final DCommand vpCmd = createEnclosingCommand();
            /* delete the diagram */
            addDeleteDiagramTask(vpCmd, diagram);

            /* remove the dangling references of the viewpoint */
            vpCmd.getTasks().add(new AbstractCommandTask() {

                public void execute() {
                    RemoveDanglingReferences.removeDanglingReferences(EcoreUtil.getRootContainer(diagram));
                }

                public String getLabel() {
                    return null;
                }
            });
            cmd = vpCmd;

        }
        return cmd;
    }

    private Command buildDeleteDiagramElementFromDiagram() {
        if (permissionAuthority.canEditInstance(diagramElement) && permissionAuthority.canEditInstance(diagramElement.eContainer())) {
            final DCommand cmd = createEnclosingCommand();
            cmd.getTasks().add(createDeleteDiagramElementTask(diagramElement));

            final List<EObject> contents = Lists.newArrayList(this.modelAccessor.eAllContents(diagramElement, "EdgeTarget"));
            contents.add(diagramElement);
            for (final EObject element : contents) {
                if (element instanceof EdgeTarget) {
                    final EdgeTarget target = (EdgeTarget) element;
                    for (final DEdge edge : Iterables.concat(target.getIncomingEdges(), target.getOutgoingEdges())) {
                        cmd.getTasks().add(createDeleteDiagramElementTask(edge));
                    }
                }
            }
            return cmd;
        }
        return UnexecutableCommand.INSTANCE;
    }

    private DeleteDDiagramElementTask createDeleteDiagramElementTask(final DDiagramElement aDiagramElement) {
        DeleteDDiagramElementTask task = new DeleteDDiagramElementTask(aDiagramElement, modelAccessor);
        return task;
    }

    private Command buildDeleteDiagramElement() {

        Command cmd = UnexecutableCommand.INSTANCE;
        // We first check that the permission authority allows to delete the
        // given diagram element
        if (permissionAuthority.canEditInstance(diagramElement) && permissionAuthority.canEditInstance(diagramElement.eContainer())) {

            // We also check that the underlying semantic elements can be
            // deleted
            boolean semanticElementCanBeDeleted = true;
            final Set<EObject> allSemanticElements = getSemanticElementsToDestroy(diagramElement);
            for (final EObject semantic : allSemanticElements) {
                final EObject container = semantic.eContainer();
                if (!permissionAuthority.canDeleteInstance(semantic) || (container != null && !permissionAuthority.canEditInstance(container))) {
                    semanticElementCanBeDeleted = false;
                }
            }

            // If both graphical and semantic elements can be deleted, we build
            // the command
            if (semanticElementCanBeDeleted) {
                final DCommand result = createEnclosingCommand();
                final DDiagram foundDiagram = SiriusUtil.findDiagram(diagramElement);
                setTool();
                if (tool != null) {
                    addDeleteDiagramElementFromTool(result);
                    EObject rootContainer = null;
                    final boolean automaticRefresh = Platform.getPreferencesService().getBoolean(SiriusPlugin.ID, SiriusPreferencesKeys.PREF_AUTO_REFRESH.name(), false, null);
                    if (automaticRefresh) {
                        result.getTasks().add(new RemoveDanglingReferencesTask(EcoreUtil.getRootContainer(((DSemanticDecorator) diagramElement).getTarget())));
                        addRefreshTask(diagramElement, result, tool);
                        rootContainer = EcoreUtil.getRootContainer(diagramElement);
                    } else {
                        result.getTasks().add(new RemoveSemanticDanglingReferenceTask(permissionAuthority, allSemanticElements, uiCallback));
                        EObjectQuery eObjectQuery = new EObjectQuery(diagramElement);
                        DRepresentation dRepresentation = eObjectQuery.getRepresentation().get();
                        rootContainer = dRepresentation;
                    }
                    result.getTasks().add(new RemoveDanglingReferencesTask(rootContainer));
                    cmd = new NoNullResourceCommand(result, diagramElement);
                } else {
                    cmd = buildDeleteDiagramElementCommandWithoutTool(result, foundDiagram);
                }
            }
        }

        return cmd;
    }

    private void setTool() {
        DiagramElementMapping mapping = null;
        mapping = diagramElement.getDiagramElementMapping();
        if (mapping != null) {
            tool = mapping.getDeletionDescription();
        }
    }

    private void addDeleteDiagramElementFromTool(final DCommand cmd) {
        final EObject semanticContainer = ((DSemanticDecorator) diagramElement).getTarget();
        final EObject viewContainer = diagramElement.eContainer();
        // check precondition
        boolean delete = true;
        if (tool.getPrecondition() != null && !StringUtil.isEmpty(tool.getPrecondition().trim())) {
            delete = false;
            final IInterpreter interpreter = InterpreterUtil.getInterpreter(diagramElement);
            interpreter.setVariable(IInterpreterSiriusVariables.CONTAINER_VIEW, viewContainer);
            interpreter.setVariable(IInterpreterSiriusVariables.VIEW, diagramElement);
            interpreter.setVariable(IInterpreterSiriusVariables.ELEMENT, semanticContainer);
            try {
                delete = false;
                delete = interpreter.evaluateBoolean(semanticContainer, tool.getPrecondition());
            } catch (final EvaluationException e) {
                RuntimeLoggerManager.INSTANCE.error(tool, ToolPackage.eINSTANCE.getAbstractToolDescription_Precondition(), e);
            }
            interpreter.unSetVariable(IInterpreterSiriusVariables.CONTAINER_VIEW);
            interpreter.unSetVariable(IInterpreterSiriusVariables.VIEW);
            interpreter.unSetVariable(IInterpreterSiriusVariables.ELEMENT);
        }
        if (delete) {

            cmd.getTasks().addAll(buildDeleteFromToolTask(semanticContainer, viewContainer).getTasks());
            cmd.getTasks().add(new DeleteSeveralDDiagramElementsTask(editingDomain, modelAccessor, uiCallback, cmd, diagramElement));
            if (diagramElement instanceof DEdge) {
                Option<EdgeMapping> edgeMapping = new IEdgeMappingQuery(((DEdge) diagramElement).getActualMapping()).getEdgeMapping();
                if (edgeMapping.some() && !edgeMapping.get().isUseDomainElement()) {
                    // Add the delete task for relation edges only.
                    cmd.getTasks().add(new DeleteDDiagramElementTask(diagramElement, modelAccessor));
                }
            }
        } else {
            cmd.getTasks().add(UnexecutableTask.INSTANCE);
        }
    }

    private DCommand buildDeleteFromToolTask(final EObject deletedSemanticElement, final EObject containerView) {
        final DCommand result = createEnclosingCommand();
        if (permissionAuthority.canEditInstance(containerView)) {
            final IInterpreter interpreter = InterpreterUtil.getInterpreter(deletedSemanticElement);
            final Map<AbstractVariable, Object> variables = new HashMap<AbstractVariable, Object>();
            result.getTasks().add(new InitInterpreterVariablesTask(variables, interpreter, uiCallback));
            variables.put(tool.getContainerView(), containerView);
            variables.put(tool.getElement(), deletedSemanticElement);
            variables.put(tool.getElementView(), diagramElement);

            addDiagramVariable(result, containerView, interpreter);

            Option<DDiagram> parentDiagram = new EObjectQuery(containerView).getParentDiagram();
            if (tool.getInitialOperation() != null && tool.getInitialOperation().getFirstModelOperations() != null) {
                result.getTasks().add(taskHelper.buildTaskFromModelOperation(parentDiagram.get(), deletedSemanticElement, tool.getInitialOperation().getFirstModelOperations()));
            }
        } else {
            result.getTasks().add(UnexecutableTask.INSTANCE);
        }
        return result;
    }

    private Command buildDeleteDiagramElementCommandWithoutTool(final DCommand result, final DDiagram foundDiagram) {
        Command cmd;

        final Set<EObject> allSemanticElements = getSemanticElementsToDestroy(diagramElement);
        // allSemanticElements.add(diagramElement.getTarget());
        if (allSemanticElements.isEmpty()) {
            // Do not return unexcutable command, actions will be deactivated
            // when an element is not deletable, here the result needs to be
            // return to handle multi-selection.
            cmd = result;
        } else {
            /*
             * Now delete all the diagram and diagram elements corresponding to
             * the semantic elements to delete
             */
            EObjectQuery eObjectQuery = new EObjectQuery(diagramElement);
            DRepresentation dRepresentation = eObjectQuery.getRepresentation().get();
            final Set<DSemanticDecorator> diagramElements = taskHelper.getDElementToClearFromSemanticElements(dRepresentation, allSemanticElements);
            for (final DSemanticDecorator decorator : diagramElements) {
                result.getTasks().add(new DeleteDDiagramElementTask(decorator, modelAccessor));
                // If the semantic decorator is related to edges, these edges
                // should also be deleted
                if (decorator instanceof EdgeTarget) {
                    for (DEdge incommingEdge : ((EdgeTarget) decorator).getIncomingEdges()) {
                        result.getTasks().add(new DeleteDDiagramElementTask(incommingEdge, modelAccessor));
                    }
                    for (DEdge outgoingEdge : ((EdgeTarget) decorator).getOutgoingEdges()) {
                        result.getTasks().add(new DeleteDDiagramElementTask(outgoingEdge, modelAccessor));
                    }
                }
            }

            /*
             * Now delete all the semantic elements
             */
            for (final EObject semantic : allSemanticElements) {
                DeleteDDiagramElementTask deleteSemanticElementTask = new DeleteDDiagramElementTask(semantic, modelAccessor);
                result.getTasks().add(deleteSemanticElementTask);
            }

            /*
             * and remove possible dangling references
             */
            final boolean automaticRefresh = Platform.getPreferencesService().getBoolean(SiriusPlugin.ID, SiriusPreferencesKeys.PREF_AUTO_REFRESH.name(), false, null);
            if (automaticRefresh) {
                if (diagramElement.getTarget() != null) {
                    result.getTasks().add(new RemoveDanglingReferencesTask(EcoreUtil.getRootContainer(diagramElement.getTarget())));
                }
                result.getTasks().add(new RemoveDanglingReferencesTask(EcoreUtil.getRootContainer(diagramElement)));
            } else {
                // result.getTasks().add(new
                // RemoveDanglingReferencesTask(dRepresentation, uiCallback));
                result.getTasks().add(new RemoveSemanticDanglingReferenceTask(permissionAuthority, allSemanticElements, uiCallback));
            }
            // Add a refresh task if the autoRefresh is on.
            addRefreshTask(diagramElement, result, tool);
            /* Avoid notifications of the outline on each change */
            cmd = new NoNullResourceCommand(result, diagramElement);
        }
        return cmd;
    }

    /**
     * Add all the semantic elements to destroy to delete the specified diagram
     * element.
     */
    private Set<EObject> getSemanticElementsToDestroy(final DDiagramElement currentDiagramElement) {
        Set<EObject> elementsToDestroy = Sets.newHashSet();
        for (final EObject semantic : currentDiagramElement.getSemanticElements()) {
            if (semantic != null) {
                elementsToDestroy.add(semantic);
                elementsToDestroy.addAll(getAllChildren(semantic));
            }
        }
        for (final EObject child : currentDiagramElement.eContents()) {
            if (child instanceof DDiagramElement) {
                elementsToDestroy.addAll(getSemanticElementsToDestroy((DDiagramElement) child));
            }
        }
        return elementsToDestroy;
    }

    private Set<EObject> getAllChildren(final EObject semanticElement) {
        return Sets.newHashSet(semanticElement.eAllContents());
    }

    /**
     * Delete commands
     */
    private void addDeleteDiagramTask(final DCommand cmd, final DDiagram vp) {
        cmd.getTasks().add(new DeleteDDiagramTask(vp));
    }

    /**
     * Appends a command that delete the specified diagram to the specified
     * command.
     * 
     * @param cmd
     *            the command.
     * @param diagramToDelete
     *            the diagram to delete.
     */
    public void addDeleteDiagramTasks(final DCommand cmd, final DDiagram diagramToDelete) {
        if (permissionAuthority.canEditInstance(diagramToDelete) && permissionAuthority.canEditInstance(diagramToDelete.eContainer())) {

            /* delete the diagram */
            addDeleteDiagramTask(cmd, diagramToDelete);

            /* delete the subdiagrams */
            final Iterator<EObject> it = diagramToDelete.eAllContents();
            while (it.hasNext()) {
                final EObject eObj = it.next();
                if (eObj instanceof DDiagram) {
                    addDeleteDiagramTask(cmd, (DDiagram) eObj);
                }
            }

            /* remove the dangling references of the diagram */
            cmd.getTasks().add(new AbstractCommandTask() {

                public void execute() {
                    RemoveDanglingReferences.removeDanglingReferences(EcoreUtil.getRootContainer(diagramToDelete));
                }

                public String getLabel() {
                    return null;
                }
            });
        } else {
            cmd.getTasks().add(UnexecutableTask.INSTANCE);
        }
    }

    /**
     * Returns all the diagram elements to delete.
     * 
     * @param root
     *            the root diagram element.
     * @param semanticElements
     *            semantic elements.
     * @return all the diagram elements to delete.
     */
    public Set<DSemanticDecorator> getDElementToClearFromSemanticElements(final EObject root, final Set<EObject> semanticElements) {
        return taskHelper.getDElementToClearFromSemanticElements(root, semanticElements);
    }

    /**
     * {@inheritDoc}
     */
    protected String getEnclosingCommandLabel() {
        String commandLabel = DELETE;
        if (diagram != null) {
            commandLabel = DELETE_DIAGRAM_LABEL;
        } else if (diagramElement != null) {
            if (deleteFromDiagram) {
                commandLabel = DELETE_FROM_DIAGRAM_LABEL;
            } else if (tool == null) {
                commandLabel = DELETE_FROM_MODEL;
            } else {
                commandLabel = new IdentifiedElementQuery(tool).getLabel();
            }
        }
        return commandLabel;
    }
}
