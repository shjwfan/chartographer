package org.shjwfan.web.usecases.decorators;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;
import org.shjwfan.web.usecases.DeleteChartaUseCase;

public class DeleteChartaUseCaseLoggerDecorator extends LoggerDecorator implements DeleteChartaUseCase {

  private final DeleteChartaUseCase deleteUseCase;

  public DeleteChartaUseCaseLoggerDecorator(DeleteChartaUseCase deleteUseCase) {
    this.deleteUseCase = deleteUseCase;
  }

  @Override
  public CompletableFuture<Void> delete(String id) {
    Instant startedAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    logger.info("Charta {} deleting started at: {}", id, startedAt);

    return deleteUseCase.delete(id)
        .thenAccept(unused -> {
          Instant finishedAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);
          Duration duration = Duration.between(startedAt, finishedAt);

          long durationInSeconds = duration.toSeconds();
          long durationInMillis = duration.toMillis();

          logger.info("Charta {} deleting finished at: {}, duration in seconds: {}, duration in millis: {}",
              id, finishedAt, durationInSeconds, durationInMillis);
        })
        .exceptionally(e -> {
          logger.error("Charta deleting error: ", e);
          return null;
        });
  }
}
