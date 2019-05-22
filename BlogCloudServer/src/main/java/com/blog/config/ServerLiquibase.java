package com.blog.config;

import com.blog.config.Configuration;
import com.blog.config.ConfigStore;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

/**
 * @author haijun.zhang
 */
public class ServerLiquibase {

    private static final String DB_FILE = "db/server-db.xml";

    private static Connection getConnection() throws ClassNotFoundException, SQLException {
        ConfigStore.DB dbConfig = Configuration.getInstance().getConfig().getDb();
        Connection connection = null;
        if (dbConfig != null) {
            Class.forName(dbConfig.getDriver());
            connection = DriverManager.getConnection(dbConfig.getUrl(), dbConfig.getUser(), dbConfig.getPassword());
        }
        return connection;
    }

    public static void initLiquibase() {
        try {
            Database db = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(getConnection()));
            Liquibase liquibase = new Liquibase(DB_FILE, new ClassLoaderResourceAccessor(), db);
            liquibase.update("");
        } catch (ClassNotFoundException | SQLException | LiquibaseException ex) {
            Logger.getLogger(ServerLiquibase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
