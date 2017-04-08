/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.Socket;

/**
 *
 * @author truedemon
 */
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class IncomingMessageThread implements Runnable {
    Thread t;
    Socket client;
    private boolean connectionStatus;
    private boolean waiting = true;
    private boolean startRun = false;
    Scanner scanner = null;
    String temp = "";
    AlgoSimulator lol = null;
    IncomingMessageThread(Socket client) {
            this.client = client;
            
            t = new Thread(this);
            t.start();

    }
    
    public void run() {
        try {
            scanner = new Scanner(client.getInputStream());
            while (waiting) {
            	temp = scanner.nextLine();
            	System.out.println(client.getInetAddress()+" : " + temp);
            	
            	if(temp.equals("explore"))
            	{
            		//start navigation
            	}
            	else if(temp.equals("shortest"))
            	{
            		//start shortest
            	}
            	else if(temp.equals("response"))
            	{
            		//resume Thread
            	}
            	else if(temp.equals("sensor"))
            	{
            		//update sensor
            	}
            }
        } catch (IOException e) {
            System.out.println(e);
            scanner.close();
        }
    }
    
    public String getMessage() throws IOException {  
    	
    	scanner = new Scanner(client.getInputStream());
    	String message = scanner.nextLine();
		return message;
    }
    
    
}