package com.blog.file;

import com.blog.proto.BlogStore;
import org.apache.commons.lang.StringUtils;

/**
 * @author haijun.zhang
 */
public class StoreFactory {

    public void loadStore(BlogStore.StoreTypeEnum type, String hash) {
        if (StringUtils.isBlank(hash) || hash.length() < 4) {
            
        }else{
            
        }
    }
}
