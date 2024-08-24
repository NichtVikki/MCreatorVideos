package com.vikkivuk.videos;

import net.mcreator.ui.MCreator;
import net.mcreator.ui.action.ActionRegistry;
import net.mcreator.ui.action.BasicAction;
import net.mcreator.ui.init.UIRES;

public class PluginActions extends ActionRegistry {
    public final BasicAction importVideoFile;

    public PluginActions(MCreator mcreator) {
        super(mcreator);
        this.importVideoFile = new PluginVideoActions.ImportVideos(this).setIcon(UIRES.get("16px.play"));
    }
}
