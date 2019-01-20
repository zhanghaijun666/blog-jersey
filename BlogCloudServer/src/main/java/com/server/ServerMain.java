package com.server;

import com.config.Configuration;
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

    public static final URI BASE_URI = UriBuilder.fromUri("http://0.0.0.0").port(8080).build();

    public static SimpleServer startServer() {
        final ResourceConfig resourceConfig = new ResourceConfig().packages("com.service");
        resourceConfig.register(SecurityRequestFilter.class);
        resourceConfig.register(RolesAllowedDynamicFeature.class);
        return SimpleContainerFactory.create(BASE_URI, resourceConfig);
    }

    public static void main(String[] args) throws IOException {
        Configuration.getInstance().loadConfig();
        ServerLiquibase.initLiquibase();
        final SimpleServer server = startServer();
    }
}
