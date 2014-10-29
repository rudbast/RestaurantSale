/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restaurantsale.Tools;

import java.net.*;
import java.io.*;
import restaurantsale.GUI.MainServer;

/**
 *
 * @author rudolf
 */
public class TCPServer extends Thread {

    private final int PORT = 6789;
    private final MainServer mainServer;

    public TCPServer(MainServer mainServer) {
        this.mainServer = mainServer;
    }

    @Override
    public void run() {
        try{
            ServerSocket listenSocket = new ServerSocket(PORT);

            while(true) {
                Socket clientSocket = listenSocket.accept();
                mainServer.checkClientIP(clientSocket.getInetAddress().toString().split("/")[1]);
                Connection c = new Connection(clientSocket, mainServer);
            }
        } catch(IOException e) {
            System.out.println("Listen: " + e.getMessage());
        }
    }
}

class Connection extends Thread {
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
    MainServer mainServer;

    public Connection (Socket aClientSocket, MainServer mainServer) {
        try {
            clientSocket = aClientSocket;
            this.mainServer = mainServer;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream( clientSocket.getOutputStream());

            java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    start();
                }
            });
        } catch(IOException e) {
            System.out.println("Connection: "+ e.getMessage());
        }
    }

    @Override
    public void run(){
        try {
            String msg = in.readUTF();
            String reply = RequestHandler.decodeMessage(msg);
            switch (msg.split(";")[0]) {
                case "ORD":
//                    mainServer.updateTableQueue(reply);
                    mainServer.sendToAllClient(RequestHandler.decodeMessage("QUE"));
                    break;
//                case "QUE":
//                    mainServer.sendToAllClient(reply);
//                    break;
            }

            out.writeUTF(reply);
        } catch(EOFException e) {
            System.out.println("EOF: "+ e.getMessage());
        } catch(IOException e) {
            System.out.println("IO:s a"+ e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e){}
        }
    }
}