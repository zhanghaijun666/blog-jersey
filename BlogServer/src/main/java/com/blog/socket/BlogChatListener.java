package com.blog.socket;

import com.blog.login.BlogSession;
import com.blog.login.BlogSessionFactory;
import java.io.IOException;
import org.simpleframework.http.Request;
import org.simpleframework.http.socket.DataFrame;
import org.simpleframework.http.socket.Frame;
import org.simpleframework.http.socket.FrameListener;
import org.simpleframework.http.socket.FrameType;
import org.simpleframework.http.socket.Reason;
import org.simpleframework.http.socket.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhanghaijun
 */
public class BlogChatListener implements FrameListener {

    private static final Logger logger = LoggerFactory.getLogger(BlogChatListener.class);
    private final BlogChat blogChatRoom;

    public BlogChatListener(BlogChat blogChat) {
        this.blogChatRoom = blogChat;
    }

    @Override
    public void onFrame(Session session, Frame frame) {
        FrameType type = frame.getType();
        String text = frame.getText();
        Request request = session.getRequest();
        BlogSession blogSession = BlogSessionFactory.instance().getSession(request);
        if (null == blogSession) {
            return;
        }
        try {
            if (type == FrameType.TEXT) {
                Frame replay = new DataFrame(type, "(" + blogSession.getName() + ") " + text);
                blogChatRoom.distribute(blogSession.getUserId(), replay);
            }
        } catch (IOException ex) {
            logger.error("onFrame error: {}", ex.getMessage());
        }
        logger.info("onFrame(" + type + ")");
    }

    @Override
    public void onError(Session session, Exception cause) {
        logger.info("onError(" + cause + ")");
    }

    @Override
    public void onClose(Session session, Reason reason) {
        logger.info("onError(" + reason + ")");
    }

}
