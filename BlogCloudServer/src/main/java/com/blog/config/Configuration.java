package com.blog.config;

import com.blog.proto.ConfigStore;
import com.googlecode.protobuf.format.JsonFormat;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;

/**
 * @author zhanghaijun
 */
public class Configuration {

    private final static Configuration BLOG_CONFIG = new Configuration();
    private ConfigStore.Config config = null;
    private File webDir = null;
    private File fileStore = null;

    private Configuration() {
    }

    public static Configuration getInstance() {
        return BLOG_CONFIG;
    }

    public ConfigStore.Config getConfig() {
        return config;
    }

    public File getWebDir() {
        return webDir;
    }

    public File getFileStore() {
        return fileStore;
    }

    public String getWebDir(String path) {
        String dir = getConfig().getWebDir();
        if (dir.endsWith("/")) {
            return webDir + path;
        } else {
            return dir + "/" + path;
        }
    }

    public long getSessionTimeoutMills() {
        return config.getSessionTimeout() * 1000;
    }

    public String getSalt() {
        return StringUtils.isNoneEmpty(config.getSalt()) ? config.getSalt() : "dinner@TY";
    }

    public void loadConfig() throws IOException {
        config = readConfig().build();
        webDir = new File(config.getWebDir());
        fileStore = new File(Configuration.getInstance().getConfig().getFileStorage().getUrl());
    }

    private ConfigStore.Config.Builder readConfig() throws FileNotFoundException, IOException {
        File configFile = new File("config/config.json");
        JsonFormat jsonFormat = new JsonFormat();
        ConfigStore.Config.Builder configBuilder = ConfigStore.Config.newBuilder();
        jsonFormat.merge(new FileInputStream(configFile), configBuilder);
        return configBuilder;
    }
}
