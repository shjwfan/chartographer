package org.shjwfan.lib.bitmap;

import java.io.IOException;
import java.io.OutputStream;

public interface BitmapWriter {

  void write(OutputStream os, Bitmap bitmap) throws IOException;
}
