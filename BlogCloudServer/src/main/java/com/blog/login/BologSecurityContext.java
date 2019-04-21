package com.blog.login;

import java.security.Principal;
import javax.ws.rs.core.SecurityContext;
import org.simpleframework.http.Request;

/**
 * @author haijun.zhang
 */
public class BologSecurityContext implements SecurityContext {

    private BlogSession session;
    private final Request request;

    public BologSecurityContext(Request request) {
        this.request = request;
    }

    @Override
    public Principal getUserPrincipal() {
        if (session == null) {
            session = BlogSessionFactory.instance().getSession(request);
        }
        return session;
    }

    @Override
    public boolean isUserInRole(String role) {
        BlogSession appSession = (BlogSession) getUserPrincipal();
        return appSession != null && appSession.getRoles().contains(role);
    }

    @Override
    public boolean isSecure() {
        return true;
    }

    @Override
    public String getAuthenticationScheme() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
