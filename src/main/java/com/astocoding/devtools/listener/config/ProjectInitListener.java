package com.astocoding.devtools.listener.config;

import com.astocoding.devtools.listener.AutoFormatListener;
import com.astocoding.devtools.listener.EditorPasteListener;
import com.astocoding.devtools.listener.VirtualFileListenerImpl;
import com.intellij.ide.ui.laf.LafManagerImpl;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.editor.actionSystem.EditorActionManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.openapi.vfs.LocalFileSystem;
import org.jetbrains.annotations.NotNull;

public class ProjectInitListener implements ProjectManagerListener {

    @Override
    public void projectOpened(@NotNull Project project) {
        ProjectManagerListener.super.projectOpened(project);

        LocalFileSystem.getInstance().addVirtualFileListener(new VirtualFileListenerImpl());

        EditorActionManager.getInstance().setActionHandler(IdeActions.ACTION_EDITOR_PASTE,new EditorPasteListener());

        EditorActionManager.getInstance().setActionHandler(IdeActions.ACTION_EDITOR_ENTER,new AutoFormatListener(
                EditorActionManager.getInstance().getActionHandler(IdeActions.ACTION_EDITOR_ENTER)
        ));
//        EditorActionManager.getInstance().setActionHandler(IdeActions.ACTION_EDITOR_START_NEW_LINE,new AutoFormatListener());
    }

}
