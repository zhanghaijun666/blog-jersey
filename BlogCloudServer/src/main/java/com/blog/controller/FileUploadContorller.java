package com.blog.controller;

import com.blog.config.Configuration;
import com.blog.proto.BlogStore;
import com.blog.utils.BlogMediaType;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import org.apache.commons.io.FileUtils;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

/**
 * @author zhanghaijun
 */
@Path("/uploadfile")
public class FileUploadContorller {

    @Inject
    Request request;
    @Inject
    Response response;
    @Context
    SecurityContext security;

    @GET
    @Path("/checkhash/{hash}")
    @RolesAllowed("user")
    public BlogStore.RspInfo isExistHash(@PathParam("hash") String fileHash) {

        return BlogStore.RspInfo.newBuilder().setCode(BlogStore.ReturnCode.Return_OK).build();
    }

    @POST
    @Path("/item")
    @RolesAllowed("user")
    public BlogStore.RspInfo uploadItem() {

        return BlogStore.RspInfo.newBuilder().setCode(BlogStore.ReturnCode.Return_OK).build();
    }

    @POST
    @Path("/{path: .*}")
//    @Consumes({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
    @Produces({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
    @RolesAllowed("user")
    public BlogStore.RspInfoList uploadFile(@PathParam("path") String filePath) {
        if (request.getContentLength() == 0) {
            System.out.println(request.getContentLength());
        }
//        System.out.println(EncryptUtils.sha1(FileUtil.toByteArray(request.getInputStream())));

        File upload = new File(Configuration.getInstance().getFileStore(), UUID.randomUUID().toString() + "------" + filePath);
        try {
            FileUtils.copyInputStreamToFile(request.getInputStream(), upload);
        } catch (IOException e) {
        }
        return BlogStore.RspInfoList.newBuilder().setCode(BlogStore.ReturnCode.Return_OK).build();
    }

}
