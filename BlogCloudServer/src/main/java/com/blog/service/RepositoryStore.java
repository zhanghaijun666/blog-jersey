package com.blog.service;

import com.blog.file.FileUrl;
import com.blog.file.StorageFile;
import com.blog.file.StoreUtil;
import com.blog.proto.BlogStore;
import com.tools.EncryptUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 * @author zhanghaijun
 */
public class RepositoryStore {

    FileUrl fileUrl;

    public RepositoryStore(FileUrl fileUrl) {
        this.fileUrl = fileUrl;
    }

    /**
     * 获取目录的tree集合
     *
     * @param filePath
     * @param rootTreeHash
     * @return
     */
    private static List< BlogStore.StoreTree> getTreeItemList(String filePath, String rootTreeHash) {
        List<String> dirs = Arrays.asList(filePath.split("/"));
        List< BlogStore.StoreTree> treeList = new ArrayList<>();
        int index = 0;
        String treeHash = rootTreeHash;
        BlogStore.StoreTree tree = null;
        do {
            tree = (BlogStore.StoreTree) StorageFile.readStorag(BlogStore.StoreTypeEnum.StoreTypeTree, treeHash);
            treeHash = "";
            String pathName = dirs.get(index);
            if (tree != null && StringUtils.isNotBlank(pathName)) {
                treeList.add(tree);
                for (BlogStore.StoreTree item : tree.getTreeItemList()) {
                    if (pathName.equals(item.getName()) && StoreUtil.isFolder(item)) {
                        treeHash = item.getHash();
                    }
                }
            }
        } while (StringUtils.isNotBlank(treeHash) && tree != null);
        return treeList;
    }

    public static void operateFilesUpdateTree(FileUrl fileUrl, Action action) {
        RepositoryStore repositoryStore = new RepositoryStore(fileUrl);
        if (StringUtils.isBlank(repositoryStore.fileUrl.getRootHash()) || StringUtils.equals(repositoryStore.fileUrl.getRootHash(), FileUrl.DEFAULT_ROOT_HASH)) {
            return;
        }
        BlogStore.StoreCommit commit = (BlogStore.StoreCommit) StorageFile.readStorag(BlogStore.StoreTypeEnum.StoreTypeCommit, repositoryStore.fileUrl.getRootHash());
        List< BlogStore.StoreTree> treeList = RepositoryStore.getTreeItemList(repositoryStore.fileUrl.getPath(), commit.getTreeHash());

        for (int i = treeList.size() - 1; i >= 0; i++) {
//            StorageFile.writeStorag(BlogStore.StoreTypeEnum.StoreTypeTree, EncryptUtils.sha1(treeList.get(i).toByteArray()), treeList.get(i));

        }

    }

    public static void addFileUpdateTree(FileUrl fileUrl, BlogStore.StoreFile storeFile) {
        RepositoryStore.operateFilesUpdateTree(fileUrl, new Action() {
            @Override
            public BlogStore.StoreTree perform() {
                return null;
            }
        });
    }

}

interface Action {

    public BlogStore.StoreTree perform();
}
