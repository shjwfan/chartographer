package org.shjwfan.web.usecases.decorators;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;
import org.shjwfan.web.usecases.RetrieveChartaUseCase;
import org.springframework.core.io.Resource;

public class RetrieveChartaUseCaseLoggerDecorator extends LoggerDecorator implements RetrieveChartaUseCase {

  private final RetrieveChartaUseCase retrieveUseCase;

  public RetrieveChartaUseCaseLoggerDecorator(RetrieveChartaUseCase retrieveUseCase) {
    this.retrieveUseCase = retrieveUseCase;
  }

  @Override
  public CompletableFuture<Resource> retrieve(String id, int x, int y, int width, int height) {
    Instant startedAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    logger.info("Charta {} retrieving started at: {}, with x: {}, y: {}, width: {} and height: {}", id, startedAt, x, y, width, height);

    return retrieveUseCase.retrieve(id, x, y, width, height)
        .thenApply(retrieved -> {
          Instant finishedAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);
          Duration duration = Duration.between(startedAt, finishedAt);

          long durationInSeconds = duration.toSeconds();
          long durationInMillis = duration.toMillis();

          logger.info("Charta {} retrieving finished at: {}, x: {}, y: {}, width: {} and height: {}, duration in seconds: {}, duration in millis: {}",
              id, finishedAt, x, y, width, height, durationInSeconds, durationInMillis);

          return retrieved;
        })
        .exceptionally(e-> {
          logger.error("Charta {} retrieving error: ", id, e);
          return null;
        });
  }
}
