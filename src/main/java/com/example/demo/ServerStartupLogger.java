package com.example.demo;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ServerStartupLogger implements ApplicationRunner {

    private int serverPort;
    private String contextPath;

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
        System.out.println("*********************************************************");
    }
} 