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
import java.net.Socket;
import java.util.NoSuchElementException;
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

	// Boolean to indicate whether reading is needed
	// Incoming message semaphore will only be signaled when getReading is true;
	private boolean getReading = true;

	private ProgressControl control;

	IncomingMessageThread(Socket client, ProgressControl control) {
		this.client = client;
		this.getReading = true;
		this.control = control;

		t = new Thread(this);
		t.start();

	}

	public void disableReading() {
		this.getReading = false;
	}

	public void enableReading() {
		this.getReading = true;
	}

	public void run() {
		try {
			scanner = new Scanner(client.getInputStream());
			while (waiting) {
				temp = scanner.nextLine();
				System.out.println(client.getInetAddress() + " : " + temp);

				if (temp.equals("explore")) {
					// start navigation
				} else if (temp.equals("shortest")) {
					// start shortest
				} else if (temp.substring(0, 1).equals("x")) {
					// get reading

					// resume Thread
					int size = temp.length();
					if (this.getReading) {
						GlobalVariables.sensorInput = temp.substring(2, size);
						control.signalIncoming();
					}
				}
			}
		} catch (IOException | NoSuchElementException e) {
			// scanner.close();

			System.out.println("reconnecting");
			Tcp_socket reconnect = null;
			try {
				reconnect = new Tcp_socket();
			} catch (NumberFormatException | InterruptedException | IOException e1) {

			}
			new IncomingMessageThread(reconnect.client, control);
			Thread.currentThread().stop();

		}
	}

	public String getMessage() throws IOException {

		scanner = new Scanner(client.getInputStream());
		String message = scanner.nextLine();
		return message;
	}

}