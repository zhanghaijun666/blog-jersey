package com.server.login;

import com.db.User;
import com.proto.BlogStore;

/**
 * @author zhanghaijun
 */
public class SystemAuthenticator implements Authenticator {

    @Override
    public User checkUserInfo(BlogStore.User requestUser) {
        return User.getSystemActiveUser(requestUser.getUsername(), requestUser.getPassword());
    }

}
