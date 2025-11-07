package com.raytex.raytex;

import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Process {
    public static BufferedImage getImage(String latex, float scale, int inset) throws IOException {
        Color colors = new JBColor(Color.BLACK, Color.WHITE);
        TeXFormula formula = new TeXFormula(latex);
        formula.setColor(colors);
        TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 15f * scale);

        BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(new JBColor(
                new Color(0, 0, 0, 0),
                new Color(0, 0, 0, 0)));
        g.fillRect(0, 0, image.getWidth(), image.getHeight());

        icon.paintIcon(null, g, 0, 0);
        g.dispose();
        return image;
    }
}
