package org.shjwfan.lib.bitmap;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.shjwfan.lib.bitmap.headers.DibHeader;
import org.shjwfan.lib.bitmap.headers.FileHeader;
import org.springframework.stereotype.Service;

@Service
public class BitmapWriter24BitsBased implements BitmapWriter {

  @Override
  public void write(OutputStream os, Bitmap bitmap) throws IOException {
    writeFileHeader(os, bitmap.fileHeader());
    writeDibHeader(os, bitmap.dibHeader());
    writeData(os, bitmap.data());
  }

  public void writeFileHeader(OutputStream os, FileHeader fileHeader) throws IOException {
    ByteBuffer buffer = ByteBuffer.allocate(14).order(ByteOrder.LITTLE_ENDIAN);

    byte[] id = fileHeader.id().getBytes();
    buffer.put(id);

    int fileSize = fileHeader.fileSize();
    buffer.putInt(fileSize);

    short reserved0 = fileHeader.reserved0();
    buffer.putShort(reserved0);

    short reserved1 = fileHeader.reserved1();
    buffer.putShort(reserved1);

    int dataOffset = fileHeader.dataOffset();
    buffer.putInt(dataOffset);

    byte[] array = buffer.array();
    os.write(array);
  }

  public void writeDibHeader(OutputStream os, DibHeader dibHeader) throws IOException {
    ByteBuffer buffer = ByteBuffer.allocate(40).order(ByteOrder.LITTLE_ENDIAN);

    int size = dibHeader.length();
    buffer.putInt(size);

    int width = dibHeader.width();
    buffer.putInt(width);

    int height = dibHeader.height();
    buffer.putInt(height);

    short colorPlanes = dibHeader.colorPlanes();
    buffer.putShort(colorPlanes);

    short format = dibHeader.format();
    buffer.putShort(format);

    int biRgb = dibHeader.biRgb();
    buffer.putInt(biRgb);

    int dataLength = dibHeader.dataLength();
    buffer.putInt(dataLength);

    int printWidth = dibHeader.printWidth();
    buffer.putInt(printWidth);

    int printHeight = dibHeader.printHeight();
    buffer.putInt(printHeight);

    int colorsCount = dibHeader.colorsCount();
    buffer.putInt(colorsCount);

    int colorsCountImportant = dibHeader.colorsCountImportant();
    buffer.putInt(colorsCountImportant);

    byte[] array = buffer.array();
    os.write(array);
  }

  public void writeData(OutputStream os, byte[][] data) throws IOException {
    for (byte[] row : data) {
      os.write(row);
    }
  }
}
