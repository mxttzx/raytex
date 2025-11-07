package com.raytex.raytex;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Raytex extends AnAction {
    private static RaytexRepository repository;
    private static final Pattern LATEX_BLOCK = Pattern.compile("\\$\\$(.+?)\\$\\$");

    private Project getProject(AnActionEvent e) {
        Project project = e.getProject();

        if (project == null) {
            Messages.showErrorDialog("Unable to load project", "Error");
        }

        return project;
    }

    private Editor getEditor(Project project) {
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (editor == null) {
            Messages.showErrorDialog(project, "Unable to load editor", "Error");
        }

        return editor;
    }

    private PsiFile getPsiFile(Project project, Document document) {
        PsiFile psiFile = PsiDocumentManager
                .getInstance(project)
                .getPsiFile(document);

        if (psiFile == null) {
            Messages.showErrorDialog(project, "Unable to load psi file", "Error");
        }

        return psiFile;
    }

    private RaytexRenderEntry renderLatex(Editor editor, String latex, int start, int end) throws IOException {
        RaytexRenderEntry entry = repository.getOrCreateRenderEntry(latex);
        entry.start = start;
        entry.end = end;

        foldComment(editor, entry, start, end);

        ApplicationManager.getApplication().invokeLater(() -> {
            try {
                BufferedImage image = ImageIO.read(entry.path.toFile());
                Renderer renderer = new Renderer(image);
                editor.getInlayModel().addInlineElement(entry.end, true, renderer);
                entry.setActive(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return entry;
    }

    private void foldComment(Editor editor, RaytexRenderEntry entry, int start, int end) throws IOException {
        WriteCommandAction.runWriteCommandAction(editor.getProject(), () -> {
            FoldingModel foldingModel = editor.getFoldingModel();
            foldingModel.runBatchFoldingOperation(() -> {
                FoldRegion region = foldingModel.addFoldRegion(start, end, "[]:");
                if (region != null) {
                    region.setExpanded(false);
                    entry.setActive(true);
                }
            });
        });
    }

    private void unfoldComment(Editor editor, RaytexRenderEntry entry, int start, int end) throws IOException {
        if (entry.getInlay() != null) {
            entry.getInlay().dispose();
            entry.setInlay(null);
        }

        WriteCommandAction.runWriteCommandAction(editor.getProject(), () -> {
            for (FoldRegion region : editor.getFoldingModel().getAllFoldRegions()) {
                if (region.getStartOffset() == entry.start && region.getEndOffset() == entry.end) {
                    region.setExpanded(true);
                    entry.setActive(false);
                }
            }
        });
    }
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = getProject(e);
        Editor editor = getEditor(project);

        if (repository == null) {
            repository = new RaytexRepository(project);
        }

        Document document = editor.getDocument();
        PsiFile psiFile = getPsiFile(project, document);

        psiFile.accept(new PsiRecursiveElementWalkingVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (element instanceof PsiComment) {
                    String text = element.getText();
                    Matcher matcher = LATEX_BLOCK.matcher(text);
                    int offset = element.getTextRange().getStartOffset();

                    while (matcher.find()) {
                        try {
                            String latex = matcher.group(1);
                            int start = offset + matcher.start(0);
                            int end = offset + matcher.end(0);

                            RaytexRenderEntry entry = renderLatex(editor, latex, start, end);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
                super.visitElement(element);
            }
        });
    }
}