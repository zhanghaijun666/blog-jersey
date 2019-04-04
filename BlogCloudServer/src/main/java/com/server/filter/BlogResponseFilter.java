package com.server.filter;

import java.io.IOException;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;

/**
 * @author zhanghaijun
 */
@PreMatching
@Priority(Priorities.HEADER_DECORATOR + 50)
public class BlogResponseFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        System.out.println("===blog response filter===");
        if (requestContext.getMethod().equalsIgnoreCase("GET") && requestContext.getUriInfo().getPath().startsWith("static")) {
        }
    }
}
