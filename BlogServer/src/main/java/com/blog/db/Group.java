package com.blog.db;

import org.javalite.activejdbc.annotations.Table;

/**
 * @author zhanghaijun
 */
@Table("group")
public class Group extends org.javalite.activejdbc.Model implements CommonModel, Repository {

    private static final long serialVersionUID = 1L;

    @Override
    public String getRootHash() {
        return getString("root_hash");
    }

    @Override
    public void setRootHash(String rootHash) {
        setString("root_hash", rootHash);
    }

    @Override
    public String getName() {
        return getString("name");
    }

    @Override
    public String getNickname() {
        return this.getName();
    }

    @Override
    public Integer getStatus() {
        return getInteger("status");
    }

}
