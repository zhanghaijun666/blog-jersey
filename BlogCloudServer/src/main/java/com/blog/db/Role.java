package com.blog.db;

import com.blog.config.BlogStore;
import com.tools.BasicConvertUtils;
import java.util.List;
import org.javalite.activejdbc.annotations.Table;

/**
 * @author zhanghaijun
 */
@Table("roles")
public class Role extends org.javalite.activejdbc.Model implements CommonModel {

    private static final long serialVersionUID = 1L;
    public static final int DEFAULT_USER_ROLE_ID = 0;

    public int getRoleId() {
        return getInteger("id");
    }

    public String getRoleName() {
        return getString("role_name");
    }

    public String getNote() {
        return BasicConvertUtils.toString(getString("note"), "");
    }

    public String getIcon() {
        return BasicConvertUtils.toString(getString("icon"), "");
    }

    public int getStatus() {
        return getInteger("status");
    }

    public static BlogStore.Role RoleBuilder(Role role) {
        if (null == role) {
            return BlogStore.Role.getDefaultInstance();
        }
        return BlogStore.Role.newBuilder()
                .setId(role.getRoleId())
                .setRoleName(role.getRoleName())
                .setNote(role.getNote())
                .setStatus(role.getStatus())
                .setIcon(role.getIcon())
                .build();
    }

    public static int saveMenu(BlogStore.Role role, int userId) {
        Role dbRole = Role.findFirst(" role_name = ? AND status = ? ", role.getRoleName(), BlogStore.Status.StatusActive_VALUE);
        if (null == dbRole) {
            dbRole = Role.create("role_name", role.getRoleName(), BlogStore.Status.StatusActive_VALUE, "created_by", userId);
        }
        dbRole.set("note", role.getNote());
        dbRole.setInteger("updated_by", userId);
        dbRole.setString("icon", role.getIcon());
        dbRole.saveIt();
        return BlogStore.ReturnCode.Return_OK_VALUE;
    }

    public static BlogStore.Role getRole(int roleId) {
        return Role.RoleBuilder(Role.getRoleById(roleId));
    }

    public static Role getRoleById(int roleId) {
        return Role.findById(roleId);
    }

    public static BlogStore.RoleList getRoleALl() {
        BlogStore.RoleList.Builder roleList = BlogStore.RoleList.newBuilder();
        List<Role> dbRoleList = Role.findAll();
        dbRoleList.forEach((dbRole) -> {
            roleList.addItems(RoleBuilder(dbRole));
        });
        return roleList.build();
    }

}
