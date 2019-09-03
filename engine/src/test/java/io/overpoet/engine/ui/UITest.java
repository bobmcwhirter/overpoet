package io.overpoet.engine.ui;

import io.overpoet.spi.ui.RequestHandler;
import io.overpoet.spi.ui.UI;
import org.junit.Test;

import static io.overpoet.spi.ui.UI.Method.GET;
import static io.overpoet.spi.ui.UI.Method.POST;
import static org.fest.assertions.api.Assertions.assertThat;

public class UITest {

    @Test
    public void testConfigure() {
        UIImpl rootImpl = new UIImpl("/foo");
        UI root = rootImpl;

        RequestHandler handler1 = (req, resp) -> {
        };
        RequestHandler handler2 = (req, resp) -> {
        };
        root.get(handler1);
        root.path("/bar", (bar) -> {
            bar.get(handler2);
        });

        Dispatch dispatch = rootImpl.findDispatch(GET, "/foo/bar");
        assertThat(dispatch).isNotNull();
        assertThat(dispatch.handler()).isSameAs(handler2);

        dispatch = rootImpl.findDispatch(GET, "/foo");
        assertThat(dispatch).isNotNull();
        assertThat(dispatch.handler()).isSameAs(handler1);

        dispatch = rootImpl.findDispatch(POST, "/foo");
        assertThat(dispatch).isNull();

    }
}
