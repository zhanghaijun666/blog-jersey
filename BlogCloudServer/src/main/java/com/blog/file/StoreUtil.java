package com.blog.file;

import com.blog.proto.BlogStore;
import com.blog.utils.BlogMediaType;

/**
 * @author zhanghaijun
 */
public class StoreUtil {

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
