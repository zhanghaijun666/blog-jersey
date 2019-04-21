package com.blog.login;

import com.blog.login.BlogSession;
import com.blog.db.User;
import com.tools.BlogUtils;
import java.util.concurrent.ConcurrentHashMap;
import org.simpleframework.http.Cookie;
import org.simpleframework.http.Request;

/**
 * @author haijun.zhang
 */
public class BlogSessionFactory {

    private static final ConcurrentHashMap<String, BlogSession> LOGIN_APPSESSIONS = new ConcurrentHashMap<>();
    private static final BlogSessionFactory SESSION_FACTORY = new BlogSessionFactory();

    private BlogSessionFactory() {
    }

    public static BlogSessionFactory instance() {
        return SESSION_FACTORY;
    }

    private String generatorSessionId() {
        String sessionId = Long.toString(System.currentTimeMillis()) + Double.toString(Math.random());
        return BlogUtils.sha1Hex(sessionId);
    }

    public BlogSession createSession(User user, Boolean isRememberMe) {
        BlogSession session = new BlogSession(generatorSessionId(), user.getUserId(), user.getUsername(), isRememberMe);
        LOGIN_APPSESSIONS.put(session.getId(), session);
        return session;
    }

    public BlogSession getSession(Request request) {
        String sessionId = null;
        for (Cookie cookie : request.getCookies()) {
            String cookieName = cookie.getName();
            if ("cookie".equals(cookieName) || "session".equals(cookieName)) {
                sessionId = cookie.getValue();
            }
        }
        if (sessionId != null) {
            BlogSession session = LOGIN_APPSESSIONS.get(sessionId);
            if (session != null) {
                if (session.isAlive()) {
                    session.touch();
                    return session;
                } else {
                    LOGIN_APPSESSIONS.remove(sessionId);
                }
            }
        }
        return null;
    }

    public void removeSession(BlogSession session) {
        LOGIN_APPSESSIONS.remove(session.getId());
    }
}
