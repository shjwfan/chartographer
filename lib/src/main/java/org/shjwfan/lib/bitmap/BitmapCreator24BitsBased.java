package org.shjwfan.lib.bitmap;

import static org.shjwfan.lib.bitmap.utils.BitmapUtils24BitsBased.rowLength;
import static org.shjwfan.lib.bitmap.utils.BitmapUtils24BitsBased.rowLengthOffset;

import java.io.IOException;
import java.io.OutputStream;
import org.shjwfan.lib.bitmap.exceptions.BitmapCreatorException;
import org.shjwfan.lib.bitmap.headers.DibHeaderCreator24BitsBased;
import org.shjwfan.lib.bitmap.headers.FileHeaderCreator24BitsBased;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BitmapCreator24BitsBased implements BitmapCreator {

  @Autowired
  private DibHeaderCreator24BitsBased dibHeaderCreator;

  @Autowired
  private FileHeaderCreator24BitsBased fileHeaderCreator;

  @Autowired
  private BitmapWriter24BitsBased writer;

  @Override
  public void create(OutputStream os, int width, int height, byte r, byte g, byte b, byte a) throws IOException {
    check(width, height);

    int rowLength = rowLength(width);
    int rowLengthAlignment = rowLengthOffset(width);

    writer.writeFileHeader(os, fileHeaderCreator.create(width, height));
    writer.writeDibHeader(os, dibHeaderCreator.create(width, height));

    byte[][] data = new byte[height][rowLength + rowLengthAlignment];

    for (int i = height - 1; i >= 0; i--) {
      for (int j = 0; j < rowLength; j += 3) {
        data[i][j + 2] = r;
        data[i][j + 1] = g;
        data[i][j] = b;
      }
    }

    writer.writeData(os, data);
  }

  private void check(int width, int height) {
    checkWidth(width);
    checkHeight(height);
  }

  private void checkWidth(int width) {
    if (width <= 0) {
      throw new BitmapCreatorException(String.format("invalid width: %d, width must be positive", width));
    }

    if (width >= 50000) {
      throw new BitmapCreatorException(String.format("invalid width: %d, width must not exceed 50000", width));
    }
  }

  private void checkHeight(int height) {
    if (height <= 0) {
      throw new BitmapCreatorException(String.format("invalid height: %d, height must be positive", height));
    }

    if (height >= 50000) {
      throw new BitmapCreatorException(String.format("invalid height: %d, height must not exceed 50000", height));
    }
  }
}
