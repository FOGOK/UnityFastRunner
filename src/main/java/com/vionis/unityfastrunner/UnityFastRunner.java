package com.vionis.unityfastrunner;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.util.PopupUtil;
import com.jetbrains.rider.model.BuildResultKind;
import kotlin.Unit;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class UnityFastRunner {


    public static final UnityFastRunner INSTANCE = new UnityFastRunner();

    public void RunAction(AnActionEvent e, boolean isDebug) {
        Project project = e.getProject();
        if (project == null) {
            Messages.showErrorDialog("Current project is null", "Oh no");
            return;
        }

        AnActionEvent lastEvent = ServiceManager.getService(SelectedModuleKeeper.class).getLastActionEventToRebuild();
        if (lastEvent == null) {
            Messages.showWarningDialog("Please select target core project in Solution Explorer, " +
                    "click right mouse btn, select \"Advanced Build Actions\", " +
                    "and click to \"Build Unity Project With This Core\"", "Hmm");
            return;
        }

        BuildHelper.INSTANCE.BuildSelectedProjects(lastEvent, (buildResultKind -> {
            if (buildResultKind.equals(BuildResultKind.Successful) || buildResultKind.equals(BuildResultKind.HasWarnings))
                RunUnity(project, e, isDebug);

            return Unit.INSTANCE;
        }));
    }

    private void RunUnity(Project project, AnActionEvent e, boolean isDebug){
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

        String currentPathToGame = currentUnityPath + "\\builds\\build.exe";
        if (!Markdown.IsCorrectFile(currentPathToGame))
        {
            Messages.showErrorDialog("Bad path to unity game: \"" + currentPathToGame + "\". Please select target core project in Solution Explorer, " +
                    "click right mouse btn, select \"Advanced Build Actions\", " +
                    "and click to \"Build Unity Project With This Core\"", "Oh no");
            return;
        }


        //replace dlls, update some assets
        ProgressManager.getInstance().run(new Task.Backgroundable(project, "Replacing Dlls And Update Some Assets in Unity Project") {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {

                File sourceLocation = new File(currentDlllsStorePath);
                File targetLocation = new File(currentUnityPath + "\\builds\\build_Data\\Managed");

                if (!targetLocation.exists()) {
                    Messages.showErrorDialog("TargetLocation  (" + targetLocation + ") is not exists... ", "Oh no");
                    return;
                }

                try {
                    Markdown.CopyDllFiles(sourceLocation, targetLocation, indicator);
                } catch (IOException e) {
                    PopupUtil.showBalloonForActiveFrame("Exception in copy dlls: " + e, MessageType.ERROR);
                    return;
                }

                String commandToRepackAssets = currentUnityPath + "\\updateDefs.bat";
                final int resultCode;
                try {
                    ProcessBuilder builder = new ProcessBuilder(
                            "cmd.exe", "/c",  commandToRepackAssets);
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

                try {
                    new ProcessBuilder(currentPathToGame).start();
                } catch (IOException e) {
                    PopupUtil.showBalloonForActiveFrame("Exception in starting game: " + e, MessageType.ERROR);
                }

                PopupUtil.showBalloonForActiveFrame("Success!", MessageType.INFO);

                indicator.setFraction(1);
            }
        });
    }

}
