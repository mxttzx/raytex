package com.raytex.raytex;

import com.intellij.openapi.editor.Inlay;

import java.awt.*;
import java.io.File;
import java.nio.file.Path;

public class RaytexRenderEntry {
    public final String latex;
    public final Color color;
    public boolean active;
    public Inlay<?> inlay;
    public int start;
    public int end;

    public RaytexRenderEntry(String latex, Color color, boolean active, Inlay<?> inlay,  int start, int end) {
        this.latex = latex;
        this.color = color;
        this.active = active;
        this.inlay = inlay;
        this.start = start;
        this.end = end;
    }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Inlay<?> getInlay() { return inlay; }
    public void setInlay(Inlay<?> inlay) { this.inlay = inlay; }
}
