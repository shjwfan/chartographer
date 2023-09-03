package org.shjwfan.lib.bitmap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.shjwfan.lib.bitmap.utils.TestUtils.getTestResourceAsPath;
import static org.shjwfan.lib.bitmap.utils.TestUtils.getTestResourceAsStream;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.shjwfan.lib.bitmap.configurations.LazyInitConfiguration;
import org.shjwfan.lib.bitmap.exceptions.BitmapRetrieverException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(LazyInitConfiguration.class)
class BitmapRetriever24BitsBasedTest {

  @Autowired
  private BitmapRetriever24BitsBased retriever;

  @Autowired
  private BitmapReader24BitsBased reader;

  @Autowired
  private BitmapWriter24BitsBased writer;

  @ParameterizedTest
  @CsvSource(textBlock = """
      0, 0, 250, 250, 'wbbb.bmp',
      250, 0, 250, 250, 'bwbb.bmp',
      0, 250, 250, 250, 'bbwb.bmp',
      250, 250, 250, 250, 'bbbw.bmp',
      """)
  void testRetrieve_mustRetrieveBase(int x, int y, int w, int h, String fileName) throws IOException {
    Path stubPath = getTestResourceAsPath("bitmap/24-bits", "stub.bmp");
    try (OutputStream os = Files.newOutputStream(stubPath, StandardOpenOption.CREATE)) {
      Bitmap bitmap = retriever.retrieve(reader.read(getTestResourceAsStream("bitmap/24-bits", fileName)), x, y, w, h);
      writer.write(os, bitmap);
    }

    byte[] actualBytes = Files.readAllBytes(stubPath);
    byte[] expectedBytes = Files.readAllBytes(getTestResourceAsPath("bitmap/24-bits", "250x250.bmp"));

    assertThat(actualBytes).isEqualTo(expectedBytes);

    Files.deleteIfExists(stubPath);
  }

  @Test
  void testRetrieve_mustThrowsBitmapRetrieverExceptionOnUnsupportedFormat() {
    Bitmap unsupportedBitmap = mock(Bitmap.class);
    when(unsupportedBitmap.format()).thenReturn(42);

    String message = "unsupported base bitmap format: 42, only 24 bits bitmap format is supported";

    assertThatThrownBy(() -> retriever.retrieve(unsupportedBitmap, 0, 0, 0, 0))
        .isInstanceOf(BitmapRetrieverException.class)
        .hasMessage(message);
  }

  @ParameterizedTest
  @CsvSource(textBlock = """
      -1, 1, 1, 1, 'invalid x: -1, x must be positive or 0',
      1, 1, -1, 1, 'invalid width: -1, width must be positive',
      1, -1, 1, 1, 'invalid y: -1, y must be positive or 0',
      1, 1, 1, -1, 'invalid height: -1, height must be positive',
      """)
  void testRetrieve_throwsBitmapRetrieverExceptionOnInvalidDimensions(int x, int y, int width, int height, String message) {
    Bitmap bitmap = mock(Bitmap.class);
    when(bitmap.format()).thenReturn(24);
    when(bitmap.width()).thenReturn(250);
    when(bitmap.height()).thenReturn(250);

    assertThatThrownBy(() -> retriever.retrieve(bitmap, x, y, width, height))
        .isInstanceOf(BitmapRetrieverException.class)
        .hasMessage(message);
  }

  @ParameterizedTest
  @CsvSource(textBlock = """
      250, 1, 1, 1, 'invalid sum of x and width: 251, sum of x and width must not exceed width of base bitmap: 250',
      1, 250, 1, 1, 'invalid sum of y and height: 251, sum of y and height must not exceed height of base bitmap: 250',
      """)
  void testRetrieve_throwsBitmapRetrieverExceptionOnInvalidDimensionsSum(int x, int y, int width, int height, String message) {
    Bitmap bitmap = mock(Bitmap.class);
    when(bitmap.format()).thenReturn(24);
    when(bitmap.width()).thenReturn(250);
    when(bitmap.height()).thenReturn(250);

    assertThatThrownBy(() -> retriever.retrieve(bitmap, x, y, width, height))
        .isInstanceOf(BitmapRetrieverException.class)
        .hasMessage(message);
  }
}
