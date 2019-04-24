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
    private final BlogStore.StoreTree tree;

    public StorageTreeAttr(String filePath, String Hash, BlogStore.StoreTree tree) {
        this.filePath = filePath;
        this.Hash = Hash;
        this.tree = tree;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getHash() {
        return Hash;
    }

    public BlogStore.StoreTree getTree() {
        return tree;
    }

    public boolean isFolder() {
        return StringUtils.equals(BlogMediaType.DIRECTORY_CONTENTTYPE, tree.getContentType());
    }

    public BlogStore.StoreTree buildNewTree(String oldTreeHash, String newTreeHash) {
        List<String> oldTreeHashList = this.getTree().getTreeHashItemList();
        BlogStore.StoreTree.Builder tree = BlogStore.StoreTree.newBuilder(this.getTree());
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
