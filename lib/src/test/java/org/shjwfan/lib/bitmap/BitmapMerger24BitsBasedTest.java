package org.shjwfan.lib.bitmap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.shjwfan.lib.bitmap.utils.TestUtils.getTestResourceAsStream;

import java.io.IOException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.shjwfan.lib.bitmap.configurations.LazyInitConfiguration;
import org.shjwfan.lib.bitmap.exceptions.BitmapMergerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(LazyInitConfiguration.class)
class BitmapMerger24BitsBasedTest {

  @Autowired
  private BitmapMerger merger;

  @Autowired
  private BitmapReader reader;

  @ParameterizedTest
  @CsvSource(textBlock = """
      0, 0, 'wbbb.bmp',
      250, 0, 'bwbb.bmp',
      0, 250, 'bbwb.bmp',
      250, 250, 'bbbw.bmp',
      """)
  void testMerge_mustMergeBaseWithOther(int x, int y, String resource) throws IOException, BitmapMergerException {
    Bitmap base = reader.read(getTestResourceAsStream("bitmap/24-bits", "500x500.bmp"));
    Bitmap other = reader.read(getTestResourceAsStream("bitmap/24-bits", "250x250.bmp"));

    merger.merge(base, other, x, y);

    Bitmap expected = reader.read(getTestResourceAsStream("bitmap/24-bits", resource));
    assertThat(expected).isEqualTo(base);
    assertThat(expected.data()).isDeepEqualTo(base.data());
  }

  @ParameterizedTest
  @CsvSource(textBlock = """
      24, 42, 'unsupported other bitmap format: 42, only 24 bits bitmap format is supported',
      42, 24, 'unsupported base bitmap format: 42, only 24 bits bitmap format is supported',
      """)
  void testMerge_throwsBitmapMergerExceptionOnUnsupportedFormat(int baseFormat, int otherFormat, String message) {
    Bitmap base = mock(Bitmap.class);
    when(base.format()).thenReturn(baseFormat);

    Bitmap other = mock(Bitmap.class);
    when(other.format()).thenReturn(otherFormat);

    assertThatThrownBy(() -> merger.merge(base, other, 0, 0))
        .isInstanceOf(BitmapMergerException.class)
        .hasMessage(message);
  }

  @ParameterizedTest
  @CsvSource(textBlock = """
      -1, 0, 'invalid x: -1, x must be positive or 0',
      0, -1, 'invalid y: -1, y must be positive or 0',
      501, 0, 'invalid x: 501, x must not exceed width of base bitmap: 500',
      0, 501, 'invalid y: 501, y must not exceed height of base bitmap: 500',
      """)
  void testMerge_throwsBitmapMergerExceptionOnInvalidDimensions(int x, int y, String message) {
    Bitmap base = mock(Bitmap.class);
    when(base.format()).thenReturn(24);
    when(base.width()).thenReturn(500);
    when(base.height()).thenReturn(500);

    Bitmap other = mock(Bitmap.class);
    when(other.format()).thenReturn(24);
    when(other.width()).thenReturn(250);
    when(other.height()).thenReturn(250);

    assertThatThrownBy(() -> merger.merge(base, other, x, y))
        .isInstanceOf(BitmapMergerException.class)
        .hasMessage(message);
  }
}
