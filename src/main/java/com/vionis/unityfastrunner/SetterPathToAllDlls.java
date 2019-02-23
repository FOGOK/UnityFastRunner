package com.vionis.unityfastrunner;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.util.PopupUtil;

public class SetterPathToAllDlls extends AnAction {

    public static final String DllsStoreKey = "fastrununitydllspath";

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            Messages.showErrorDialog("Current project is null", "Oh no");
            return;
        }

        String currentDlllsStorePath = PropertiesComponent.getInstance(project).getValue(DllsStoreKey, "");

        String allDllsPathFromDialog = Messages.showInputDialog("Please set path to all project dlls", "Hm", Messages.getQuestionIcon(), currentDlllsStorePath, null);
        if (allDllsPathFromDialog == null || allDllsPathFromDialog.equals("") || currentDlllsStorePath.equals(allDllsPathFromDialog))
            return;

        if (!Markdown.IsCorrectDirectory(allDllsPathFromDialog))
        {
            Messages.showErrorDialog("Bad path to dlls project: \"" + allDllsPathFromDialog + "\". Please set correct path.", "Oh no");
            return;
        }

        PropertiesComponent.getInstance(project).setValue(DllsStoreKey, allDllsPathFromDialog);
        PopupUtil.showBalloonForActiveFrame("Success set new project dlls path to \"" + allDllsPathFromDialog + "\"", MessageType.INFO);
    }
}