package com.raytex.raytex;

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
        return image.getHeight(null);
    }

    @Override
    public void paint(@NotNull Inlay inlay, @NotNull Graphics g, @NotNull Rectangle targetRegion, @NotNull TextAttributes textAttributes) {
        Insets insets = JBUI.insets(2);

        g.setColor(textAttributes.getEffectColor());
        g.setFont(g.getFont().deriveFont(Font.ITALIC));
        g.drawImage(image, targetRegion.x + insets.left, targetRegion.y + insets.top, null);
    }
}
