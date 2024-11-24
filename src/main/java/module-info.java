module org.wifijava.mailproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.mail;
    requires static lombok;
    requires java.sql;
    requires jakarta.persistence;
    requires mysql.connector.j;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires org.jsoup;
    requires java.desktop;

    exports org.wifijava.mailproject.persistence.entity;
    opens org.wifijava.mailproject.persistence.entity to org.hibernate.orm.core;
    opens org.wifijava.mailproject to javafx.fxml;
    exports org.wifijava.mailproject;
    exports org.wifijava.mailproject.constants;
    opens org.wifijava.mailproject.constants to javafx.fxml;
    exports org.wifijava.mailproject.data;
    opens org.wifijava.mailproject.data to javafx.fxml;
    exports org.wifijava.mailproject.io;
    opens org.wifijava.mailproject.io to javafx.fxml;
    exports org.wifijava.mailproject.io.provider;
    opens org.wifijava.mailproject.io.provider to javafx.fxml;
    exports org.wifijava.mailproject.controller;
    opens org.wifijava.mailproject.controller to javafx.fxml;
}