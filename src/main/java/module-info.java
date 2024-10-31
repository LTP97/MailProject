module org.wifijava.mailproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.mail;


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
}