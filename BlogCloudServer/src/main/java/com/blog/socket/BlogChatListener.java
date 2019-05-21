package com.blog.socket;

import org.simpleframework.http.Cookie;
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
    private final BlogChat room;

    public BlogChatListener(BlogChat room) {
        this.room = room;
    }

    @Override
    public void onFrame(Session session, Frame frame) {
        FrameType type = frame.getType();
        String text = frame.getText();
        Request request = session.getRequest();
        Cookie user = request.getCookie("user");
        String name = user.getValue();
        if (type == FrameType.TEXT) {
            Frame replay = new DataFrame(type, "(" + name + ") " + text);
            room.distribute(name, replay);
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
