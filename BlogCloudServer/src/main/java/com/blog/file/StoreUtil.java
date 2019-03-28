package com.blog.file;

import com.blog.config.Configuration;
import com.blog.proto.BlogStore;
import com.blog.utils.BlogMediaType;
import java.io.File;
import java.util.Arrays;
import org.apache.commons.lang.StringUtils;

/**
 * @author zhanghaijun
 */
public class StoreUtil {

    public static final String BLOB_DIR = "blob";
    public static final String COMMIT_DIR = "commit";
    public static final String TREE_DIR = "tree";

    public static String getHashFullPath(BlogStore.StoreTypeEnum type, String hash) {
        String store = "";
        switch (type) {
            case StoreTypeCommit:
                store = StoreUtil.COMMIT_DIR;
                break;
            case StoreTypeTree:
                store = StoreUtil.TREE_DIR;
                break;
            case StoreTypeBlob:
                store = StoreUtil.BLOB_DIR;
                break;
        }
        return new File(Configuration.getInstance().getFileStore(), store + File.separator + StoreUtil.getHashPath(hash)).getAbsolutePath();
    }

    public static String getHashPath(String hash) {
        if (StringUtils.isBlank(hash) || hash.length() < 5) {
            return hash;
        }
        return StringUtils.join(Arrays.asList(hash.substring(0, 2), hash.substring(hash.length() - 4), hash), File.separator);
    }

    public static String getStoreContentType(BlogStore.Store store) {
        String contentType;
        switch (store.getStoreType()) {
            case StoreTypeTree:
                contentType = BlogMediaType.DIRECTORY_CONTENTTYPE;
                break;
            case StoreTypeBlob:
                contentType = store.getBlob().getContentType();
                break;
            default:
                contentType = "application/octet-stream";
        }
        return contentType;
    }
}
