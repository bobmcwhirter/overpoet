package io.overpoet.core.ui.server;

import java.nio.charset.Charset;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.overpoet.spi.ui.BaseHandler;
import io.overpoet.spi.ui.EventSink;
import io.overpoet.spi.ui.EventsHandler;
import io.overpoet.spi.ui.Request;
import io.overpoet.spi.ui.RequestHandler;
import io.overpoet.spi.ui.Response;
import io.overpoet.spi.ui.UI;
import io.overpoet.core.ui.Dispatch;
import io.overpoet.core.ui.RequestImpl;
import io.overpoet.core.ui.ResponseImpl;
import io.overpoet.core.ui.UIManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatchHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger("overpoet.ui");

    public DispatchHandler(UIManager uiManager) {
        this.uiManager = uiManager;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof FullHttpRequest)) {
            super.channelRead(ctx, msg);
            return;
        }


        FullHttpRequest httpReq = (FullHttpRequest) msg;

        UI.Method method = method(httpReq);
        String path = path(httpReq);

        LOG.debug("request {} {}", method, path);

        Dispatch<BaseHandler<?>> dispatch = this.uiManager.findDispatch(method, path);
        if ( dispatch != null ) {
            LOG.debug("dispatch {} {}", method, path);
            Request request = request(dispatch, httpReq);
            if (dispatch.handler() instanceof RequestHandler) {
                Response response = response(ctx);
                this.uiManager.dispatch( ()->{
                    ((RequestHandler) dispatch.handler()).accept(request, response);
                });
            } else if (dispatch.handler() instanceof EventsHandler) {
                EventSink sink = null;
                ((EventsHandler) dispatch.handler()).accept(request, sink);
            }
        } else {
            LOG.warn("not found {} {}", method, path);
            Response response = response(ctx);
            response.content().writeCharSequence("not found", Charset.forName("UTF-8"));
            response.status(404);
            response.close();
        }
    }

    private Request request(Dispatch<BaseHandler<?>> dispatch, FullHttpRequest httpReq) {
        return new RequestImpl(dispatch.match(), httpReq);
    }

    private Response response(ChannelHandlerContext ctx) {
        return new ResponseImpl(ctx.alloc().compositeBuffer(), (response)->{
            ctx.pipeline().writeAndFlush(response);
            LOG.debug( "response complete");
        });
    }

    private UI.Method method(FullHttpRequest request) {
        HttpMethod m = request.method();
        if (m.equals(HttpMethod.GET)) {
            return UI.Method.GET;
        } else if (m.equals(HttpMethod.POST)) {
            return UI.Method.POST;
        } else if (m.equals(HttpMethod.PUT)) {
            return UI.Method.PUT;
        } else if (m.equals(HttpMethod.DELETE)) {
            return UI.Method.PUT;
        }

        return null;
    }

    private String path(FullHttpRequest request) {
        QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
        return decoder.path();
    }

    private final UIManager uiManager;
}
