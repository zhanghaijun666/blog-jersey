package com.db;

import com.proto.BlogStore;
import com.utils.BasicConvertUtils;
import java.util.List;
import org.javalite.activejdbc.annotations.Table;

/**
 * @author zhanghaijun
 */
@Table("menus")
public class Menu extends CommonModel {

    private static final long serialVersionUID = 1L;
    public static final int DEFAULT_MENU_PARENT_ID = 0;

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

    public String getTemplate() {
        return BasicConvertUtils.toString(getString("template"), "");
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

    public static BlogStore.Menu MenuBuilder(Menu menu) {
        if (null == menu) {
            return BlogStore.Menu.getDefaultInstance();
        }
        return BlogStore.Menu.newBuilder()
                .setMenuId(menu.getMenuId())
                .setParentId(menu.getParentId())
                .setName(menu.getName())
                .setIcon(menu.getIcon())
                .setTemplate(menu.getTemplate())
                .setHash(menu.getHash())
                .setIsDeletable(menu.isDeletable())
                .setIsDefaultShow(menu.isDefaultShow())
                .setStatus(menu.getStatus())
                .build();
    }

    public static int saveMenu(BlogStore.Menu menu, int userId) {
        Menu dbMenu = Menu.findFirst("name = ? AND status = ? ", menu.getName(), BlogStore.Status.StatusActive_VALUE);
        if (null == dbMenu) {
            dbMenu = Menu.create("name", menu.getName(), "status", BlogStore.Status.StatusActive_VALUE, "created_by", userId);
        }
        dbMenu.setInteger("parent_id", menu.getParentId());
        dbMenu.setString("icon", menu.getIcon());
        dbMenu.setString("template", menu.getTemplate());
        dbMenu.setString("hash", menu.getHash());
        dbMenu.setBoolean("deletable", menu.getIsDeletable());
        dbMenu.setBoolean("default_show", menu.getIsDefaultShow());
        dbMenu.setInteger("updated_by", userId);
        dbMenu.saveIt();
        return BlogStore.ReturnCode.OK_VALUE;
    }

    public static BlogStore.MenuList getHashMenus(String hash) {
        BlogStore.MenuList.Builder menuList = BlogStore.MenuList.newBuilder();
        List<Menu> list = Menu.find("hash = ? AND status = ? ", hash, BlogStore.Status.StatusActive_VALUE);
        for (Menu menu : list) {
            menuList.addItems(MenuBuilder(menu));
        }
        return menuList.build();
    }

    public static BlogStore.MenuList getMenuAll(String hash) {
        BlogStore.MenuList.Builder menuList = BlogStore.MenuList.newBuilder();
        List<Menu> list = Menu.findAll();
        for (Menu menu : list) {
            menuList.addItems(MenuBuilder(menu));
        }
        return menuList.build();
    }

}
