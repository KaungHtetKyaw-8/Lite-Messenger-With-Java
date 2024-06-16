package org.khk.sockettest.Client;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

import java.io.IOException;

public class MessageController {

    private VBox msgList;
    private MessageClient msgClient;

    @FXML
    private ScrollPane displayListPane;

    @FXML
    private TextArea MsgText;

    @FXML
    private TextField uname;

    @FXML
    private TextField uid;

    @FXML
    private TextField rid;

    @FXML
    protected void onSendButtonClick() {
        String messageText;
        if ((!(messageText = getMsgText()).isBlank()) && msgClient.connectionStatus()) {
            displayMsg(messageText,'S');
            msgClient.unicastSend(messageText);
        }
    }

    @FXML
    protected void onConnectionBuild() {

        if (uname.getText().isBlank()){
            alert(Alert.AlertType.ERROR,"TextField Required : ","Plz Fill User Name...");
            return;
        }

        if (msgClient != null){
            return;
        }

        msgClient = new MessageClient(this);
        try {
            msgClient.startConnection(MessageClient.IP,MessageClient.PORT);
            new Thread(msgClient).start();
            alert(Alert.AlertType.INFORMATION,"Connection","Server Connection Successful!!");
        } catch (IOException e) {
            alert(Alert.AlertType.ERROR,"Connection","Cannot connect to Server...");
        }
    }

    @FXML
    protected void onDisconnect(){
        try {
            if (msgClient.connectionStatus()) {
                msgClient.disConnect();
                alert(Alert.AlertType.INFORMATION,"Disconnection","Successfully Disconnected!");
            }
        } catch (IOException e) {
            alert(Alert.AlertType.ERROR,"Disconnection","Cannot Disconnect...");
        }
    }

    public MessageController() {
        msgList = new VBox();
    }

    protected String getMsgText() {
        String msg = "";
        msg = MsgText.getText();
        MsgText.clear();
        return msg;
    }

    protected void displayMsg(String msg,char type) {
        FlowPane flowPane = new FlowPane();
        Label message = new Label();
        message.setText(msg);

        msgList.setMaxWidth(displayListPane.getWidth());
        msgList.setSpacing(10);
        msgList.setPadding(new Insets(5,5,5,5 ));

        flowPane.setMaxWidth(displayListPane.getWidth());
        flowPane.setPrefWidth(displayListPane.getWidth());

        message.setMaxWidth(displayListPane.getWidth()/2);
        message.setWrapText(true);
        message.setPadding(new Insets(5,10,5,10 ));
        message.setStyle("-fx-background-color:  linear-gradient(from 0.0% 0.0% to 100.0% 100.0%, #ad4ed0 0.0%, #9a41b8 100.0%);" +
                "-fx-background-radius: 10px 10px 10px 10px;");
        message.setTextFill(Paint.valueOf("#fbf6fd"));

        if (type == 'S') {
            flowPane.setAlignment(Pos.CENTER_RIGHT);
        } else {
            flowPane.setAlignment(Pos.CENTER_LEFT);
        }

        flowPane.getChildren().add(message);

        msgList.getChildren().add(flowPane);

        displayListPane.setContent(msgList);
    }

    public String getUserName(){
        return uname.getText();
    }

    public void setUID(String id){
        uid.setText(id);
    }

    public String getUID(){
        return uid.getText();
    }

    public String getRID(){
        return rid.getText();
    }

    protected void alert(Alert.AlertType alertType,String title,String alertMessage) {
        Alert alert = new Alert(alertType,alertMessage, ButtonType.OK);
        alert.setTitle(title);
        alert.show();
    }

}