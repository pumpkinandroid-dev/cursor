package com.cnbeta.summary;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class App {
    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(System.getProperty("PORT", "8080"));

        Server server = new Server(port);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        // API servlet
        context.addServlet(new ServletHolder(new ApiServlet()), "/api/summary");

        // Static resources
        context.addServlet(new ServletHolder(new StaticFileServlet("/public")), "/*");

        server.setHandler(context);
        server.start();
        server.join();
    }
}



