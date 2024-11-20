package org.wifijava.mailproject.persistence.util;


import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.wifijava.mailproject.constants.Constants;
import org.wifijava.mailproject.controller.AlertService;
import org.wifijava.mailproject.data.DBConnection;
import org.wifijava.mailproject.persistence.repository.ConfigService;

public class HibernateUtil {

    public static SessionFactory getSessionFactory() {
        SessionFactory sessionFactory;
        DBConnection dbConnection = ConfigService.getDBConnection();
        Configuration configuration = new Configuration();

        try {
            configuration.configure("hibernate.cfg.xml");
        } catch (HibernateException e) {
            AlertService.showErrorDialog(Constants.DB_CONFIG_FILE_ERROR);
            System.exit(1);
        }

        configuration.setProperty("hibernate.connection.url", dbConnection.url());
        configuration.setProperty("hibernate.connection.username", dbConnection.username());
        configuration.setProperty("hibernate.connection.password", dbConnection.password());

        try {
            sessionFactory = configuration.buildSessionFactory();
            return sessionFactory;
        } catch (HibernateException e) {
            e.printStackTrace();
            if(e.getMessage().contains("Unknown database")){
                AlertService.showErrorDialog(Constants.DB_NOT_CREATED);
                System.exit(1337);
            }
            else {
                AlertService.showErrorDialog(Constants.DB_CONNECTION_ERROR);
                System.exit(1337);
            }
        }
        return null;
    }
}
