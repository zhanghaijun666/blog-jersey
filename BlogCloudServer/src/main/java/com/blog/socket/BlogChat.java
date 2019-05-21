package com.blog.socket;

import com.blog.login.BlogSession;
import com.blog.login.BlogSessionFactory;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.simpleframework.http.Request;
import org.simpleframework.http.socket.Frame;
import org.simpleframework.http.socket.FrameChannel;
import org.simpleframework.http.socket.Session;
import org.simpleframework.http.socket.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhanghaijun
 */
public class BlogChat implements Service {
    
    private static final Logger logger = LoggerFactory.getLogger(BlogChat.class);
    
    private final BlogChatListener listener;
    private final Map<Integer, FrameChannel> sockets;
    
    public BlogChat() {
        this.listener = new BlogChatListener(this);
        this.sockets = new ConcurrentHashMap<>();
    }
    
    @Override
    public void connect(Session connection) {
        Request request = connection.getRequest();
        BlogSession blogSession = BlogSessionFactory.instance().getSession(request);
        if (null == blogSession) {
            return;
        }
        try {
            FrameChannel socket = connection.getChannel();
            socket.register(listener);
            sockets.put(blogSession.getUserId(), socket);
        } catch (IOException e) {
            logger.error("connect error:{}", e.getMessage());
        }
    }
    
    public void distribute(Integer userId, Frame frame) throws IOException {
        FrameChannel operation = sockets.get(userId);
        if (null == operation) {
            return;
        }
        operation.send(frame);
    }
}
