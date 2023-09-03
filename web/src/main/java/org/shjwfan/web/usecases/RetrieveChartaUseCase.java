package org.shjwfan.web.usecases;

import java.util.concurrent.CompletableFuture;
import org.springframework.core.io.Resource;

public interface RetrieveChartaUseCase {

  CompletableFuture<Resource> retrieve(String id, int x, int y, int width, int height);
}
