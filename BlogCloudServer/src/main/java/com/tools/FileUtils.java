package com.tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.commons.lang.StringUtils;

/**
 * @author zhanghaijun
 *
 * http://tool.oschina.net/commons
 */
public class FileUtils {

    public static String getFileContentType(String fileUrl) {
        String contentType = null;
        try {
            contentType = Files.probeContentType(Paths.get(fileUrl));
        } catch (IOException e) {
        }
        return contentType;
    }

    public static String getFileContentType(File file) {
        String contentType = null;
        try {
            contentType = Files.probeContentType(file.toPath());
        } catch (IOException e) {
        }
        return contentType;
    }

    public static String getFileName(String path) {
        if (StringUtils.isBlank(path)) {
            return path;
        }
        int separatorIndex = path.lastIndexOf("/");
        if (separatorIndex != -1) {
            return path.substring(separatorIndex + 1);
        }
        return path;
    }
}
