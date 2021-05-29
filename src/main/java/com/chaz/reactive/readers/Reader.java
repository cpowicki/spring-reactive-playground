package com.chaz.reactive.readers;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.function.Consumer;

public interface Reader {
    void read(File file,  Consumer<ByteBuffer> consumer) throws IOException;
}
