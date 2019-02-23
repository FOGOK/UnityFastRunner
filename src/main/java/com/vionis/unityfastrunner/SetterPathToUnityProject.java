package com.vionis.unityfastrunner;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.util.PopupUtil;

public class SetterPathToUnityProject extends AnAction {

    public static final String UnityStoreKey = "fastrununitypath";

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            Messages.showErrorDialog("Current project is null", "Oh no");
            return;
        }

        String currentUnityStorePath = PropertiesComponent.getInstance(project).getValue(UnityStoreKey, "");

        String unityStorePathFromDialog = Messages.showInputDialog("Please set path to unity project", "Hm", Messages.getQuestionIcon(), currentUnityStorePath, null);
        if (unityStorePathFromDialog == null || unityStorePathFromDialog.equals("") || currentUnityStorePath.equals(unityStorePathFromDialog))
            return;

        if (!Markdown.IsCorrectDirectory(unityStorePathFromDialog))
        {
            Messages.showErrorDialog("Bad path to unity project: \"" + unityStorePathFromDialog + "\". Please set correct path.", "Oh no");
            return;
        }

        PropertiesComponent.getInstance(project).setValue(UnityStoreKey, unityStorePathFromDialog);
        PopupUtil.showBalloonForActiveFrame("Success set new unity project path to \"" + unityStorePathFromDialog + "\"", MessageType.INFO);
    }
}
