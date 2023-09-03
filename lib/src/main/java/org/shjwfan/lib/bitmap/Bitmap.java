package org.shjwfan.lib.bitmap;

import java.util.Objects;
import org.shjwfan.lib.bitmap.headers.DibHeader;
import org.shjwfan.lib.bitmap.headers.FileHeader;

public record Bitmap(
    @SuppressWarnings("NullAway.Init") FileHeader fileHeader,
    @SuppressWarnings("NullAway.Init") DibHeader dibHeader,
    @SuppressWarnings("NullAway.Init") byte[][] data
) {

  public int width() {
    return dibHeader.width();
  }

  public int height() {
    return dibHeader.height();
  }

  public int format() {
    return dibHeader.format();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Bitmap bitmap = (Bitmap) o;
    return Objects.equals(fileHeader, bitmap.fileHeader)
        && Objects.equals(dibHeader, bitmap.dibHeader);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fileHeader, dibHeader);
  }

  @Override
  public String toString() {
    return "Bitmap{" +
        "fileHeader=" + fileHeader +
        ", dibHeader=" + dibHeader +
        '}';
  }
}
