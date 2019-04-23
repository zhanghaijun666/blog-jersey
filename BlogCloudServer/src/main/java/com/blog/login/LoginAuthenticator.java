package com.blog.login;

import com.blog.db.User;
import com.blog.proto.BlogStore;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.simpleframework.http.Response;

/**
 * @author zhanghaijun
 */
public class LoginAuthenticator {

    public static final String SESSION_KEY = "blog_bogin_session";

    private static final List<Authenticator> AuthenticatorPlatform = new ArrayList<Authenticator>();

    static {
        AuthenticatorPlatform.add(new LoginAuthenticatorForSystem());
        AuthenticatorPlatform.add(new LoginAuthenticatorForText());
    }

    public static BlogStore.ReturnCode authenticator(BlogStore.UserInfo requestUser, Response response) {
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
        BlogSession session = BlogSessionFactory.instance().createSession(dbUser, requestUser.getRememberMe());
        response.setCookie(LoginAuthenticator.SESSION_KEY, session.getId());
        return BlogStore.ReturnCode.Return_OK;
    }
}
