package com.vikkivuk.videos.exts;

import javax.annotation.Nullable;
import javax.swing.JComboBox;
import net.mcreator.ui.MCreator;
import com.vikkivuk.videos.exts.VideoType;

import java.io.File;

public class VideoComboBox extends JComboBox<String> {
    @Nullable
    public File getResourceRoot(MCreator mcreator) {
        return new File(mcreator.getWorkspaceFolder(), "src/main/resources/assets/" + mcreator.getWorkspace().getWorkspaceSettings().getModID());
    }

    public VideoComboBox(MCreator mcreator, VideoType videoType) {
        super();
        File[] videoFiles = new File(getResourceRoot(mcreator), "videos").listFiles();

        if (videoFiles != null) {
            for (File videoFile : videoFiles) {
                if (videoFile.getName().endsWith(".mp4") || videoFile.getName().endsWith(".gif")) {
                    addItem(videoFile.getName());
                }
            }
        }
    }

    public void setVideoFromFileName(String fileName) {
        setSelectedItem(fileName);
    }

    public boolean hasVideo() {
        return getSelectedItem() != null;
    }

    public String getVideoName() {
        return (String) getSelectedItem();
    }
}