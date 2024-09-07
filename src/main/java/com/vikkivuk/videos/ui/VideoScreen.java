package com.vikkivuk.videos.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import me.srrapero720.watermedia.api.player.SyncVideoPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;

import java.awt.*;

public class VideoScreen {

    private final SyncVideoPlayer player;
    private ResourceLocation videoTextureLocation;
    private final Minecraft minecraft;

    public int height = 0;
    public int width = 0;

    // VIDEO INFO
    private int videoTexture = -1;

    public VideoScreen(String url, int volume, boolean controlBlocked, boolean canSkip, int optionInMode, int optionInSecs, int optionOutMode, int optionOutSecs) {
        this.minecraft = Minecraft.getInstance();
        this.player = new SyncVideoPlayer(minecraft);
        this.player.setVolume((int) (minecraft.options.getSoundSourceVolume(SoundSource.MASTER) * volume));

        if (!controlBlocked) {
            player.start(url);
            this.width = player.getDimensions().width;
            this.height = player.getDimensions().height;
        } else {
            player.startPaused(url);
        }
    }

    public void renderToGraphics(Graphics2D g, int x, int y, int width, int height) {
        if (player.isPlaying()) {
            videoTexture = player.getGlTexture(); // Get the OpenGL texture ID

            if (videoTexture != -1) {
                // Create a ResourceLocation if not already done
                if (videoTextureLocation == null) {
                    videoTextureLocation = createResourceLocationForVideoTexture(videoTexture);
                }

                // Calculate appropriate width and height for rendering the video
                Dimension videoDimensions = player.getDimensions();
                int renderWidth, renderHeight;

                // Maintain aspect ratio
                float screenAspectRatio = (float) width / height;
                float videoAspectRatio = (float) ((float) videoDimensions.getWidth() / videoDimensions.getHeight());

                if (videoAspectRatio > screenAspectRatio) {
                    renderWidth = width;
                    renderHeight = (int) (width / videoAspectRatio);
                } else {
                    renderWidth = (int) (height * videoAspectRatio);
                    renderHeight = height;
                }

                int xOffset = x + (width - renderWidth) / 2;
                int yOffset = y + (height - renderHeight) / 2;

                RenderSystem.setShaderTexture(0, videoTextureLocation);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                g.drawImage(null, xOffset, yOffset, renderWidth, renderHeight, null); // This is a placeholder

                RenderSystem.disableBlend();
            } else {
                renderBlackBackground(g, x, y, width, height);
            }
        } else {
            renderBlackBackground(g, x, y, width, height);
        }
    }

    private ResourceLocation createResourceLocationForVideoTexture(int textureId) {
        return new ResourceLocation("videotexture", String.valueOf(textureId));
    }

    private void renderBlackBackground(Graphics2D g, int x, int y, int width, int height) {
        g.setColor(new Color(0, 0, 0, 255));
        g.fillRect(x, y, width, height);
    }

    public void startVideo() {
        if (player != null) {
            player.play();
        }
    }

    public void stopVideo() {
        if (player != null) {
            player.stop();
        }
    }

    public void onClose() {
        if (player != null) {
            player.stop();
            player.release();
        }
    }
}