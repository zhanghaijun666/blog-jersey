package com.blog.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

/**
 * https://howtodoinjava.com/jersey-jax-rs-tutorials/
 *
 * @author zhanghaijun
 */
@Path("/uploaddemo")
public class FileUploadDemo {

    @Inject
    Request request;
    @Inject
    Response response;
    @Context
    SecurityContext security;

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
