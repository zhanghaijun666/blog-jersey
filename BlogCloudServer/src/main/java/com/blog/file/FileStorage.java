package com.blog.file;

import com.blog.config.Configuration;
import com.blog.proto.BlogStore;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import org.apache.commons.lang.StringUtils;

/**
 * @author haijun.zhang
 */
public class FileStorage {

    public final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(FileStorage.class);

    public static final String BLOB_DIR = "blob";
    public static final String COMMIT_DIR = "commit";
    public static final String TREE_DIR = "tree";

    public BlogStore.Store loadStore(BlogStore.StoreTypeEnum type, String hash) {
        BlogStore.Store store = null;
        String fileFullPath = FileStorage.getHashFullPath(type, hash);
        File file = new File(fileFullPath);
        FileInputStream input = null;
        if (file.exists() && file.isFile() && file.canRead()) {
            try {
                input = new FileInputStream(file);
                byte[] databuf = new byte[input.available()];
                input.read(databuf);
                store = BlogStore.Store.parseFrom(databuf);
            } catch (FileNotFoundException ex) {
                logger.error("Unable to open " + fileFullPath, ex);
            } catch (IOException ex) {
                logger.error("Unable to open " + fileFullPath, ex);
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException ex) {
                    }
                }
            }
        }
        return store;
    }

    public static String getHashFullPath(BlogStore.StoreTypeEnum type, String hash) {
        String store = "";
        switch (type) {
            case StoreCommit:
                store = FileStorage.COMMIT_DIR;
                break;
            case StoreTree:
                store = FileStorage.TREE_DIR;
                break;
            case StoreBlob:
                store = FileStorage.BLOB_DIR;
                break;
        }
        return new File(Configuration.getInstance().getFileStore(), store + File.separator + FileStorage.getHashPath(hash)).getAbsolutePath();
    }

    public static String getHashPath(String hash) {
        if (StringUtils.isBlank(hash) || hash.length() < 5) {
            return hash;
        }
        return StringUtils.join(Arrays.asList(hash.substring(0, 2), hash.substring(hash.length() - 4), hash), File.separator);
    }
}
