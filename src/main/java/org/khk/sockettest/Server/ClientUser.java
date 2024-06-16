package org.khk.sockettest.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientUser implements Runnable{
    private String id;
    private String name;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ClientUser(String id, Socket socket) {
        this.id = id;
        this.socket = socket;

        try {
            out = new PrintWriter(this.socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

            out.println("!!uid " + getId());
        } catch (IOException e) {
            System.out.println("User ID : " + id + "Input/Output Error!");
            throw new RuntimeException(e);
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PrintWriter getOut() {
        return out;
    }

    public BufferedReader getIn() {
        return in;
    }

    private void sendMessage(String s_ID,String r_ID,String Message) {
        MessageServer.unicastSend(s_ID,r_ID,Message);
    }

    private void disConnect() throws IOException {
        MessageServer.removeUser(getId());
        socket.close();
        getIn().close();
        getOut().close();
    }

    private void requests(String input) throws IOException {
        if (!input.contains("!!")) {
            getOut().println("Server : Your request cannot identify");
        }
        System.out.println(input);
        String inarr[] = input.split(" ");

        switch (inarr[0]) {
            case "!!send" : sendMessage(inarr[1],inarr[2],inarr[3]); break;

            case "!!uname" : setName(inarr[1]); break;

            case "!!discon" : disConnect(); break;

            default: System.out.println(input); break;
        }
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                requests(message);
            }
        } catch (IOException e) {
            System.out.println(getId()+ " : Terminated!!");
        }
    }
}
