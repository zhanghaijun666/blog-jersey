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

    public static BlogStore.ReturnCode addTreeItem(FileUrl parentFileUrl, String treeHash, long treeSize) {
        return StorageFactory.StorageFactory(parentFileUrl, (BlogStore.StorageItem oldStoreItem) -> {
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
        List<StorageTreeAttr> list = StorageUtil.getTreeItemList(fileUrl.getPath(), commitHash);
        BlogStore.ReturnCode returnCode = BlogStore.ReturnCode.Return_OK;
        if (list.isEmpty()) {
            // 第一次更新文件
            list.add(new StorageTreeAttr("", "", BlogStore.StorageItem.newBuilder()
                    .setType(BlogStore.StoreTypeEnum.StoreTypeCommit)
                    .setOwner(BlogStore.Operator.newBuilder().setGptype(fileUrl.getGpType()).setGpid(fileUrl.getGpId()).build())
                    .setUpdate(BlogStore.Operator.newBuilder().setGptype(BlogStore.GtypeEnum.User_VALUE).setGpid(fileUrl.getUserId()).build())
                    .setCreateTime(System.currentTimeMillis())
                    .setUpdateTime(System.currentTimeMillis())
                    .setFileName("")
                    .setSize(0)
                    .setContentType(BlogMediaType.DIRECTORY_CONTENTTYPE)
                    .setParent("")
                    .build()));
        }
        //依次向上更新tree
        StorageTreeAttr newChildTreeAttr = null;
        for (int i = list.size() - 1; i >= 0; i--) {
            BlogStore.StorageItem newTree = null;
            if (i == list.size() - 1) {
                newTree = action.perform(list.get(i).getStorageItem());
            } else {
                newTree = StorageTreeAttr.buildFolderNewTree(list.get(i).getStorageItem(), list.get(i + 1).getHash(), list.get(i + 1).getStorageItem().getSize(), newChildTreeAttr.getHash(), newChildTreeAttr.getStorageItem().getSize());
            }
            BlogStore.StoreTypeEnum storeType = BlogStore.StoreTypeEnum.StoreTypeTree;
            if (newTree.getType() == BlogStore.StoreTypeEnum.StoreTypeCommit) {
                newTree = newTree.toBuilder().setParent(list.get(i).getHash()).build();
                storeType = BlogStore.StoreTypeEnum.StoreTypeCommit;
            }
            String treeHash = EncryptUtils.sha1(newTree.toByteArray());
            newChildTreeAttr = new StorageTreeAttr("", treeHash, newTree);
            returnCode = StorageFile.writeStorag(storeType, treeHash, newTree);
            if (returnCode != BlogStore.ReturnCode.Return_OK) {
                break;
            }
        }
        if (returnCode == BlogStore.ReturnCode.Return_OK) {
            RepositoryService.updateCommitHash(fileUrl.getGpType(), fileUrl.getGpId(), newChildTreeAttr.getHash());
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
        List<StorageTreeAttr> storageAttr = StorageUtil.getTreeItemList(fileUrl.getPath(), fileUrl.getRootHash());
        if (storageAttr.isEmpty()) {
            return null;
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
                .setFullPath(fileUrl.getOriginPath())
                .setContentType(storageItem.getContentType())
                .setSize(storageItem.getSize())
                .setCreateTime(storageItem.getCreateTime())
                .setUpdateTime(storageItem.getUpdateTime())
                .build());
        List<BlogStore.FileItem> childFileItems = new ArrayList<>();
        for (String treeHash : childTreeHashList) {
            BlogStore.StorageItem tree = StorageFile.readStorag(BlogStore.StoreTypeEnum.StoreTypeTree, treeHash);
            if (null != tree) {
                childFileItems.add(BlogStore.FileItem.newBuilder()
                        .setFileName(tree.getFileName())
                        .setFullPath(fileUrl.getChild(tree.getFileName()).getOriginPath())
                        .setContentType(tree.getContentType())
                        .setSize(tree.getSize())
                        .setCreateTime(tree.getCreateTime())
                        .setUpdateTime(tree.getUpdateTime())
                        .build());
            }
        }
        childFileItems.sort((o1, o2) -> {
            if (StringUtils.equals(o1.getContentType(), BlogMediaType.DIRECTORY_CONTENTTYPE) && !StringUtils.equals(o2.getContentType(), BlogMediaType.DIRECTORY_CONTENTTYPE)) {
                return -1;
            }
            if (!StringUtils.equals(o1.getContentType(), BlogMediaType.DIRECTORY_CONTENTTYPE) && StringUtils.equals(o2.getContentType(), BlogMediaType.DIRECTORY_CONTENTTYPE)) {
                return 1;
            }
            return o1.getFileName().compareTo(o2.getFileName());
        });
        list.addAllItem(childFileItems);
        return list.build();
    }
}

interface Action {

    // 更新parent的commit或者tree
    public BlogStore.StorageItem perform(BlogStore.StorageItem oldStoreItem);
}
