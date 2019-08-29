package io.overpoet.core.ui.impl.server;

import java.nio.charset.Charset;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.overpoet.core.ui.BaseHandler;
import io.overpoet.core.ui.EventSink;
import io.overpoet.core.ui.EventsHandler;
import io.overpoet.core.ui.Request;
import io.overpoet.core.ui.RequestHandler;
import io.overpoet.core.ui.Response;
import io.overpoet.core.ui.UI;
import io.overpoet.core.ui.impl.Dispatch;
import io.overpoet.core.ui.impl.RequestImpl;
import io.overpoet.core.ui.impl.ResponseImpl;
import io.overpoet.core.ui.impl.UIManager;
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

        LOG.info("request {} {}", method, path);

        Dispatch<BaseHandler<?>> dispatch = this.uiManager.findDispatch(method, path);
        if ( dispatch != null ) {
            LOG.info("dispatch {} {}", method, path);
            Request request = request(dispatch, httpReq);
            if (dispatch.handler() instanceof RequestHandler) {
                Response response = response(ctx);
                ((RequestHandler) dispatch.handler()).accept(request, response);
            } else if (dispatch.handler() instanceof EventsHandler) {
                EventSink sink = null;
                ((EventsHandler) dispatch.handler()).accept(request, sink);
            }
        } else {
            LOG.info("not found {} {}", method, path);
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
            System.err.println( "sending it down the pipeline");
            ctx.pipeline().writeAndFlush(response);
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
