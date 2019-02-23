package com.vionis.unityfastrunner.actions.builders;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.util.PopupUtil;
import com.jetbrains.rider.projectView.nodes.ProjectModelNode;
import com.vionis.unityfastrunner.services.SelectedModuleKeeper;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SelectModule extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        final Project project = e.getProject();
        if (project == null) {
            Messages.showErrorDialog("Current project is null", "Oh no");
            return;
        }

        SelectedModuleKeeper keeper = ServiceManager.getService(SelectedModuleKeeper.class);
        keeper.setDataContext(e.getDataContext());
        PopupUtil.showBalloonForActiveFrame("Selected " + Arrays.stream(keeper.getProjectModelNodes()).map(ProjectModelNode::getName).collect(Collectors.toList()) + " project(s)", MessageType.INFO);
    }
}
