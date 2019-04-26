package com.blog.service;

import com.blog.file.FileUrl;
import com.blog.file.StorageFactory;
import com.blog.file.StorageFile;
import com.blog.file.StorageTreeAttr;
import com.blog.proto.BlogStore;
import com.blog.utils.FileUtils;
import com.tools.EncryptUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

/**
 * @author zhanghaijun
 */
public class FileService {

    public static final int MAX_BLOB_SIZE = 32 * 1024 * 1024;

    /**
     *
     * @param fileUrl
     * @param inputStream
     * @return 单文件上传
     * @throws IOException
     */
    public static BlogStore.ReturnCode UploadFile(FileUrl fileUrl, InputStream inputStream) throws IOException {
        byte[] fileByte = FileUtils.toByteArray(inputStream);
        List<byte[]> blobList = FileUtils.SplitList(fileByte, FileService.MAX_BLOB_SIZE);
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

    /**
     *
     * @param fileUrl
     * @param newFileName
     * @return 文件或文件夹重命名
     */
    public static BlogStore.ReturnCode renameFile(FileUrl fileUrl, String newFileName) {
        if (StringUtils.isBlank(newFileName)) {
            return BlogStore.ReturnCode.Return_ERROR;
        }
        StorageTreeAttr storageAttr = StorageFactory.getStorage(fileUrl);
        BlogStore.StorageItem storage = storageAttr.getStorageItem();
        if (storage.getType() != BlogStore.StoreTypeEnum.StoreTypeTree && storage.getType() != BlogStore.StoreTypeEnum.StoreTypeFile) {
            return BlogStore.ReturnCode.Return_ERROR;
        }
        BlogStore.StorageItem newStorage = BlogStore.StorageItem.newBuilder(storage)
                .setFileName(newFileName)
                .setUpdate(BlogStore.Operator.newBuilder().setGptype(BlogStore.GtypeEnum.User_VALUE).setGpid(fileUrl.getUserId()).build())
                .build();
        String treeHash = EncryptUtils.sha1(newStorage.toByteArray());
        BlogStore.ReturnCode code = StorageFile.writeStorag(BlogStore.StoreTypeEnum.StoreTypeTree, treeHash, newStorage);
        if (code == BlogStore.ReturnCode.Return_OK) {
            code = StorageFactory.renameTreeItem(fileUrl, storageAttr.getHash(), storage.getSize(), treeHash, storage.getSize());
        }
        return code;
    }

    /**
     * @param fileUrl
     * @return 删除文件或者文件夹
     */
    public static BlogStore.ReturnCode deldeteFile(FileUrl fileUrl) {
        StorageTreeAttr storageAttr = StorageFactory.getStorage(fileUrl);
        BlogStore.StorageItem storage = storageAttr.getStorageItem();
        if (storage.getType() != BlogStore.StoreTypeEnum.StoreTypeTree && storage.getType() != BlogStore.StoreTypeEnum.StoreTypeFile) {
            return BlogStore.ReturnCode.Return_ERROR;
        }
        return StorageFactory.deleteTreeItem(fileUrl, storageAttr.getHash(), storage.getSize());
    }
}
