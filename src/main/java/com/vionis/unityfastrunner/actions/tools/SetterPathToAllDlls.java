package com.vionis.unityfastrunner.actions.tools;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.vionis.unityfastrunner.Markdown;

public class SetterPathToAllDlls extends AnAction {

    public static final String DllsStoreKey = "fastrununitydllspath";

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            Messages.showErrorDialog("Current project is null", "Oh no");
            return;
        }

        VarSetter.SetNewVar(project, "path to all project dlls", DllsStoreKey, Markdown::IsCorrectDirectory);
    }


}