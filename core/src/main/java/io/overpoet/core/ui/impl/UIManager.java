package io.overpoet.core.ui.impl;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import io.overpoet.core.platform.Platform;
import io.overpoet.core.ui.BaseHandler;
import io.overpoet.core.ui.UI;
import io.overpoet.core.ui.impl.server.UIServer;

public class UIManager {

    public UIManager() {
        this.root = new UIImpl("/");
        this.root.get((req, resp)->{
            System.err.println( "handling root: " + req.uri());
            resp.content().writeCharSequence("Howdy", Charset.forName("UTF-8"));
            resp.close();
        });
    }

    public UI forPlatform(Platform platform) {
        return this.root.path(platform.id());
    }

    public <T extends BaseHandler<?>> Dispatch<T> findDispatch(UI.Method method, String path) {
        return this.root.findDispatch(method, path);
    }

    public void start(int port) throws InterruptedException {
        InetSocketAddress bind = new InetSocketAddress(port);
        this.server = new UIServer(this).start(bind);
    }

    private final UIImpl root;

    private int server;
}
