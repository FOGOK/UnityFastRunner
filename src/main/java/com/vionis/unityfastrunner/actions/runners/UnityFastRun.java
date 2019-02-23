package com.vionis.unityfastrunner.actions.runners;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.vionis.unityfastrunner.UnityFastRunner;

public class UnityFastRun extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        UnityFastRunner.INSTANCE.RunAction(e, false);
    }
}
