package org.khk.sockettest.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.HashMap;
import java.util.Map;


public class MessageServer {
    public static final String IP = "localhost";
    public static final int PORT = 4444;

    //                               comment sender receiver messageContent
    private static final String SEND_Format = "!!send %s %s %s";
    private static final int MAX_ID = 5;

    private static SocketAddress socketAddress;
    private static ServerSocket msgServer;
    private static PrintWriter out;
    private static BufferedReader in;
    private static Map<String,ClientUser> allUserList;
    private static Map<String,ClientUser> activeUserList;

    public static void ServerStart(String ip,int port) throws IOException {
        msgServer = new ServerSocket();
        socketAddress = new InetSocketAddress(InetAddress.getByName(ip),port);
        msgServer.bind(socketAddress);

        System.out.println("Server address : " + msgServer.getInetAddress() + " : " + msgServer.getLocalPort());

        allUserList = new HashMap<>();
        activeUserList = new HashMap<>();

        while (true) {
            Socket clientsocket = msgServer.accept();
            System.out.println("New Client Connected: " + clientsocket);

            // Unit Id Create
            String id = GeneratId();

            //New Request Added
            allUserList.put(id,new ClientUser(id,clientsocket));
            activeUserList.put(id,new ClientUser(id,clientsocket));

            Thread thread = new Thread(activeUserList.get(id));
            thread.start();
        }
    }

    public static void unicastSend(String s_ID, String r_ID, String Message) {
        activeUserList.forEach((id, clientUser) -> {
            if (r_ID.equals(id)) {
                synchronized (clientUser) {
                    clientUser.getOut().println(String.format(MessageServer.SEND_Format, activeUserList.get(s_ID).getName(), r_ID, Message));
                }
            }
        });
    }

    public static void broadcastSend(String s_ID,String Message) {
        activeUserList.forEach((id, clientUser) -> {
            synchronized (clientUser) {
                clientUser.getOut().println(String.format(MessageServer.SEND_Format,s_ID,id,Message));
            }
        });
    }


    public static void removeUser(String u_ID) {
        activeUserList.remove(u_ID);
        System.out.println("User ID : " + u_ID + "is removed!");
    }

    public static void ServerStop() throws IOException {
        msgServer.close();
    }

    private static String GeneratId() {
        if (allUserList.size() < MAX_ID) {
            return String.format("C%03d",allUserList.size() + 1);
        }else {
            return "Max";
        }
    }

    public static void main(String[] args) throws IOException {
        ServerStart(MessageServer.IP,MessageServer.PORT);
    }
}
