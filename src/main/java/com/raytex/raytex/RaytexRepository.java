package com.raytex.raytex;

import com.intellij.openapi.project.Project;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class RaytexRepository {
    private static final String CACHE_DIR = ".raytex-cache";
    private final Path cache;
    private final Map<String, RaytexRenderEntry> repository = new HashMap<>();

    public RaytexRepository(Project project) throws RuntimeException {
        cache = project.getBasePath() != null
                ? Paths.get(project.getBasePath(), CACHE_DIR)
                : Paths.get(CACHE_DIR);

        if (!Files.exists(cache)) {
            try {
                Files.createDirectories(cache);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public RaytexRenderEntry getOrCreateRenderEntry(String latex) throws IOException {
        String key = hash(latex);
        RaytexRenderEntry entry = repository.get(key);

        if (entry == null) {
            Path path = cache.resolve(key + ".png");
            BufferedImage image = Process.getImage(latex, 1.0f, 2);
            ImageIO.write(image, "png", path.toFile());
            entry = new RaytexRenderEntry(key, latex, path, true, null, 0, 0);

            repository.put(key, entry);
        }

        return entry;
    }

    private String hash(String latex) {
        return Integer.toHexString(latex.hashCode());
    }
}
