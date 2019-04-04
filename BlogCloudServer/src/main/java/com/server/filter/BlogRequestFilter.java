package com.server.filter;

import java.io.IOException;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;

/**
 * @author zhanghaijun
 */
@PreMatching
@Priority(Priorities.HEADER_DECORATOR + 50)
public class BlogRequestFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        System.out.println(requestContext.getUriInfo().getPath());
        System.out.println("===blog request filter===");
    }
}
