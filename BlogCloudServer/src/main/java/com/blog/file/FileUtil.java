package com.blog.file;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author haijun.zhang
 */
public class FileUtil {

    public static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

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
