package org.shjwfan.web.usecases;

import java.util.concurrent.CompletableFuture;

public interface CreateChartaUseCase {

  CompletableFuture<String> create(int width, int height);
}
