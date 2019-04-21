package com.blog.login;

import com.blog.db.User;
import com.blog.proto.BlogStore;

/**
 * @author zhanghaijun
 */
public class LoginAuthenticatorForSystem implements Authenticator {

    @Override
    public User checkUserInfo(BlogStore.User requestUser) {
        return User.getSystemActiveUser(requestUser.getUsername(), requestUser.getPassword());
    }

}
