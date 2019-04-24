package com.blog.file;

import com.blog.proto.BlogStore;
import com.blog.service.RepositoryService;
import com.blog.utils.FileUtils;
import com.tools.EncryptUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhanghaijun
 */
public class StorageFactory {

    public static final int MAX_BLOB_SIZE = 32 * 1024 * 1024;

    public static BlogStore.ReturnCode UploadFile(FileUrl fileUrl, InputStream inputStream) throws IOException {
        List<byte[]> blobList = FileUtils.SplitList(FileUtils.toByteArray(inputStream), StorageFactory.MAX_BLOB_SIZE);
        Map<String, byte[]> blobMap = new HashMap<>();
        for (int i = 0; i < blobList.size(); i++) {
            blobMap.put(EncryptUtils.sha1(blobList.get(i)), blobList.get(i));
        }
        String fileHash = EncryptUtils.sha1(blobMap.keySet());

        BlogStore.ReturnCode code = BlogStore.ReturnCode.Return_OK;
        for (Map.Entry<String, byte[]> entry : blobMap.entrySet()) {
            if (code != BlogStore.ReturnCode.Return_OK) {
                break;
            }
            BlogStore.StoreBlob.Builder blob = BlogStore.StoreBlob.newBuilder();
            blob.setCommitter(BlogStore.Operator.newBuilder().setDate(System.currentTimeMillis()).setGptype(BlogStore.GtypeEnum.User_VALUE).setGpid(fileUrl.getUserId()).build());
            blob.setName(FileUtils.getFileName(fileUrl.getPath()));
            blob.setContentType("");
            blob.setSize(entry.getValue().length);
            code = StorageFile.writeStorag(BlogStore.StoreTypeEnum.StoreTypeBlob, entry.getKey(), blob.build(), entry.getValue());
        }
        if (code == BlogStore.ReturnCode.Return_OK) {
            StorageFactory.addFile(fileUrl, blobMap.keySet());
        }
        return code;
    }

    public static BlogStore.ReturnCode addFile(FileUrl fileUrl, Collection<String> blobHashList) {
        return StorageFactory.StorageFactory(fileUrl, (BlogStore.StoreTree oldStoreTree) -> {
            BlogStore.StoreTree.Builder storeTree = BlogStore.StoreTree.newBuilder(oldStoreTree);
            storeTree.addFileItem(BlogStore.StringList.newBuilder().addAllImte(blobHashList).build());
            return storeTree.build();
        });
    }

    public static BlogStore.ReturnCode addFolder(FileUrl fileUrl, String newTreeHash) {
        return StorageFactory.StorageFactory(fileUrl, (BlogStore.StoreTree oldStoreTree) -> {
            BlogStore.StoreTree.Builder storeTree = BlogStore.StoreTree.newBuilder(oldStoreTree);
            storeTree.addTreeHashItem(newTreeHash);
            return storeTree.build();
        });
    }

    /**
     * 更新treeItem
     *
     * @param fileUrl
     * @return
     */
    private static BlogStore.ReturnCode StorageFactory(FileUrl fileUrl, Action action) {
        String commitHash = fileUrl.getRootHash();
        BlogStore.StoreCommit commit = (BlogStore.StoreCommit) StorageFile.readStorag(BlogStore.StoreTypeEnum.StoreTypeCommit, commitHash);
        List<StorageTreeAttr> list = new ArrayList<>();
        if (null == commit) {
            list.add(new StorageTreeAttr("", "", BlogStore.StoreTree.getDefaultInstance()));
        } else {
            list = StorageUtil.getTreeItemList(fileUrl.getPath(), commit.getTreeHash());
        }
        StorageTreeAttr currentTreeAttr = list.remove(list.size() - 1);
        //  对当前的tree做增删改查操作，改变当前的treeItem
        BlogStore.StoreTree newTree = action.perform(currentTreeAttr.getTree());
        String treeHash = EncryptUtils.sha1(newTree.toByteArray());
        BlogStore.ReturnCode code = StorageFile.writeStorag(BlogStore.StoreTypeEnum.StoreTypeTree, treeHash, newTree);
        for (int i = list.size() - 1; i >= 0; i++) {
            if (code != BlogStore.ReturnCode.Return_OK) {
                break;
            }
            newTree = list.get(i).buildNewTree(currentTreeAttr.getHash(), treeHash);
            treeHash = EncryptUtils.sha1(newTree.toByteArray());
            currentTreeAttr = list.get(i);
            code = StorageFile.writeStorag(BlogStore.StoreTypeEnum.StoreTypeTree, treeHash, newTree);
        }
        if (code == BlogStore.ReturnCode.Return_OK) {
            BlogStore.StoreCommit.Builder newCommit = null == commit ? BlogStore.StoreCommit.newBuilder() : BlogStore.StoreCommit.newBuilder(commit);
            newCommit.setTreeHash(treeHash);
            newCommit.setParent(commitHash);
            String newCommitHash = EncryptUtils.sha1(newCommit.build().toByteArray());
            code = StorageFile.writeStorag(BlogStore.StoreTypeEnum.StoreTypeCommit, newCommitHash, newCommit.build());
            if (code == BlogStore.ReturnCode.Return_OK) {
                RepositoryService.updateCommitHash(fileUrl.getGpType(), fileUrl.getGpId(), newCommitHash);
            }
        }
        return code;
    }

    public static BlogStore.FileItemList getFileItemList(FileUrl fileUrl) {
        BlogStore.FileItemList.Builder list = BlogStore.FileItemList.newBuilder();
        List<StorageTreeAttr> storageTreeAttr = StorageUtil.getTreeItemList(fileUrl.getPath(), fileUrl.getRootHash());
        if (storageTreeAttr.isEmpty()) {
            return list.build();
        }
        StorageTreeAttr prentTreeArr = storageTreeAttr.get(storageTreeAttr.size() - 1);
        BlogStore.StoreTree parentTree = prentTreeArr.getTree();
        list.setParentFile(BlogStore.FileItem.newBuilder()
                .setName(parentTree.getName())
                .setContentType(parentTree.getContentType())
                .setSize(parentTree.getSize())
                .setCreateTime(parentTree.getCreateTime())
                .setUpdateTime(parentTree.getUpdateTime())
                .build());

        if (prentTreeArr.isFolder()) {
            for (String treeHash : parentTree.getTreeHashItemList()) {
                BlogStore.StoreTree tree = (BlogStore.StoreTree) StorageFile.readStorag(BlogStore.StoreTypeEnum.StoreTypeTree, treeHash);
                if (null != tree) {
                    list.addItem(BlogStore.FileItem.newBuilder()
                            .setName(tree.getName())
                            .setContentType(tree.getContentType())
                            .setSize(tree.getSize())
                            .setCreateTime(tree.getCreateTime())
                            .setUpdateTime(tree.getUpdateTime())
                            .build());
                }
            }
        }
        return list.build();
    }
}

interface Action {

    public BlogStore.StoreTree perform(BlogStore.StoreTree oldStoreTree);
}
