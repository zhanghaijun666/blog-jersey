package com.service;

import java.io.InputStream;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author zhanghaijun
 */
public class UploadFile {

    @POST
    @Path("/uploadEventCsv")
    @Consumes({MediaType.MULTIPART_FORM_DATA})  //指定接受类型
    @Produces(MediaType.APPLICATION_JSON)
    public void uploadFile(@FormParam("file") InputStream fileInputStream) throws Exception {
        System.out.println("上传文件：" + fileInputStream.getClass().getName());
    }
}
