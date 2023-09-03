package org.shjwfan.web.utils;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;

@Service
public class BootAtBasedIdService implements IdService {

  private final Instant bootAt = Instant.now();
  private final AtomicInteger nextId = new AtomicInteger();

  @Override
  public String nextId() {
    return String.format("%d_%d", bootAt.toEpochMilli(), nextId.getAndIncrement());
  }
}
