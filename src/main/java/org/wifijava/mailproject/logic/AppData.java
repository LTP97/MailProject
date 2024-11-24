package org.wifijava.mailproject.logic;

import javafx.stage.Stage;
import lombok.Data;
import org.hibernate.SessionFactory;
import org.wifijava.mailproject.constants.Constants;
import org.wifijava.mailproject.controller.AlertService;
import org.wifijava.mailproject.data.MailAccount;
import org.wifijava.mailproject.persistence.util.HibernateUtil;


@Data
public class AppData {
    private static AppData instance;

    private MailAccount currentAccount;
    private SessionFactory sessionFactory;
    private Stage stage;

    private AppData() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
        if (sessionFactory == null) {
            AlertService alertService = new AlertService();
            alertService.showErrorDialog(Constants.UNKNOWN_ERROR);
            System.exit(1);
        }

    }

    public static AppData getInstance() {
        if (instance == null) instance = new AppData();
        return instance;
    }


}
