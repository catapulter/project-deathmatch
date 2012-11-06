package deathmatch.main;

import java.awt.Canvas;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

public class Server {

	// Class variables
	int maxPlayers;
	
	
	// CONSTRUCTORS
	
	public static void main(String[] args) {
		
		Server main = new Server();
		main.start();
	}// main()
	
	// PUBLIC METHODS
	
	/* start()
	 *  Description - Called at the start of the program
	 */
	public void start() {
		
		
		// Start the receive thread
		Thread receiveThread = new Thread(new ReceiveThread());
		receiveThread.start();
		
		// Start send thread
		Thread sendThread = new Thread(new SendThread());
		sendThread.start();
		
		// Read commands
		while(true) {
			
			
		}
		
	}// start()
	
	
	// PRIVATE METHODS
	


	// THREAD CLASSES
	
	private static class ReceiveThread
		implements Runnable {
		
		byte[] receiveData = new byte[1024];
		
		public void run() {
			
			DatagramSocket serverSocket;
			
			try {
				serverSocket = new DatagramSocket(9001);
				
				while(true) {
					
					System.out.println("Waiting to receive...");
					DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
					serverSocket.receive(receivePacket);
					String sentence = new String(receivePacket.getData());
	                System.out.println("RECEIVED: " + sentence);
	                
	                if(sentence.substring(0, 2).equals("NC")) {
	                	// New connection received
	                }
				}
			} catch (SocketException e) {
				System.out.println("Unable to open a socket connection on specified port number.");
				System.exit(0);
			} catch (IOException e) {
				System.out.println("Unable to receive packet.");
			}
			
		}
	}
	
	private static class SendThread
		implements Runnable {
		
        byte[] sendData = new byte[1024];
		
		public void run() {
			
			
		}
	}
}
