package com.vionis.unityfastrunner;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.components.ComponentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.jetbrains.rider.build.BuildHost;
import com.jetbrains.rider.build.BuildParameters;
import com.jetbrains.rider.build.ProjectDescriptor;
import com.jetbrains.rider.model.BuildResultKind;
import com.jetbrains.rider.model.BuildTarget;
import com.jetbrains.rider.projectView.nodes.ProjectModelNode;
import com.jetbrains.rider.projectView.nodes.ProjectModelNodeExtensionKt;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class BuildHelper {

    public static final BuildHelper INSTANCE = new BuildHelper();

    public void BuildSelectedProjects(Project project, ProjectModelNode[] projectModelNodes, @NotNull Function1<? super BuildResultKind, Unit> onFinished){
        if (project != null && projectModelNodes != null) {
            this.actionPerformedOnMultipleInternal(projectModelNodes, project, onFinished);
        }
    }

    public static final ProjectModelNode[] getItems(@NotNull DataContext dataContext) {
        ProjectModelNode[] var4 = ProjectModelNodeExtensionKt.getBackendProjectModelNodes(dataContext, true);
        ProjectModelNode[] var7 = var4;
        Collection var8 = (Collection)(new ArrayList(var4.length));
        int var9 = var4.length;

        for(int var10 = 0; var10 < var9; ++var10) {
            ProjectModelNode var11 = var7[var10];
            ProjectModelNode var17 = getItemInternal(var11);
            var8.add(var17);
        }

        Collection var6 = (Collection) CollectionsKt.filterNotNull((Iterable)((List)var8));
        if (var6 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.util.Collection<T>");
        } else {
            Object[] var10000 = var6.toArray(new ProjectModelNode[0]);
            if (var10000 == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
            } else {
                ProjectModelNode[] var5 = (ProjectModelNode[])var10000;
                int var16 = var4.length;
                int var18 = var5.length;
                return var16 != var18 ? new ProjectModelNode[0] : var5;
            }
        }
    }

    private static ProjectModelNode getItemInternal(@NotNull ProjectModelNode item) {
        Intrinsics.checkParameterIsNotNull(item, "item");
        return !ProjectModelNodeExtensionKt.isProject(item) && !ProjectModelNodeExtensionKt.isSolution(item) && !ProjectModelNodeExtensionKt.isSolutionFolder(item) ? null : item;
    }

    private void actionPerformedOnMultipleInternal(@NotNull ProjectModelNode[] items, @NotNull Project project, @NotNull Function1<? super BuildResultKind, Unit> onFinished) {
        Intrinsics.checkParameterIsNotNull(items, "items");
        Intrinsics.checkParameterIsNotNull(project, "project");
        ProjectModelNode[] var6 = items;
        int var7 = items.length;
        int var8 = 0;

        boolean var10000;
        while(true) {
            if (var8 >= var7) {
                var10000 = false;
                break;
            }

            ProjectModelNode var9 = var6[var8];
            if (ProjectModelNodeExtensionKt.isSolution(var9)) {
                var10000 = true;
                break;
            }

            ++var8;
        }

        List var23;
        if (var10000) {
            var23 = CollectionsKt.emptyList();
        } else {
            ArrayList var18 = new ArrayList();
            this.a(items, var18);
            Iterable var20 = (Iterable)var18;
            Collection var22 = (Collection)(new ArrayList(CollectionsKt.collectionSizeOrDefault(var20, 10)));
            Iterator var10 = var20.iterator();

            while(var10.hasNext()) {
                Object var11 = var10.next();
                ProjectDescriptor var12 = (ProjectDescriptor)var11;
                String var17 = var12.getLocation().getPath();
                var22.add(var17);
            }

            var23 = (List)var22;
        }

        List var5 = var23;
        ComponentManager var21 = (ComponentManager)project;
        Object var24 = var21.getComponent(BuildHost.class);
        if (var24 != null) {
            BuildHost var19 = (BuildHost)var24;
            if (!var19.requestBuild(new BuildParameters(new BuildTarget(), var5, false, false), onFinished)) {
                (new Notification("Build", "Another build is already in progress", "Another build is already in progress", NotificationType.WARNING)).notify(project);
            }
        }
    }

    private final void a(ProjectModelNode[] var1, ArrayList var2) {
        int var7 = var1.length;

        for(int var6 = 0; var6 < var7; ++var6) {
            ProjectModelNode var5 = var1[var6];
            if (ProjectModelNodeExtensionKt.isProject(var5)) {
                VirtualFile var8 = var5.getVirtualFile();
                if (var8 != null) {
                    var2.add(new ProjectDescriptor(var8));
                }
            }

            if (ProjectModelNodeExtensionKt.isSolutionFolder(var5)) {
                Collection var9 = (Collection)var5.getChildren(false, false);
                if (var9 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.util.Collection<T>");
                }

                Object[] var10000 = var9.toArray(new ProjectModelNode[0]);
                if (var10000 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
                }

                ProjectModelNode[] var12 = (ProjectModelNode[])var10000;
                this.a(var12, var2);
            }
        }

    }

}
