package com.findit;

import io.netty.channel.Channel;
import org.glassfish.jersey.netty.httpserver.NettyHttpContainerProvider;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args ) {
        try {
            int port = Objects.nonNull(System.getenv("PORT")) ?
                    Integer.valueOf(System.getenv("PORT")) : 8080;

            URI baseUri = UriBuilder.fromUri("http://localhost/").port(port).build();
            ResourceConfig resourceConfig = new ResourceConfig().packages("com.findit.chamada.api");
            NettyHttpContainerProvider.createServer(baseUri, resourceConfig, false);
            System.out.printf("Application running on %s\n", baseUri.toURL().toExternalForm());
        } catch (Exception e) {}
    }
}
