package io.overpoet.spi.ui;

import io.netty.buffer.ByteBuf;

public interface Response {
    ByteBuf content();

    void close();

    void status(int status);
}
