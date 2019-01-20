package com.server.filter;

import com.server.AppSession;
import com.server.SessionFactory;
import java.io.IOException;
import java.security.Principal;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.SecurityContext;
import org.simpleframework.http.Request;

/**
 * @author haijun.zhang
 */
public class SecurityRequestFilter implements ContainerRequestFilter {

    @Inject
    Request request;

    @Override
    public void filter(ContainerRequestContext crc) throws IOException {
        crc.setSecurityContext(new SecurityContext() {
            private AppSession session;

            @Override
            public Principal getUserPrincipal() {
                if (session == null) {
                    session = SessionFactory.instance().getSession(request);
                }
                return session;
            }

            @Override
            public boolean isUserInRole(String role) {
                AppSession appSession = (AppSession) getUserPrincipal();
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
        });
    }

}
