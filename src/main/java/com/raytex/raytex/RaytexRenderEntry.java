package com.raytex.raytex;

import com.intellij.openapi.editor.Inlay;

import java.io.File;
import java.nio.file.Path;

public class RaytexRenderEntry {
    public final String id;
    public final String latex;
    public Path path;
    public boolean active;
    public Inlay<?> inlay;
    public int start;
    public int end;

    public RaytexRenderEntry(String id, String latex, Path path, boolean active, Inlay<?> inlay,  int start, int end) {
        this.id = id;
        this.latex = latex;
        this.path = path;
        this.active = active;
        this.inlay = inlay;
        this.start = start;
        this.end = end;
    }
    public String getId() { return id; };
    public Path getPath() { return path; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Inlay<?> getInlay() { return inlay; }
    public void setInlay(Inlay<?> inlay) { this.inlay = inlay; }
}
