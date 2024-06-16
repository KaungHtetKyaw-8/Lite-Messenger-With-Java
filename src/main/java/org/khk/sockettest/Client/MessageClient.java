package org.khk.sockettest.Client;

import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MessageClient implements Runnable {
    public static final String IP = "localhost";
    public static final int PORT = 4444;

    //                               comment sender receiver messageContent
    private static final String SEND_Format = "!!send %s %s %s";
    private static final String USER_INFO = "!!uname %s";

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private MessageController msgController;

    public MessageClient(MessageController msgController) {
        this.msgController = msgController;
    }

    public void startConnection(String ip, int port)  throws IOException{
        clientSocket = new Socket(ip,port);
        out = new PrintWriter(clientSocket.getOutputStream(),true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        out.println(String.format(USER_INFO,msgController.getUserName()));
    }

    public void unicastSend(String msg) {
        out.println(String.format(SEND_Format,msgController.getUID(),msgController.getRID(),msg.replace(" ","_")));
    }

    public String receiveMessage() throws IOException {
        return in.readLine();
    }

    public boolean connectionStatus() {
        if (!(clientSocket == null)) {
            return  clientSocket.isConnected();
        }
        return false;
    }

    public void disConnect() throws IOException {
        out.println("!!discon");
        clientSocket.close();
        in.close();
        out.close();
    }

    public PrintWriter getOut() {
        return out;
    }

    public BufferedReader getIn() {
        return in;
    }

    private void requests(String input) throws IOException {
        String inarr[];
        synchronized (input) {
            if (!input.contains("!!")) {
                System.out.println("Connection content wrong!!");
                return;
            }

            inarr = input.split(" ");

            if (!input.contains("!!")) {
                System.out.println("Connection content wrong!!");
                return;
            }

        }


        switch (inarr[0]) {
            case "!!send" : msgController.displayMsg(inarr[1] + " : " + inarr[3].replace("_"," "),'R'); break;

            case "!!uid" : msgController.setUID(inarr[1]); break;

            default: System.out.println(input); break;
        }
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = receiveMessage()) != null) {
                Thread.sleep(200);

                String mesg = message;
                Platform.runLater(() -> {
                    try {
                        requests(mesg);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

            }
        } catch (IOException e) {
            System.out.println("Terminated!!");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
