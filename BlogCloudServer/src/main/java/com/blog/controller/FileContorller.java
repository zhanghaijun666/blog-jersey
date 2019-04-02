package com.blog.controller;

import com.blog.login.AppSession;
import com.blog.proto.BlogStore;
import com.blog.service.FileUploadService;
import com.blog.utils.BlogMediaType;
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
public class FileContorller {

    @Inject
    Request request;
    @Inject
    Response response;
    @Context
    SecurityContext security;

    @POST
    @Path("/file/{path: .*}")
//    @Consumes({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
    @Produces({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
    @RolesAllowed("user")
    public BlogStore.RspInfoList uploadFile(@PathParam("path") String filePath, BlogStore.TreeUpdateItemList list) {
        AppSession session = (AppSession) security.getUserPrincipal();
        if (request.getContentLength() == 0) {
            return BlogStore.RspInfoList.newBuilder().setCode(BlogStore.ReturnCode.Return_ERROR).build();
        }
        return FileUploadService.fileUpload(request, response);
    }

//    public String uploadFiledemo(String filePath) {
////        request.getInputStream();
//        if ("application/json".equalsIgnoreCase(request.getValue("Content-Type"))) {
////            JsonFormat.parser().merge(new InputStreamReader(request.getInputStream(), "UTF-8"), CloudStore.TreeUpdateReq.newBuilder());
//        } else {
////            updateReq = CloudStore.TreeUpdateReq.parseFrom(req.getInputStream()).toBuilder();
//        }
//        return "OK!!!";
//    }
}
