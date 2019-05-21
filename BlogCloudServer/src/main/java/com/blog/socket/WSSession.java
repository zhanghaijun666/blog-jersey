package com.blog.socket;

import java.util.Map;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.socket.FrameChannel;
import org.simpleframework.http.socket.Session;

/**
 * @author zhanghaijun
 */
public class WSSession implements Session {

    private final Request request;
    private final Response response;
    private final FrameChannel frameChannel;

    public WSSession(Request request, Response response, FrameChannel frameChannel) {
        this.request = request;
        this.response = response;
        this.frameChannel = frameChannel;
    }

    @Override
    public Map getAttributes() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getAttribute(Object key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FrameChannel getChannel() {
        return frameChannel;
    }

    @Override
    public Request getRequest() {
        return request;
    }

    @Override
    public Response getResponse() {
        return response;
    }

}
