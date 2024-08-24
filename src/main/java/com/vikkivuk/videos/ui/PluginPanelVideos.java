package com.vikkivuk.videos.ui;

import com.vikkivuk.videos.MCreatorVideos;
import com.vikkivuk.videos.PluginVideoActions;
import net.mcreator.ui.component.TransparentToolBar;
import net.mcreator.ui.component.util.ComponentUtils;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.init.UIRES;
import net.mcreator.ui.workspace.IReloadableFilterable;
import net.mcreator.ui.workspace.WorkspacePanel;
import net.mcreator.util.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.*;
import java.util.List;

public class PluginPanelVideos extends JPanel implements IReloadableFilterable {
    private final WorkspacePanel workspacePanel;
    private final FilterModel listmodel = new FilterModel();
    private final JList<File> videoList;

    public PluginPanelVideos(final WorkspacePanel workspacePanel) {
        super(new BorderLayout());
        this.videoList = new JList<>(this.listmodel);
        this.setOpaque(false);
        this.workspacePanel = workspacePanel;
        this.videoList.setOpaque(false);
        this.videoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.videoList.setLayoutOrientation(JList.VERTICAL);
        this.videoList.setVisibleRowCount(-1);
        this.videoList.setCellRenderer(new Render());
        this.videoList.addMouseMotionListener(new MouseAdapter() {
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                int idx = PluginPanelVideos.this.videoList.locationToIndex(e.getPoint());
                File videoFile = PluginPanelVideos.this.videoList.getModel().getElementAt(idx);
                if (videoFile != null)
                    workspacePanel.getMCreator().getStatusBar().setMessage(videoFile.getName());
            }
        });

        JScrollPane sp = new JScrollPane(this.videoList);
        sp.setOpaque(false);
        sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sp.getViewport().setOpaque(false);
        sp.getVerticalScrollBar().setUnitIncrement(11);
        sp.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        this.add("Center", sp);

        TransparentToolBar bar = new TransparentToolBar();
        bar.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 0));

        JButton importVideoButton = L10N.button("action.workspace.resources.import_video_file");
        importVideoButton.setIcon(UIRES.get("16px.play"));
        importVideoButton.setContentAreaFilled(false);
        importVideoButton.setOpaque(false);
        importVideoButton.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        importVideoButton.addActionListener(e -> {
            // Implement the video import action here
            MCreatorVideos.actions.importVideoFile.doAction();
            this.reloadElements();
        });
        ComponentUtils.deriveFont(importVideoButton, 12.0F);
        bar.add(importVideoButton);

        JButton deleteButton = L10N.button("workspace.videos.delete_selected", new Object[0]);
        deleteButton.setIcon(UIRES.get("16px.delete"));
        deleteButton.setOpaque(false);
        deleteButton.setContentAreaFilled(false);
        deleteButton.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        deleteButton.addActionListener(e -> {
            this.deleteCurrentlySelected();
        });
        bar.add(deleteButton);

        this.videoList.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    PluginPanelVideos.this.deleteCurrentlySelected();
                }
            }
        });

        this.add("North", bar);
    }

    private void deleteCurrentlySelected() {
        File selectedFile = this.videoList.getSelectedValue();
        if (selectedFile != null) {
            int n = JOptionPane.showConfirmDialog(this.workspacePanel.getMCreator(),
                    L10N.t("workspace.videos.delete_confirm_message", new Object[0]),
                    L10N.t("common.confirmation", new Object[0]),
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null);
            if (n == JOptionPane.YES_OPTION) {
                selectedFile.delete();
                this.reloadElements();
            }
        }
    }

    public void reloadElements() {
        this.listmodel.removeAllElements();
        // Load video files from the workspace
        List<File> videoFiles = getVideoFilesFromWorkspace();
        for (File videoFile : videoFiles) {
            this.listmodel.addElement(videoFile);
        }
        this.refilterElements();
    }

    public void refilterElements() {
        this.listmodel.refilter();
    }

    // Method to load the list of video files from the workspace
    private List<File> getVideoFilesFromWorkspace() {
        File videoDir = PluginVideoActions.getVideoDir(workspacePanel.getMCreator());
        return PluginVideoActions.getVideoFiles(workspacePanel.getMCreator());
    }

    private class FilterModel extends DefaultListModel<File> {
        List<File> items = new ArrayList<>();
        List<File> filterItems = new ArrayList<>();

        FilterModel() {}

        public int indexOf(Object elem) {
            return elem instanceof File ? this.filterItems.indexOf(elem) : -1;
        }

        public File getElementAt(int index) {
            return !this.filterItems.isEmpty() && index < this.filterItems.size() ? this.filterItems.get(index) : null;
        }

        public int getSize() {
            return this.filterItems.size();
        }

        public void addElement(File o) {
            this.items.add(o);
            this.refilter();
        }

        public void removeAllElements() {
            super.removeAllElements();
            this.items.clear();
            this.filterItems.clear();
        }

        public boolean removeElement(Object a) {
            if (a instanceof File) {
                this.items.remove(a);
                this.filterItems.remove(a);
            }
            return super.removeElement(a);
        }

        void refilter() {
            this.filterItems.clear();
            String term = PluginPanelVideos.this.workspacePanel.search.getText();
            this.filterItems.addAll(this.items.stream()
                    .filter(Objects::nonNull)
                    .filter(item -> item.getName().toLowerCase(Locale.ENGLISH).contains(term.toLowerCase(Locale.ENGLISH)))
                    .toList());
            if (PluginPanelVideos.this.workspacePanel.sortName.isSelected()) {
                this.filterItems.sort(Comparator.comparing(File::getName));
            }
            if (PluginPanelVideos.this.workspacePanel.desc.isSelected()) {
                Collections.reverse(this.filterItems);
            }
            this.fireContentsChanged(this, 0, this.getSize());
        }
    }

    static class Render extends JLabel implements ListCellRenderer<File> {
        Render() {
            setHorizontalAlignment(SwingConstants.LEFT);
            setVerticalAlignment(SwingConstants.CENTER);
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            setOpaque(true);
        }

        public JLabel getListCellRendererComponent(JList<? extends File> list, File videoFile, int index, boolean isSelected, boolean cellHasFocus) {
            setBackground(isSelected ? UIManager.getColor("List.selectionBackground") : UIManager.getColor("List.background"));
            setForeground(isSelected ? UIManager.getColor("List.selectionForeground") : UIManager.getColor("List.foreground"));

            setText(videoFile.getName());
            setToolTipText(videoFile.getAbsolutePath());
            setIcon(UIRES.get("video")); // Placeholder for video icon

            // Stretch label to use full width
            setPreferredSize(new Dimension(list.getWidth() - 10, getPreferredSize().height));

            return this;
        }
    }
}
