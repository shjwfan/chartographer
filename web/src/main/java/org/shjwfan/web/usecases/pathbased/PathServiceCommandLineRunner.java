package org.shjwfan.web.usecases.pathbased;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class PathServiceCommandLineRunner implements CommandLineRunner {

  private final Logger logger = getLogger(getClass());

  @Autowired
  private PathService pathService;

  @Override
  public void run(String... args) {
    if (args.length > 0) {
      pathService.setBasePath(args[0]);
    }
    logger.info("Started with base path: {}", pathService.getBasePath());
  }
}
