package com.server;

import com.blog.config.ServerLiquibase;
import com.blog.config.ServerLogs;
import ch.qos.logback.core.joran.spi.JoranException;
import com.blog.config.Configuration;
import com.blog.config.ConfigStore;
import com.jersey.SimpleContainerFactory2;
import com.server.filter.BlogFilter;
import com.server.filter.SecurityRequestFilter;
import java.io.IOException;
import java.net.URI;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.glassfish.jersey.simple.SimpleServer;

/**
 * @author haijun.zhang
 */
public class ServerMain {

    public static void main(String[] args) throws IOException, JoranException {
        Configuration.getInstance().loadConfig();
        ServerLogs.logInit();
        ServerLiquibase.initLiquibase();
        final SimpleServer server = startServer();
    }

    public static SimpleServer startServer() {
        ConfigStore.Server configServer = Configuration.getInstance().getConfig().getServer();
        final URI BASE_URI = UriBuilder.fromUri("http://" + configServer.getHost()).port(configServer.getPort()).build();
        final ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.packages("com.blog.controller", "com.jersey.provider");
        resourceConfig.register(BlogFilter.class);
        resourceConfig.register(SecurityRequestFilter.class);
        resourceConfig.register(RolesAllowedDynamicFeature.class);
        resourceConfig.register(MultiPartFeature.class);
        return SimpleContainerFactory2.create(BASE_URI, resourceConfig);
    }
}
