package org.shjwfan.lib.bitmap;

import java.io.IOException;
import java.io.InputStream;

public interface BitmapReader {

  Bitmap read(InputStream is) throws IOException;
}
