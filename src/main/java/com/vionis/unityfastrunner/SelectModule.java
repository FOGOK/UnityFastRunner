package com.vionis.unityfastrunner;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

public class SelectModule extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        final Project project = e.getProject();
        if (project == null) {
            Messages.showErrorDialog("Current project is null", "Oh no");
            return;
        }

        ServiceManager.getService(SelectedModuleKeeper.class).setLastActionEventToRebuild(e);
    }
}
