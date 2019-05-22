package com.blog.login;

import com.blog.db.User;
import com.blog.config.BlogStore;
import com.tools.BasicConvertUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhanghaijun
 */
public class LoginAuthenticatorForText implements Authenticator {

    private static final Logger logger = LoggerFactory.getLogger(LoginAuthenticatorForText.class);
    private static final Pattern USERPARSER = Pattern.compile("(?<username>[^:]+):(?<password>[^:]+):(?<roleId>[^:]+)");

    @Override
    public User checkUserInfo(BlogStore.UserInfo requestUser) {
        User dbUser = null;
        List<BlogStore.UserInfo> textUsers = readTextUsers();
        for (BlogStore.UserInfo textUser : textUsers) {
            if (textUser.getUsername().equals(requestUser.getUsername()) && textUser.getPassword().equals(requestUser.getPassword())) {
                dbUser = User.saveUser(textUser, User.DEFAULT_USER_ID, BlogStore.Authenticator.TEXT_AUTHENTICATOR_VALUE);
                break;
            }
        }
        return dbUser;
    }

    private static List<BlogStore.UserInfo> readTextUsers() {
        List<BlogStore.UserInfo> users = new ArrayList<>();
        File userText = new File("user.txt");
        if (userText.exists()) {
            String line = null;
            try (BufferedReader fr = new BufferedReader(new FileReader(userText))) {
                while ((line = fr.readLine()) != null) {
                    line = line.trim();
                    if (!line.startsWith("#")) {
                        Matcher m = USERPARSER.matcher(line);
                        if (m.matches()) {
                            BlogStore.UserInfo.Builder user = BlogStore.UserInfo.newBuilder();
                            user.setUsername(m.group("username"));
                            user.setPassword(m.group("password"));
                            String roleId = m.group("roleId");
                            if (StringUtils.isNotBlank(roleId)) {
                                BlogStore.Role.Builder role = BlogStore.Role.newBuilder();
                                role.setId(BasicConvertUtils.toInteger(roleId, 0));
                                user.setRole(role);
                            }
                            users.add(user.build());
                        }
                    }
                }
            } catch (FileNotFoundException ex) {
                logger.warn("user.txt not found", ex);
            } catch (IOException ex) {
                logger.warn("read user.txt catch an error", ex);
            }
        }
        return users;
    }

}
