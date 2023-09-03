package org.shjwfan.web.usecases.pathbased;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PathService {

  @SuppressWarnings("NullAway.Init")
  @Value("${chartographer.base-path:}")
  private String basePath;

  public String getBasePath() {
    return basePath;
  }

  public void setBasePath(String basePath) {
    this.basePath = basePath;
  }

  public <T extends Throwable> void checkIfExistsOrElseThrow(String id, Supplier<? extends T> throwableSupplier) throws T {
    Path path = getPath(id);
    if (!Files.exists(path)) {
      throw throwableSupplier.get();
    }
  }

  public <T extends Throwable> void deleteIfExistsOrElseThrow(String id, Supplier<? extends T> throwableSupplier) throws T, IOException {
    Path path = getPath(id);
    if (Files.exists(path)) {
      Files.delete(path);
    } else {
      throw throwableSupplier.get();
    }
  }

  public BufferedInputStream newBufferedInputStream(String id, OpenOption... options) throws IOException {
    Path path = getPath(id);
    return new BufferedInputStream(Files.newInputStream(path, options));
  }

  public BufferedOutputStream newBufferedOutputStream(String id, OpenOption... options) throws IOException {
    Path path = getPath(id);
    return new BufferedOutputStream(Files.newOutputStream(path, options));
  }

  private Path getPath(String id) {
    return Paths.get(basePath, String.format("%s.bmp", id));
  }
}
