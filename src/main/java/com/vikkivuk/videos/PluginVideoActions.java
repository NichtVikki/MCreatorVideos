package com.vikkivuk.videos;

import net.mcreator.generator.GeneratorUtils;
import net.mcreator.io.FileIO;
import net.mcreator.io.Transliteration;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.action.BasicAction;
import net.mcreator.ui.dialogs.file.FileDialogs;
import net.mcreator.ui.init.L10N;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class PluginVideoActions {

    // Action for importing video files
    public static class ImportVideos extends BasicAction {
        public ImportVideos(PluginActions actionRegistry) {
            super(actionRegistry, L10N.t("action.workspace.resources.import_video_file"), actionEvent -> {
                File videoFile = FileDialogs.getOpenDialog(actionRegistry.getMCreator(), new String[] { ".mp4", ".gif" });
                if (videoFile != null)
                    importVideoFile(actionRegistry.getMCreator(), videoFile);
            });
        }
    }

    // Get the directory where video files are stored
    public static File getVideoDir(MCreator mcreator) {
        return new File(GeneratorUtils.getSpecificRoot(mcreator.getWorkspace(), mcreator.getWorkspace().getGeneratorConfiguration(), "mod_assets_root"), "videos/");
    }

    // List all video files in the directory
    private static List<File> listVideosInDir(@Nullable File dir) {
        if (dir == null) {
            return Collections.emptyList();
        } else {
            List<File> retval = new ArrayList<>();
            File[] files = dir.listFiles();
            if (files != null) {
                for (File element : files) {
                    if (element.getName().endsWith(".mp4") || element.getName().endsWith(".gif")) {
                        retval.add(element);
                    }
                }
            }
            return retval;
        }
    }

    // Get all video files from the video directory
    public static List<File> getVideoFiles(MCreator mcreator) {
        return listVideosInDir(getVideoDir(mcreator));
    }

    // Import a video file into the video directory and trigger a UI refresh
    public static void importVideoFile(MCreator mcreator, File videoFile) {
        File targetDir = getVideoDir(mcreator);
        if (!targetDir.exists()) {
            targetDir.mkdirs(); // Ensure the directory exists
        }
        FileIO.copyFile(videoFile, new File(targetDir,
                Transliteration.transliterateString(videoFile.getName()).toLowerCase(Locale.ENGLISH).trim()
                        .replace(":", "").replace(" ", "_")));
    }
}
