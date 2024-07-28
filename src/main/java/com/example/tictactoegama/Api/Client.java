package com.example.tictactoegama.Api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private Socket socket;
    private boolean isConnected;
    private static Client client;
    private PrintWriter sender;
    private BufferedReader reader;

    public  static Client getInstance() throws InstantiationException
    {
        if(client == null)
        {
            throw new InstantiationException("You have to call init function first.");
        }

        return client;
    }

    public static void init(String ip, int port)
    {
        client = new Client(ip,port);
    }
    private  Client(String IP, int port) {
        try {
            socket = new Socket(IP, port);
            System.out.println("Connected to server at " + IP + " on port " + port);
            isConnected = true;

        } catch (IOException e) {
            e.printStackTrace();
            isConnected = false;
        }
    }

    private Client(String IP) {
        this(IP, 5005);
    }

    public boolean isConnected() {
        return isConnected;
    }
    public Socket getSocket() {
        return socket;
    }

}
