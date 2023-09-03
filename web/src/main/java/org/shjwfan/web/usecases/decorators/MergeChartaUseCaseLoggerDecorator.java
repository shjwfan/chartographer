package org.shjwfan.web.usecases.decorators;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;
import org.shjwfan.web.usecases.MergeChartaUseCase;
import org.springframework.core.io.Resource;

public class MergeChartaUseCaseLoggerDecorator extends LoggerDecorator implements MergeChartaUseCase {

  private final MergeChartaUseCase mergeUseCase;

  public MergeChartaUseCaseLoggerDecorator(MergeChartaUseCase mergeUseCase) {
    this.mergeUseCase = mergeUseCase;
  }

  @Override
  public CompletableFuture<Void> merge(String id, Resource resource, int x, int y) {
    Instant startedAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    logger.info("Charta {} merging started at: {}, with x: {} and y: {}", id, startedAt, x, y);

    return mergeUseCase.merge(id, resource, x, y)
        .thenAccept(unused -> {
          Instant finishedAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);
          Duration duration = Duration.between(startedAt, finishedAt);

          long durationInSeconds = duration.toSeconds();
          long durationInMillis = duration.toMillis();

          logger.info("Charta {} merging finished at: {}, with x: {} and y: {}, duration in seconds: {}, duration in millis: {}",
              id, finishedAt, x, y, durationInSeconds, durationInMillis);
        });
  }
}
