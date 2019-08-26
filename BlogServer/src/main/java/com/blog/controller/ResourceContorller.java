package com.blog.controller;

import com.blog.config.Configuration;
import com.blog.login.BlogSession;
import com.blog.utils.BlogMediaType;
import com.blog.utils.FileUtils;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import org.apache.commons.lang.StringUtils;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

/**
 * @author haijun.zhang
 */
@Path("/")
public class ResourceContorller {

    private static final byte[] NEWLINE = "\r\n".getBytes();
    private static final String WEB_DIR = Configuration.getInstance().getConfig().getWebDir();
    private static final String WEB_DIR_USER;
    private static final String WEB_DIR_ADMIN;

    static {
        String dir = WEB_DIR.substring(WEB_DIR.indexOf("/"));
        if (!dir.endsWith("/")) {
            dir += "/";
        }
        WEB_DIR_USER = dir + "user/";
        WEB_DIR_ADMIN = dir + "admin/";
    }

    @Inject
    Request request;
    @Inject
    Response response;
    @Context
    SecurityContext security;

    @GET
    @Path("test")
    @Produces(BlogMediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it!";
    }

    @GET
    public void index() throws IOException {
        getResource("packed-index.html");
    }

    @GET
    @Path("/static/{path: .*}")
    public void getResource(@PathParam("path") String filePath) throws IOException {
        BlogSession session = (BlogSession) security.getUserPrincipal();
        File file = new File(WEB_DIR, filePath);
        String contentType = FileUtils.getFileContentType(file.getAbsolutePath());
        if (StringUtils.isNotBlank(contentType)) {
            response.setContentType(contentType);
        }
//        response.setDate("Last-Modified", System.currentTimeMillis());
//        response.setValue("Cache-Control", "max-age=604800"); //client always revalidates the content one week
//        response.setValue("Cache-Control", "must-revalidate,max-age=0");//client always revalidates the content before serving it from the client cache
//        response.setValue("Cache-Control", "private, max-age=0");
        try (OutputStream out = response.getOutputStream()) {
            writeResource(file, out, session);
        }
    }

    public void writeResource(File file, final OutputStream out, BlogSession session) throws IOException {
        if (file.getCanonicalPath().contains(WEB_DIR_USER) && (null == session || !session.getRoles().contains("user"))) {
            return;
        }
        if (file.getCanonicalPath().contains(WEB_DIR_ADMIN) && (null == session || !session.getRoles().contains("admin"))) {
            return;
        }
        if (file.getName().startsWith("packed-")) {
            file = new File(file.getParentFile(), file.getName() + ".txt");
        }
        if (file.exists() && file.canRead()) {
            System.out.println("writeResource()" + file.getCanonicalPath());
            if (file.getName().startsWith("packed-")) {
                try (BufferedReader in = new BufferedReader(new FileReader(file))) {
                    String filePath;
                    while ((filePath = in.readLine()) != null) {
                        filePath = filePath.trim();
                        if (filePath.length() > 0 && !filePath.startsWith("#")) {
                            File innerFile = new File(file.getParent(), filePath);
                            writeResource(innerFile, out, session);
                            out.write(NEWLINE);
                        }
                    }
                }
            } else if (file.isDirectory()) {
                File[] childFils = file.listFiles();
                for (File childFile : childFils) {
                    writeResource(childFile, out, session);
                    out.write(NEWLINE);
                }
            } else {
                byte[] buffer = new byte[2048];
                try (BufferedInputStream innerStream = new BufferedInputStream(new FileInputStream(file))) {
                    int bytesSize;
                    while ((bytesSize = innerStream.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesSize);
                    }
                    out.flush();
                }
            }
        } else {
            Logger.getLogger(ResourceContorller.class.getName()).log(Level.SEVERE, "not foud resource file :{0}", file.getCanonicalPath());
        }
    }
}
