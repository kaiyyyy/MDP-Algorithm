
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

public class Tcp_socket {

	public IncomingMessageThread incoming = null;
	public OutgoingMessageThread outgoing = null;
	public static Socket client = null;
	public boolean connected = false;

	public Tcp_socket() throws InterruptedException, NumberFormatException, UnknownHostException, IOException {
		String ipAddress = "172.21.147.246";
		String port = "5000";
		System.out.println("sending connection request to IP : " + ipAddress + " PORT : " + port);

		while (!connected) {
			try {

				System.out.println("Attempting connection...");
				TimeUnit.MILLISECONDS.sleep(1000);
				client = new Socket(ipAddress, Integer.parseInt(port));
				System.out.println("successfully connected.");
				connected = true;
			} catch (ConnectException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				System.out.println("connection failed");
			}

		}

	}

	// TODO code application logic here
	/*
	 * public static void main(String[] args) throws InterruptedException,
	 * NumberFormatException, UnknownHostException, IOException {
	 * 
	 * Tcp_socket a = new Tcp_socket(); Thread in, out; a.incoming = new
	 * IncomingMessageThread(client); a.outgoing = new
	 * OutgoingMessageThread(client);
	 * 
	 * TimeUnit.MILLISECONDS.sleep(10); a.outgoing.sendThisMessage("hello");
	 * 
	 * }
	 */

}
