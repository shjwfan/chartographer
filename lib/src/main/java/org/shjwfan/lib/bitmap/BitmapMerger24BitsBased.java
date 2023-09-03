package org.shjwfan.lib.bitmap;

import static org.shjwfan.lib.bitmap.utils.BitmapUtils24BitsBased.rowLength;

import org.shjwfan.lib.bitmap.exceptions.BitmapMergerException;
import org.springframework.stereotype.Service;

@Service
public class BitmapMerger24BitsBased implements BitmapMerger {

  @Override
  public Bitmap merge(Bitmap base, Bitmap other, int x, int y) {
    check(base, other, x, y);

    int baseRowLength = rowLength(base.width());
    int otherRowLength = rowLength(other.width());

    byte[][] baseData = base.data();
    byte[][] otherData = other.data();

    x *= 3;
    int baseHeightIndex = base.height() - y - 1;
    for (int otherHeightIndex = other.height() - 1; baseHeightIndex >= 0 && otherHeightIndex >= 0; baseHeightIndex--, otherHeightIndex--) {
      for (int rowIndex = 0; (rowIndex + x < baseRowLength && rowIndex < otherRowLength); rowIndex++) {
        baseData[baseHeightIndex][rowIndex + x] = otherData[otherHeightIndex][rowIndex];
      }
    }

    return base;
  }

  private void check(Bitmap base, Bitmap other, int x, int y) {
    checkFormat(base, other);
    checkX(base, x);
    checkY(base, y);
  }

  private void checkFormat(Bitmap base, Bitmap other) {
    if (base.format() != 24) {
      throw new BitmapMergerException(String.format("unsupported base bitmap format: %d, only 24 bits bitmap format is supported", base.format()));
    }

    if (other.format() != 24) {
      throw new BitmapMergerException(String.format("unsupported other bitmap format: %d, only 24 bits bitmap format is supported", other.format()));
    }
  }

  private void checkX(Bitmap base, int x) {
    if (x < 0) {
      throw new BitmapMergerException(String.format("invalid x: %d, x must be positive or 0", x));
    }

    if (x >= base.width()) {
      throw new BitmapMergerException(String.format("invalid x: %d, x must not exceed width of base bitmap: %d", x, base.width()));
    }
  }

  private void checkY(Bitmap base, int y) {
    if (y < 0) {
      throw new BitmapMergerException(String.format("invalid y: %d, y must be positive or 0", y));
    }

    if (y >= base.height()) {
      throw new BitmapMergerException(String.format("invalid y: %d, y must not exceed height of base bitmap: %d", y, base.height()));
    }
  }
}
