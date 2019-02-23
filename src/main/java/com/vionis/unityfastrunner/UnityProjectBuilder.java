package com.vionis.unityfastrunner;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.util.PopupUtil;
import com.intellij.pom.Navigatable;
import com.jetbrains.rider.model.BuildResultKind;
import kotlin.Unit;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class UnityProjectBuilder {

    public static final UnityProjectBuilder INSTANCE = new UnityProjectBuilder();

    public void RunAction(AnActionEvent e, boolean isDebug) {
        final Navigatable nav = e.getData(CommonDataKeys.NAVIGATABLE);
        if (nav == null) {
            Messages.showErrorDialog("Please select one module", "Oh no");
            return;
        }

        final Project project = e.getProject();
        if (project == null) {
            Messages.showErrorDialog("Current project is null", "Oh no");
            return;
        }


        ServiceManager.getService(SelectedModuleKeeper.class).setLastActionEventToRebuild(e);
        BuildHelper.INSTANCE.BuildSelectedProjects(e, (buildResultKind -> {

            if (buildResultKind.equals(BuildResultKind.Successful) || buildResultKind.equals(BuildResultKind.HasWarnings))
                BuildUnity(project, isDebug);

            return Unit.INSTANCE;
        }));
    }

    private void BuildUnity(Project project, boolean isDebug){

        String currentDlllsStorePath = PropertiesComponent.getInstance(project).getValue(SetterPathToAllDlls.DllsStoreKey, "");
        if (!Markdown.IsCorrectDirectory(currentDlllsStorePath))
        {
            Messages.showErrorDialog("Bad path to project dlls: \"" + currentDlllsStorePath + "\". Please set " +
                    "correct path to project dlls in Tools -> UnityFastRun Settings -> Set path to all project dlls", "Oh no");
            return;
        }

        String currentUnityPath = PropertiesComponent.getInstance(project).getValue(SetterPathToUnityProject.UnityStoreKey, "");
        if (!Markdown.IsCorrectDirectory(currentUnityPath))
        {
            Messages.showErrorDialog("Bad path to unity project: \"" + currentUnityPath + "\". Please set " +
                    "correct path to unity project in Tools -> UnityFastRun Settings -> Set path to unity project", "Oh no");
            return;
        }

        ProgressManager.getInstance().run(new Task.Backgroundable(project, "Building Unity Project") {
            @Override
            public void run(@NotNull final ProgressIndicator progressIndicator) {

                progressIndicator.setFraction(0.5f);

                //build unity project
                final String commandToBuildUnity = currentUnityPath + (isDebug ? "\\build_debug.bat" : "\\build.bat");
                final int resultCode;
                try {
                    ProcessBuilder builder = new ProcessBuilder(
                            "cmd.exe", "/c",  commandToBuildUnity);
                    Process p = builder.start();
                    p.waitFor(10, TimeUnit.MINUTES);
                    resultCode = p.exitValue();

                } catch (Exception e) {
                    PopupUtil.showBalloonForActiveFrame("Exception in execute unity build: " + e, MessageType.ERROR);
                    return;
                }

                if (resultCode != 0)
                {
                    PopupUtil.showBalloonForActiveFrame("Bad result code in unity build: (" + resultCode + "). See log in \"" + currentUnityPath + "\" to get more info", MessageType.ERROR);
                    return;
                }

                progressIndicator.setFraction(1f);

                PopupUtil.showBalloonForActiveFrame("Unity project has built" , MessageType.INFO);
            }
        });
    }
}
