package com.blog.file;

import com.blog.proto.BlogStore;
import com.blog.service.RepositoryService;
import com.blog.utils.BlogMediaType;
import com.blog.utils.FileUtils;
import com.tools.EncryptUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 * @author zhanghaijun
 */
public class StorageFactory {

    public static BlogStore.ReturnCode addTreeItem(FileUrl fileUrl, String treeHash, long treeSize) {
        return StorageFactory.StorageFactory(fileUrl.getParent(), (BlogStore.StorageItem oldStoreItem) -> {
            return StorageTreeAttr.buildFolderNewTree(oldStoreItem, "", 0, treeHash, treeSize);
        });
    }

    public static BlogStore.ReturnCode renameTreeItem(FileUrl fileUrl, String oldTreeHash, long oldTreeSize, String newTreeHash, long newTreeSize) {
        return StorageFactory.StorageFactory(fileUrl.getParent(), (BlogStore.StorageItem oldStoreItem) -> {
            return StorageTreeAttr.buildFolderNewTree(oldStoreItem, oldTreeHash, oldTreeSize, newTreeHash, newTreeSize);
        });
    }

    public static BlogStore.ReturnCode deleteTreeItem(FileUrl fileUrl, String deldeteTreeHash, long deldeteTreeSize) {
        return StorageFactory.StorageFactory(fileUrl.getParent(), (BlogStore.StorageItem oldStoreItem) -> {
            return StorageTreeAttr.buildFolderNewTree(oldStoreItem, deldeteTreeHash, deldeteTreeSize, "", 0);
        });
    }

    /**
     * 更新treeItem
     *
     * @param fileUrl 更新文件的parentFileUrl
     * @return
     */
    private static BlogStore.ReturnCode StorageFactory(FileUrl fileUrl, Action action) {
        String commitHash = fileUrl.getRootHash();
        BlogStore.StorageItem commit = StorageFile.readStorag(BlogStore.StoreTypeEnum.StoreTypeCommit, commitHash);
        BlogStore.ReturnCode returnCode = BlogStore.ReturnCode.Return_OK;
        BlogStore.StorageItem newCommit = null;
        if (null == commit) {
            // 第一次更新文件
            newCommit = action.perform(BlogStore.StorageItem.newBuilder()
                    .setType(BlogStore.StoreTypeEnum.StoreTypeCommit)
                    .setOwner(BlogStore.Operator.newBuilder().setGptype(fileUrl.getGpType()).setGpid(fileUrl.getGpId()).build())
                    .setUpdate(BlogStore.Operator.newBuilder().setGptype(BlogStore.GtypeEnum.User_VALUE).setGpid(fileUrl.getUserId()).build())
                    .setCreateTime(System.currentTimeMillis())
                    .setUpdateTime(System.currentTimeMillis())
                    .setFileName("")
                    .setSize(0)
                    .setContentType(BlogMediaType.DIRECTORY_CONTENTTYPE)
                    .setParent("")
                    .build());
        } else {
            List<StorageTreeAttr> list = StorageUtil.getTreeItemList(fileUrl.getPath(), commit);
            if (list.isEmpty()) {
                //根目录下更新文件
                newCommit = action.perform(commit);
            } else {
                //其他目录下更新文件
                StorageTreeAttr currentTreeAttr = null;
                BlogStore.StorageItem newTree = null;
                String treeHash = null;
                long oldTreeSize = 0;
                long newTreeSize = 0;
                for (int i = list.size() - 1; i >= 0; i++) {
                    if (returnCode != BlogStore.ReturnCode.Return_OK) {
                        break;
                    }
                    currentTreeAttr = list.get(i);
                    oldTreeSize = currentTreeAttr.getStorageItem().getSize();
                    newTreeSize = null == newTree ? 0 : newTree.getSize();
                    if (i == list.size() - 1) {
                        newTree = action.perform(currentTreeAttr.getStorageItem());
                    } else {
                        newTree = StorageTreeAttr.buildFolderNewTree(currentTreeAttr.getStorageItem(), currentTreeAttr.getHash(), oldTreeSize, treeHash, newTreeSize);
                    }
                    treeHash = EncryptUtils.sha1(newTree.toByteArray());
                    returnCode = StorageFile.writeStorag(BlogStore.StoreTypeEnum.StoreTypeTree, treeHash, newTree);
                }
                List<String> oldTreeHashList = commit.getTreeHashItemList();
                List<String> newTreeHashList = new ArrayList<>();
                for (String hash : oldTreeHashList) {
                    if (StringUtils.equals(hash, currentTreeAttr.getHash())) {
                        newTreeHashList.add(currentTreeAttr.getHash());
                    } else {
                        newTreeHashList.add(hash);
                    }
                }
                newCommit = BlogStore.StorageItem.newBuilder(commit)
                        .setUpdate(BlogStore.Operator.newBuilder().setGptype(BlogStore.GtypeEnum.User_VALUE).setGpid(fileUrl.getUserId()).build())
                        .setUpdateTime(System.currentTimeMillis())
                        .setSize(0)
                        .setParent(commitHash)
                        .addAllTreeHashItem(newTreeHashList)
                        .build();
            }
        }
        if (returnCode == BlogStore.ReturnCode.Return_OK) {
            String newCommitHash = EncryptUtils.sha1(newCommit.toByteArray());
            returnCode = StorageFile.writeStorag(BlogStore.StoreTypeEnum.StoreTypeCommit, newCommitHash, newCommit);
            if (returnCode == BlogStore.ReturnCode.Return_OK) {
                RepositoryService.updateCommitHash(fileUrl.getGpType(), fileUrl.getGpId(), newCommitHash);
            }
        }
        return returnCode;
    }

    /**
     * 获取fileUtrl.getPath()对应的StorageItem
     *
     * @param fileUrl
     * @return
     */
    public static StorageTreeAttr getStorage(FileUrl fileUrl) {
        BlogStore.StorageItem commit = StorageFile.readStorag(BlogStore.StoreTypeEnum.StoreTypeCommit, fileUrl.getRootHash());
        if (null == commit) {
            return null;
        }
        List<StorageTreeAttr> storageAttr = StorageUtil.getTreeItemList(fileUrl.getPath(), commit);
        if (storageAttr.isEmpty()) {
            return new StorageTreeAttr("/", fileUrl.getRootHash(), commit);
        } else {
            return storageAttr.get(storageAttr.size() - 1);
        }
    }

    /**
     * 获取fileUrl的文件列表
     *
     * @param fileUrl
     * @return
     */
    public static BlogStore.FileItemList getFileItemList(FileUrl fileUrl) {
        BlogStore.FileItemList.Builder list = BlogStore.FileItemList.newBuilder();
        StorageTreeAttr storageAttr = StorageFactory.getStorage(fileUrl);
        if (null == storageAttr) {
            return list.build();
        }
        BlogStore.StorageItem storageItem = storageAttr.getStorageItem();
        if (null == storageItem) {
            return list.build();
        }
        List<String> childTreeHashList = FileUtils.isFolder(storageItem.getContentType()) ? storageItem.getTreeHashItemList() : new ArrayList<>();
        list.setParentFile(BlogStore.FileItem.newBuilder()
                .setFileName(storageItem.getFileName())
                .setContentType(storageItem.getContentType())
                .setSize(storageItem.getSize())
                .setCreateTime(storageItem.getCreateTime())
                .setUpdateTime(storageItem.getUpdateTime())
                .build());
        for (String treeHash : childTreeHashList) {
            BlogStore.StorageItem tree = StorageFile.readStorag(BlogStore.StoreTypeEnum.StoreTypeTree, treeHash);
            if (null != tree) {
                list.addItem(BlogStore.FileItem.newBuilder()
                        .setFileName(tree.getFileName())
                        .setContentType(tree.getContentType())
                        .setSize(tree.getSize())
                        .setCreateTime(tree.getCreateTime())
                        .setUpdateTime(tree.getUpdateTime())
                        .build());
            }
        }
        return list.build();
    }
}

interface Action {

    // 更新parent的commit或者tree
    public BlogStore.StorageItem perform(BlogStore.StorageItem oldStoreItem);
}
