package com.blog.db;

/**
 * @author zhanghaijun
 */
public interface Repository {

    public String getRootHash();

    public String getName();

    public String getNickname();

    public Integer getStatus();

    public void setRootHash(String rootHash);

    public boolean saveIt();
}
