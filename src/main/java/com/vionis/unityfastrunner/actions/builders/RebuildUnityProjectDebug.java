package com.vionis.unityfastrunner.actions.builders;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.vionis.unityfastrunner.UnityProjectBuilder;

public class RebuildUnityProjectDebug extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        UnityProjectBuilder.INSTANCE.RunAction(e, true, true);
    }
}