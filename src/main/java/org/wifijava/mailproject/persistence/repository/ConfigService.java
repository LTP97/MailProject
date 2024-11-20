package org.wifijava.mailproject.persistence.repository;

import org.wifijava.mailproject.constants.Constants;
import org.wifijava.mailproject.controller.AlertService;
import org.wifijava.mailproject.data.DBConnection;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigService {

    public static DBConnection getDBConnection() {
        Properties props = new Properties();
        String dbUrl = "";
        String dbUsername = "";
        String dbPassword = "";
        try (InputStream input = ConfigService.class.getClassLoader().getResourceAsStream("config.properties")) {
            props.load(input);
            dbUrl = props.getProperty("db.url");
            dbUsername = props.getProperty("db.username");
            dbPassword = props.getProperty("db.password");
        } catch (IOException e) {
            AlertService.showErrorDialog(Constants.DB_PROPERTIES_ERROR);
            System.exit(1);
        }

        return new DBConnection(dbUrl, dbUsername, dbPassword);
    }
}
