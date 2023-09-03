package org.shjwfan.web;

import java.util.concurrent.CompletableFuture;
import org.shjwfan.web.openapi.v1.ChartasApi;
import org.shjwfan.web.openapi.v1.CreateChartaResponse;
import org.shjwfan.web.usecases.CreateChartaUseCase;
import org.shjwfan.web.usecases.DeleteChartaUseCase;
import org.shjwfan.web.usecases.MergeChartaUseCase;
import org.shjwfan.web.usecases.RetrieveChartaUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChartasApiRestController implements ChartasApi {

  @Autowired
  private CreateChartaUseCase createUseCase;

  @Autowired
  private DeleteChartaUseCase deleteUseCase;

  @Autowired
  private MergeChartaUseCase mergeUseCase;

  @Autowired
  private RetrieveChartaUseCase retrieveUseCase;

  @Override
  public CompletableFuture<ResponseEntity<CreateChartaResponse>> create(Integer width, Integer height) {
    return createUseCase.create(width, height)
        .thenApply(CreateChartaResponse::new)
        .thenApply(body -> ResponseEntity.ok().body(body));
  }

  @Override
  public CompletableFuture<ResponseEntity<Void>> delete(String id) {
    return deleteUseCase.delete(id)
        .thenApply(unused -> ResponseEntity.ok(null));
  }

  @Override
  public CompletableFuture<ResponseEntity<Void>> merge(String id, Integer x, Integer y, Resource body) {
    return mergeUseCase.merge(id, body, x, y)
        .thenApply(unused -> ResponseEntity.ok(null));
  }

  @Override
  public CompletableFuture<ResponseEntity<Resource>> retrieve(String id, Integer x, Integer y, Integer width, Integer height) {
    return retrieveUseCase.retrieve(id, x, y, width, height)
        .thenApply(resource -> ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource));
  }
}
