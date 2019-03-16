package com.service;

import com.google.protobuf.util.JsonFormat;
import com.server.AppSession;
import com.server.BlogMediaType;
import java.io.InputStreamReader;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

/**
 * @author zhanghaijun
 */
@Path("/upload")
public class FileUpload {

    @Inject
    Request request;
    @Inject
    Response response;
    @Context
    SecurityContext security;

    @POST
    @Path("/file/{path: .*}")
    @Produces({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
    @RolesAllowed("user")
    public String uploadFile(@PathParam("path") String filePath) {
        AppSession session = (AppSession) security.getUserPrincipal();
        int userId = session.getUserId();
//        request.getInputStream();
        if ("application/json".equalsIgnoreCase(request.getValue("Content-Type"))) {
//            JsonFormat.parser().merge(new InputStreamReader(request.getInputStream(), "UTF-8"), CloudStore.TreeUpdateReq.newBuilder());
        } else {
//            updateReq = CloudStore.TreeUpdateReq.parseFrom(req.getInputStream()).toBuilder();
        }

        return "OK!!!";
    }

}
