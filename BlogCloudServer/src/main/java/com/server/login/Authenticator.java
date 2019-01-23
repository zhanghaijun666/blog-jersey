package com.server.login;

import com.db.User;
import com.proto.BlogStore;

/**
 * @author zhanghaijun
 */
public interface Authenticator {

    public User checkUserInfo(BlogStore.User requestUser);
}
