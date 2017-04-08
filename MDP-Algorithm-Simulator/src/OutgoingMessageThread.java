/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author truedemon
 */

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class OutgoingMessageThread {
    //Thread t;
    Socket client;
    //Scanner scanner;
    PrintStream psWriter;
    private String message;
    boolean newMessage;
    OutgoingMessageThread(Socket client) {
        this.client = client;
        //t = new Thread(this);
        //t.start();
        this.message = "";
        this.newMessage = false;
        
    }

    /*public void run() {
        try {
            //scanner = new Scanner(System.in);
            psWriter = new PrintStream(client.getOutputStream());
            
            	if(this.newMessage == true)
            	{
	                if (this.message !=null)
	                {
	                	System.out.println(message);
	                	psWriter.write(message.getBytes(),0,message.length());
	                	this.newMessage = false;
	                }
            	}
            
        } catch (IOException e) {
            System.out.println(e);
            psWriter.close();
        }
    }*/
    
    public void sendThisMessage(String S){
    	this.message = S;
    	this.newMessage = true;
    	//this.run();
    	try {
    		psWriter = new PrintStream(client.getOutputStream());
        
	    	if(this.newMessage == true)
	    	{
	            if (this.message !=null)
	            {
	            	System.out.println(message);
	            	psWriter.write(message.getBytes(),0,message.length());

	            	this.newMessage = false;
	            }
	    	}
    
		} catch (IOException e) {
		    System.out.println(e);
		    psWriter.close();
		}
    }
    
    
}
