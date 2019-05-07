package com.blog.controller;

import com.blog.utils.BlogMediaType;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

/**
 * @author haijun.zhang
 */
@Path("socket")
public class SocketController {

    @Inject
    Request request;
    @Inject
    Response response;
    @Context
    SecurityContext security;

    @GET
    @Consumes({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
    @Produces({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
    @RolesAllowed("user")
    public void connectSocket() {
        
    }
}
