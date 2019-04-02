package com.blog.service;

import com.blog.proto.BlogStore;

/**
 * @author zhanghaijun
 */
public class FileUploadService {

    public static BlogStore.RspInfoList fileUpload(BlogStore.TreeUpdateItemList fileList, int userId) {
        for (BlogStore.TreeUpdateItem item : fileList.getItemList()) {
//            item.getContent();
        }

        return BlogStore.RspInfoList.newBuilder().setCode(BlogStore.ReturnCode.Return_OK).build();
    }
}
