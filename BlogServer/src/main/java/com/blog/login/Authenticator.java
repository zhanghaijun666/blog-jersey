package com.blog.login;

import com.blog.db.User;
import com.blog.config.BlogStore;

/**
 * @author zhanghaijun
 */
public interface Authenticator {

    public User checkUserInfo(BlogStore.UserInfo requestUser);
}
