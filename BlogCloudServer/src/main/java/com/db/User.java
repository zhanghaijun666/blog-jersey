package com.db;

import com.proto.PBStore;
import com.utils.BlogUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.javalite.activejdbc.annotations.Table;

/**
 * @author zhanghaijun
 */
@Table("users")
public class User extends CommonModel {

    public static final int DEFAULTUSERID = 0;

    public int getUserId() {
        return getInteger("id");
    }

    public String getUsername() {
        return getString("username");
    }

    public String getPassword() {
        return getString("password");
    }

    public Integer getRoleId() {
        return getInteger("role_id");
    }

    public Integer getStatus() {
        return getInteger("status");
    }

    public void setStatus(int status) {
        setInteger("status", status);
    }

    public String getNickname() {
        String nick = getString("nickname");
        if (null == nick || StringUtils.isBlank(nick)) {
            return getUsername();
        }
        return nick;
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

    public static PBStore.User UserBuilder(User user) {
        if (null == user) {
            return PBStore.User.getDefaultInstance();
        }
        return PBStore.User.newBuilder()
                .setUserId(user.getUserId())
                .setUsername(user.getUsername())
                .setNickname(user.getNickname())
                .setEmail(user.getEmail())
                .setPhone(user.getPhone())
                .setStatus(user.getStatus())
                .setRole(Role.getRole(user.getRoleId()))
                .setAuthenticator(user.getAuthenticator())
                .build();
    }

    public static User saveUser(PBStore.User user, int userId, int AuthPlatform) {
        User dbUser = User.findFirst("username = ? AND status = ? AND authenticator = ? ", user.getUsername(), PBStore.Status.StatusActive_VALUE, AuthPlatform);
        if (null == dbUser) {
            dbUser = User.create("username", user.getUsername(), "status", PBStore.Status.StatusActive_VALUE, "authenticator", AuthPlatform, "created_by", userId);
        }
        if (AuthPlatform == PBStore.Authenticator.SYSTEM_AUTHENTICATOR_VALUE) {
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

    public static int saveUser(PBStore.User user, User dbUser, int userId) {
        if (null == dbUser) {
            return PBStore.ReturnCode.USER_EMPTY_VALUE;
        }
        dbUser.setString("username", user.getUsername());
        dbUser.setString("nickname", user.getNickname());
        dbUser.setInteger("role_id", user.getRole().getId());
        dbUser.setString("email", user.getEmail());
        dbUser.setString("phone", user.getPhone());
        dbUser.setInteger("updated_by", userId);
        dbUser.saveIt();
        return PBStore.ReturnCode.OK_VALUE;
    }

    public static boolean isExist(String username) {
        return User.count(" username = ? AND status = ? ", username, PBStore.Status.StatusActive_VALUE) > 0;
    }

    public static User getSystemActiveUser(String username, String password) {
        List<Object> params = new ArrayList<>();
        params.add(username);
        params.add(BlogUtils.sha1Hex(password));
        params.add(PBStore.Status.StatusActive_VALUE);
        params.add(PBStore.Authenticator.SYSTEM_AUTHENTICATOR_VALUE);
        return User.findFirst("username = ? AND password = ? AND status = ? AND authenticator = ? ", params.toArray());
    }

    public static List<User> findAllActiveUser() {
        return User.find("status = ? ", PBStore.Status.StatusActive_VALUE);
    }

}
