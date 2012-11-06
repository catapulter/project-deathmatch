package deathmatch.main;

import java.awt.Canvas;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

public class Client {

	// Class variables
	private Canvas canvas;
	private String gameMode;
	private ArrayList<Entity> entities;
	private Map map;
	
	private InetAddress serverIP;
	private int serverPort;
	
	private DatagramSocket sendSocket;
	
	// CONSTRUCTORS
	
	public static void main(String[] args) {
		
		Client main = new Client();
		main.start();
	}// main()
	
	// PUBLIC METHODS
	
	/* start()
	 *  Description - Called at the start of the program
	 */
	public void start() {
		
		// Load configuration file
		loadConfig("config.cfg");
				
		// start server receive thread
		int receivePort = 0;
		DatagramSocket receiveSocket;
		try {
			receiveSocket = new DatagramSocket();
			receivePort = receiveSocket.getLocalPort();
			Thread ReceiveThread = new Thread(new ReceiveThread(receiveSocket));
		} catch (SocketException e) {
			System.out.println("Error establishing recieveSocket.");
			System.exit(0);
		}
		
		// establish connection
		
		byte[] sendData;
		try {
			sendSocket = new DatagramSocket();
			sendSocket.connect(serverIP, serverPort);
			sendData = new String("NC" + Integer.toString(receivePort)).getBytes();
			System.out.println("Sending : " + sendData);
			DatagramPacket packet = new DatagramPacket(sendData, sendData.length, serverIP, serverPort);
			sendSocket.send(packet);
			System.out.println("Sent.");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}// start()
	
	
	// PRIVATE METHODS
	
	/* loadConfig()
	 *  Description - Loads a config file with client variables
	 */
	private void loadConfig(String fileName) {
		
		BufferedReader file;
		String temp;
		
		try {
			file = new BufferedReader(new FileReader(fileName));
			
			while((temp = file.readLine()) != null) {
                String[] sa = temp.split("=");
                
                if(sa[0].trim().equals("serverAddress")) {
                	sa = sa[1].split(":");
                	serverIP = InetAddress.getByName(sa[0].trim());
                	serverPort = Integer.parseInt(sa[1].trim());
                	System.out.println("Server IP = " + serverIP);
                	System.out.println("Server Port = " + serverPort);
                }
            }
		} catch (Exception e) {
			System.out.println("Could not load config file.");
			System.exit(0);
		}
		
	}
	
	// THREAD CLASSES
	
	private static class ReceiveThread
		implements Runnable {
		
		byte[] receiveData = new byte[1024];
		DatagramSocket clientSocket;
		
		public ReceiveThread(DatagramSocket receiveSocket) {
			
			clientSocket = receiveSocket;
		}
		
		public void run() {
			
			try {
				while(true) {
					DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
					clientSocket.receive(receivePacket);
					String sentence = new String(receivePacket.getData());
	                System.out.println("RECEIVED: " + sentence);
				}
			} catch (SocketException e) {
				System.out.println("Unable to open a socket connection on specified port number.");
				System.exit(0);
			} catch (IOException e) {
				System.out.println("Unable to receive packet.");
			}
			
		}
	}
	
	
	/*private static class SendThread
		implements Runnable {
		
	    byte[] sendData = new byte[1024];
	    DatagramSocket clientSocket;
	    
	    public SendThread(int port) {
	    	
	    	try {
				clientSocket = new DatagramSocket(port);
	    	} catch (SocketException e) {
				System.out.println("Unable to open a socket connection on specified port number.");
				System.exit(0);
			}
	    }
	    
	    public void run() {
						
			try {
								
				while(true) {
					DatagramPacket receivePacket = new DatagramPacket(sendData, sendData.length);
					clientSocket.send(receivePacket);
					String sentence = new String(receivePacket.getData());
	                System.out.println("RECEIVED: " + sentence);
				}
			} catch (IOException e) {
				System.out.println("Unable to receive packet.");
			}
			
		}
	}*/
	
}
