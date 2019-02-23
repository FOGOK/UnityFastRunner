package com.vionis.unityfastrunner.services;

import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.components.ComponentManager;
import com.intellij.openapi.project.Project;
import com.jetbrains.rider.projectView.nodes.ProjectModelNode;
import com.vionis.unityfastrunner.BuildHelper;

public class SelectedModuleKeeper {

    private Project project;
    private ProjectModelNode[] projectModelNodes;

    public SelectedModuleKeeper(ComponentManager componentManager) {

    }

    public void setDataContext(DataContext dataContext) {
        project = dataContext.getData(CommonDataKeys.PROJECT);
        projectModelNodes = BuildHelper.getItems(dataContext);
    }

    public Project getProject() {
        return project;
    }

    public ProjectModelNode[] getProjectModelNodes() {
        return projectModelNodes;
    }
}
