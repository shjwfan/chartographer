package org.shjwfan.lib.bitmap;

import static org.shjwfan.lib.bitmap.utils.BitmapUtils24BitsBased.rowLength;
import static org.shjwfan.lib.bitmap.utils.BitmapUtils24BitsBased.rowLengthOffset;

import org.shjwfan.lib.bitmap.exceptions.BitmapRetrieverException;
import org.shjwfan.lib.bitmap.headers.DibHeaderCreator24BitsBased;
import org.shjwfan.lib.bitmap.headers.FileHeaderCreator24BitsBased;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BitmapRetriever24BitsBased implements BitmapRetriever {

  @Autowired
  private DibHeaderCreator24BitsBased dibHeaderCreator;

  @Autowired
  private FileHeaderCreator24BitsBased fileHeaderCreator;

  @Override
  public Bitmap retrieve(Bitmap base, int x, int y, int width, int height) {
    check(base, x, y, width, height);

    int retrievedRowLength = rowLength(width);
    int retrievedRowLengthOffset = rowLengthOffset(width);

    byte[][] baseData = base.data();
    byte[][] retrievedData = new byte[height][retrievedRowLength + retrievedRowLengthOffset];

    x *= 3;
    int baseHeightIndex = base.height() - y - 1;
    for (int retrievedHeightIndex = height - 1; baseHeightIndex >= 0 && retrievedHeightIndex >= 0; baseHeightIndex--, retrievedHeightIndex--) {
      if (retrievedRowLength >= 0) {
        byte[] baseRow = baseData[baseHeightIndex];
        byte[] retrievedRow = retrievedData[retrievedHeightIndex];
        System.arraycopy(baseRow, x, retrievedRow, 0, retrievedRowLength);
      }
    }

    return new Bitmap(fileHeaderCreator.create(width, height), dibHeaderCreator.create(width, height), retrievedData);
  }

  private void check(Bitmap base, int x, int y, int width, int height) {
    checkFormat(base);
    checkWidth(base, x, width);
    checkHeight(base, y, height);
  }

  private void checkFormat(Bitmap base) {
    if (base.format() != 24) {
      throw new BitmapRetrieverException(String.format("unsupported base bitmap format: %d, only 24 bits bitmap format is supported", base.format()));
    }
  }

  private void checkWidth(Bitmap base, int x, int width) {
    if (x < 0) {
      throw new BitmapRetrieverException(String.format("invalid x: %d, x must be positive or 0", x));
    }

    if (width <= 0) {
      throw new BitmapRetrieverException(String.format("invalid width: %d, width must be positive", width));
    }

    if (x + width > base.width()) {
      throw new BitmapRetrieverException(
          String.format("invalid sum of x and width: %d, sum of x and width must not exceed width of base bitmap: %d", x + width, base.width()));
    }
  }

  private void checkHeight(Bitmap base, int y, int height) {
    if (y < 0) {
      throw new BitmapRetrieverException(String.format("invalid y: %d, y must be positive or 0", y));
    }

    if (height <= 0) {
      throw new BitmapRetrieverException(String.format("invalid height: %d, height must be positive", height));
    }

    if (y + height > base.height()) {
      throw new BitmapRetrieverException(
          String.format("invalid sum of y and height: %d, sum of y and height must not exceed height of base bitmap: %d", y + height, base.height()));
    }
  }
}
