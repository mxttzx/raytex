package com.raytex.raytex;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.raytex.raytex.grammars.RaytexLexerAdapter;
import com.raytex.raytex.grammars.RaytexTokenTypes;
import org.jetbrains.annotations.Debug;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import static com.intellij.psi.util.PsiEditorUtil.getPsiFile;

public class RaytexRender extends AnAction {
    private void renderInlineComment(Editor editor, RaytexRenderEntry entry) {
        FoldingModel foldingModel = editor.getFoldingModel();
        foldingModel.runBatchFoldingOperation(() -> {
            for (FoldRegion region : editor.getFoldingModel().getAllFoldRegions()) {
                if (region.getStartOffset() == entry.start && region.getEndOffset() == entry.end) {
                    foldingModel.removeFoldRegion(region);
                }
            }

            FoldRegion region = foldingModel.addFoldRegion(entry.start, entry.end, StringUtil.THREE_DOTS);
            if (region != null) {
                region.setExpanded(false);
            }
        });

        InlayModel inlayModel = editor.getInlayModel();
        ApplicationManager.getApplication().invokeLater(() -> {
            RaytexElementRenderer renderer = new RaytexElementRenderer(entry, entry.color);
            int offset = Math.max(0, entry.start - 1);
            inlayModel.addInlineElement(offset, true, renderer);
        });

        CaretListener listener = new CaretListener() {
            @Override
            public void caretPositionChanged(final @NotNull CaretEvent e) {
                FoldRegion region = RaytexHelper.getFoldRegion(editor, entry.start, entry.end);
                if (region != null && region.isExpanded()) {
                    int offset = editor.getCaretModel().getOffset();
                    if (offset < region.getStartOffset() ||  offset > region.getEndOffset()) {
                        editor.getFoldingModel().runBatchFoldingOperation(() -> region.setExpanded(false));
                    }
                }
            }
        };
        editor.getCaretModel().addCaretListener(listener);
    }

    private void renderBlockComment(Editor editor, RaytexRenderEntry entry) {
        InlayModel inlayModel = editor.getInlayModel();
        ApplicationManager.getApplication().invokeLater(() -> {
            RaytexElementRenderer renderer = new RaytexElementRenderer(entry, entry.color);
            inlayModel.addBlockElement(entry.end, true, true, 0, renderer);
        });
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = RaytexHelper.getProject(e);
        Editor editor = RaytexHelper.getEditor(project);
        PsiFile psiFile = getPsiFile(editor);

        psiFile.accept(new PsiRecursiveElementWalkingVisitor() {
            @Override
            public void visitComment(@NotNull PsiComment comment) {
                String text = comment.getText();

                Lexer lexer = new RaytexLexerAdapter();
                lexer.start(text);

                while (lexer.getTokenType() != null) {
                    if (lexer.getTokenType() == RaytexTokenTypes.LATEX_DELIM) {
                        int start = lexer.getTokenEnd();
                        lexer.advance();

                        StringBuilder latexBuilder = new StringBuilder();
                        int contentStart = start;

                        while (lexer.getTokenType() != null &&
                                lexer.getTokenType() != RaytexTokenTypes.LATEX_DELIM) {
                            if (lexer.getTokenType() == RaytexTokenTypes.COMMENT_TEXT) {
                                latexBuilder.append(text, lexer.getTokenStart(), lexer.getTokenEnd());
                            }
                            lexer.advance();
                        }

                        int contentEnd = lexer.getTokenStart();
                        String latex = latexBuilder.toString().trim();

                        int globStart = comment.getTextRange().getStartOffset() + contentStart;
                        int globEnd = comment.getTextRange().getStartOffset() + contentEnd;

                        RaytexRenderEntry entry = new RaytexRenderEntry(
                                latex,
                                editor.getColorsScheme().getAttributes(
                                        DefaultLanguageHighlighterColors.LINE_COMMENT
                                ).getForegroundColor(),
                                true,
                                null,
                                globStart,
                                globEnd
                        );
                        renderInlineComment(editor, entry);
                    } else {
                        lexer.advance();
                    }
                }
                super.visitComment(comment);
            }
        });
    }
}