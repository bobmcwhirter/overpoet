package io.overpoet.core.ui.impl;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.ForkJoinPool;

import io.overpoet.core.platform.Platform;
import io.overpoet.core.ui.BaseHandler;
import io.overpoet.core.ui.UI;
import io.overpoet.core.ui.impl.server.UIServer;

public class UIManager {

    public UIManager(ForkJoinPool pool) {
        this.pool = pool;
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
        this.server = new UIServer(this);
        this.server.start(bind);
    }

    public void stop() throws InterruptedException {
        this.server.stop();
    }

    public void dispatch(Runnable task) {
        this.pool.submit(task);
    }

    private final UIImpl root;

    private final ForkJoinPool pool;

    private UIServer server;
}
