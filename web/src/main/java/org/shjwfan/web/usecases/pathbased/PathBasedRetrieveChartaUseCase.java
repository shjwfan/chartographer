package org.shjwfan.web.usecases.pathbased;

import static java.nio.file.StandardOpenOption.READ;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import org.shjwfan.lib.bitmap.Bitmap;
import org.shjwfan.lib.bitmap.BitmapReader;
import org.shjwfan.lib.bitmap.BitmapRetriever;
import org.shjwfan.lib.bitmap.BitmapWriter;
import org.shjwfan.web.usecases.RetrieveChartaUseCase;
import org.shjwfan.web.usecases.exceptions.ChartaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class PathBasedRetrieveChartaUseCase implements RetrieveChartaUseCase {

  @Autowired
  private BitmapReader reader;

  @Autowired
  private BitmapRetriever retriever;

  @Autowired
  private BitmapWriter writer;

  @Autowired
  private PathService pathService;

  @Override
  public CompletableFuture<Resource> retrieve(String id, int x, int y, int width, int height) {
    return getBaseReadFuture(id)
        .thenApply(base -> retriever.retrieve(base, x, y, width, height))
        .thenApply(retrieved -> {
          try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            writer.write(os, retrieved);
            return new ByteArrayResource(os.toByteArray());
          } catch (Exception e) {
            throw new IllegalStateException(e);
          }
        });
  }

  private CompletableFuture<Bitmap> getBaseReadFuture(String id) {
    return CompletableFuture.supplyAsync(() -> {
      pathService.checkIfExistsOrElseThrow(id,
          () -> new ChartaNotFoundException(String.format("charta with id: %s was not found", id)));

      try (InputStream is = pathService.newBufferedInputStream(id, READ)) {
        return reader.read(is);
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }
    });
  }
}
