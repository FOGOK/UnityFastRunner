package com.vionis.unityfastrunner;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.components.ComponentManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.jetbrains.rider.projectView.nodes.ProjectModelNode;
import org.jetbrains.annotations.NotNull;

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
