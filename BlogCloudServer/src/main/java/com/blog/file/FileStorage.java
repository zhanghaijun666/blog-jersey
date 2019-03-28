package com.blog.file;

import com.blog.proto.BlogStore;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author haijun.zhang
 */
public class FileStorage {

    public final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(FileStorage.class);

    public static BlogStore.Store readStore(BlogStore.StoreTypeEnum type, String hash) {
        BlogStore.Store store = null;
        String fileFullPath = StoreUtil.getHashFullPath(type, hash);
        File file = new File(fileFullPath);
        FileInputStream input = null;
        if (file.exists() && file.isFile() && file.canRead()) {
            try {
                input = new FileInputStream(file);
                byte[] databuf = new byte[input.available()];
                input.read(databuf);
                store = BlogStore.Store.parseFrom(databuf);
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
        return store;
    }

    public static BlogStore.ReturnCode writeStore(BlogStore.Store store, String hash) {
        String fileFullPath = StoreUtil.getHashFullPath(store.getStoreType(), hash);
        File file = new File(fileFullPath);
        FileOutputStream out = null;
        if (file.getParentFile().mkdirs()) {
            try {
                out = new FileOutputStream(file);
                out.write(store.toByteArray());
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
}
