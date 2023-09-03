package org.shjwfan.lib.bitmap.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestUtils {

  public static Path getTestResourceAsPath(String dirName, String fileName) {
    return Path.of("src/test/resources", dirName, fileName);
  }

  public static InputStream getTestResourceAsStream(String dirName, String fileName) throws IOException {
    return Files.newInputStream(getTestResourceAsPath(dirName, fileName));
  }

  private TestUtils() {
  }
}
