package org.shjwfan.lib.bitmap;

public interface BitmapRetriever {

  Bitmap retrieve(Bitmap base, int x, int y, int width, int height);
}
