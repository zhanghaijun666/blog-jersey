package com.blog.socket;

import com.blog.login.BlogSession;
import com.blog.login.BlogSessionFactory;
import org.simpleframework.http.Request;
import org.simpleframework.http.socket.FrameChannel;
import org.simpleframework.http.socket.Session;
import org.simpleframework.http.socket.service.Service;

/**
 * @author zhanghaijun
 */
public class SocketService implements Service {

    @Override
    public void connect(Session session) {
        FrameChannel socket = session.getChannel();
        Request request = session.getRequest();
        BlogSession blogSession = BlogSessionFactory.instance().getSession(request);
    }

}
