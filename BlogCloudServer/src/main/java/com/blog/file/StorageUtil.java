package com.blog.file;

import com.blog.proto.BlogStore;
import com.blog.utils.BlogMediaType;
import com.blog.utils.FileUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 * @author zhanghaijun
 */
public class StorageUtil {

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
    public static List<StorageTreeAttr> getTreeItemList(String filePath, String rootTreeHash) {
        List<StorageTreeAttr> list = new ArrayList<>();
        if (StringUtils.isBlank(filePath) || StringUtils.isBlank(rootTreeHash) || StringUtils.equals(rootTreeHash, FileUrl.DEFAULT_ROOT_HASH)) {
            return list;
        }
        List<String> dirctorieList = FileUtils.getParentDirctories("", filePath, true);
        int index = 0;
        String treeHash = rootTreeHash;
        BlogStore.StoreTree tree = null;
        do {
            tree = (BlogStore.StoreTree) StorageFile.readStorag(BlogStore.StoreTypeEnum.StoreTypeTree, treeHash);
            String dirctorie = dirctorieList.get(index);
            if (tree != null && StringUtils.isNotBlank(dirctorie)) {
                list.add(new StorageTreeAttr(dirctorie, treeHash, tree));
                treeHash = "";
                for (String itemHash : tree.getTreeHashItemList()) {
                    tree = (BlogStore.StoreTree) StorageFile.readStorag(BlogStore.StoreTypeEnum.StoreTypeTree, itemHash);
                    if (tree != null && StringUtils.equals(FileUtils.getFileName(dirctorie), tree.getName())) {
                        treeHash = itemHash;
                    }
                }
            }
        } while (tree != null && StringUtils.isNotBlank(treeHash));
        return list;
    }
}
