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
        String fileHash = EncryptUtils.sha1(blobMap.keySet());

        BlogStore.ReturnCode code = BlogStore.ReturnCode.Return_OK;
        for (Map.Entry<String, byte[]> entry : blobMap.entrySet()) {
            if (code != BlogStore.ReturnCode.Return_OK) {
                break;
            }
            BlogStore.StoreBlob blob = BlogStore.StoreBlob.newBuilder()
                    .setCommitter(BlogStore.Operator.newBuilder().setGptype(BlogStore.GtypeEnum.User_VALUE).setGpid(fileUrl.getUserId()).build())
                    .setName(FileUtils.getFileName(fileUrl.getPath()))
                    .setContentType("")
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
//                    .setFileName(FileUtils.getFileName(fileUrl.getPath()))
                    .setSize(fileByte.length)
                    .setContentType("")
                    .addFileItem(BlogStore.StringList.newBuilder().addAllImte(blobMap.keySet()).build())
                    .build();
            String treeHash = EncryptUtils.sha1(fileTree.toByteArray());
            code = StorageFile.writeStorag(BlogStore.StoreTypeEnum.StoreTypeTree, treeHash, fileTree);
            if (code == BlogStore.ReturnCode.Return_OK) {
//                code = StorageFactory.addTreeItem(fileUrl.getParent(), treeHash, fileByte.length);
            }
        }
        return code;
    }

//    public static BlogStore.ReturnCode addTreeItem(FileUrl fileUrl, String treeHash, long treeSize) {
//        return StorageFactory.StorageFactory(fileUrl, (BlogStore.StorageItem oldStoreItem) -> {
//            return null;
//        });
//    }

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
            newCommit = action.perform(BlogStore.StorageItem.getDefaultInstance());
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
                for (int i = list.size() - 1; i > 0; i++) {
                    if (returnCode != BlogStore.ReturnCode.Return_OK) {
                        break;
                    }
                    currentTreeAttr = list.get(i);
                    if (i == list.size() - 1) {
                        newTree = action.perform(currentTreeAttr.getStorageItem());
                    } else {
                        newTree = list.get(i).buildNewTree(currentTreeAttr.getHash(), treeHash);
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
                newCommit = BlogStore.StorageItem.newBuilder()
                        .setParent(commitHash)
                        .addAllTreeHashItem(newTreeHashList)
                        .setCreateTime(commit.getCreateTime())
                        .setUpdateTime(commit.getUpdateTime())
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

    public static BlogStore.FileItemList getFileItemList(FileUrl fileUrl) {
        BlogStore.FileItemList.Builder list = BlogStore.FileItemList.newBuilder();
        BlogStore.StorageItem commit = StorageFile.readStorag(BlogStore.StoreTypeEnum.StoreTypeCommit, fileUrl.getRootHash());
        if (null == commit) {
            return list.build();
        }
        List<StorageTreeAttr> storageTreeAttr = StorageUtil.getTreeItemList(fileUrl.getPath(), commit);
        BlogStore.FileItem prentFile;
        List<String> childTreeHashList;
        if (storageTreeAttr.isEmpty()) {
            childTreeHashList = commit.getTreeHashItemList();
            prentFile = BlogStore.FileItem.newBuilder()
                    .setFileName("")
                    .setContentType(BlogMediaType.DIRECTORY_CONTENTTYPE)
                    .setSize(commit.getSize())
                    .setCreateTime(commit.getCreateTime())
                    .setUpdateTime(commit.getUpdateTime())
                    .build();
        } else {
            StorageTreeAttr prentTreeArr = storageTreeAttr.get(storageTreeAttr.size() - 1);
            childTreeHashList = prentTreeArr.isFolder() ? prentTreeArr.getStorageItem().getTreeHashItemList() : new ArrayList<>();
            prentFile = BlogStore.FileItem.newBuilder()
                    .setFileName(prentTreeArr.getStorageItem().getFileName())
                    .setContentType(prentTreeArr.getStorageItem().getContentType())
                    .setSize(prentTreeArr.getStorageItem().getSize())
                    .setCreateTime(prentTreeArr.getStorageItem().getCreateTime())
                    .setUpdateTime(prentTreeArr.getStorageItem().getUpdateTime())
                    .build();
        }
        list.setParentFile(prentFile);
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
