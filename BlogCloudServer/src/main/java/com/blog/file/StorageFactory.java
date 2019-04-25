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
            BlogStore.StoreBlob.Builder blob = BlogStore.StoreBlob.newBuilder();
            blob.setCommitter(BlogStore.Operator.newBuilder().setDate(System.currentTimeMillis()).setGptype(BlogStore.GtypeEnum.User_VALUE).setGpid(fileUrl.getUserId()).build());
            blob.setName(FileUtils.getFileName(fileUrl.getPath()));
            blob.setContentType("");
            blob.setSize(entry.getValue().length);
            code = StorageFile.writeStorag(BlogStore.StoreTypeEnum.StoreTypeBlob, entry.getKey(), blob.build(), entry.getValue());
        }
        if (code == BlogStore.ReturnCode.Return_OK) {
            BlogStore.StoreTree fileTree = BlogStore.StoreTree.newBuilder()
                    .setCommitter(BlogStore.Operator.newBuilder().setGptype(BlogStore.GtypeEnum.User_VALUE).setGpid(fileUrl.getUserId()).setDate(System.currentTimeMillis()).build())
                    .setOwner(BlogStore.Operator.newBuilder().setGptype(fileUrl.getGpType()).setGpid(fileUrl.getGpId()).setDate(System.currentTimeMillis()).build())
                    .setName(FileUtils.getFileName(fileUrl.getPath()))
                    .setSize(fileByte.length)
                    .setContentType("")
                    .setCreateTime(System.currentTimeMillis())
                    .setUpdateTime(System.currentTimeMillis())
                    .addFileItem(BlogStore.StringList.newBuilder().addAllImte(blobMap.keySet()).build())
                    .build();
            String treeHash = EncryptUtils.sha1(fileTree.toByteArray());
            code = StorageFile.writeStorag(BlogStore.StoreTypeEnum.StoreTypeTree, treeHash, fileTree);
            if (code == BlogStore.ReturnCode.Return_OK) {
                code = StorageFactory.addTreeItem(fileUrl.getParent(), treeHash, fileByte.length);
            }
        }
        return code;
    }

    public static BlogStore.ReturnCode addTreeItem(FileUrl fileUrl, String treeHash, long treeSize) {
        return StorageFactory.StorageFactory(fileUrl, (BlogStore.StoreTree oldStoreTree) -> {
            BlogStore.StoreTree.Builder storeTree = BlogStore.StoreTree.newBuilder(oldStoreTree);
            storeTree.addTreeHashItem(treeHash);
            storeTree.setSize(oldStoreTree.getSize() + treeSize);
            return storeTree;
        });
    }

    public static BlogStore.ReturnCode addFolder(FileUrl fileUrl, String fileName) {
        BlogStore.StoreTree fileTree = BlogStore.StoreTree.newBuilder()
                .setCommitter(BlogStore.Operator.newBuilder().setGptype(BlogStore.GtypeEnum.User_VALUE).setGpid(fileUrl.getUserId()).setDate(System.currentTimeMillis()).build())
                .setOwner(BlogStore.Operator.newBuilder().setGptype(fileUrl.getGpType()).setGpid(fileUrl.getGpId()).setDate(System.currentTimeMillis()).build())
                .setName(fileName)
                .setSize(0)
                .setContentType(BlogMediaType.DIRECTORY_CONTENTTYPE)
                .setCreateTime(System.currentTimeMillis())
                .setUpdateTime(System.currentTimeMillis())
                .build();
        String treeHash = EncryptUtils.sha1(fileTree.toByteArray());
        BlogStore.ReturnCode code = StorageFile.writeStorag(BlogStore.StoreTypeEnum.StoreTypeTree, treeHash, fileTree);
        if (code == BlogStore.ReturnCode.Return_OK) {
            return StorageFactory.addTreeItem(fileUrl.getParent(), treeHash, 0);
        }
        return BlogStore.ReturnCode.Return_ERROR;

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
            list = StorageUtil.getTreeItemList(fileUrl.getPath(), commit);
        }
        BlogStore.ReturnCode code = BlogStore.ReturnCode.Return_OK;
        if (list.size() > 1) {

        }

        StorageTreeAttr currentTreeAttr = list.remove(list.size() - 1);
        //  对当前的tree做增删改查操作，改变当前的treeItem
        BlogStore.StoreTree newTree = action.perform(currentTreeAttr.getTree()).setUpdateTime(System.currentTimeMillis()).build();
        String treeHash = EncryptUtils.sha1(newTree.toByteArray());
        code = StorageFile.writeStorag(BlogStore.StoreTypeEnum.StoreTypeTree, treeHash, newTree);
        for (int i = list.size() - 1; i > 0; i++) {
            if (code != BlogStore.ReturnCode.Return_OK) {
                break;
            }
            newTree = list.get(i).buildNewTree(currentTreeAttr.getHash(), treeHash);
            treeHash = EncryptUtils.sha1(newTree.toByteArray());
            currentTreeAttr = list.get(i);
            code = StorageFile.writeStorag(BlogStore.StoreTypeEnum.StoreTypeTree, treeHash, newTree);
        }
        if (code == BlogStore.ReturnCode.Return_OK) {
            BlogStore.StoreCommit.Builder newCommit;
            if (null == commit) {
                newCommit = BlogStore.StoreCommit.newBuilder();
                newCommit.addTreeHashItem(treeHash);
            } else {
                List<String> oldTreeHashList = commit.getTreeHashItemList();
                newCommit = BlogStore.StoreCommit.newBuilder(commit);
                newCommit.getTreeHashItemList().clear();
                for (String hash : oldTreeHashList) {
                    if (StringUtils.equals(hash, currentTreeAttr.getHash())) {
                        newCommit.addTreeHashItem(currentTreeAttr.getHash());
                    } else {
                        newCommit.addTreeHashItem(hash);
                    }
                }
            }
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
        BlogStore.StoreCommit commit = (BlogStore.StoreCommit) StorageFile.readStorag(BlogStore.StoreTypeEnum.StoreTypeCommit, fileUrl.getRootHash());
        if (null == commit) {
            return list.build();
        }
        List<StorageTreeAttr> storageTreeAttr = StorageUtil.getTreeItemList(fileUrl.getPath(), commit);
        BlogStore.FileItem prentFile;
        List<String> childTreeHashList;
        if (storageTreeAttr.isEmpty()) {
            childTreeHashList = commit.getTreeHashItemList();
            prentFile = BlogStore.FileItem.newBuilder()
                    .setName("")
                    .setContentType(BlogMediaType.DIRECTORY_CONTENTTYPE)
                    .setSize(commit.getSize())
                    .setCreateTime(commit.getCreateTime())
                    .setUpdateTime(commit.getUpdateTime())
                    .build();
        } else {
            StorageTreeAttr prentTreeArr = storageTreeAttr.get(storageTreeAttr.size() - 1);
            childTreeHashList = prentTreeArr.isFolder() ? prentTreeArr.getTree().getTreeHashItemList() : new ArrayList<>();
            prentFile = BlogStore.FileItem.newBuilder()
                    .setName(prentTreeArr.getTree().getName())
                    .setContentType(prentTreeArr.getTree().getContentType())
                    .setSize(prentTreeArr.getTree().getSize())
                    .setCreateTime(prentTreeArr.getTree().getCreateTime())
                    .setUpdateTime(prentTreeArr.getTree().getUpdateTime())
                    .build();
        }
        list.setParentFile(prentFile);
        for (String treeHash : childTreeHashList) {
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
        return list.build();
    }
}

interface Action {

    public BlogStore.StoreTree.Builder perform(BlogStore.StoreTree oldStoreTree);
}
