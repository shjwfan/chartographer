package org.shjwfan.lib.bitmap.utils;

public class BitmapUtils24BitsBased {

  public static int rowLength(int width) {
    return width * 3;
  }

  public static int rowLengthOffset(int width) {
    return (4 - (width * 3) % 4) % 4;
  }

  private BitmapUtils24BitsBased() {
  }
}
