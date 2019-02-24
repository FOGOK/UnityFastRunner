package com.vionis.unityfastrunner.actions.tools;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.util.PopupUtil;
import com.intellij.util.Function;

public class VarSetter {

    public static void SetNewVar(Project project, String varName, String key, Function<String, Boolean> validateFunc) {
        String currentDlllsStorePath = PropertiesComponent.getInstance(project).getValue(key, "");

        String allDllsPathFromDialog = Messages.showInputDialog("Please set " + varName, "Hm", Messages.getQuestionIcon(), currentDlllsStorePath, null);
        if (allDllsPathFromDialog == null || allDllsPathFromDialog.equals("") || currentDlllsStorePath.equals(allDllsPathFromDialog))
            return;

        if (!validateFunc.fun(allDllsPathFromDialog))
        {
            Messages.showErrorDialog("Bad " + varName + ": \"" + allDllsPathFromDialog + "\". Please set correct.", "Oh no");
            return;
        }

        PropertiesComponent.getInstance(project).setValue(key, allDllsPathFromDialog);
        PopupUtil.showBalloonForActiveFrame("Success set new " + varName + ": \"" + allDllsPathFromDialog + "\"", MessageType.INFO);
    }
}
