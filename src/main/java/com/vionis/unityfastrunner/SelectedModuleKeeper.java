package com.vionis.unityfastrunner;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ComponentManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class SelectedModuleKeeper {

    private AnActionEvent lastActionEventToRebuild;

    public SelectedModuleKeeper(ComponentManager componentManager) {

    }

    public void setLastActionEventToRebuild(AnActionEvent lastActionEventToRebuild) {
        this.lastActionEventToRebuild = lastActionEventToRebuild;
    }

    public AnActionEvent getLastActionEventToRebuild() {
        return lastActionEventToRebuild;
    }
}
