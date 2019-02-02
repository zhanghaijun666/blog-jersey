package com.server;

import ch.qos.logback.core.joran.spi.JoranException;
import com.config.Configuration;
import com.proto.ConfigStore;
import com.server.filter.SecurityRequestFilter;
import java.io.IOException;
import java.net.URI;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.glassfish.jersey.simple.SimpleContainerFactory;
import org.glassfish.jersey.simple.SimpleServer;

/**
 * @author haijun.zhang
 */
public class ServerMain {

    public static SimpleServer startServer() {
        ConfigStore.Server configServer = Configuration.getInstance().getConfig().getServer();
        final URI BASE_URI = UriBuilder.fromUri("http://" + configServer.getHost()).port(configServer.getPort()).build();
        final ResourceConfig resourceConfig = new ResourceConfig().packages("com.service","com.jersey.provider");
        resourceConfig.register(SecurityRequestFilter.class);
        resourceConfig.register(RolesAllowedDynamicFeature.class);
        return SimpleContainerFactory.create(BASE_URI, resourceConfig);
    }

    public static void main(String[] args) throws IOException, JoranException {
        Configuration.getInstance().loadConfig();
        LogHelper.logInit();
        ServerLiquibase.initLiquibase();
        final SimpleServer server = startServer();
    }
}
