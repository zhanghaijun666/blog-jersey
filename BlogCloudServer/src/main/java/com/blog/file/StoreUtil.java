package com.blog.file;

import com.blog.proto.BlogStore;
import com.blog.utils.BlogMediaType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 * @author zhanghaijun
 */
public class StoreUtil {

    public static boolean isFolder(BlogStore.StoreTree item) {
        return BlogMediaType.DIRECTORY_CONTENTTYPE.equals(item.getContentType());
    }

    /**
     * 获取目录的tree集合
     *
     * @param filePath
     * @param rootTreeHash
     * @return
     */
    public static List< BlogStore.StoreTree> getTreeItemList(String filePath, String rootTreeHash) {
        List<String> dirs = Arrays.asList(filePath.split("/"));
        List< BlogStore.StoreTree> treeList = new ArrayList<>();
        int index = 0;
        String treeHash = rootTreeHash;
        BlogStore.StoreTree tree = null;
        do {
            tree = (BlogStore.StoreTree) StorageFile.readStorag(BlogStore.StoreTypeEnum.StoreTypeTree, treeHash);
            String pathName = dirs.get(index);
            if (tree != null && StringUtils.isNotBlank(pathName)) {
                treeList.add(tree.toBuilder().setHash(treeHash).build());
                treeHash = "";
                for (BlogStore.StoreTree item : tree.getTreeItemList()) {
                    if (pathName.equals(item.getName()) && StoreUtil.isFolder(item)) {
                        treeHash = item.getHash();
                    }
                }
            }
        } while (tree != null && StringUtils.isNotBlank(treeHash));
        return treeList;
    }
}
