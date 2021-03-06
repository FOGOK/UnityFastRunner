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
import com.vionis.unityfastrunner.actions.tools.SetterPathToAllDlls;
import com.vionis.unityfastrunner.actions.tools.SetterPathToUnityFastRunnerScripts;
import com.vionis.unityfastrunner.actions.tools.SetterPathToUnityProject;
import com.vionis.unityfastrunner.services.SelectedModuleKeeper;
import kotlin.Unit;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class UnityProjectBuilder {

    public static final UnityProjectBuilder INSTANCE = new UnityProjectBuilder();

    public void RunAction(AnActionEvent e, boolean isDebug, boolean isRebuild) {

        final Project project = e.getProject();
        if (project == null) {
            Messages.showErrorDialog("Current project is null", "Oh no");
            return;
        }


        SelectedModuleKeeper keeper = ServiceManager.getService(SelectedModuleKeeper.class);
        keeper.setDataContext(e.getDataContext());

        if (keeper.getProjectModelNodes() == null) {
            Messages.showErrorDialog("Please select one or more module(s)", "Oh no");
            return;
        }

        BuildHelper.INSTANCE.BuildSelectedProjects(keeper.getProject(), keeper.getProjectModelNodes(), isRebuild, (buildResultKind -> {

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


        String currentPathToScripts = PropertiesComponent.getInstance(project).getValue(SetterPathToUnityFastRunnerScripts.UnityScriptsKey, "");
        if (!Markdown.IsCorrectDirectory(currentPathToScripts))
        {
            Messages.showErrorDialog("Bad path to unity fast runner scripts: \"" + currentPathToScripts + "\". Please set " +
                    "correct path to unity fast runner scripts in Tools -> UnityFastRun Settings -> Set path to unity fast runner scripts", "Oh no");
            return;
        }

        ProgressManager.getInstance().run(new Task.Backgroundable(project, "Building Unity Project") {
            @Override
            public void run(@NotNull final ProgressIndicator progressIndicator) {

                progressIndicator.setFraction(0.5f);

                //build unity project
                final String commandToBuildUnity = currentPathToScripts + (isDebug ? "\\build_debug.bat" : "\\build.bat");
                final int resultCode;
                try {
                    Process p = Runtime.getRuntime().exec(commandToBuildUnity);
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
