package com.findit;

import com.findit.bd.connector.PostgresConnector;
import io.netty.channel.Channel;
import org.glassfish.jersey.netty.httpserver.NettyHttpContainerProvider;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.sql.Connection;
import java.util.Objects;

public class AppLocal {

    public static Connection connection;

    public static void main(String[] args ) {
        try {
            URI baseUri = UriBuilder.fromUri("http://localhost/").port(8080).build();
            //ResourceConfig resourceConfig = new ResourceConfig().packages("com.findit.dota2.api");
            ResourceConfig resourceConfig = new ResourceConfig().packages("com.findit.chamada.api");
            connection = PostgresConnector.getConnection();
            Channel server = NettyHttpContainerProvider.createServer(baseUri, resourceConfig, false);
            System.out.println("Press ENTER to terminate...");
            System.in.read();
            server.close().await();
        } catch (Exception e) {
            System.out.print(e);
        }
    }
}
