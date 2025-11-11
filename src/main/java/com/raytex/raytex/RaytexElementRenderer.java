package com.raytex.raytex;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorCustomElementRenderer;
import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;

public class RaytexElementRenderer implements EditorCustomElementRenderer {
    private final Image image;
    public RaytexElementRenderer(RaytexRenderEntry entry, Color color) {
        try {
            image = Process.getImage(entry.latex, color);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public int calcWidthInPixels(@NotNull Inlay inlay) {
        return image.getWidth(null);
    }

    @Override
    public int calcHeightInPixels(@NotNull Inlay inlay) {
        Editor editor = inlay.getEditor();
        int lineHeight = editor.getLineHeight();
        int imageHeight = image.getHeight(null);

        int lines = (int) Math.ceil((double) imageHeight / lineHeight);

        return lines * lineHeight;
    }

    @Override
    public void paint(@NotNull Inlay inlay,
                      @NotNull Graphics g,
                      @NotNull Rectangle targetRegion,
                      @NotNull TextAttributes textAttributes) {
        int imageWidth = image.getWidth(null);
        int imageHeight = image.getHeight(null);

        int inlayHeight = targetRegion.height;

        int yOffset = targetRegion.y + (inlayHeight - imageHeight) / 2;
        int xOffset = targetRegion.x + JBUI.scale(2);

        Graphics2D graphics = (Graphics2D) g.create();
        try {
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            graphics.setColor(textAttributes.getEffectColor());
            graphics.setFont(graphics.getFont().deriveFont(Font.ITALIC));
            graphics.drawImage(image, xOffset, yOffset, null);
        } finally {
            graphics.dispose();
        }
    }
}
