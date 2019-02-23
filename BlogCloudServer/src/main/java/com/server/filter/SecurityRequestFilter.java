package com.server.filter;

import com.server.BologSecurityContext;
import java.io.IOException;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;
import org.simpleframework.http.Request;

/**
 * @author haijun.zhang
 */
@Provider
@PreMatching
public class SecurityRequestFilter implements ContainerRequestFilter {

    @Inject
    Request request;

    @Override
    public void filter(ContainerRequestContext crc) throws IOException {
        crc.setSecurityContext(new BologSecurityContext(request));
    }
}
