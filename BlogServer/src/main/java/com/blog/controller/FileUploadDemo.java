package com.blog.controller;

import com.blog.config.Configuration;
import com.blog.utils.BlogMediaType;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

/**
 * https://blog.csdn.net/wolfcode_cn/article/category/7728421
 * https://howtodoinjava.com/jersey-jax-rs-tutorials/
 *
 * @author zhanghaijun
 */
@Path("/filedemo")
public class FileUploadDemo {

    /**
     * 第一种方式上传
     *
     * @param fileInputStream
     * @param disposition
     * @param ctx
     * @return
     */
    @POST
    @Path("/upload1")
    @Produces({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
    @Consumes(BlogMediaType.MULTIPART_FORM_DATA)
    public String upload(@FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition disposition, @Context ServletContext ctx) {

        //获取文件名；
        System.out.println("getFileName : " + disposition.getFileName());
        //获取字段名称，即<input type="file" name="xxx"）中的xxx
        System.out.println("getName : " + disposition.getName());
        //获取该段content-disposition的内容长度
        System.out.println("getSize : " + disposition.getSize());
        //获取该段content-disposition的类型，比如form-data
        System.out.println("getType : " + disposition.getType());
        //获取本次请求的请求值，比如{name=file, filename=3.jpg}
        System.out.println("getParameters : " + disposition.getParameters());

        File upload = new File(Configuration.getInstance().getFileStore(), UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(disposition.getFileName()));
        try {
            FileUtils.copyInputStreamToFile(fileInputStream, upload);
        } catch (IOException e) {
        }
        return "success";
    }

    /**
     * 第二种方式上传
     *
     * @param bp
     * @param ctx
     * @return
     */
    @POST
    @Path("/upload2")
    @Produces({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
    @Consumes(BlogMediaType.MULTIPART_FORM_DATA)
    public String upload2(@FormDataParam("file") FormDataBodyPart bp, @Context ServletContext ctx) {
        File upload = new File(Configuration.getInstance().getFileStore(), UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(bp.getFormDataContentDisposition().getFileName()));
        try {
            FileUtils.copyInputStreamToFile(bp.getValueAs(InputStream.class), upload);
        } catch (IOException e) {
        }
        return "success";
    }

    /**
     * 图片文件下载
     *
     * @param imageName
     * @return
     * @throws IOException
     */
    @GET
    @Path("/images2/{name}")
    public Response showImg(@PathParam("name") String imageName) throws IOException {
        File file = new File(Configuration.getInstance().getFileStore(), "images" + File.separator + imageName);
        if (!file.exists()) {
            return Response.status(Status.NOT_FOUND).build();
        } else {
            return Response.ok(file).header("Content-disposition", "attachment;filename=" + imageName).header("Cache-Control", "no-cache").build();
        }
    }

    @GET
    @Path("/images3/{name}")
    public Response showImg2(@PathParam("name") String imageName) throws IOException {
        final File f = new File(Configuration.getInstance().getFileStore(), "images" + File.separator + imageName);
        if (!f.exists()) {
            return Response.status(Status.NOT_FOUND).build();
        } else {
            return Response.ok(new StreamingOutput() {
                @Override
                public void write(OutputStream output) throws IOException, WebApplicationException {
                    output.write(FileUtils.readFileToByteArray(f));
                }
            }).header("Content-disposition", "attachment;filename=" + imageName).header("Cache-Control", "no-cache").build();
        }
    }

//    /**
//     * 第一种方式上传
//     *
//     * @param fileInputStream
//     * @param disposition
//     * @return
//     */
//    @POST
//    @Path("filedemo1")
//    @Consumes(BlogMediaType.MULTIPART_FORM_DATA)
//    @Produces({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
//    public BlogStore.RspInfoList uploadimage1(@FormDataParam("file") InputStream fileInputStream, @FormDataParam("file") FormDataContentDisposition disposition) {
//        String imageName = Calendar.getInstance().getTimeInMillis() + disposition.getFileName();
//        File file = new File(Configuration.getInstance().getFileStore(), imageName);
//        System.out.println("file " + file.getAbsolutePath());
//        try {
//            //使用common io的文件写入操作
//            FileUtils.copyInputStreamToFile(fileInputStream, file);
//            //原来自己的文件写入操作
//            //saveFile(fileInputStream, file);
//        } catch (IOException ex) {
//        }
//        return BlogStore.RspInfoList.newBuilder().setCode(BlogStore.ReturnCode.Return_OK).build();
//    }
//
//    /**
//     * 第二种方式上传 使用FormDataMultiPart 获取表单数据
//     *
//     * @param form
//     * @return
//     * @throws UnsupportedEncodingException
//     */
//    @POST
//    @Path("filedemo2")
//    @Consumes(BlogMediaType.MULTIPART_FORM_DATA)
//    @Produces({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
//    public BlogStore.RspInfoList uploadimage2(FormDataMultiPart form) throws UnsupportedEncodingException {
//        //获取文件流
//        FormDataBodyPart filePart = form.getField("file");
//        //获取表单的其他数据
//        FormDataBodyPart usernamePart = form.getField("username");
//        //ContentDisposition headerOfFilePart = filePart.getContentDisposition();
//        //把表单内容转换成流
//        InputStream fileInputStream = filePart.getValueAs(InputStream.class);
//        FormDataContentDisposition formDataContentDisposition = filePart.getFormDataContentDisposition();
//        String source = formDataContentDisposition.getFileName();
//        String result = new String(source.getBytes("ISO8859-1"), "UTF-8");
//        System.out.println("formDataContentDisposition.getFileName()result " + result);
//        File file = new File(Configuration.getInstance().getFileStore(), result);
//        System.out.println("file " + file.getAbsolutePath());
//        try {
//            //保存文件
//            FileUtils.copyInputStreamToFile(fileInputStream, file);
//            //saveFile(fileInputStream, file);
//        } catch (IOException ex) {
//        }
////        response.setCharacterEncoding("UTF-8");
//        return BlogStore.RspInfoList.newBuilder().setCode(BlogStore.ReturnCode.Return_OK).build();
//    }
//
//    /**
//     *
//     * 不从web服务器去读图片,在磁盘某个目录的文件可以通过流的方式去获取 ,通过 response.getOutputStream()放回数据
//     *
//     * @param imageName image-name
//     * @param type extension of image
//     * @throws IOException
//     */
//    @GET
//    @Path("/images/{name}.{type}")
//    public void showImg(@PathParam("name") String imageName, @PathParam("type") String type) throws IOException {
//        File file = new File(Configuration.getInstance().getFileStore(), "images" + File.separator + imageName + "." + type);
//        System.out.println("file " + file.getAbsolutePath());
//        FileUtils.copyFile(file, response.getOutputStream());
//    }
//
//    // 保存文件信息到磁盘 
//    private void saveFile(InputStream uploadedInputStream, File file) {
//        System.out.println("------saveFile-----");
//        try {
//            try (OutputStream outpuStream = new FileOutputStream(file)) {
//                int read = 0;
//                byte[] bytes = new byte[1024];
//                while ((read = uploadedInputStream.read(bytes)) != -1) {
//                    outpuStream.write(bytes, 0, read);
//                }
//                outpuStream.flush();
//            }
//        } catch (IOException e) {
//        }
//    }
}
