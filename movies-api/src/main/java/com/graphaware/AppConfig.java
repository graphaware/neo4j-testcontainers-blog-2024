package com.graphaware;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AppConfig {

    private static Properties properties;

    static {
        properties = new Properties();
        try  {
            properties.load(AppConfig.class.getClassLoader().getResourceAsStream("config.properties"));
            System.setProperties(properties);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately (e.g., logging, exiting the application)
        }
    }

    public static String getDatabaseUrl() {
        return System.getProperty("database.url");
    }

    public static void setDatabaseUrl(String url) {
        System.setProperty("database.url", url);
    }

    public static String getDatabaseUsername() {
        return System.getProperty("database.username");
    }

    public static String getDatabasePassword() {
        return System.getProperty("database.password");
    }

}

