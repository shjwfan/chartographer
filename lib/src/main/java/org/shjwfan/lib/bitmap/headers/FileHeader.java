package org.shjwfan.lib.bitmap.headers;

public record FileHeader(@SuppressWarnings("NullAway.Init") String id, int fileSize, short reserved0, short reserved1, int dataOffset) {

}
