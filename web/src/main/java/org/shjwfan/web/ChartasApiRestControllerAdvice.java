package org.shjwfan.web;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Map;
import org.shjwfan.lib.bitmap.exceptions.BitmapCreatorException;
import org.shjwfan.lib.bitmap.exceptions.BitmapMergerException;
import org.shjwfan.lib.bitmap.exceptions.BitmapRetrieverException;
import org.shjwfan.web.openapi.v1.UnexpectedResponse;
import org.shjwfan.web.usecases.exceptions.ChartaNotFoundException;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ChartasApiRestControllerAdvice {

  private static final Map<Class<? extends Throwable>, HttpStatus> EXCEPTION_CLASS_2_STATUS = Map.of(ChartaNotFoundException.class, HttpStatus.NOT_FOUND,
      BitmapCreatorException.class, HttpStatus.BAD_REQUEST, BitmapRetrieverException.class, HttpStatus.BAD_REQUEST, BitmapMergerException.class, HttpStatus.BAD_REQUEST);

  private final Logger logger;

  public ChartasApiRestControllerAdvice() {
    logger = getLogger(getClass());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<UnexpectedResponse> handleException(Exception e) {
    HttpStatus status;

    do {
      status = EXCEPTION_CLASS_2_STATUS.get(e.getClass());
      if (e.getCause() instanceof Exception cause) {
        e = cause;
      }
    } while (status == null && e.getCause() == null);

    logger.error("unexpected exception", e);
    return new ResponseEntity<>(new UnexpectedResponse(e.getMessage()), status != null ? status : HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
