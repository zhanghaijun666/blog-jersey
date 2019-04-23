package com.blog.file;

import com.blog.config.Configuration;
import com.blog.proto.BlogStore;
import com.tools.BasicConvertUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author zhanghaijun
 */
public class StorageFile {

    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(StorageFile.class);

    private static final String BLOB_DIR = "blob";
    private static final String COMMIT_DIR = "commit";
    private static final String TREE_DIR = "tree";
    private static final int FILE_HEADER_MESSAGE_LENGTH = 10;

    public static com.google.protobuf.Message readStorag(BlogStore.StoreTypeEnum type, String hash) {
        com.google.protobuf.Message message = null;
        String fileFullPath = StorageFile.getHashFullPath(type, hash);
        File file = new File(fileFullPath);
        FileInputStream input = null;
        if (file.exists() && file.isFile() && file.canRead()) {
            try {
                input = new FileInputStream(file);
                int available = input.available();
                byte[] messageLengByte = new byte[Math.min(StorageFile.FILE_HEADER_MESSAGE_LENGTH, available)];
                input.read(messageLengByte, 0, messageLengByte.length);
                int messageLength = BasicConvertUtils.byteArrayToInt(messageLengByte);
                byte[] databuf = new byte[Math.min(messageLength, available - messageLengByte.length)];
                input.read(databuf, messageLengByte.length, messageLengByte.length + databuf.length);
                switch (type) {
                    case StoreTypeCommit:
                        message = BlogStore.StoreCommit.parseFrom(databuf);
                        break;
                    case StoreTypeTree:
                        message = BlogStore.StoreTree.parseFrom(databuf);
                        break;
                    case StoreTypeBlob:
                        message = BlogStore.StoreBlob.parseFrom(databuf);
                        break;
                    default:
                        message = null;
                }
            } catch (FileNotFoundException ex) {
                logger.error("Unable to open " + fileFullPath, ex);
            } catch (IOException ex) {
                logger.error("Unable to open " + fileFullPath, ex);
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException ex) {
                    }
                }
            }
        }
        return message;
    }

    private static BlogStore.ReturnCode writeStorag(byte[] byteArray, File file) {
        if (file.exists()) {
            return BlogStore.ReturnCode.Return_OK;
        }
        file.getParentFile().mkdirs();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(byteArray);
        } catch (FileNotFoundException ex) {
            logger.error("Unable to write {}", ex);
            return BlogStore.ReturnCode.Return_ERROR;
        } catch (IOException ex) {
            logger.error("Unable to write {}", ex);
            return BlogStore.ReturnCode.Return_ERROR;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                }
            }
        }
        return BlogStore.ReturnCode.Return_OK;
    }

    public static BlogStore.ReturnCode writeStorag(BlogStore.StoreTypeEnum type, String hash, com.google.protobuf.Message message) {
        return StorageFile.writeStorag(type, hash, message, null);
    }

    public static BlogStore.ReturnCode writeStorag(BlogStore.StoreTypeEnum type, String hash, com.google.protobuf.Message message, byte[] blob) {
        byte[] newBuff = new byte[StorageFile.FILE_HEADER_MESSAGE_LENGTH];
        byte[] messageLengByte = BasicConvertUtils.intToByteArray(message.toByteArray().length);
        System.arraycopy(messageLengByte, 0, newBuff, 0, Math.min(newBuff.length, messageLengByte.length));
        return StorageFile.writeStorag(ArrayUtils.addAll(ArrayUtils.addAll(newBuff, message.toByteArray()), blob), new File(StorageFile.getHashFullPath(type, hash)));
    }

    private static String getHashFullPath(BlogStore.StoreTypeEnum type, String hash) {
        String store = "";
        switch (type) {
            case StoreTypeCommit:
                store = StorageFile.COMMIT_DIR;
                break;
            case StoreTypeTree:
                store = StorageFile.TREE_DIR;
                break;
            case StoreTypeBlob:
                store = StorageFile.BLOB_DIR;
                break;
        }
        return new File(Configuration.getInstance().getFileStore(), store + File.separator + StorageFile.getHashPath(hash)).getAbsolutePath();
    }

    public static String getHashPath(String hash) {
        if (StringUtils.isBlank(hash) || hash.length() < 5) {
            return hash;
        }
        return StringUtils.join(Arrays.asList(hash.substring(0, 2), hash.substring(hash.length() - 4), hash), File.separator);
    }

    public static BlogStore.StoreTree getStoreItem(FileUrl fileUrl) {
        if (StringUtils.isBlank(fileUrl.getRootHash()) || StringUtils.equals(fileUrl.getRootHash(), FileUrl.DEFAULT_ROOT_HASH)) {
            return null;
        }
        List<String> dirs = Arrays.asList(fileUrl.getPath().split("/"));
        BlogStore.StoreCommit commit = (BlogStore.StoreCommit) StorageFile.readStorag(BlogStore.StoreTypeEnum.StoreTypeCommit, fileUrl.getRootHash());
        BlogStore.StoreTree tree = (BlogStore.StoreTree) StorageFile.readStorag(BlogStore.StoreTypeEnum.StoreTypeTree, commit.getTreeHash());

        int index = 0;
        while (tree != null && index < dirs.size()) {
            String pathName = dirs.get(index);
            if (StringUtils.isBlank(pathName)) {
                index++;
                continue;
            }
            for (BlogStore.StoreTree item : tree.getTreeItemList()) {
                if (pathName.equals(item.getName()) && StoreUtil.isFolder(item)) {
                    tree = (BlogStore.StoreTree) StorageFile.readStorag(BlogStore.StoreTypeEnum.StoreTypeTree, item.getHash());
                    break;
                }
            }
            index++;
        }
        return tree;
    }
}
