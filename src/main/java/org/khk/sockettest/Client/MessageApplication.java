package org.khk.sockettest.Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MessageApplication extends Application {
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        MessageApplication.sceneLoad("chat_view.fxml");
        stage.setTitle("Messenger!!");
        stage.setScene(MessageApplication.scene);
        stage.show();
    }

    public static void sceneLoad(String fileName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MessageApplication.class.getResource(fileName));
        MessageApplication.scene = new Scene(fxmlLoader.load());
    }

    public static void main(String[] args) {
        launch();
    }
}