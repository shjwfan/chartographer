package org.shjwfan.web.usecases;

import java.util.concurrent.CompletableFuture;
import org.springframework.core.io.Resource;

public interface MergeChartaUseCase {

  CompletableFuture<Void> merge(String id, Resource resource, int x, int y);
}
