package io.overpoet.engine.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import io.overpoet.spi.ui.BaseHandler;
import io.overpoet.spi.ui.EventsHandler;
import io.overpoet.spi.ui.RequestHandler;
import io.overpoet.spi.ui.UI;

import static io.overpoet.spi.ui.UI.Method.DELETE;
import static io.overpoet.spi.ui.UI.Method.EVENTS;
import static io.overpoet.spi.ui.UI.Method.GET;
import static io.overpoet.spi.ui.UI.Method.POST;
import static io.overpoet.spi.ui.UI.Method.PUT;

public class UIImpl implements UI {

    public UIImpl(String path) {
        this.pathMatcher = new PathMatcher(path);
    }

    @Override
    public UI get(RequestHandler handler) {
        this.handlers.put(GET, handler);
        return this;
    }

    @Override
    public UI post(RequestHandler handler) {
        this.handlers.put(POST, handler);
        return this;
    }

    @Override
    public UI put(RequestHandler handler) {
        this.handlers.put(PUT, handler);
        return this;
    }

    @Override
    public UI delete(RequestHandler handler) {
        this.handlers.put(DELETE, handler);
        return this;
    }

    @Override
    public UI events(EventsHandler handler) {
        this.handlers.put(EVENTS, handler);
        return this;
    }

    public UI path(String path) {
        String thisPath = this.pathMatcher.path();
        String childPath = thisPath;
        if ( ! childPath.endsWith("/") && ! path.startsWith("/")) {
            childPath += "/";
        }
        childPath += path;
        UIImpl child = new UIImpl(childPath);
        this.children.add(child);
        return child;
    }

    @Override
    public UI path(String path, Consumer<UI> config) {
        UI child = path(path);
        config.accept(child);
        return this;
    }

    public Dispatch findDispatch(Method method, String path) {
        Dispatch dispatch = null;
        for (UIImpl child : this.children) {
            dispatch = child.findDispatch(method, path);
            if (dispatch != null) {
                return dispatch;
            }
        }

        BaseHandler<?> handler = this.handlers.get(method);
        if ( handler == null ) {
            return null;
        }
        PathMatch pathMatch = this.pathMatcher.attemptMatch(path);
        if ( pathMatch != null ) {
            return new Dispatch(pathMatch, handler);
        }

        return null;
    }

    private final PathMatcher pathMatcher;

    private Map<Method, BaseHandler<?>> handlers = new HashMap<>();

    private List<UIImpl> children = new ArrayList<>();
}
