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
    Scanner scanner = null;
    IncomingMessageThread(Socket client) {
            this.client = client;
            
            t = new Thread(this);
            t.start();

    }
    
    public void run() {
        try {
            scanner = new Scanner(client.getInputStream());
            while (true) {
                if(scanner.nextLine().equals("start\n"))
                {
                	String[] args = null;
					AlgoSimulator.main(args);
                }
            	System.out.println(client.getInetAddress()+" : " + scanner.nextLine());
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