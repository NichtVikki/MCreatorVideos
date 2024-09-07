package com.vikkivuk.videos.ui;

import net.mcreator.element.parts.gui.GUIComponent;
import net.mcreator.element.parts.procedure.Procedure;
import net.mcreator.ui.wysiwyg.WYSIWYGEditor;
import net.mcreator.workspace.Workspace;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Constructor;

public class VideoComponent extends GUIComponent {
    private final String videoUrl;
    private final String placeholderImagePath;
    private VideoScreen videoScreen;
    private final Procedure displayCondition;

    public VideoComponent(int x, int y, String videoUrl, String placeholderImagePath, @Nullable Procedure displayCondition) {
        super(x, y);
        this.videoUrl = videoUrl;
        this.placeholderImagePath = placeholderImagePath;

        // Initialize the VideoScreen here
//        this.videoScreen = new VideoScreen(videoUrl, 100, false, true, -1, -1, -1, -1);
        this.displayCondition = displayCondition;
    }
    
    @Override
    public String getName() {
        return "video_component";
    }

    @Override
    public int getWidth(Workspace workspace) {
        return videoScreen != null ? videoScreen.width : 0;
    }

    @Override
    public int getHeight(Workspace workspace) {
        return videoScreen != null ? videoScreen.height : 0;
    }

    public int getWidth() {
        return videoScreen != null ? videoScreen.width : 0;
    }

    public int getHeight() {
        return videoScreen != null ? videoScreen.height : 0;
    }

    @Override
    public void paintComponent(int cx, int cy, WYSIWYGEditor wysiwygEditor, Graphics2D g) {
        if (videoScreen != null) {
            // Render the video screen within the component's area
            videoScreen.renderToGraphics(g, cx, cy, getWidth(), getHeight());
        } else {
            // Fallback or placeholder image
            // You might want to draw the placeholder image if the video is not ready
             g.drawImage(new ImageIcon(placeholderImagePath).getImage(), cx, cy, getWidth(), getHeight(), null);
        }
    }

    @Override
    public int getWeight() {
        return 40;
    }

    public void startVideo() {
        if (videoScreen != null) {
            videoScreen.startVideo();
        }
    }

    public void stopVideo() {
        if (videoScreen != null) {
            videoScreen.stopVideo();
        }
    }

    public void onClose() {
        if (videoScreen != null) {
            videoScreen.onClose();
        }
    }

    public String getVideoPath() {
        return videoUrl;
    }

    public String getPlaceholderImage() {
        return placeholderImagePath;
    }

    public Procedure getDisplayCondition() {
        return displayCondition;
    }
}