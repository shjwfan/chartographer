package org.shjwfan.web.usecases.pathbased;

import static org.slf4j.LoggerFactory.getLogger;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class PathServiceCommandLineRunner implements CommandLineRunner {

  private static final String USER_DIR_PROPERTY = "user.dir";

  private final Logger logger = getLogger(getClass());

  @Autowired
  private PathService pathService;

  @Override
  public void run(String... args) {
    if (args.length > 0) {
      pathService.setBasePath(args[0]);
    }
    String basePath = pathService.getBasePath();
    if (StringUtils.isBlank(basePath)) {
      basePath = System.getProperty(USER_DIR_PROPERTY);
    }
    logger.info("Started with base path: {}", basePath);
  }
}
