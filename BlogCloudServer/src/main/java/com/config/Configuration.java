package com.config;

import com.googlecode.protobuf.format.JsonFormat;
import com.proto.ConfigStore;
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
    private ConfigStore.Config config;

    private Configuration() {

    }

    public static Configuration getInstance() {
        return BLOG_CONFIG;
    }

    public ConfigStore.Config getConfig() {
        return config;
    }

    private File webDir = null;

    public File getWebDir() {
        return webDir;
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
    }

    private ConfigStore.Config.Builder readConfig() throws FileNotFoundException, IOException {
        File configFile = new File("config/config.json");
        JsonFormat jsonFormat = new JsonFormat();
        ConfigStore.Config.Builder configBuilder = ConfigStore.Config.newBuilder();
        jsonFormat.merge(new FileInputStream(configFile), configBuilder);
        return configBuilder;
    }
}
