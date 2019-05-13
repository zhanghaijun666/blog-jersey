package com.blog.controller;

import com.blog.socket.ChatRoom;
import com.blog.utils.BlogMediaType;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
import org.simpleframework.http.socket.service.PathRouter;
import org.simpleframework.http.socket.service.Service;

/**
 * @author haijun.zhang
 */
@Path("/socket")
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
    public Service connectSocket() {
        try {
            Map<String, Service> registry = new HashMap<>();
            Service primary = new ChatRoom();
            registry.put("chat", primary);
            PathRouter pathRouter = new PathRouter(registry, primary);
            return pathRouter.route(request, response);
        } catch (IOException ex) {
            System.out.println("com.blog.controller.SocketController.connectSocket()");
        }
        return null;
    }
}
