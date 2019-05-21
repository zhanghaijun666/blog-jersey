package com.blog.controller;

import com.blog.socket.BlogChat;
import com.blog.socket.SocketService;
import com.blog.socket.WSSession;
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
    
    private PathRouter path_Router_Socket = null;
    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SocketController.class);
    
    public PathRouter getPathRouter() throws IOException {
        if (path_Router_Socket == null) {
            Map<String, Service> registry = new HashMap<>();
            registry.put("/socket/chat", new BlogChat());
            path_Router_Socket = new PathRouter(registry, new SocketService());
        }
        return path_Router_Socket;
    }
    
    @GET
    @Path("/{.*}")
    @Consumes({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
    @Produces({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
    @RolesAllowed("user")
    public void connectSocket() {
        try {
            Service service = getPathRouter().route(request, response);
            service.connect(new WSSession(request, response, null));
            
        } catch (IOException ex) {
            logger.error("IOException : {}", ex.getMessage());
        }
    }
}
