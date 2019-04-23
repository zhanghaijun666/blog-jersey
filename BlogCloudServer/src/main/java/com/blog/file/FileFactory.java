package com.blog.file;

import com.blog.proto.BlogStore;
import com.tools.EncryptUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

/**
 * @author zhanghaijun
 */
public class FileFactory {

    public static final int MAX_BLOB_SIZE = 32 * 1024 * 1024;

    public static BlogStore.ReturnCode UploadFile(FileUrl fileUrl, InputStream inputStream) throws IOException {
        List<byte[]> blobList = FileUtil.SplitList(FileUtil.toByteArray(inputStream), FileFactory.MAX_BLOB_SIZE);
        Map<String, byte[]> blobMap = new HashMap<>();
        for (int i = 0; i < blobList.size(); i++) {
            blobMap.put(EncryptUtils.sha1(blobList.get(i)), blobList.get(i));
        }
        String fileHash = EncryptUtils.sha1(blobMap.keySet());
        BlogStore.StoreBlob.Builder blob = BlogStore.StoreBlob.newBuilder();
        BlogStore.ReturnCode code = BlogStore.ReturnCode.Return_OK;
        for (Map.Entry<String, byte[]> entry : blobMap.entrySet()) {
            if (code != BlogStore.ReturnCode.Return_OK) {
                break;
            }
            code = StorageFile.writeStorag(BlogStore.StoreTypeEnum.StoreTypeBlob, entry.getKey(), blob.build(), entry.getValue());
        }
        if (code == BlogStore.ReturnCode.Return_OK) {
            String rootHash = fileUrl.getRootHash();
            if (StringUtils.isBlank(rootHash)) {

            } else {
                StorageFile.getStoreItem(fileUrl);
            }

        }

        return code;
    }

    private static void updataStore(FileUrl fileUrl) {
        if (StringUtils.isBlank(fileUrl.getRootHash()) || StringUtils.equals(fileUrl.getRootHash(), FileUrl.DEFAULT_ROOT_HASH)) {
            return;
        }
        List<String> dirs = Arrays.asList(fileUrl.getPath().split("/"));
        BlogStore.StoreCommit commit = (BlogStore.StoreCommit) StorageFile.readStorag(BlogStore.StoreTypeEnum.StoreTypeCommit, fileUrl.getRootHash());
        Map<String, BlogStore.StoreTree> treeMap = new HashMap<>();

        int index = 0;
        String treeHash = commit.getTreeHash();
        BlogStore.StoreTree tree = null;
        do {
            tree = (BlogStore.StoreTree) StorageFile.readStorag(BlogStore.StoreTypeEnum.StoreTypeTree, treeHash);

            String pathName = dirs.get(index);
            if (tree != null) {
                treeMap.put(tree.getName(), tree);
                treeHash = "";
                for (BlogStore.StoreTree item : tree.getTreeItemList()) {
                    if (pathName.equals(item.getName()) && StoreUtil.isFolder(item)) {
                        treeHash = item.getHash();
                    }
                }
            }
        } while (StringUtils.isNotBlank(treeHash) && tree != null);
    }

}
