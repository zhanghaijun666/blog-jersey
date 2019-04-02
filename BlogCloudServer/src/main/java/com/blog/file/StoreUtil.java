package com.blog.file;

import com.blog.proto.BlogStore;
import com.blog.utils.BlogMediaType;
import java.io.File;
import java.util.Arrays;
import org.apache.commons.lang.StringUtils;

/**
 * @author zhanghaijun
 */
public class StoreUtil {

    public static String getHashPath(String hash) {
        if (StringUtils.isBlank(hash) || hash.length() < 5) {
            return hash;
        }
        return StringUtils.join(Arrays.asList(hash.substring(0, 2), hash.substring(hash.length() - 4), hash), File.separator);
    }

    public static boolean isFolder(BlogStore.treeItem item) {
        return BlogMediaType.DIRECTORY_CONTENTTYPE.equals(item.getContentType());
    }

//    public static String getStoreContentType(BlogStore.Store store) {
//        String contentType;
//        switch (store.getStoreType()) {
//            case StoreTypeTree:
//                contentType = BlogMediaType.DIRECTORY_CONTENTTYPE;
//                break;
//            case StoreTypeBlob:
//                contentType = store.getBlob().getContentType();
//                break;
//            default:
//                contentType = "application/octet-stream";
//        }
//        return contentType;
//    }
}
