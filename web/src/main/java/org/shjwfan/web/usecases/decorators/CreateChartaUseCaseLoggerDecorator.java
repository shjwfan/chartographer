package org.shjwfan.web.usecases.decorators;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;
import org.shjwfan.web.usecases.CreateChartaUseCase;

public class CreateChartaUseCaseLoggerDecorator extends LoggerDecorator implements CreateChartaUseCase {

  private final CreateChartaUseCase createUseCase;

  public CreateChartaUseCaseLoggerDecorator(CreateChartaUseCase createUseCase) {
    this.createUseCase = createUseCase;
  }

  @Override
  public CompletableFuture<String> create(int width, int height) {
    Instant startedAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    logger.info("Charta creating started at: {}, with width: {} and height: {}", startedAt, width, height);

    return createUseCase.create(width, height)
        .thenApply(id -> {
          Instant finishedAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);
          Duration duration = Duration.between(startedAt, finishedAt);

          long durationInSeconds = duration.toSeconds();
          long durationInMillis = duration.toMillis();

          logger.info("Finishing charta {} creating, with width: {} and height: {}, at {}, duration in seconds: {}, duration in millis: {}",
              id, width, height, finishedAt, durationInSeconds, durationInMillis);

          return id;
        });
  }
}
