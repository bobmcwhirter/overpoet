package io.overpoet.core.ui;

import io.netty.buffer.ByteBuf;

public interface Response {
    ByteBuf content();

    void close();

    void status(int status);
}
