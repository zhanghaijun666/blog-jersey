package com.blog.controller;

import com.blog.file.StorageFactory;
import com.blog.file.FileUrl;
import com.blog.login.BlogSession;
import com.blog.proto.BlogStore;
import com.blog.service.FileService;
import com.blog.utils.BlogMediaType;
import java.io.IOException;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
@Path("/file")
public class FileContorller {

    @Inject
    Request request;
    @Inject
    Response response;
    @Context
    SecurityContext security;

    @POST
    @Path("/upload/{path: .*}")
//    @Consumes({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
    @Produces({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
    @RolesAllowed("user")
    public BlogStore.RspInfoList uploadFile(@PathParam("path") String filePath) throws IOException {
        if (request.getContentLength() == 0) {
            return BlogStore.RspInfoList.newBuilder().setCode(BlogStore.ReturnCode.Return_ERROR).build();
        }
        BlogSession session = (BlogSession) security.getUserPrincipal();
        FileUrl fileUrl = new FileUrl(filePath, session.getUserId());
        return BlogStore.RspInfoList.newBuilder().setCode(FileService.UploadFile(fileUrl, request.getInputStream())).build();
    }

    @GET
    @Path("/get/{path: .*}")
    @Produces({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
    @RolesAllowed("user")
    public BlogStore.FileItemList getFileItemList(@PathParam("path") String filePath) {
        BlogSession session = (BlogSession) security.getUserPrincipal();
        FileUrl fileUrl = new FileUrl(filePath, session.getUserId());
        if (fileUrl.getGpType() == BlogStore.GtypeEnum.User_VALUE && fileUrl.getGpId() == session.getUserId()) {
            return StorageFactory.getFileItemList(fileUrl);
        }
        return BlogStore.FileItemList.getDefaultInstance();
    }

    @POST
    @Path("/deldete/{path: .*}")
    @Consumes({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
    @Produces({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
    @RolesAllowed("user")
    public BlogStore.RspInfoList deleteFile(@PathParam("path") String filePath) {
        BlogSession session = (BlogSession) security.getUserPrincipal();
        return BlogStore.RspInfoList.newBuilder().setCode(FileService.deldeteFile(new FileUrl(filePath, session.getUserId()))).build();
    }

    @PUT
    @Path("/rename{path: .*}")
    @Consumes({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
    @Produces({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
    @RolesAllowed("user")
    public BlogStore.RspInfo renameFile(@PathParam("path") String filePath, String newFileName) {
        BlogSession session = (BlogSession) security.getUserPrincipal();
        return BlogStore.RspInfo.newBuilder().setCode(FileService.renameFile(new FileUrl(filePath, session.getUserId()), newFileName)).build();
    }

//    @GET
//    @Path("/download/{path: .*}")
//    @Produces({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
//    @RolesAllowed("user")
//    public void fileDownload(@PathParam("path") String filePath) {
//        BlogSession session = (BlogSession) security.getUserPrincipal();
//        FileUrl fileUrl = new FileUrl(filePath, session.getUserId());
//        BlogStore.StorageItem storage = StorageFactory.getStorage(fileUrl);
//        if (FileUtils.isFolder(storage.getContentType())) {
//
//        } else {
////            设置文件ContentType类型，会自动判断下载文件类型
//            response.setContentType("multipart/form-data");
////        response.setHeader("Content-Disposition", "attachment;fileName=" + FileUtils.getFileName(fileUrl.getPath()));
//
//        }
//    }
}
