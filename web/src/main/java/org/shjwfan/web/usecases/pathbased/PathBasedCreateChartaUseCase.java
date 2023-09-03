package org.shjwfan.web.usecases.pathbased;

import static java.nio.file.StandardOpenOption.CREATE;

import java.io.OutputStream;
import java.util.concurrent.CompletableFuture;
import org.shjwfan.lib.bitmap.BitmapCreator;
import org.shjwfan.web.usecases.CreateChartaUseCase;
import org.shjwfan.web.utils.IdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PathBasedCreateChartaUseCase implements CreateChartaUseCase {

  @Autowired
  private BitmapCreator creator;

  @Autowired
  private IdService idService;

  @Autowired
  private PathService pathService;

  @Override
  public CompletableFuture<String> create(int width, int height) {
    return CompletableFuture.supplyAsync(() -> {
      String id = idService.nextId();
      try (OutputStream os = pathService.newBufferedOutputStream(id, CREATE)) {
        creator.create(os, width, height, (byte) 255, (byte) 255, (byte) 255, (byte) 0);
        return id;
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }
    });
  }
}
