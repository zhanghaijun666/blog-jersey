package com.blog.socket;

import java.io.IOException;
import org.simpleframework.http.socket.Frame;
import org.simpleframework.http.socket.FrameChannel;
import org.simpleframework.http.socket.FrameListener;
import org.simpleframework.http.socket.Reason;

/**
 * @author zhanghaijun
 */
public class BlogChatFrameChannel implements FrameChannel {

    @Override
    public void send(byte[] data) throws IOException {
    }

    @Override
    public void send(String text) throws IOException {
    }

    @Override
    public void send(Frame frame) throws IOException {
    }

    @Override
    public void register(FrameListener listener) throws IOException {
    }

    @Override
    public void remove(FrameListener listener) throws IOException {
    }

    @Override
    public void close(Reason reason) throws IOException {
    }

    @Override
    public void close() throws IOException {
    }

}
