package org.shjwfan.lib.bitmap;

import static org.shjwfan.lib.bitmap.utils.BitmapUtils24BitsBased.rowLength;
import static org.shjwfan.lib.bitmap.utils.BitmapUtils24BitsBased.rowLengthOffset;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.shjwfan.lib.bitmap.headers.DibHeader;
import org.shjwfan.lib.bitmap.headers.FileHeader;
import org.springframework.stereotype.Service;

@Service
public class BitmapReader24BitsBased implements BitmapReader {

  @Override
  public Bitmap read(InputStream is) throws IOException {
    FileHeader fileHeader = readFileHeader(is);
    DibHeader dibHeader = readDibHeader(is);
    byte[][] data = readData(is, dibHeader.width(), dibHeader.height());
    return new Bitmap(fileHeader, dibHeader, data);
  }

  public FileHeader readFileHeader(InputStream is) throws IOException {
    byte[] bytes = new byte[14];
    if (14 != is.read(bytes)) {
      throw new IOException();
    }

    ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);

    String id = new String(new byte[]{buffer.get(), buffer.get()});
    return new FileHeader(id, buffer.getInt(), buffer.getShort(), buffer.getShort(), buffer.getInt());
  }

  public DibHeader readDibHeader(InputStream is) throws IOException {
    byte[] bytes = new byte[40];
    if (40 != is.read(bytes)) {
      throw new IOException();
    }

    ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);

    return new DibHeader(buffer.getInt(), buffer.getInt(), buffer.getInt(),
        buffer.getShort(), buffer.getShort(), buffer.getInt(), buffer.getInt(),
        buffer.getInt(), buffer.getInt(), buffer.getInt(), buffer.getInt());
  }

  private byte[][] readData(InputStream is, int width, int height) throws IOException {
    int rowLength = rowLength(width);
    int rowLengthOffset = rowLengthOffset(width);

    byte[][] data = new byte[height][rowLength + rowLengthOffset];

    for (int heightIndex = 0; heightIndex < height; heightIndex++) {
      byte[] row = data[heightIndex];
      if (row.length != is.read(row)) {
        throw new IOException();
      }
    }

    return data;
  }
}
