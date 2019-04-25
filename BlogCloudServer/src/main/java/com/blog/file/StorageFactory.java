package com.blog.file;

import com.blog.proto.BlogStore;
import com.blog.service.RepositoryService;
import com.blog.utils.BlogMediaType;
import com.blog.utils.FileUtils;
import com.tools.EncryptUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

/**
 * @author zhanghaijun
 */
public class StorageFactory {

    public static final int MAX_BLOB_SIZE = 32 * 1024 * 1024;

    public static BlogStore.ReturnCode UploadFile(FileUrl fileUrl, InputStream inputStream) throws IOException {
        byte[] fileByte = FileUtils.toByteArray(inputStream);
        List<byte[]> blobList = FileUtils.SplitList(fileByte, StorageFactory.MAX_BLOB_SIZE);
        Map<String, byte[]> blobMap = new HashMap<>();
        for (int i = 0; i < blobList.size(); i++) {
            blobMap.put(EncryptUtils.sha1(blobList.get(i)), blobList.get(i));
        }
        String contentType = FileUtils.getFileContentType(fileUrl.getPath());
//        整个文件的哈市值
//        String fileHash = EncryptUtils.sha1(blobMap.keySet());

        BlogStore.ReturnCode code = BlogStore.ReturnCode.Return_OK;
        for (Map.Entry<String, byte[]> entry : blobMap.entrySet()) {
            if (code != BlogStore.ReturnCode.Return_OK) {
                break;
            }
            BlogStore.StoreBlob blob = BlogStore.StoreBlob.newBuilder()
                    .setCommitter(BlogStore.Operator.newBuilder().setGptype(BlogStore.GtypeEnum.User_VALUE).setGpid(fileUrl.getUserId()).build())
                    .setName(FileUtils.getFileName(fileUrl.getPath()))
                    .setContentType(contentType)
                    .setSize(entry.getValue().length)
                    .setCreateTime(System.currentTimeMillis())
                    .build();
            code = StorageFile.writeStorag(BlogStore.StoreTypeEnum.StoreTypeFile, entry.getKey(), blob, entry.getValue());
        }
        if (code == BlogStore.ReturnCode.Return_OK) {
            BlogStore.StorageItem fileTree = BlogStore.StorageItem.newBuilder()
                    .setType(BlogStore.StoreTypeEnum.StoreTypeFile)
                    .setOwner(BlogStore.Operator.newBuilder().setGptype(fileUrl.getGpType()).setGpid(fileUrl.getGpId()).build())
                    .setUpdate(BlogStore.Operator.newBuilder().setGptype(BlogStore.GtypeEnum.User_VALUE).setGpid(fileUrl.getUserId()).build())
                    .setCreateTime(System.currentTimeMillis())
                    .setUpdateTime(System.currentTimeMillis())
                    .setFileName(FileUtils.getFileName(fileUrl.getPath()))
                    .setSize(fileByte.length)
                    .setContentType(contentType)
                    .addFileItem(BlogStore.StringList.newBuilder().addAllImte(blobMap.keySet()).build())
                    .build();
            String treeHash = EncryptUtils.sha1(fileTree.toByteArray());
            code = StorageFile.writeStorag(BlogStore.StoreTypeEnum.StoreTypeTree, treeHash, fileTree);
            if (code == BlogStore.ReturnCode.Return_OK) {
                code = StorageFactory.addTreeItem(fileUrl, treeHash, fileByte.length);
            }
        }
        return code;
    }

    public static BlogStore.ReturnCode addTreeItem(FileUrl fileUrl, String treeHash, long treeSize) {
        return StorageFactory.StorageFactory(fileUrl.getParent(), (BlogStore.StorageItem oldStoreItem) -> {
            return StorageTreeAttr.buildFolderNewTree(oldStoreItem, "", 0, treeHash, treeSize);
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
    public static BlogStore.StorageItem getStorage(FileUrl fileUrl) {
        BlogStore.StorageItem commit = StorageFile.readStorag(BlogStore.StoreTypeEnum.StoreTypeCommit, fileUrl.getRootHash());
        if (null == commit) {
            return null;
        }
        List<StorageTreeAttr> storageAttr = StorageUtil.getTreeItemList(fileUrl.getPath(), commit);
        if (storageAttr.isEmpty()) {
            return commit;
        } else {
            return storageAttr.get(storageAttr.size() - 1).getStorageItem();
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
        BlogStore.StorageItem prentStorageArr = StorageFactory.getStorage(fileUrl);
        if (null == prentStorageArr) {
            return list.build();
        }
        List<String> childTreeHashList = FileUtils.isFolder(prentStorageArr.getContentType()) ? prentStorageArr.getTreeHashItemList() : new ArrayList<>();
        list.setParentFile(BlogStore.FileItem.newBuilder()
                .setFileName(prentStorageArr.getFileName())
                .setContentType(prentStorageArr.getContentType())
                .setSize(prentStorageArr.getSize())
                .setCreateTime(prentStorageArr.getCreateTime())
                .setUpdateTime(prentStorageArr.getUpdateTime())
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
