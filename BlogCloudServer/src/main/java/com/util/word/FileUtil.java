package com.util.word;

import java.io.File;

/**
 * @author zhanghaijun
 */
public class FileUtil {

    public static boolean DeleteFile(File file) {
        if (!file.exists()) {
            return true;
        } else if (file.isFile()) {
            return file.delete();
        } else {
            File[] files = file.listFiles();
            boolean flag = true;
            for (int i = 0; i < files.length; i++) {
                if (!flag) {
                    break;
                }
                flag = DeleteFile(files[i]);
            }
            return flag && file.delete();
        }
    }
}
