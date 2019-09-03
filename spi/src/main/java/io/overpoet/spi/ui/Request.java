package io.overpoet.spi.ui;

import java.util.List;

import io.netty.buffer.ByteBuf;

public interface Request {
    String uri();

    List<String> parameters(String name);

    String header(String name);

    ByteBuf content();
}
