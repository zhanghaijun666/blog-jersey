package com.service;

import com.config.Configuration;
import com.server.AppSession;
import com.utils.FileUtils;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import org.apache.commons.lang.StringUtils;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

/**
 * @author haijun.zhang
 */
@Path("/")
public class ResourceService {

    private static final byte[] NEWLINE = "\r\n".getBytes();
    private static final String WEB_DIR = Configuration.getInstance().getConfig().getWebDir();
    private static final String WEB_DIR_USER = Configuration.getInstance().getWebDir("user");
    private static final String WEB_DIR_ADMIN = Configuration.getInstance().getWebDir("admin");

    @Inject
    Request req;
    @Inject
    Response res;
    @Context
    SecurityContext security;

    @GET
    @Path("test")
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it!";
    }

    @GET
    public void index() {
        getResource("packed-index.html");
    }

    @GET
    @Path("/static/{path: .*}")
    public void getResource(@PathParam("path") String filePath) {
        AppSession session = (AppSession) security.getUserPrincipal();
        File file = new File(WEB_DIR, filePath);
        String contentType = FileUtils.getFileContentType(file);
        if (StringUtils.isNotBlank(contentType)) {
            res.setContentType(contentType);
        }
        try (OutputStream out = res.getOutputStream()) {
            getResource(file, out, session);
        } catch (IOException ex) {
            throw new NotFoundException(file.getPath() + "is not found" + ex.getMessage());
        }
    }

    public static void getResource(File file, final OutputStream out, AppSession session) {
        try {
            if (file.getCanonicalPath().contains(WEB_DIR_USER) && null == session) {
                return;
            }
            if (file.getCanonicalPath().contains(WEB_DIR_ADMIN) && (null == session || session.getRoles().contains("admin"))) {
                return;
            }
            writeFile(out, file, new byte[10240], session);
        } catch (IOException ex) {
            throw new NotFoundException(file.getPath() + "is not found" + ex.getMessage());
        }
    }

    private static void writeFile(final OutputStream out, File file, final byte[] buffer, AppSession session) throws IOException {
        if (file.getName().startsWith("packed-")) {
            file = new File(file.getParent(), file.getName() + ".txt");
        }
        if (file.exists() && file.canRead()) {
            if (file.getName().startsWith("packed-")) {
                try (BufferedReader in = new BufferedReader(new FileReader(file))) {
                    String filePath;
                    while ((filePath = in.readLine()) != null) {
                        filePath = filePath.trim();
                        if (filePath.length() > 0 && !filePath.startsWith("#")) {
                            getResource(new File(file.getParent(), filePath), out, session);
                            out.write(NEWLINE);
                        }
                    }
                }
            } else if (file.isDirectory()) {
                File[] childFils = file.listFiles();
                for (File childFile : childFils) {
                    getResource(childFile, out, session);
                    out.write(NEWLINE);
                }
            } else {
                try (BufferedInputStream innerStream = new BufferedInputStream(new FileInputStream(file))) {
                    int bytesSize;
                    while ((bytesSize = innerStream.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesSize);
                    }
                }
            }
        } else {
            System.out.println("com.service.ResourceService.writeFile()" + file.getPath());
        }
    }
}
