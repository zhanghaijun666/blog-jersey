package com.blog.db;

import com.blog.config.BlogStore;
import com.tools.BlogUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.javalite.activejdbc.annotations.Table;

/**
 * @author zhanghaijun
 */
@Table("users")
public class User extends org.javalite.activejdbc.Model implements CommonModel, Repository {

    private static final long serialVersionUID = 1L;
    public static final int DEFAULT_USER_ID = 0;

    public int getUserId() {
        return getInteger("id");
    }

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
        return getString("username");
    }

    @Override
    public String getNickname() {
        String nick = getString("nickname");
        if (null == nick || StringUtils.isBlank(nick)) {
            return getName();
        }
        return nick;
    }

    @Override
    public Integer getStatus() {
        return getInteger("status");
    }

    public String getPassword() {
        return getString("password");
    }

    public Integer getRoleId() {
        return getInteger("role_id");
    }

    public void setStatus(int status) {
        setInteger("status", status);
    }

    public String getEmail() {
        return getString("email");
    }

    public String getPhone() {
        return getString("phone");
    }

    public int getAuthenticator() {
        return getInteger("authenticator");
    }

    public static BlogStore.UserInfo UserBuilder(User user) {
        if (null == user) {
            return BlogStore.UserInfo.getDefaultInstance();
        }
        return BlogStore.UserInfo.newBuilder()
                .setUserId(user.getUserId())
                .setUsername(user.getName())
                .setNickname(user.getNickname())
                .setEmail(user.getEmail())
                .setPhone(user.getPhone())
                .setStatus(user.getStatus())
                .setRole(Role.getRole(user.getRoleId()))
                .setAuthenticator(user.getAuthenticator())
                .build();
    }

    public static User saveUser(BlogStore.UserInfo user, int userId, int AuthPlatform) {
        User dbUser = User.findFirst("username = ? AND status = ? AND authenticator = ? ", user.getUsername(), BlogStore.Status.StatusActive_VALUE, AuthPlatform);
        if (null == dbUser) {
            dbUser = User.create("username", user.getUsername(), "status", BlogStore.Status.StatusActive_VALUE, "authenticator", AuthPlatform, "created_by", userId);
        }
        if (AuthPlatform == BlogStore.Authenticator.SYSTEM_AUTHENTICATOR_VALUE) {
            dbUser.setString("password", BlogUtils.sha1Hex(user.getPassword()));
        }
        dbUser.setString("nickname", user.getNickname());
        dbUser.setInteger("role_id", user.getRole().getId());
        dbUser.setString("email", user.getEmail());
        dbUser.setString("phone", user.getPhone());
        dbUser.setInteger("updated_by", userId);
        dbUser.saveIt();
        return dbUser;
    }

    public static int saveUser(BlogStore.UserInfo user, User dbUser, int userId) {
        if (null == dbUser) {
            return BlogStore.ReturnCode.Return_USER_EMPTY_VALUE;
        }
        dbUser.setString("username", user.getUsername());
        dbUser.setString("nickname", user.getNickname());
        dbUser.setInteger("role_id", user.getRole().getId());
        dbUser.setString("email", user.getEmail());
        dbUser.setString("phone", user.getPhone());
        dbUser.setInteger("updated_by", userId);
        dbUser.saveIt();
        return BlogStore.ReturnCode.Return_OK_VALUE;
    }

    public static boolean isExist(String username) {
        return User.count(" username = ? AND status = ? ", username, BlogStore.Status.StatusActive_VALUE) > 0;
    }

    public static User getSystemActiveUser(String username, String password) {
        List<Object> params = new ArrayList<>();
        params.add(username);
        params.add(BlogUtils.sha1Hex(password));
        params.add(BlogStore.Status.StatusActive_VALUE);
        params.add(BlogStore.Authenticator.SYSTEM_AUTHENTICATOR_VALUE);
        return User.findFirst("username = ? AND password = ? AND status = ? AND authenticator = ? ", params.toArray());
    }

    public static List<User> findAllActiveUser() {
        return User.find("status = ? ", BlogStore.Status.StatusActive_VALUE);
    }

}
