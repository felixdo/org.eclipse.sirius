/*******************************************************************************
 * Copyright (c) 2017, 2018 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *    Felix Dorner <felix.dorner@gmail.com> - Bug 533002
 *******************************************************************************/
package org.eclipse.sirius.diagram.ui.internal.view.factories;

import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.util.ViewType;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.preferences.IPreferenceConstants;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.TextShapeViewFactory;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.notation.FillStyle;
import org.eclipse.gmf.runtime.notation.LineStyle;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.ShapeStyle;
import org.eclipse.gmf.runtime.notation.Style;
import org.eclipse.gmf.runtime.notation.TextAlignment;
import org.eclipse.gmf.runtime.notation.TextStyle;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.sirius.diagram.ui.business.api.query.ViewQuery;
import org.eclipse.sirius.viewpoint.DRepresentationDescriptor;
import org.eclipse.swt.graphics.RGB;

import com.google.common.collect.Iterables;

/**
 * Specific view factory for Notes created from the Palette. Default label alignment of Note's shapes have been updated
 * in GMF runtime 1.8.0 (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=432387). This factory creates notes with the
 * old default alignment value.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 *
 */
public class SiriusNoteViewFactory extends TextShapeViewFactory {

    /**
     * Create an {@link EAnnotation} that set the default vertical alignment (org.eclipse.draw2d.PositionConstants.TOP).
     * 
     * @return an {@link EAnnotation} with the default vertical alignment set.
     */
    public static EAnnotation createDefaultVerticalAlignmentEAnnotation() {
        EAnnotation specificStyles = EcoreFactory.eINSTANCE.createEAnnotation();
        specificStyles.setSource(ViewQuery.SPECIFIC_STYLES);
        EObject defaultVerticalAlignment = EcoreFactory.eINSTANCE.create(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY);
        if (defaultVerticalAlignment instanceof EStringToStringMapEntryImpl) {
            ((EStringToStringMapEntryImpl) defaultVerticalAlignment).setKey(ViewQuery.VERTICAL_ALIGNMENT);
            ((EStringToStringMapEntryImpl) defaultVerticalAlignment).setValue(String.valueOf(PositionConstants.TOP));
            specificStyles.getDetails().add((EStringToStringMapEntryImpl) defaultVerticalAlignment);
        }
        return specificStyles;
    }

    /**
     * Set the {@link TextStyle} with the default horizontal alignment.
     * 
     * @param styles
     *            the styles of the Note.
     */
    public void setDefaultHorizontalAlignment(Collection<Style> styles) {
        for (TextStyle textStyle : Iterables.filter(styles, TextStyle.class)) {
            textStyle.setTextAlignment(TextAlignment.CENTER_LITERAL);
            return;
        }
    }

    @Override
    protected List<?> createStyles(View view) {
        List<Style> styles = super.createStyles(view);
        EAnnotation verticalAlignment = createDefaultVerticalAlignmentEAnnotation();
        view.getEAnnotations().add(verticalAlignment);
        setDefaultHorizontalAlignment(styles);
        return styles;
    }

    /**
     * Method NoteView. creation constructor. This is copied and slighly adapted from gmf.NoteviewFactory to handle
     * DRepresentationDescriptors as view elements.
     * 
     * @param semanticAdapter
     * @param containerView
     * @param semanticHint
     * @param index
     * @param persisted
     */
    @Override
    public View createView(IAdaptable semanticAdapter, View containerView, String semanticHint, int index, boolean persisted, final PreferencesHint preferencesHint) {
        View view = super.createView(semanticAdapter, containerView, semanticHint, index, persisted, preferencesHint);
        // if a note view get created with a diagram semantic element
        // linked to it then we mark the note view as a diagram link
        // this will trigger the noteEdit part to switch the figure
        // to the DiagramLink mode (no border, no fill color and center
        // alignment)
        if (view.getElement() instanceof DRepresentationDescriptor) {
            if (semanticHint == null || semanticHint.length() == 0)
                view.setType(ViewType.NOTE);
            EAnnotation annotation = EcoreFactory.eINSTANCE.createEAnnotation();
            annotation.setSource(Properties.DIAGRAMLINK_ANNOTATION);
            view.getEAnnotations().add(annotation);
        }
        return view;
    }

    @Override
    protected void initializeFromPreferences(View view) {
        super.initializeFromPreferences(view);
        // support the diagram link mode
        boolean isLink = view.getElement() instanceof DRepresentationDescriptor;

        IPreferenceStore store = (IPreferenceStore) getPreferencesHint().getPreferenceStore();
        FillStyle fillStyle = (FillStyle) view.getStyle(NotationPackage.Literals.FILL_STYLE);
        if (fillStyle != null) {
            // fill color
            RGB fillRGB = PreferenceConverter.getColor(store, isLink ? IPreferenceConstants.PREF_NOTE_FILL_COLOR : IPreferenceConstants.PREF_NOTE_FILL_COLOR);

            fillStyle.setFillColor(FigureUtilities.RGBToInteger(fillRGB).intValue());
        }

        LineStyle lineStyle = (LineStyle) view.getStyle(NotationPackage.Literals.LINE_STYLE);
        if (lineStyle != null) {
            // line color
            RGB lineRGB = PreferenceConverter.getColor(store, isLink ? IPreferenceConstants.PREF_NOTE_LINE_COLOR : IPreferenceConstants.PREF_NOTE_LINE_COLOR);

            lineStyle.setLineColor(FigureUtilities.RGBToInteger(lineRGB).intValue());
        }
    }

    @Override
    protected void decorateView(View containerView, View view, IAdaptable semanticAdapter, String semanticHint, int index, boolean persisted) {
        ShapeStyle style = (ShapeStyle) view.getStyle(NotationPackage.eINSTANCE.getShapeStyle());
        if (style != null) {
            style.setLineWidth(1);
            style.setTransparency(0);
        }
        super.decorateView(containerView, view, semanticAdapter, semanticHint, index, persisted);
    }

}
