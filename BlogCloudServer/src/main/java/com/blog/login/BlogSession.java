package com.blog.login;

import com.blog.config.Configuration;
import com.blog.db.Role;
import com.blog.db.User;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

/**
 * @author haijun.zhang
 */
public class BlogSession implements Principal, HttpSession {

    private final Map<String, Object> attributes = new ConcurrentHashMap<>();
    private final String sessionId;
    private final long creationTime;
    private long lastAccessedTime;
    private final int userId;
    private final String username;
    private List<String> roles;
    private final Boolean isRememberMe;

    public BlogSession(String sessionId, int userId, String username, Boolean isRememberMe) {
        this.lastAccessedTime = System.currentTimeMillis();
        this.creationTime = System.currentTimeMillis();
        this.sessionId = sessionId;
        this.userId = userId;
        this.username = username;
        this.isRememberMe = isRememberMe;
    }

    public boolean isAlive() {
        if (isRememberMe) {
            return true;
        } else {
            return System.currentTimeMillis() - lastAccessedTime < Configuration.getInstance().getSessionTimeoutMills();
        }
    }

    public void touch() {
        lastAccessedTime = System.currentTimeMillis();
    }

    public List<String> getRoles() {
        if (roles == null) {
            roles = new ArrayList<>();
            User user = User.findById(userId);
            if (user != null) {
                roles.add("user");
                Role role = Role.getRoleById(user.getRoleId());
                if (null != role) {
                    roles.add(role.getRoleName());
                }
            }
        }
        return Collections.unmodifiableList(roles);
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public String getName() {
        return username;
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public String getId() {
        return sessionId;
    }

    @Override
    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    @Override
    public ServletContext getServletContext() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setMaxInactiveInterval(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getMaxInactiveInterval() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public HttpSessionContext getSessionContext() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getAttribute(String string) {
        return attributes.get(string);
    }

    @Override
    public Object getValue(String string) {
        return attributes.get(string);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return Collections.enumeration(attributes.keySet());
    }

    @Override
    public String[] getValueNames() {
        return attributes.keySet().toArray(new String[attributes.size()]);
    }

    @Override
    public void setAttribute(String string, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void putValue(String string, Object o) {
        attributes.put(string, o);
    }

    @Override
    public void removeAttribute(String string) {
        attributes.remove(string);
    }

    @Override
    public void removeValue(String string) {
        attributes.replace(string, null);
    }

    @Override
    public void invalidate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isNew() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
