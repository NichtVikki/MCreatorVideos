package com.vikkivuk.videos.exts;

import net.mcreator.ui.init.L10N;

public enum VideoType {
    VIDEO("video");

    private final String id;

    VideoType(String id) {
        this.id = id;
    }

    public String getID() {
        return id;
    }

    @Override
    public String toString() {
        return L10N.t("dialog.videos_import." + this.id);
    }
}

