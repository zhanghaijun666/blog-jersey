package com.blog.service;

import com.blog.service.file.FileUrl;
import com.blog.service.file.StorageFactory;
import com.blog.service.file.StorageFile;
import com.blog.service.file.StorageTreeAttr;
import com.blog.proto.BlogStore;
import com.blog.utils.BlogMediaType;
import com.blog.utils.FileUtils;
import com.tools.EncryptUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.simpleframework.http.Response;

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
            BlogStore.StoreBlob blob = BlogStore.StoreBlob.newBuilder()
                    .setCommitter(BlogStore.Operator.newBuilder().setGptype(BlogStore.GtypeEnum.User_VALUE).setGpid(fileUrl.getUserId()).build())
                    .setName(FileUtils.getFileName(fileUrl.getPath()))
                    .setContentType(contentType)
                    .setSize(entry.getValue().length)
                    .setCreateTime(System.currentTimeMillis())
                    .build();
            code = StorageFile.writeStorag(BlogStore.StoreTypeEnum.StoreTypeFile, entry.getKey(), blob, entry.getValue());
            if (code != BlogStore.ReturnCode.Return_OK) {
                break;
            }
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
                    .addAllBlobHashItem(blobMap.keySet())
                    .build();
            String treeHash = EncryptUtils.sha1(fileTree.toByteArray());
            code = StorageFile.writeStorag(BlogStore.StoreTypeEnum.StoreTypeTree, treeHash, fileTree);
            if (code == BlogStore.ReturnCode.Return_OK) {
                code = StorageFactory.addTreeItem(fileUrl.getParent(), treeHash, fileByte.length);
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
        if (StringUtils.isBlank(newFileName) && StringUtils.equals(fileUrl.getFileName(), newFileName)) {
            return BlogStore.ReturnCode.Return_ERROR;
        }
        StorageTreeAttr storageAttr = StorageFactory.getStorage(fileUrl);
        if (null == storageAttr) {
            return BlogStore.ReturnCode.Return_ERROR;
        }
        BlogStore.StorageItem storage = storageAttr.getStorageItem();
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
        if (null == storageAttr) {
            return BlogStore.ReturnCode.Return_ERROR;
        }
        BlogStore.StorageItem storage = storageAttr.getStorageItem();
        return StorageFactory.deleteTreeItem(fileUrl, storageAttr.getHash(), storage.getSize());
    }

    public static BlogStore.ReturnCode addFolder(FileUrl parentFileUrl, String folderName) {
        if (StringUtils.isBlank(folderName)) {
            return BlogStore.ReturnCode.Return_ERROR;
        }
        StorageTreeAttr storageAttr = StorageFactory.getStorage(parentFileUrl);
        if (null == storageAttr) {
            return BlogStore.ReturnCode.Return_ERROR;
        }
        BlogStore.StorageItem newFolderTree = BlogStore.StorageItem.newBuilder()
                .setType(BlogStore.StoreTypeEnum.StoreTypeTree)
                .setOwner(BlogStore.Operator.newBuilder().setGptype(parentFileUrl.getGpType()).setGpid(parentFileUrl.getGpId()).build())
                .setUpdate(BlogStore.Operator.newBuilder().setGptype(BlogStore.GtypeEnum.User_VALUE).setGpid(parentFileUrl.getUserId()).build())
                .setCreateTime(System.currentTimeMillis())
                .setUpdateTime(System.currentTimeMillis())
                .setFileName(folderName)
                .setSize(0)
                .setContentType(BlogMediaType.DIRECTORY_CONTENTTYPE)
                .build();
        String treeHash = EncryptUtils.sha1(newFolderTree.toByteArray());
        BlogStore.ReturnCode code = StorageFile.writeStorag(BlogStore.StoreTypeEnum.StoreTypeTree, treeHash, newFolderTree);
        if (code == BlogStore.ReturnCode.Return_OK) {
            code = StorageFactory.addTreeItem(parentFileUrl, treeHash, newFolderTree.getSize());
        }
        return code;
    }

    public static void downloadFile(FileUrl fileUrl, Response response) throws IOException {
        StorageTreeAttr storageAttr = StorageFactory.getStorage(fileUrl);
        byte[] fileByte = StorageFile.readFile(storageAttr);
        if (null == fileByte) {
            return;
        }
//        response.setContentType(storageAttr.getStorageItem().getContentType());
//        response.setDate("Last-Modified", System.currentTimeMillis());
//        response.setValue("Accept-Ranges", "bytes");
//        response.setValue("Content-Encoding", "UTF-8");
//        response.setValue("Cache-Control", "must-revalidate,max-age=0");
//        response.setContentType("multipart/form-data");
//        response.setHeader("Content-Disposition", "attachment;fileName=" + FileUtils.getFileName(fileUrl.getPath()));
//        response.setContentType("text/html;charset=UTF-8");
//        response.setStatus(Status.OK);
//        response.setValue("Server-Time", BasicConvertUtils.toString(System.currentTimeMillis(), ""));

//        response.reset();
        response.addValue("Content-Disposition", "attachment;filename=" + URLEncoder.encode(FileUtils.getFileName(fileUrl.getPath()), "utf-8"));
        response.addValue("Content-Length", "" + fileByte.length);
        response.setContentType(storageAttr.getStorageItem().getContentType());

        try (OutputStream out = response.getOutputStream()) {
            out.write(fileByte);
            out.flush();
        }
    }

}
