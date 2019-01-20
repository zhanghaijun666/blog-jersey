package com.db;

import com.proto.PBStore;
import com.utils.BasicConvertUtils;
import java.util.List;
import org.javalite.activejdbc.annotations.Table;

/**
 * @author zhanghaijun
 */
@Table("roles")
public class Role extends CommonModel {

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

    public static PBStore.Role RoleBuilder(Role role) {
        if (null == role) {
            return PBStore.Role.getDefaultInstance();
        }
        return PBStore.Role.newBuilder()
                .setId(role.getRoleId())
                .setRoleName(role.getRoleName())
                .setNote(role.getNote())
                .setStatus(role.getStatus())
                .setIcon(role.getIcon())
                .build();
    }

    public static int saveMenu(PBStore.Role role, int userId) {
        Role dbRole = Role.findFirst(" role_name = ? AND status = ? ", role.getRoleName(), PBStore.Status.StatusActive_VALUE);
        if (null == dbRole) {
            dbRole = Role.create("role_name", role.getRoleName(), PBStore.Status.StatusActive_VALUE, "created_by", userId);
        }
        dbRole.set("note", role.getNote());
        dbRole.setInteger("updated_by", userId);
        dbRole.setString("icon", role.getIcon());
        dbRole.saveIt();
        return PBStore.ReturnCode.OK_VALUE;
    }

    public static PBStore.Role getRole(int roleId) {
        return Role.RoleBuilder(Role.getRoleById(roleId));
    }

    public static Role getRoleById(int roleId) {
        return Role.findById(roleId);
    }

    public static PBStore.RoleList getRoleALl() {
        PBStore.RoleList.Builder roleList = PBStore.RoleList.newBuilder();
        List<Role> dbRoleList = Role.findAll();
        dbRoleList.forEach((dbRole) -> {
            roleList.addItems(RoleBuilder(dbRole));
        });
        return roleList.build();
    }

}
