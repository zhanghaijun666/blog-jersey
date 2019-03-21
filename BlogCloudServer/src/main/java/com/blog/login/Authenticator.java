package com.blog.login;

import com.blog.db.User;
import com.blog.proto.BlogStore;

/**
 * @author zhanghaijun
 */
public interface Authenticator {

    public User checkUserInfo(BlogStore.User requestUser);
}
