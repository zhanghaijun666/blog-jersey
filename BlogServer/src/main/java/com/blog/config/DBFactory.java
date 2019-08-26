package com.blog.config;

import com.blog.config.Configuration;
import com.blog.config.ConfigStore.DB;
import org.javalite.activejdbc.Base;

/**
 * @author zhanghaijun
 */
public class DBFactory {

    public static org.javalite.activejdbc.DB open() {
        DB db = Configuration.getInstance().getConfig().getDb();
        return Base.open(db.getDriver(), db.getUrl(), db.getUser(), db.getPassword());
    }
}
