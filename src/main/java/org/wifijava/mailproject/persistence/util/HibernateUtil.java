package org.wifijava.mailproject.persistence.util;


import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.wifijava.mailproject.constants.Constants;
import org.wifijava.mailproject.controller.AlertService;
import org.wifijava.mailproject.data.DBConnection;

public class HibernateUtil {

    public static SessionFactory getSessionFactory() {
        SessionFactory sessionFactory;
        DBConnection dbConnection = DBConnectionFactory.getDBConnectionFromConfigFile();
        Configuration configuration = new Configuration();

        try {
            configuration.configure("hibernate.cfg.xml");
        } catch (HibernateException e) {
            AlertService alertService = new AlertService();
            alertService.showErrorDialog(Constants.DB_CONFIG_FILE_ERROR);
            System.exit(1);
        }

        configuration.setProperty("hibernate.connection.url", dbConnection.url());
        configuration.setProperty("hibernate.connection.username", dbConnection.username());
        configuration.setProperty("hibernate.connection.password", dbConnection.password());

        //todo: change error codes to correct number
        try {
            sessionFactory = configuration.buildSessionFactory();
            return sessionFactory;
        } catch (HibernateException e) {
            e.printStackTrace();
            AlertService alertService = new AlertService();
            if (e.getMessage().contains("Unknown database")) {
                alertService.showErrorDialog(Constants.DB_NOT_CREATED_ERROR);
                System.exit(1337);
            } else {
                alertService.showErrorDialog(Constants.DB_CONNECTION_ERROR);
                System.exit(1337);
            }
        }
        return null;
    }
}
