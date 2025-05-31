package com.example.demo;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class ServerStartupLogger implements ApplicationRunner {

    private int serverPort;
    private String contextPath;

    @Autowired
    private DataSource dataSource;

    @EventListener
    public void onApplicationEvent(WebServerInitializedEvent event) {
        serverPort = event.getWebServer().getPort();
        contextPath = event.getApplicationContext().getEnvironment().getProperty("server.servlet.context-path", "");
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String serverUrl = String.format("http://localhost:%d%s", serverPort, contextPath);
        System.out.println("\n\n*********************************************************");
        System.out.println("Server running at: " + serverUrl);
        
        // Check database connection
        try (Connection connection = dataSource.getConnection()) {
            String dbUrl = connection.getMetaData().getURL();
            String dbUser = connection.getMetaData().getUserName();
            System.out.println("Database Connection: SUCCESS");
            System.out.println("Database URL: " + dbUrl);
            System.out.println("Database User: " + dbUser);
        } catch (Exception e) {
            System.out.println("Database Connection: FAILED");
            System.out.println("Error: " + e.getMessage());
        }
        
        System.out.println("*********************************************************");
    }
} 