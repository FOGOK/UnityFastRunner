package com.vionis.unityfastrunner.actions.tools;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.vionis.unityfastrunner.Markdown;

public class SetterPathToUnityFastRunnerScripts extends AnAction {

    public static final String UnityScriptsKey = "fastrununityfastrunnerscriptspath";

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            Messages.showErrorDialog("Current project is null", "Oh no");
            return;
        }

        VarSetter.SetNewVar(project, "path to unity fast runner scripts", UnityScriptsKey, Markdown::IsCorrectDirectory);
    }
}