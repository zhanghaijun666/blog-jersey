package com.blog.service;

import com.blog.file.FileUrl;
import com.blog.file.StorageFile;
import com.blog.file.StoreUtil;
import com.blog.proto.BlogStore;
import com.tools.EncryptUtils;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 * @author zhanghaijun
 */
public class RepositoryStore {

    private FileUrl fileUrl;

    public RepositoryStore(FileUrl fileUrl) {
        this.fileUrl = fileUrl;
    }

    public FileUrl getFileUrl() {
        return fileUrl;
    }

    public void operateFilesUpdateTree() {
        String commitHash = this.getFileUrl().getRootHash();
        if (StringUtils.isBlank(commitHash) || StringUtils.equals(commitHash, FileUrl.DEFAULT_ROOT_HASH)) {
            return;
        }
        BlogStore.StoreCommit commit = (BlogStore.StoreCommit) StorageFile.readStorag(BlogStore.StoreTypeEnum.StoreTypeCommit, commitHash);
        List< BlogStore.StoreTree> treeList = StoreUtil.getTreeItemList(this.getFileUrl().getPath(), commit.getTreeHash());

        BlogStore.StoreTree currentTree = treeList.remove(treeList.size() - 1);
        String treeHash = EncryptUtils.sha1(currentTree.toByteArray());
        BlogStore.ReturnCode code = StorageFile.writeStorag(BlogStore.StoreTypeEnum.StoreTypeTree, treeHash, currentTree);
        for (int i = treeList.size() - 1; i >= 0; i++) {
            if (code != BlogStore.ReturnCode.Return_OK) {
                break;
            }
            
            StorageFile.writeStorag(BlogStore.StoreTypeEnum.StoreTypeTree, EncryptUtils.sha1(treeList.get(i).toByteArray()), treeList.get(i));

        }

    }

    public static void addFileUpdateTree(FileUrl fileUrl, BlogStore.StoreFile storeFile) {

    }

}

interface Action {

    public BlogStore.StoreTree perform();
}
