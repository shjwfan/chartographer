package org.shjwfan.lib.bitmap.headers;

import static org.shjwfan.lib.bitmap.utils.BitmapUtils24BitsBased.rowLength;
import static org.shjwfan.lib.bitmap.utils.BitmapUtils24BitsBased.rowLengthOffset;

import org.springframework.stereotype.Service;

@Service
public class DibHeaderCreator24BitsBased implements DibHeaderCreator {

  // TODO: printWidth, printHeight, colorsCount, colorsCountImportant
  @Override
  public DibHeader create(int width, int height) {
    int rowLength = rowLength(width);
    int rowLengthOffset = rowLengthOffset(width);
    return new DibHeader(40, width, height, (short) 1, (short) 24, 0, (rowLength + rowLengthOffset) * height, 0, 0, 0, 0);
  }
}
