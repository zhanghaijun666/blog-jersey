package com.blog.login;

import com.blog.db.User;
import com.blog.proto.BlogStore;

/**
 * @author zhanghaijun
 */
public class SystemAuthenticator implements Authenticator {

    @Override
    public User checkUserInfo(BlogStore.User requestUser) {
        return User.getSystemActiveUser(requestUser.getUsername(), requestUser.getPassword());
    }

}
