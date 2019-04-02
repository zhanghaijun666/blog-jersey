package com.blog.file;

import com.blog.config.Configuration;
import com.blog.proto.BlogStore;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author zhanghaijun
 */
public class StorageFile {

    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(StorageFile.class);

    private static final String BLOB_DIR = "blob";
    private static final String COMMIT_DIR = "commit";
    private static final String TREE_DIR = "tree";

    public static com.google.protobuf.Message readStorag(BlogStore.StoreTypeEnum type, String hash) {
        com.google.protobuf.Message message = null;
        String fileFullPath = StorageFile.getHashFullPath(type, hash);
        File file = new File(fileFullPath);
        FileInputStream input = null;
        if (file.exists() && file.isFile() && file.canRead()) {
            try {
                input = new FileInputStream(file);
                byte[] databuf = new byte[input.available()];
                input.read(databuf);
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

    public static BlogStore.ReturnCode writeStorag(BlogStore.StoreTypeEnum type, com.google.protobuf.Message message, String hash) {
        String fileFullPath = StorageFile.getHashFullPath(type, hash);
        File file = new File(fileFullPath);
        FileOutputStream out = null;
        if (file.getParentFile().mkdirs()) {
            try {
                out = new FileOutputStream(file);
                out.write(message.toByteArray());
            } catch (FileNotFoundException ex) {
                logger.error("Unable to write " + fileFullPath, ex);
                return BlogStore.ReturnCode.Return_ERROR;
            } catch (IOException ex) {
                logger.error("Unable to write " + fileFullPath, ex);
                return BlogStore.ReturnCode.Return_ERROR;
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException ex) {
                    }
                }
            }
        }
        return BlogStore.ReturnCode.Return_OK;
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
        return new File(Configuration.getInstance().getFileStore(), store + File.separator + StoreUtil.getHashPath(hash)).getAbsolutePath();
    }
}
