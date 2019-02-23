package com.vionis.unityfastrunner;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class BuildUnityProjectDebug extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        UnityProjectBuilder.INSTANCE.RunAction(e, true);
    }
}
