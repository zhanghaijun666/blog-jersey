package com.blog.file;

import com.blog.proto.BlogStore;
import com.blog.utils.FileUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 * @author zhanghaijun
 */
public class StorageUtil {

    /**
     * 获取目录的tree集合
     *
     * @param filePath
     * @param commit
     * @return
     */
    public static List<StorageTreeAttr> getTreeItemList(String filePath, BlogStore.StorageItem commit) {
        List<StorageTreeAttr> list = new ArrayList<>();
        if (StringUtils.isBlank(filePath) || null == commit || commit.getTreeHashItemList().isEmpty()) {
            return list;
        }
        List<String> dirctorieList = FileUtils.getParentDirctories("", filePath, true);
        List<String> treeHashList = commit.getTreeHashItemList();
        int index = 0;
        while (!treeHashList.isEmpty()) {
            BlogStore.StorageItem tree = null;
            index++;
            if (index >= dirctorieList.size()) {
                break;
            }
            String dirctorie = dirctorieList.get(index);
            for (String treeHash : commit.getTreeHashItemList()) {
                tree = StorageFile.readStorag(BlogStore.StoreTypeEnum.StoreTypeTree, treeHash);
                if (null != tree && StringUtils.equals(FileUtils.getFileName(dirctorie), tree.getFileName())) {
                    list.add(new StorageTreeAttr(dirctorie, treeHash, tree));
                    treeHashList = tree.getTreeHashItemList();
                    break;
                }
            }
        }
        return list;
    }
}
