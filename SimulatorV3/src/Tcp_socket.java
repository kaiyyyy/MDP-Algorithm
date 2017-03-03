/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class Tcp_socket {
	
	public IncomingMessageThread incoming = null;
	public OutgoingMessageThread outgoing = null;
	public static Socket client = null;
    public Tcp_socket(){
    	String ipAddress = "172.21.147.246";
        String port = "5000";
        System.out.println("sending connection request to IP : " +ipAddress + " PORT : " +port);
        
		
		try {
			client = new Socket(ipAddress,Integer.parseInt(port));
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        System.out.println("successfully connected.");
        
     
        
    	
    }
        // TODO code application logic here
    public static void main(String[] args) throws InterruptedException {
    	/*
    	 * use this code block to start communication thread
    	 * 
    	 * 
    	 */
    	Tcp_socket a = new Tcp_socket();
    	Thread in, out;
    	a.incoming = new IncomingMessageThread(client);
        a.outgoing = new OutgoingMessageThread(client);

      
        TimeUnit.MILLISECONDS.sleep(10);
        a.outgoing.sendThisMessage("hello");
        
            
    		
    		
    }
    
}
