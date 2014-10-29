/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restaurantsale.Tools;

import java.io.*;
import java.net.*;

/**
 *
 * @author rudolf
 */
public class TCPClient {

    private final String SERVER_IP;
    private final int PORT = 6789;

    public TCPClient(String serverIP) {
        this.SERVER_IP = serverIP;
    }

    public String sendMessage(String message) {
        Socket s = null;
        String reply = "";
        try{
            s = new Socket(SERVER_IP, PORT);
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            out.writeUTF(message); // UTF is a string encoding
            reply = in.readUTF();

            s.close();
        } catch (UnknownHostException e){
            System.out.println("Sock: "+e.getMessage());
        } catch (EOFException e){
            System.out.println("EOF: "+e.getMessage());
        } catch (IOException e){
            System.out.println("IO: "+e.getMessage());
        } finally {
            if(s!=null)
                try {
                    s.close();
                } catch (IOException e){}
        }
        return reply;
    }
}