package com.blog.file;

import com.blog.proto.BlogStore;
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

    public BlogStore.StorageItem buildNewTree(String oldTreeHash, String newTreeHash) {
        List<String> oldTreeHashList = this.getStorageItem().getTreeHashItemList();
        BlogStore.StorageItem.Builder tree = BlogStore.StorageItem.newBuilder(this.getStorageItem());
        tree.getTreeHashItemList().clear();
        for (String hash : oldTreeHashList) {
            if (StringUtils.equals(hash, oldTreeHash)) {
                tree.addTreeHashItem(oldTreeHash);
            } else {
                tree.addTreeHashItem(hash);
            }
        }
        return tree.build();
    }
}
