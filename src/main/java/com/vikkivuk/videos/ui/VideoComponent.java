package com.vikkivuk.videos.ui;

import net.mcreator.element.parts.gui.GUIComponent;
import net.mcreator.element.parts.procedure.Procedure;
import net.mcreator.ui.wysiwyg.WYSIWYGEditor;
import net.mcreator.workspace.Workspace;

import javax.swing.*;
import java.awt.*;

public class VideoComponent extends GUIComponent {

    private String videoPath;
    private String placeholderImage;
    private Procedure displayCondition;

    public VideoComponent(int x, int y, String videoPath, String placeholderImage, Procedure displayCondition) {
        super(x, y);
        this.videoPath = videoPath;
        this.placeholderImage = placeholderImage;
        this.displayCondition = displayCondition;
    }

    public VideoComponent(int x, int y, String videoPath, String placeholderImage, Procedure displayCondition, AnchorPoint anchorPoint) {
        this(x, y, videoPath, placeholderImage, displayCondition);
        this.anchorPoint = anchorPoint;
    }

    @Override
    public String getName() {
        return "video_" + (videoPath != null ? videoPath : "placeholder");
    }

    @Override
    public int getWidth(Workspace workspace) {
        // Returning a default width, could be based on video dimensions if needed
        return 320;
    }

    @Override
    public int getHeight(Workspace workspace) {
        // Returning a default height, could be based on video dimensions if needed
        return 240;
    }

    @Override
    public int getWeight() {
        return 50;  // Adjust based on how "important" this component is relative to others
    }

    public java.awt.Image getPlaceholderImage(Workspace workspace) {
        if (placeholderImage != null && !placeholderImage.isEmpty()) {
            return new ImageIcon(placeholderImage).getImage();
        }
        return null;
    }

    @Override
    public void paintComponent(int cx, int cy, WYSIWYGEditor wysiwygEditor, Graphics2D g) {
        java.awt.Image placeholder = this.getPlaceholderImage(wysiwygEditor.mcreator.getWorkspace());

        if (placeholder != null) {
            g.drawImage(placeholder, cx, cy, getWidth(wysiwygEditor.mcreator.getWorkspace()), getHeight(wysiwygEditor.mcreator.getWorkspace()), wysiwygEditor);
        } else {
            // Draw a simple placeholder rectangle if no placeholder image is provided
            g.setColor(Color.BLACK);
            g.fillRect(cx, cy, getWidth(wysiwygEditor.mcreator.getWorkspace()), getHeight(wysiwygEditor.mcreator.getWorkspace()));
            g.setColor(Color.WHITE);
            g.drawString("Video Placeholder", cx + 10, cy + 20);
        }
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getPlaceholderImage() {
        return placeholderImage;
    }

    public void setPlaceholderImage(String placeholderImage) {
        this.placeholderImage = placeholderImage;
    }

    public Procedure getDisplayCondition() {
        return displayCondition;
    }

    public void setDisplayCondition(Procedure displayCondition) {
        this.displayCondition = displayCondition;
    }
}