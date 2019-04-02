package com.blog.file;

import com.blog.proto.BlogStore;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 * @author haijun.zhang
 */
public class StoreTree {

    public BlogStore.StoreTree getStoreItem(FileUrl fileUrl, int userId) {
        if (StringUtils.isBlank(fileUrl.getRootHash()) || StringUtils.equals(fileUrl.getRootHash(), FileUrl.DEFAULT_ROOT_HASH)) {
            return null;
        }
        List<String> dirs = Arrays.asList(fileUrl.getPath().split("/"));
        BlogStore.StoreCommit commit = (BlogStore.StoreCommit) StorageFile.readStorag(BlogStore.StoreTypeEnum.StoreTypeCommit, fileUrl.getRootHash());
        BlogStore.StoreTree tree = (BlogStore.StoreTree) StorageFile.readStorag(BlogStore.StoreTypeEnum.StoreTypeTree, commit.getTreeHash());

        int index = 0;
        while (tree != null && index < dirs.size()) {
            String pathName = dirs.get(index);
            if (StringUtils.isBlank(pathName)) {
                index++;
                continue;
            }
            for (BlogStore.treeItem item : tree.getItemList()) {
                if (pathName.equals(item.getName()) && StoreUtil.isFolder(item)) {
                    tree = (BlogStore.StoreTree) StorageFile.readStorag(BlogStore.StoreTypeEnum.StoreTypeTree, item.getTreeHash());
                    break;
                }
            }
            index++;
        }
        return tree;
    }
}
