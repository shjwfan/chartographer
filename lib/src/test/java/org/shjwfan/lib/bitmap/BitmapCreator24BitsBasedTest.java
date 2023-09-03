package org.shjwfan.lib.bitmap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.shjwfan.lib.bitmap.utils.TestUtils.getTestResourceAsPath;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.shjwfan.lib.bitmap.configurations.LazyInitConfiguration;
import org.shjwfan.lib.bitmap.exceptions.BitmapCreatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(LazyInitConfiguration.class)
class BitmapCreator24BitsBasedTest {

  @Autowired
  private BitmapCreator24BitsBased creator;

  @ParameterizedTest
  @CsvSource(textBlock = """
      250, 250, 255, 255, 255, '250x250.bmp',
      500, 500, 0, 0, 0, '500x500.bmp',
      """)
  void testCreate_mustCreate(int w, int h, int r, int g, int b, String fileName) throws IOException {
    Path stubPath = getTestResourceAsPath("bitmap/24-bits", "stub.bmp");
    try (OutputStream os = Files.newOutputStream(stubPath, StandardOpenOption.CREATE)) {
      creator.create(os, w, h, (byte) r, (byte) g, (byte) b, (byte) 0);
    }

    byte[] actualBytes = Files.readAllBytes(stubPath);
    byte[] expectedBytes = Files.readAllBytes(getTestResourceAsPath("bitmap/24-bits", fileName));

    assertThat(actualBytes).isEqualTo(expectedBytes);

    Files.deleteIfExists(stubPath);
  }

  @ParameterizedTest
  @CsvSource(textBlock = """
      -1, 1, 'invalid width: -1, width must be positive',
      1, -1, 'invalid height: -1, height must be positive',
      0, 1, 'invalid width: 0, width must be positive',
      1, 0, 'invalid height: 0, height must be positive',
      100000, 1, 'invalid width: 100000, width must not exceed 50000',
      1, 100000, 'invalid height: 100000, height must not exceed 50000',
      """)
  void testRetrieve_throwsBitmapRetrieverExceptionOnInvalidDimensions(int width, int height, String message) {
    assertThatThrownBy(() -> creator.create(null, width, height, (byte) 0, (byte) 0, (byte) 0, (byte) 0))
        .isInstanceOf(BitmapCreatorException.class)
        .hasMessage(message);
  }
}
