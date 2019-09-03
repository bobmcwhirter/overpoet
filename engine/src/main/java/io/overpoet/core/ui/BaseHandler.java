package io.overpoet.core.ui;

import java.util.function.BiConsumer;

public interface BaseHandler<RESPONSE> extends BiConsumer<Request, RESPONSE> {
}
