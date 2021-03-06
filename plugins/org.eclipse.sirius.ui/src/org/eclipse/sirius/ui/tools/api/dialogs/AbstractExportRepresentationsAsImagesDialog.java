/*******************************************************************************
 * Copyright (c) 2010, 2017 THALES GLOBAL SERVICES and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.ui.tools.api.dialogs;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.sirius.common.tools.api.resource.ImageFileFormat;
import org.eclipse.sirius.common.ui.tools.api.util.SWTUtil;
import org.eclipse.sirius.ui.business.api.dialect.DialectUIManager;
import org.eclipse.sirius.ui.business.api.dialect.ExportFormat;
import org.eclipse.sirius.ui.business.api.dialect.ExportFormat.ExportDocumentFormat;
import org.eclipse.sirius.ui.business.api.preferences.SiriusUIPreferencesKeys;
import org.eclipse.sirius.ui.tools.internal.preference.ScaleWithLegendFieldEditorWithHelp;
import org.eclipse.sirius.viewpoint.provider.Messages;
import org.eclipse.sirius.viewpoint.provider.SiriusEditPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;

/**
 * Dialog used by the export diagram to image files action to prompt the user for a destination and image format.
 * 
 * @author jdupont
 */
public abstract class AbstractExportRepresentationsAsImagesDialog extends Dialog {

    /**
     * The file separator alternative.
     */
    public static final String FILE_SEPARATOR_ALTERNATIVE = "_"; //$NON-NLS-1$

    /**
     * The id for the persistent settings for this dialog.
     */
    public static final String DIALOG_SETTINGS_ID = "ExportRepresentationsAsImagesDialog"; //$NON-NLS-1$

    /**
     * Default extension image.
     */
    protected static final ImageFileFormat DEFAULT_VALUE = ImageFileFormat.JPG;

    /**
     * The character extension file (".").
     */
    protected static final String CHARACTER_EXTENSION_FILE = "."; //$NON-NLS-1$

    /**
     * The list of values for this enumerated type.
     */
    protected static final ImageFileFormat[] SAFE_VALUES = { DEFAULT_VALUE, ImageFileFormat.PNG, ImageFileFormat.SVG, ImageFileFormat.BMP, ImageFileFormat.GIF };

    /**
     * The empty string.
     */
    protected static final String EMPTY_STRING = ""; //$NON-NLS-1$

    /**
     * the image format label text.
     */
    protected static final String IMAGE_FORMAT_LABEL = Messages.AbstractExportRepresentationsAsImagesDialog_imageFormatLabel;

    /**
     * the browse button text.
     */
    protected static final String BROWSE_LABEL = Messages.AbstractExportRepresentationsAsImagesDialog_browseLabel;

    /**
     * The id for the persistent image format setting for this dialog.
     */
    protected static final String DIALOG_SETTINGS_IMAGE_FORMAT = "ExportRepresentationsAsImagesDialog.imageFormat"; //$NON-NLS-1$

    /**
     * an error access message.
     */
    protected static final String ACCES_ERROR_MESSAGE = Messages.AbstractExportRepresentationsAsImagesDialog_accessError;

    /**
     * an error folder not exist message.
     */
    protected static final String FOLDER_NOT_EXIST_MESSAGE = Messages.AbstractExportRepresentationsAsImagesDialog_folderDoesNotExist;

    /**
     * Combo length history path.
     */
    private static final int COMBO_HISTORY_LENGTH = 5;

    /**
     * Max quality level used to export diagram as image
     */
    private static final int MAX_QUALITY = 10;

    /**
     * Min quality level used to export diagram as image
     */
    private static final int MIN_QUALITY = 0;

    /**
     * Quality increment used to export diagram as image
     */
    private static final int INCREMENT_QUALITY = 1;

    /**
     * the folder combo field.
     */
    protected Combo folderText;

    /**
     * The extension image combo field.
     */
    protected Combo imageFormatCombo;

    /**
     * the image format selected in the image format pulldown field.
     */
    protected ImageFileFormat imageFormat;

    /**
     * the text entered into the folder text field.
     */
    protected String folder;

    /**
     * the message image field, displays the error (X) icon when the file name or folder is invalid
     */
    private Label messageImageLabel;

    /**
     * the message field, displays an error message when the file name or folder is invalid
     */
    private Label messageLabel;

    /**
     * the export to HTML checkbox.
     */
    private Button exportToHTMLCheckbox;

    /**
     * true to export to HTML.
     */
    private boolean exportToHTML;

    /**
     * true to export decorations.
     */
    private boolean exportDecorations;

    /**
     * <code>true</code> if the user wants diagrams to be scaled on export.
     */
    private boolean autoScaleDiagram;

    /**
     * Scale level when exporting diagrams as image.
     */
    private Integer diagramScaleLevel;

    /**
     * Creates an instance of the copy to image dialog.
     * 
     * @param shell
     *            the parent shell
     * @param path
     *            the default path to store the image or null
     */
    public AbstractExportRepresentationsAsImagesDialog(final Shell shell, final IPath path) {
        super(shell);
        initDialogSettings(path);
        this.diagramScaleLevel = SiriusEditPlugin.getPlugin().getPreferenceStore().getInt(SiriusUIPreferencesKeys.PREF_SCALE_LEVEL_DIAGRAMS_ON_EXPORT.name());
        this.autoScaleDiagram = diagramScaleLevel != 0;
    }

    /**
     * resolve the selected image format to an enumerated type.
     * 
     * @param ordinal
     *            the selected format in the pulldown
     * @return the image format enumerated type
     */
    public static ImageFileFormat resolveImageFormat(final int ordinal) {
        return AbstractExportRepresentationsAsImagesDialog.SAFE_VALUES[ordinal];
    }

    /**
     * Resolve the selected image format to an enumerated type.
     * 
     * @param imageFormat
     *            the selected format.
     * @return the image format enumerated type
     */
    public static ImageFileFormat resolveImageFormat(final String imageFormat) {
        for (ImageFileFormat element : AbstractExportRepresentationsAsImagesDialog.SAFE_VALUES) {
            if (element.getName().toLowerCase().equals(imageFormat.toLowerCase())) {
                return element;
            }
        }

        return AbstractExportRepresentationsAsImagesDialog.getDefaultImageFormat();
    }

    /**
     * Retrieves the default image format.
     * 
     * @return the default image format.
     */
    public static ImageFileFormat getDefaultImageFormat() {
        return ImageFileFormat.JPG;
    }

    /**
     * Check if the user choose to export as Html.
     * 
     * @return <code>true</code> if it decided to export as Html , <code>false</code> otherwise
     */
    public boolean isExportToHtml() {
        return exportToHTML;
    }

    /**
     * Check if the user choose to export decorations.
     * 
     * @return <code>true</code> if he decided to export decorations , <code>false</code> otherwise
     */
    public boolean isExportDecorations() {
        return exportDecorations;
    }

    /**
     * Check if the user choose to auto-scale exported diagrams.
     * 
     * @return <code>true</code> if the user asked for diagram auto-scaling.
     */
    public boolean isAutoScaleDiagram() {
        return autoScaleDiagram;
    }

    /**
     * Get the scale level for exported diagrams.
     * 
     * @return scale level in percent
     */
    public Integer getDiagramScaleLevelInPercent() {
        return diagramScaleLevel * 10;
    }

    /**
     * Returns height and width data in a GridData for the button that was passed in. You can call button.setLayoutData
     * with the returned data.
     * 
     * @param button
     *            which has already been made. We'll be making the GridData for this button, so be sure that the text
     *            has already been set.
     * @return GridData for this button with the suggested height and width
     */
    public static GridData makeButtonData(Button button) {
        GC gc = new GC(button);
        gc.setFont(button.getFont());

        GridData data = new GridData();
        data.heightHint = Dialog.convertVerticalDLUsToPixels(gc.getFontMetrics(), 14);
        data.widthHint = Math.max(Dialog.convertHorizontalDLUsToPixels(gc.getFontMetrics(), IDialogConstants.BUTTON_WIDTH), button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);

        gc.dispose();

        return data;
    }

    /**
     * Returns the output path.
     * 
     * @return folder
     */
    public IPath getOutputPath() {
        return new Path(folder);
    }

    /**
     * Returns the destination image file format selected by the user.
     * 
     * @return the selected image file format.
     */
    public ImageFileFormat getImageFormat() {
        return imageFormat;
    }

    /**
     * Retrieves the persistent settings for this dialog.
     * 
     * @return the persistent settings for this dialog.
     */
    protected IDialogSettings getDialogSettings() {
        IDialogSettings settings = SiriusEditPlugin.getPlugin().getDialogSettings();
        settings = settings.getSection(DIALOG_SETTINGS_ID);
        if (settings == null) {
            settings = SiriusEditPlugin.getPlugin().getDialogSettings().addNewSection(DIALOG_SETTINGS_ID);
        }
        return settings;
    }

    /**
     * Initialize the settings for this dialog.
     * 
     * @param path
     *            the default path to use for the diagram file or null if the dialog should use some default path.
     */
    protected abstract void initDialogSettings(IPath path);

    /**
     * Retrieves the persistent settings for this dialog.
     */
    protected abstract void saveDialogSettings();

    @Override
    protected void okPressed() {
        imageFormat = AbstractExportRepresentationsAsImagesDialog.resolveImageFormat(imageFormatCombo.getSelectionIndex());
        super.okPressed();
        saveDialogSettings();
    }

    /**
     * handle a browse button pressed selection.
     */
    protected abstract void handleBrowseButtonPressed();

    /**
     * validate the folder text field.
     */
    // CHECKSTYLE:OFF
    protected void validateFolderText() {

        if (folderText.getText().equals(EMPTY_STRING)) {

            setDialogErrorState(Messages.AbstractExportRepresentationsAsImagesDialog_blankFolderError);

        } else {

            IPath path = new Path(EMPTY_STRING);
            if (!path.isValidPath(folderText.getText())) {
                setDialogErrorState(Messages.AbstractExportRepresentationsAsImagesDialog_invalidFolderPathError);
                return;
            }

            final File file = new File(folderText.getText());
            final File directory = file.getParentFile();

            if (file.exists()) {
                if (!file.canWrite()) {
                    setDialogErrorState(ACCES_ERROR_MESSAGE);
                    return;
                }
            } else if (directory != null && !directory.canWrite()) {
                setDialogErrorState(ACCES_ERROR_MESSAGE);
                return;
            }
            folder = folderText.getText();
            setDialogOKState();
        }
    }

    // CHECKSTYLE:ON
    /**
     * Set the dialog into error state mode. The error image (x) label and error label are made visible and the ok
     * button is disabled.
     * 
     * @param message
     *            the error message
     */
    protected void setDialogErrorState(final String message) {
        messageLabel.setText(message);
        messageImageLabel.setVisible(true);
        messageLabel.setVisible(true);
        getButton(IDialogConstants.OK_ID).setEnabled(false);
        getButton(IDialogConstants.CANCEL_ID).getShell().setDefaultButton(getButton(IDialogConstants.CANCEL_ID));
    }

    /**
     * get the supported image formats from the enumerated type.
     * 
     * @return array of supported image formats.
     */
    protected String[] getImageSafeFormatItems() {
        final String[] items = new String[AbstractExportRepresentationsAsImagesDialog.SAFE_VALUES.length];
        for (int i = 0; i < AbstractExportRepresentationsAsImagesDialog.SAFE_VALUES.length; i++) {
            items[i] = AbstractExportRepresentationsAsImagesDialog.SAFE_VALUES[i].getName();
        }
        return items;
    }

    @Override
    protected Control createContents(Composite parent) {
        Control result = super.createContents(parent);
        validateFolderText();
        return result;
    }

    @Override
    protected Control createDialogArea(final Composite parent) {
        final Composite composite = (Composite) super.createDialogArea(parent);
        createFolderGroup(composite);
        createImageFormatGroup(composite);
        createImageSizeGroup(composite);
        createExportDecorationsGroup(composite);
        if (DialectUIManager.INSTANCE.canExport(new ExportFormat(ExportDocumentFormat.HTML, null))) {
            createGenerateHTMLGroup(composite);
        }
        createMessageGroup(composite);
        initListeners();
        return composite;
    }

    /**
     * {@inheritDoc} Configures the shell in preparation for opening this window in it.
     * 
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    @Override
    protected void configureShell(final Shell shell) {
        super.configureShell(shell);
        // shell.setText(DIALOG_TITLE);
    }

    /**
     * Create the folder group in the dialog.
     * 
     * @param parent
     *            the parent widget
     */
    protected abstract void createFolderGroup(Composite parent);

    /**
     * Initialize the field listeners when all field are created.
     */
    protected abstract void initListeners();

    /**
     * Hook method for restoring widget values to the values that they held last time this wizard was used to
     * completion.
     */
    protected abstract void restoreWidgetValues();

    /**
     * Create the image format group in the dialog.
     * 
     * @param parent
     *            the parent widget
     */
    protected abstract void createImageFormatGroup(Composite parent);

    private void createGenerateHTMLGroup(Composite parent) {

        Composite composite = SWTUtil.createCompositeHorizontalFill(parent, 1, false);
        exportToHTMLCheckbox = new Button(composite, SWT.CHECK | SWT.LEFT);
        exportToHTMLCheckbox.setText(Messages.AbstractExportRepresentationsAsImagesDialog_htmlExport);
        GridData data = new GridData(GridData.FILL, 0, true, false);
        exportToHTMLCheckbox.setLayoutData(data);
        exportToHTMLCheckbox.setSelection(exportToHTML);
        exportToHTMLCheckbox.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                exportToHTML = exportToHTMLCheckbox.getSelection();
            }
        });
    }

    private void createExportDecorationsGroup(Composite parent) {
        Composite composite = SWTUtil.createCompositeHorizontalFill(parent, 1, false);
        final Button exportDecorationsCheckBox = new Button(composite, SWT.CHECK | SWT.LEFT);
        exportDecorationsCheckBox.setText(Messages.AbstractExportRepresentationsAsImagesDialog_decorationExport);
        GridData data = new GridData(GridData.FILL, 0, true, false);
        exportDecorationsCheckBox.setLayoutData(data);
        exportDecorationsCheckBox.setSelection(exportDecorations);
        exportDecorationsCheckBox.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                exportDecorations = exportDecorationsCheckBox.getSelection();
            }
        });
    }

    private void createImageSizeGroup(Composite parent) {
        Group imageSizeGroup = new Group(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout(1, false);
        imageSizeGroup.setLayout(gridLayout);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalSpan = 2;
        imageSizeGroup.setLayoutData(gridData);
        imageSizeGroup.setText(Messages.ExportAsImage_dialogSizeGroupName);

        ScaleWithLegendFieldEditorWithHelp scaleWithLegendFieldEditor = new ScaleWithLegendFieldEditorWithHelp(SiriusUIPreferencesKeys.PREF_SCALE_LEVEL_DIAGRAMS_ON_EXPORT.name(),
                Messages.ExportAsImage_dialogSizeGroupName, Messages.ExportAsImage_sizeTooltip, imageSizeGroup);
        scaleWithLegendFieldEditor.setMinimum(MIN_QUALITY);
        scaleWithLegendFieldEditor.setMaximum(MAX_QUALITY);
        scaleWithLegendFieldEditor.setPageIncrement(INCREMENT_QUALITY);

        Scale scale = scaleWithLegendFieldEditor.getScaleControl();
        scale.setSelection(this.diagramScaleLevel);
        scale.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                diagramScaleLevel = scale.getSelection();
                autoScaleDiagram = diagramScaleLevel != 0;
            }
        });
    }

    /**
     * Create the message group in the dialog used to display error messages.
     * 
     * @param parent
     *            the parent widget
     */
    private void createMessageGroup(final Composite parent) {
        final Composite composite = SWTUtil.createCompositeHorizontalFill(parent, 2, false);

        messageImageLabel = new Label(composite, SWT.NONE);
        messageImageLabel.setImage(JFaceResources.getImage(DLG_IMG_MESSAGE_ERROR));
        messageImageLabel.setVisible(false);

        messageLabel = new Label(composite, SWT.NONE);
        final GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.FILL_HORIZONTAL);
        gridData.widthHint = 300;
        messageLabel.setLayoutData(gridData);
        messageLabel.setVisible(false);
    }

    /**
     * Set the dialog into ok state mode. The error image (x) label and error label and made not visible and the ok
     * button is enabled.
     */
    private void setDialogOKState() {
        messageImageLabel.setVisible(false);
        messageLabel.setVisible(false);
        getButton(IDialogConstants.OK_ID).setEnabled(true);
        getButton(IDialogConstants.OK_ID).getShell().setDefaultButton(getButton(IDialogConstants.OK_ID));
    }

    /**
     * Adds an entry to a history, while taking care of duplicate history items and excessively long histories. The
     * assumption is made that all histories should be of length
     * <code>WizardDataTransferPage.COMBO_HISTORY_LENGTH</code>.
     * 
     * @param history
     *            the current history
     * @param newEntry
     *            the entry to add to the history
     * @return String[] tab of history
     */
    protected String[] addToHistory(String[] history, String newEntry) {
        java.util.ArrayList<String> l = new java.util.ArrayList<String>(Arrays.asList(history));
        addToHistory(l, newEntry);
        String[] r = new String[l.size()];
        l.toArray(r);
        return r;
    }

    /**
     * Adds an entry to a history, while taking care of duplicate history items and excessively long histories. The
     * assumption is made that all histories should be of length
     * <code>WizardDataTransferPage.COMBO_HISTORY_LENGTH</code>.
     * 
     * @param history
     *            the current history
     * @param newEntry
     *            the entry to add to the history
     */
    protected void addToHistory(List<String> history, String newEntry) {
        history.remove(newEntry);
        history.add(0, newEntry);

        // since only one new item was added, we can be over the limit
        // by at most one item
        if (history.size() > COMBO_HISTORY_LENGTH) {
            history.remove(COMBO_HISTORY_LENGTH);
        }
    }

    /**
     * Create and return the help icon to show in our label.
     * 
     * @return The help icon to show in our label.
     */
    protected Image getHelpIcon() {
        ImageDescriptor findImageDescriptor = SiriusEditPlugin.Implementation.findImageDescriptor("icons/full/others/prefshelp.gif"); //$NON-NLS-1$
        return SiriusEditPlugin.getPlugin().getImage(findImageDescriptor);
    }
}
