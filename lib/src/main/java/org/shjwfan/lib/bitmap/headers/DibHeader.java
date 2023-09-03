package org.shjwfan.lib.bitmap.headers;

public record DibHeader(int length, int width, int height, short colorPlanes, short format, int biRgb, int dataLength, int printWidth, int printHeight,
                        int colorsCount, int colorsCountImportant) {

}
