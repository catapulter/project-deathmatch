package deathmatch.main;

import java.awt.Canvas;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Server {

	// Class variables
	int maxPlayers;
	HashMap<String, Integer> pendingClientList;  //Key: IPaddress:Port   Value: ReplyPort
													//Holds Pending Clients that are loading the map
	HashMap<String, Integer> clientList;  //Key: IPaddress:Port   Value: ReplyPort
											//Holds Clients who are currently playing
	HashMap<String, Point> playerList;	  //Key: IPaddress:Port   Value: Character Point Location
											//Holds Client character locations
	
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
		
		serverSetup();
		// Start the receive thread
		Thread receiveThread = new Thread(new ReceiveThread());
		receiveThread.start();
		
		// Start send thread
		Thread sendThread = new Thread(new SendThread());
		sendThread.start();
		
		// Read commands
		while(true) {
			//prompt server commands
			
		}
		
	}// start()
	
	
	// PRIVATE METHODS

	private void serverSetup(){
		System.out.print("Choose a map:\n" +
							"	1 - Test\n" +
							"	2 - Default\n" +
							"	0 - Quit\n" +
							"> ");
		Scanner readIn = new Scanner(System.in);
		readIn.nextInt();
		if(readIn.equals(1)){
			
		}
		else if(readIn.equals(2)){
			
		}
		else if(readIn.equals(0)){
			System.exit(0);
		}
		readIn.close();
		
	}//serverSetup()
	// THREAD CLASSES
	
	private class ReceiveThread
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
	                
	                
	                if(clientList.get(receivePacket.getAddress().toString()+Integer.toString(receivePacket.getPort())) != null){
	                	//Packet is from client currently playing
	                	
	                	if(sentence.equals("TC")){
	                		// Terminate Connection from client
	                		clientList.remove(receivePacket.getAddress().toString()+Integer.toString(receivePacket.getPort()));
	                	}	
	                }
	                else if(pendingClientList.get(receivePacket.getAddress().toString()+Integer.toString(receivePacket.getPort())) != null){
	                	//Packet is from pending client loading the game.
	                }
	                else{
	                	//Packet is from unknown client
	                	//check if packet is a port number
                		int replyPort = Integer.parseInt(sentence);
                		pendingClientList.put(receivePacket.getAddress().toString()+Integer.toString(receivePacket.getPort()), replyPort);
	                }
	              
	                
				}
			} catch (SocketException e) {
				System.out.println("Unable to open a socket connection on specified port number.");
				System.exit(0);
			} catch (IOException e) {
				System.out.println("Unable to receive packet.");
			} catch(NumberFormatException e){
	            System.out.println("Unable to parse port from new clients packet.");
	            //Should this do anything here?
	        }
			
		}
	}
	
	private class SendThread
		implements Runnable {
		
        byte[] sendData = new byte[1024];
		
		public void run() {
			
			
		}
	}
}
