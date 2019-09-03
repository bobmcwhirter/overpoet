package io.overpoet.spi.ui;

import java.util.function.BiConsumer;

public interface BaseHandler<RESPONSE> extends BiConsumer<Request, RESPONSE> {
}
