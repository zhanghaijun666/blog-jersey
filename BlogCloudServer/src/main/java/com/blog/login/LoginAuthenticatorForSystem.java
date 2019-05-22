package com.blog.login;

import com.blog.db.User;
import com.blog.config.BlogStore;

/**
 * @author zhanghaijun
 */
public class LoginAuthenticatorForSystem implements Authenticator {

    @Override
    public User checkUserInfo(BlogStore.UserInfo requestUser) {
        return User.getSystemActiveUser(requestUser.getUsername(), requestUser.getPassword());
    }

}
