package com.db;

import com.proto.PBStore;
import com.utils.BasicConvertUtils;
import java.util.List;
import org.javalite.activejdbc.annotations.Table;

/**
 * @author zhanghaijun
 */
@Table("menus")
public class Menu extends CommonModel {

    public int getMenuId() {
        return getInteger("id");
    }

    public int getParentId() {
        return getInteger("parent_id");
    }

    public String getName() {
        return getString("name");
    }

    public String getIcon() {
        return BasicConvertUtils.toString(getString("icon"), "");
    }

    public String getComponent() {
        return BasicConvertUtils.toString(getString("component"), "");
    }

    public String getHash() {
        return getString("hash");
    }

    public boolean isDeletable() {
        return getBoolean("deletable");
    }

    public boolean isDefaultShow() {
        return getBoolean("default_show");
    }

    public int getStatus() {
        return getInteger("status");
    }

    public static PBStore.Menu MenuBuilder(Menu menu) {
        if (null == menu) {
            return PBStore.Menu.getDefaultInstance();
        }
        return PBStore.Menu.newBuilder()
                .setMenuId(menu.getMenuId())
                .setParentId(menu.getParentId())
                .setName(menu.getName())
                .setIcon(menu.getIcon())
                .setComponent(menu.getComponent())
                .setHash(menu.getHash())
                .setDeletable(menu.isDeletable())
                .setDefaultShow(menu.isDefaultShow())
                .setStatus(menu.getStatus())
                .build();
    }

    public static int saveMenu(PBStore.Menu menu, int userId) {
        Menu dbMenu = Menu.findFirst("name = ? AND status = ? ", menu.getName(), PBStore.Status.StatusActive_VALUE);
        if (null == dbMenu) {
            dbMenu = Menu.create("name", menu.getName(), "status", PBStore.Status.StatusActive_VALUE, "created_by", userId);
        }
        dbMenu.setInteger("parent_id", menu.getParentId());
        dbMenu.setString("icon", menu.getIcon());
        dbMenu.setString("component", menu.getComponent());
        dbMenu.setString("hash", menu.getHash());
        dbMenu.setBoolean("deletable", menu.getDeletable());
        dbMenu.setBoolean("default_show", menu.getDefaultShow());
        dbMenu.setInteger("updated_by", userId);
        dbMenu.saveIt();
        return PBStore.ReturnCode.OK_VALUE;
    }

    public static PBStore.MenuList getHashMenus(String hash) {
        PBStore.MenuList.Builder menuList = PBStore.MenuList.newBuilder();
        List<Menu> list = Menu.find("hash = ? AND status = ? ", hash, PBStore.Status.StatusActive_VALUE);
        for (Menu menu : list) {
            menuList.addItems(MenuBuilder(menu));
        }
        return menuList.build();
    }

    public static PBStore.MenuList getMenuAll(String hash) {
        PBStore.MenuList.Builder menuList = PBStore.MenuList.newBuilder();
        List<Menu> list = Menu.findAll();
        for (Menu menu : list) {
            menuList.addItems(MenuBuilder(menu));
        }
        return menuList.build();
    }

}
