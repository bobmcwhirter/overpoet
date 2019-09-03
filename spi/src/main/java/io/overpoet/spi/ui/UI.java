package io.overpoet.spi.ui;

import java.util.function.Consumer;

public interface UI {

    enum Method {
        GET,
        POST,
        PUT,
        DELETE,
        EVENTS
    }

    UI get(RequestHandler handler);

    UI post(RequestHandler handler);
    UI put(RequestHandler handler);
    UI delete(RequestHandler handler);
    UI events(EventsHandler handler);

    UI path(String path, Consumer<UI> config);

}
