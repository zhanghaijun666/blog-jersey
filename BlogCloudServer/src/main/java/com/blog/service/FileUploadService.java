package com.blog.service;

import com.blog.proto.BlogStore;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhanghaijun
 */
public class FileUploadService {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadService.class);

    public static BlogStore.RspInfoList fileUpload(Request request, Response response) {
        try {
            File file = FileUploadService.getFileFromStream(request.getInputStream());

        } catch (IOException ex) {
            logger.error("Unable to get real stream", ex);
        }

//        for (BlogStore.TreeUpdateItem item : fileList.getItemList()) {
////            item.getContent();
//        }
        return BlogStore.RspInfoList.newBuilder().setCode(BlogStore.ReturnCode.Return_OK).build();
    }

    public static File getFileFromStream(InputStream stream1) {
        InputStream inputStream = stream1;
        while (inputStream instanceof FilterInputStream) {
            try {
                Field field = FilterInputStream.class.getDeclaredField("in");
                field.setAccessible(true);
                inputStream = (InputStream) (field.get(inputStream));
                if (inputStream instanceof FileInputStream) {
                    FileInputStream filestream = (FileInputStream) inputStream;
                    Field pathField = FileInputStream.class.getDeclaredField("path");
                    pathField.setAccessible(true);
                    String path = (String) pathField.get(filestream);
                    return new File(path);
                }
            } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException ex) {
                logger.error("Unable to get real stream", ex);
            }
        }
        return null;
    }
}
