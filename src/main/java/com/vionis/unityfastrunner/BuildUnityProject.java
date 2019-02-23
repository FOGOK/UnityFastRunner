package com.vionis.unityfastrunner;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class BuildUnityProject extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        UnityProjectBuilder.INSTANCE.RunAction(e, false);
    }
}
