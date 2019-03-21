package com.blog.factory;

import com.blog.login.AppSession;
import com.blog.db.User;
import com.tools.BlogUtils;
import java.util.concurrent.ConcurrentHashMap;
import org.simpleframework.http.Cookie;
import org.simpleframework.http.Request;

/**
 * @author haijun.zhang
 */
public class SreverSession {

    private static final ConcurrentHashMap<String, AppSession> LOGIN_APPSESSIONS = new ConcurrentHashMap<>();
    private static final SreverSession SESSION_FACTORY = new SreverSession();

    private SreverSession() {
    }

    public static SreverSession instance() {
        return SESSION_FACTORY;
    }

    private String generatorSessionId() {
        String sessionId = Long.toString(System.currentTimeMillis()) + Double.toString(Math.random());
        return BlogUtils.sha1Hex(sessionId);
    }

    public AppSession createSession(User user, Boolean isRememberMe) {
        AppSession session = new AppSession(generatorSessionId(), user.getUserId(), user.getUsername(), isRememberMe);
        LOGIN_APPSESSIONS.put(session.getId(), session);
        return session;
    }

    public AppSession getSession(Request request) {
        String sessionId = null;
        for (Cookie cookie : request.getCookies()) {
            String cookieName = cookie.getName();
            if ("cookie".equals(cookieName) || "session".equals(cookieName)) {
                sessionId = cookie.getValue();
            }
        }
        if (sessionId != null) {
            AppSession session = LOGIN_APPSESSIONS.get(sessionId);
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

    public void removeSession(AppSession session) {
        LOGIN_APPSESSIONS.remove(session.getId());
    }
}
