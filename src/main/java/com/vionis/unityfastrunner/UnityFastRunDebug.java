package com.vionis.unityfastrunner;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class UnityFastRunDebug extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        UnityFastRunner.INSTANCE.RunAction(e, true);
    }
}
