package com.raytex.raytex;

import com.intellij.openapi.editor.EditorCustomElementRenderer;
import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Renderer implements EditorCustomElementRenderer {
    private final BufferedImage image;

    public Renderer(BufferedImage image) {
        this.image = image;
    }

    @Override
    public int calcWidthInPixels(@NotNull Inlay inlay) {
        return image.getWidth();
    }

    @Override
    public int calcHeightInPixels(@NotNull Inlay inlay) {
        return image.getHeight();
    }

    @Override
    public void paint(@NotNull Inlay inlay, @NotNull Graphics g, @NotNull Rectangle targetRegion, @NotNull TextAttributes textAttributes) {
        Graphics2D g2 = (Graphics2D) g;

        Insets insets = JBUI.insets(4);
        g2.drawImage(image, null, targetRegion.x + insets.left, targetRegion.y + insets.top);
    }
}
