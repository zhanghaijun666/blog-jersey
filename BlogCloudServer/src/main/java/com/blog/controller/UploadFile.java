package com.blog.controller;

import com.server.AppSession;
import com.server.BlogMediaType;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import org.apache.commons.io.FileUtils;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

/**
 * @author zhanghaijun
 */
@Path("/upload")
public class UploadFile {

    private static final String ARTICLE_IMAGES_PATH = "D:/Download/demo/";

    @Inject
    Request request;
    @Inject
    Response response;
    @Context
    SecurityContext security;

    @POST
    @Path("/file")
    @Consumes({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF, BlogMediaType.MULTIPART_FORM_DATA})
    @Produces(BlogMediaType.TEXT_PLAIN)
    @RolesAllowed("user")
    public String uploadFile() {
        AppSession session = (AppSession) security.getUserPrincipal();
        File file = new File(ARTICLE_IMAGES_PATH + "pom.xml");
        try {
            FileUtils.copyInputStreamToFile(request.getInputStream(), file);
        } catch (IOException ex) {
            Logger.getLogger(UploadFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "OK";
    }
}
