package com.blog.service.file;

import com.blog.config.BlogStore;
import com.blog.utils.BlogMediaType;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 * @author zhanghaijun
 */
public class StorageTreeAttr {

    private final String filePath;
    private final String Hash;
    private final BlogStore.StorageItem storageItem;

    public StorageTreeAttr(String filePath, String Hash, BlogStore.StorageItem tree) {
        this.filePath = filePath;
        this.Hash = Hash;
        this.storageItem = tree;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getHash() {
        return Hash;
    }

    public BlogStore.StorageItem getStorageItem() {
        return storageItem;
    }

    public boolean isFolder() {
        return StringUtils.equals(BlogMediaType.DIRECTORY_CONTENTTYPE, storageItem.getContentType());
    }

    /**
     *
     * @param oldStorageItem 准备更新的StorageItem
     * @param oldTreeHash 要替换的treeHash 为空表示新增
     * @param oldSize 要替换的treeHash的大小
     * @param newTreeHash 新增或替换的treeHash 不能为空
     * @param newSize 新增或替换的treeHash大小
     * @return 更新后的StorageItem
     */
    public static BlogStore.StorageItem buildFolderNewTree(BlogStore.StorageItem oldStorageItem, String oldTreeHash, long oldSize, String newTreeHash, long newSize) {
        if (null == oldStorageItem) {
            return null;
        }
        List<String> oldTreeHashList = oldStorageItem.getTreeHashItemList();
        BlogStore.StorageItem.Builder storage = BlogStore.StorageItem.newBuilder(oldStorageItem);
        storage.setSize(oldStorageItem.getSize() - oldSize + newSize);
        storage.setUpdateTime(System.currentTimeMillis());
        if (StringUtils.isBlank(oldTreeHash) && StringUtils.isNotBlank(newTreeHash)) {
            //新增
            storage.addTreeHashItem(newTreeHash);
        } else if (StringUtils.isNotBlank(oldTreeHash)) {
            //更新 删除
            storage.clearTreeHashItem();
            for (String hash : oldTreeHashList) {
                if (StringUtils.equals(hash, oldTreeHash)) {
                    if (StringUtils.isNotBlank(newTreeHash)) {
                        storage.addTreeHashItem(newTreeHash);
                    }
                } else {
                    storage.addTreeHashItem(hash);
                }
            }
        }
        return storage.build();
    }

}
