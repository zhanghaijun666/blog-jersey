package com.blog.store;

/**
 * @author zhanghaijun
 */
public class UserStore {

    private final static org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(UserStore.class);

    public FileUrl url;
    public String ip;

    public UserStore(FileUrl url, String ip) {
        this.ip = ip;
        this.url = url;
    }
}
