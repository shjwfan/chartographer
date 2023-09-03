package org.shjwfan.web.usecases;

import java.util.concurrent.CompletableFuture;

public interface DeleteChartaUseCase {

  CompletableFuture<Void> delete(String id);
}
