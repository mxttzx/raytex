package com.raytex.raytex;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.FoldRegion;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Disposer;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.Nullable;

public class RaytexHelper {
    public static Project getProject(AnActionEvent e) {
        Project project = e.getProject();

        if (project == null) {
            Messages.showErrorDialog("Unable to load project", "Error");
        }

        return project;
    }

    public static Editor getEditor(Project project) {
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (editor == null) {
            Messages.showErrorDialog(project, "Unable to load editor", "Error");
        }

        return editor;
    }

    public static PsiFile getPsiFile(Project project, Document document) {
        PsiFile psiFile = PsiDocumentManager
                .getInstance(project)
                .getPsiFile(document);

        if (psiFile == null) {
            Messages.showErrorDialog(project, "Unable to load psi file", "Error");
        }

        return psiFile;
    }

    public static @Nullable FoldRegion getFoldRegion(Editor editor, int start, int end) {
        for (FoldRegion region : editor.getFoldingModel().getAllFoldRegions()) {
            if (region.getStartOffset() == start && region.getEndOffset() == end) {
                return region;
            }
        }
        return null;
    }

    public static void addDisposableCaretListener(Editor editor, CaretListener listener, Disposable parent) {
        editor.getCaretModel().addCaretListener(listener);
        if (parent != null) {
            Disposer.register(parent, () -> editor.getCaretModel().removeCaretListener(listener));
        }
    }
}
