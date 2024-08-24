package com.vikkivuk.videos;

import com.vikkivuk.videos.ui.PluginPanelVideos;
import com.vikkivuk.videos.ui.VideoComponent;
import com.vikkivuk.videos.ui.VideoDialog;
import net.mcreator.plugin.JavaPlugin;
import net.mcreator.plugin.Plugin;
import net.mcreator.plugin.events.workspace.MCreatorLoadedEvent;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.wysiwyg.WYSIWYGComponentRegistration;
import net.mcreator.ui.wysiwyg.WYSIWYGEditor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class MCreatorVideos extends JavaPlugin {
    public static final Logger LOG = LogManager.getLogger("MCreator Videos");
    public static PluginActions actions;

    public MCreatorVideos(Plugin plugin) {
        super(plugin);

        addListener(MCreatorLoadedEvent.class, event -> SwingUtilities.invokeLater(() -> {
            WYSIWYGEditor.COMPONENT_REGISTRY.add(new WYSIWYGComponentRegistration<>("video", "play", true, VideoComponent.class, VideoDialog.class));
            LOG.info("MCreator Videos >>> Registered WYSIWYG components");

            PluginPanelVideos panel = new PluginPanelVideos(event.getMCreator().mv);
            panel.setOpaque(false);

            event.getMCreator().mv.resourcesPan.addResourcesTab(L10N.t("menubar.videos"), panel);
            LOG.info("MCreator Videos >>> Added resources tab");

            actions = new PluginActions(event.getMCreator());
            LOG.info("MCreator Videos >>> Registered actions");
        }));

        LOG.info("MCreator Videos >>> Loaded!");
    }
}
