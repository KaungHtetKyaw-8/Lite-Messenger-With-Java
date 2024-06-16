module org.khk.sockettest {
    requires javafx.controls;
    requires javafx.fxml;

    requires jdk.net;

    exports org.khk.sockettest.Client;
    opens org.khk.sockettest.Client to javafx.fxml;
}