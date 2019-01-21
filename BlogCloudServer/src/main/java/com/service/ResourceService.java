package com.service;

import com.config.Configuration;
import com.utils.FileUtils;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.commons.lang.StringUtils;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

/**
 * @author haijun.zhang
 */
@Path("/")
public class ResourceService {

    @Inject
    Request req;
    @Inject
    Response res;

    @GET
    @Path("test")
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it!";
    }

    @GET
    public void index() {
        try {
            sendPackedFile(new File(Configuration.getInstance().getWebDir(), "packed-index.html.txt"), res.getOutputStream());
        } catch (IOException ex) {
            throw new NotFoundException("index.html is not found");
        }
    }

    @GET
    @Path("/static/{path: .*}")
    public void getResource(@PathParam("path") String filePath) {
        File webDir = Configuration.getInstance().getWebDir();
        String contentType = FileUtils.getFileContentType(new File(webDir, filePath));
        if (StringUtils.isNotBlank(contentType)) {
            res.setContentType(contentType);
        }
        try (OutputStream out = res.getOutputStream()) {
            if (FileUtils.getFileName(filePath).startsWith("packed-")) {
                sendPackedFile(new File(webDir, filePath + ".txt"), out);
            } else {
                writeFile(out, new File(webDir, filePath), new byte[10240]);
            }
        } catch (IOException ex) {
            throw new NotFoundException(filePath + "is not found");
        }
    }

    private static void writeFile(final OutputStream out, final File file, final byte[] buffer) throws IOException {
        if (file.exists() && file.canRead()) {
            if (file.isDirectory()) {
                File[] childFils = file.listFiles();
                for (File childFile : childFils) {
                    writeFile(out, childFile, buffer);
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
        }
    }
    private static final byte[] NEWLINE = "\r\n".getBytes();

    private static void sendPackedFile(File packedFile, OutputStream out) throws FileNotFoundException, IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(packedFile))) {
            String filePath;
            byte[] buffer = new byte[8192];
            while ((filePath = in.readLine()) != null) {
                filePath = filePath.trim();
                if (filePath.length() > 0 && !filePath.startsWith("#")) {
                    File innerFile = new File(packedFile.getParent(), filePath);
                    writeFile(out, innerFile, buffer);
                    out.write(NEWLINE);
                }
            }
        }
    }
}
