package org.shjwfan.web.usecases.pathbased;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.Lock;
import org.shjwfan.web.locks.LockService;
import org.shjwfan.web.usecases.DeleteChartaUseCase;
import org.shjwfan.web.usecases.exceptions.ChartaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PathBasedDeleteChartaUseCase implements DeleteChartaUseCase {

  @Autowired
  private LockService lockService;

  @Autowired
  private PathService pathService;

  @Override
  public CompletableFuture<Void> delete(String id) {
    return CompletableFuture.runAsync(() -> {
      Lock lock = lockService.computeIfAbsent(id);
      lock.lock();

      try {
        pathService.deleteIfExistsOrElseThrow(id,
            () -> new ChartaNotFoundException(String.format("charta with id: %s was not found", id)));
      } catch (Exception e) {
        throw new IllegalStateException(e);
      } finally {
        lock.unlock();
      }
    });
  }
}
