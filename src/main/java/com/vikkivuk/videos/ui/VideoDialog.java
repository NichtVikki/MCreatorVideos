package com.vikkivuk.videos.ui;

import com.vikkivuk.videos.exts.VideoComboBox;
import com.vikkivuk.videos.exts.VideoType;
import net.mcreator.blockly.data.Dependency;
import net.mcreator.element.parts.gui.GUIComponent;
import net.mcreator.ui.dialogs.wysiwyg.AbstractWYSIWYGDialog;
import net.mcreator.ui.help.IHelpContext;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.minecraft.TextureComboBox;
import net.mcreator.ui.procedure.ProcedureSelector;
import net.mcreator.ui.workspace.resources.TextureType;
import net.mcreator.ui.wysiwyg.WYSIWYGEditor;
import net.mcreator.workspace.elements.VariableTypeLoader;

import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VideoDialog extends AbstractWYSIWYGDialog<VideoComponent> {
    private VideoComboBox videoFileSelector;
    private TextureComboBox placeholderSelector;

    public VideoDialog(WYSIWYGEditor editor, @Nullable VideoComponent videoComponent) {
        super(editor, videoComponent);

        setTitle("Video Editor");
        setModal(true);

        // Main panel with GridBagLayout and padding
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Row 0: Video File Selector
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        mainPanel.add(new JLabel(L10N.t("dialog.gui.video_file")), gbc);

        videoFileSelector = new VideoComboBox(editor.mcreator, VideoType.VIDEO); // Assuming "videos" is the correct folder
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;
        mainPanel.add(videoFileSelector, gbc);

        // Row 1: Placeholder Image Selector
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        mainPanel.add(new JLabel(L10N.t("dialog.gui.placeholder_image")), gbc);

        placeholderSelector = new TextureComboBox(editor.mcreator, TextureType.OTHER); // Assuming "textures" for images
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;
        mainPanel.add(placeholderSelector, gbc);

        // Row 2: Anchor Selection (if applicable)
        JComboBox<GUIComponent.AnchorPoint> anchorPointSelector = new JComboBox<>(GUIComponent.AnchorPoint.values());
        anchorPointSelector.setSelectedItem(GUIComponent.AnchorPoint.CENTER);
        if (!editor.isNotOverlayType) {
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.weightx = 0;
            gbc.gridwidth = 1;
            mainPanel.add(new JLabel(L10N.t("dialog.gui.anchor")), gbc);

            gbc.gridx = 1;
            gbc.weightx = 1.0;
            mainPanel.add(anchorPointSelector, gbc);
        }

        // Row 3: Display Condition Selector
        ProcedureSelector displayCondition = new ProcedureSelector(
                IHelpContext.NONE.withEntry("gui/video_display_condition"), editor.mcreator,
                L10N.t("dialog.gui.video_display_condition"), ProcedureSelector.Side.CLIENT, false,
                VariableTypeLoader.BuiltInTypes.LOGIC,
                Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/guistate:map"));
        displayCondition.refreshList();

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(displayCondition, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // OK and Cancel buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton(UIManager.getString("OptionPane.okButtonText"));
        JButton cancelButton = new JButton(UIManager.getString("OptionPane.cancelButtonText"));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load existing data if editing an existing component
        if (videoComponent != null) {
            okButton.setText(L10N.t("dialog.common.save_changes"));
            videoFileSelector.setVideoFromFileName(videoComponent.getVideoPath());
            placeholderSelector.setTextureFromTextureName(videoComponent.getPlaceholderImage());
            displayCondition.setSelectedProcedure(videoComponent.getDisplayCondition());
            if (!editor.isNotOverlayType) {
                anchorPointSelector.setSelectedItem(videoComponent.getAnchorPoint());
            }
        }

        // Action Listeners
        cancelButton.addActionListener(arg01 -> setVisible(false));
        okButton.addActionListener(arg01 -> {
            setVisible(false);
            if (videoFileSelector.hasVideo()) {
                if (videoComponent == null) {
                    VideoComponent component = new VideoComponent(
                            0, 0,
                            videoFileSelector.getVideoName(),
                            placeholderSelector.getTextureName(),
                            displayCondition.getSelectedProcedure()
                    );
                    if (!editor.isNotOverlayType) {
                        component.anchorPoint = (GUIComponent.AnchorPoint) anchorPointSelector.getSelectedItem();
                    }
                    setEditingComponent(component);
                    editor.editor.addComponent(component);
                    editor.list.setSelectedValue(component, true);
                    editor.editor.moveMode();
                } else {
                    int idx = editor.components.indexOf(videoComponent);
                    editor.components.remove(videoComponent);
                    VideoComponent videoComponentNew = new VideoComponent(
                            videoComponent.getX(), videoComponent.getY(),
                            videoFileSelector.getVideoName(),
                            placeholderSelector.getTextureName(),
                            displayCondition.getSelectedProcedure()
                    );
                    if (!editor.isNotOverlayType) {
                        videoComponentNew.anchorPoint = (GUIComponent.AnchorPoint) anchorPointSelector.getSelectedItem();
                    }
                    editor.components.add(idx, videoComponentNew);
                    setEditingComponent(videoComponentNew);
                }
            }
        });

        // Adjust the dialog size based on its components
        pack();

        // Center the dialog on the screen
        setLocationRelativeTo(null);

        // Make the dialog visible
        setVisible(true);
    }
}