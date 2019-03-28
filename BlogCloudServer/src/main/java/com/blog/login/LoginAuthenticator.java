package com.blog.login;

import com.blog.db.User;
import com.blog.proto.BlogStore;
import com.blog.factory.SreverSession;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.simpleframework.http.Response;

/**
 * @author zhanghaijun
 */
public class LoginAuthenticator {

    private static final List<Authenticator> AuthenticatorPlatform = new ArrayList<Authenticator>() {
        {
            add(new SystemAuthenticator());
            add(new TextAuthenticator());
        }
    };

    public static BlogStore.ReturnCode authenticator(BlogStore.User requestUser, Response response) {
        if (StringUtils.isBlank(requestUser.getUsername()) || StringUtils.isBlank(requestUser.getPassword())) {
            return BlogStore.ReturnCode.Return_USERNAME_OR_PASSWORD_IS_EMPTY;
        }
        User dbUser = null;
        for (Authenticator auth : AuthenticatorPlatform) {
            dbUser = auth.checkUserInfo(requestUser);
            if (null != dbUser) {
                break;
            }
        }
        if (null == dbUser) {
            return BlogStore.ReturnCode.Return_USER_EMPTY;
        }
        AppSession session = SreverSession.instance().createSession(dbUser, requestUser.getRememberMe());
        response.setCookie("session", session.getId());
        return BlogStore.ReturnCode.Return_OK;
    }
}
