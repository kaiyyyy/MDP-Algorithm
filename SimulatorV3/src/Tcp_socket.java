/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class Tcp_socket {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try{
//            String ipAddress = args[0];
//            String port = args[1];
            //String ipAddress = "192.168.10.1";
        	String ipAddress = "172.21.147.246";
            String port = "5000";
            System.out.println("sending connection request to IP : " +ipAddress + " PORT : " +port);

            Socket client = new Socket(ipAddress,Integer.parseInt(port));
            System.out.println("successfully connected.");
            new IncomingMessageThread(client);
            OutgoingMessageThread out = new OutgoingMessageThread(client);
            
            for(int i = 2; i <= 10 ; i++)
            {
            	out.sendThisMessage(Integer.toString(i));
            	TimeUnit.MILLISECONDS.sleep(10);
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    
}
