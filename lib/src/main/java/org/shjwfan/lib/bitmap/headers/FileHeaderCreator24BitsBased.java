package org.shjwfan.lib.bitmap.headers;

import static org.shjwfan.lib.bitmap.utils.BitmapUtils24BitsBased.rowLength;
import static org.shjwfan.lib.bitmap.utils.BitmapUtils24BitsBased.rowLengthOffset;

import org.springframework.stereotype.Service;

@Service
public class FileHeaderCreator24BitsBased implements FileHeaderCreator {

  @Override
  public FileHeader create(int w, int h) {
    int rowLength = rowLength(w);
    int rowLengthOffset = rowLengthOffset(w);
    return new FileHeader("BM", (rowLength + rowLengthOffset) * h + 54, (short) 0, (short) 0, 54);
  }
}
