package com.blog.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.h2.store.fs.FilePath;

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
        return FilePath.get(path).getName();
    }

    /**
     * 获取父路径集合
     *
     * @param urlPrefix
     * @param filePath
     * @param isContainCurrent
     * @return
     */
    public static List<String> getParentDirctories(String urlPrefix, String filePath, boolean isContainCurrent) {
        List<String> list = new ArrayList<>();
        String[] items = StringUtils.split(filePath, "/");
        if (items.length > 0) {
            int pathLength = isContainCurrent ? items.length : items.length - 1;
            StringBuilder pathBuilder = new StringBuilder();
            for (int i = 0; i < pathLength; i++) {
                pathBuilder.append("/");
                pathBuilder.append(items[i]);
                list.add(urlPrefix + pathBuilder.toString());
            }
        }
        return list;
    }

    /**
     * 读取InputStream的字节
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

    /**
     * 按大小拆分字节数组
     *
     * @param byteArray
     * @param maxSize
     * @return
     */
    public static List<byte[]> SplitList(byte[] byteArray, int maxSize) {
        List<byte[]> result = new ArrayList<>();
        int length = byteArray.length;
        int count = length / maxSize;
        for (int i = 0; i <= count; i++) {
            result.add(Arrays.copyOfRange(byteArray, i * maxSize, Math.min(i * maxSize + maxSize, length)));
        }
        return result;
    }

}
