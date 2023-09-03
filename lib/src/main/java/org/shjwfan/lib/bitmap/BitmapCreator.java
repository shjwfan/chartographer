package org.shjwfan.lib.bitmap;

import java.io.IOException;
import java.io.OutputStream;

public interface BitmapCreator {

  void create(OutputStream os, int width, int height, byte r, byte g, byte b, byte a) throws IOException;
}
