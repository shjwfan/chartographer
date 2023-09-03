package org.shjwfan.web.usecases.pathbased;

import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.Lock;
import org.shjwfan.lib.bitmap.Bitmap;
import org.shjwfan.lib.bitmap.BitmapMerger;
import org.shjwfan.lib.bitmap.BitmapReader;
import org.shjwfan.lib.bitmap.BitmapWriter;
import org.shjwfan.web.locks.LockService;
import org.shjwfan.web.usecases.MergeChartaUseCase;
import org.shjwfan.web.usecases.exceptions.ChartaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class PathBasedMergeChartaUseCase implements MergeChartaUseCase {

  @Autowired
  private BitmapMerger merger;

  @Autowired
  private BitmapReader reader;

  @Autowired
  private BitmapWriter writer;

  @Autowired
  private LockService lockService;

  @Autowired
  private PathService pathService;

  @Override
  public CompletableFuture<Void> merge(String id, Resource resource, int x, int y) {
    CompletableFuture<Bitmap> baseReadFuture = getBaseReadFuture(id);
    CompletableFuture<Bitmap> otherReadFuture = getOtherReadFuture(resource);

    return baseReadFuture.thenCombine(otherReadFuture, (base, other) -> merger.merge(base, other, x, y))
        .thenAccept(merged -> {
          Lock lock = lockService.computeIfAbsent(id);
          lock.lock();

          pathService.checkIfExistsOrElseThrow(id,
              () -> new ChartaNotFoundException(String.format("charta with id: %s was not found", id)));

          try (OutputStream os = pathService.newBufferedOutputStream(id, WRITE)) {
            writer.write(os, merged);
          } catch (Exception e) {
            throw new IllegalStateException(e);
          } finally {
            lock.unlock();
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

  private CompletableFuture<Bitmap> getOtherReadFuture(Resource resource) {
    return CompletableFuture.supplyAsync(() -> {
      try (InputStream is = resource.getInputStream()) {
        return reader.read(is);
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }
    });
  }
}
