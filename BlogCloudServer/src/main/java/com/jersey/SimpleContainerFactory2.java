package com.jersey;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;

import javax.ws.rs.ProcessingException;

import javax.net.ssl.SSLContext;

import org.glassfish.jersey.internal.util.collection.UnsafeValue;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.simple.SimpleServer;
import org.glassfish.jersey.simple.SimpleTraceAnalyzer;
import org.glassfish.jersey.simple.internal.LocalizationMessages;

import org.simpleframework.http.core.ContainerSocketProcessor;
import org.simpleframework.transport.SocketProcessor;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

public final class SimpleContainerFactory2 {

    public static SimpleServer create(final URI address, final ResourceConfig config) {
        return create(address, null, new SimpleContainer2(config));
    }

    public static SimpleServer create(final URI address, final SSLContext context,
            final SimpleContainer2 container) {
        return _create(address, context, container, new UnsafeValue<SocketProcessor, IOException>() {
            @Override
            public SocketProcessor get() throws IOException {
                return new ContainerSocketProcessor(container);
            }
        });
    }

    private static SimpleServer _create(final URI address, final SSLContext context,
            final SimpleContainer2 container,
            final UnsafeValue<SocketProcessor, IOException> serverProvider)
            throws ProcessingException {
        if (address == null) {
            throw new IllegalArgumentException(LocalizationMessages.URI_CANNOT_BE_NULL());
        }
        final String scheme = address.getScheme();
        int defaultPort = org.glassfish.jersey.server.spi.Container.DEFAULT_HTTP_PORT;

        if (context == null) {
            if (!scheme.equalsIgnoreCase("http")) {
                throw new IllegalArgumentException(LocalizationMessages.WRONG_SCHEME_WHEN_USING_HTTP());
            }
        } else {
            if (!scheme.equalsIgnoreCase("https")) {
                throw new IllegalArgumentException(LocalizationMessages.WRONG_SCHEME_WHEN_USING_HTTPS());
            }
            defaultPort = org.glassfish.jersey.server.spi.Container.DEFAULT_HTTPS_PORT;
        }
        int port = address.getPort();

        if (port == -1) {
            port = defaultPort;
        }
        final InetSocketAddress listen = new InetSocketAddress(port);
        final Connection connection;
        try {
            final SimpleTraceAnalyzer analyzer = new SimpleTraceAnalyzer();
            final SocketProcessor server = serverProvider.get();
            connection = new SocketConnection(server, analyzer);

            final SocketAddress socketAddr = connection.connect(listen, context);
            container.onServerStart();

            return new SimpleServer() {

                @Override
                public void close() throws IOException {
                    container.onServerStop();
                    analyzer.stop();
                    connection.close();
                }

                @Override
                public int getPort() {
                    return ((InetSocketAddress) socketAddr).getPort();
                }

                @Override
                public boolean isDebug() {
                    return analyzer.isActive();
                }

                @Override
                public void setDebug(boolean enable) {
                    if (enable) {
                        analyzer.start();
                    } else {
                        analyzer.stop();
                    }
                }
            };
        } catch (final IOException ex) {
            throw new ProcessingException(LocalizationMessages.ERROR_WHEN_CREATING_SERVER(), ex);
        }
    }
}
