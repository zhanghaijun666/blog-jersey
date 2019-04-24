package com.blog.file;

import com.blog.proto.BlogStore;
import com.blog.utils.BlogMediaType;

/**
 * @author zhanghaijun
 */
public class StoreUtil {

    public static boolean isFolder(BlogStore.StoreTree item) {
        return BlogMediaType.DIRECTORY_CONTENTTYPE.equals(item.getContentType());
    }

}
