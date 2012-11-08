package deathmatch.main;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class Server {

	// Class variables
	int maxPlayers; //max number of players for selected map
	String nextMap; //next map that the server will change to / broadcast to clients
	ArrayList<String> classList; // list of available character classes
	ArrayList<String> mapList; // list of available maps
	HashMap<String, Boolean> satisfiedPendingClientList;
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
		//Initialize HashMaps
		clientList = new HashMap<String, Integer>();
		satisfiedPendingClientList = new HashMap<String, Boolean>();
		pendingClientList = new HashMap<String, Integer>();
		playerList = new HashMap<String, Point>();
		classList = new ArrayList<String>();
		mapList = new ArrayList<String>();
		
		//Get the list of available maps 
		getMaps();
		if(mapList == null){
			System.out.println("Unable to find maps.");
			System.exit(0);
		}
		//Get the list of available character classes
		getClasses();
		if(classList == null){
			System.out.println("Unable to find character classes.");
			System.exit(0);
		}
		//get the next map from the server
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
	/************
	 * DESCRIPTIONS - getMaps
	 * Returns a list of all maps in the maps folder. 
	 */
	private String[] getMaps(){
		String FILE_DIR = ".\\maps";
		FilenameFilter extFilter = new FilenameFilter() {
			public boolean accept(File directory, String fileName) {
			    return fileName.endsWith(".map");
			}
			};
		File mapDir = new File(FILE_DIR);
		if(mapDir.isDirectory()==false){
			System.out.println("Directory does not exists : " + FILE_DIR);
			return null;
		}
		String[] list = mapDir.list(extFilter);
		 
		if (list.length == 0) {
			System.out.println("no files end with : " + extFilter);
			return null;
		}
		else {
			for(int x = 0; x < list.length;x++){
				mapList.add(list[x].substring(0, list[x].indexOf(".")));
			}
		}
		return list;
	}
	
	/************
	 * DESCRIPTIONS - getClasses
	 * Returns a list of all classes from the classes folder
	 */
	private void getClasses(){
		String FILE_DIR = ".\\classes";
		FilenameFilter extFilter = new FilenameFilter() {
			public boolean accept(File directory, String fileName) {
			    return fileName.endsWith(".cls");
			}
			};
		File mapDir = new File(FILE_DIR);
		if(mapDir.isDirectory()==false){
			System.out.println("Directory does not exists : " + FILE_DIR);
			System.exit(0);
		}
		String[] list = mapDir.list(extFilter);
		
		if (list.length == 0) {
			System.out.println("no files end with : " + extFilter);
			System.exit(0);
		}
		else {
			for(int x = 0; x < list.length;x++){
				classList.add(list[x].substring(0, list[x].indexOf(".")));
			}
		}
	}
	
	/**********************
	 * DESCRIPTION - serverSetup
	 * Prompts the user for the first map 
	 * after displaying all available maps. 
	 * @param mapListPR
	 */
	private void serverSetup(){
		System.out.println("Choose a map:");
		for(int x = 0; x < mapList.size(); x++){
			System.out.println(mapList.get(x));
		}
		System.out.print("Enter map name: ");
		Scanner readIn = new Scanner(System.in);
		while(true){
			nextMap = readIn.next();
			if(mapList.contains(nextMap)) break;
			System.out.println("Invalid map name.");
			System.out.print("Enter map name: ");
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
					
					//Debug
					System.out.println("Waiting to receive...");
						
					DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
					serverSocket.receive(receivePacket);
					String sentence = new String(receivePacket.getData());
					String temp[] = receivePacket.getAddress().toString().split("/");
					String packetAddress = temp[1];
					System.out.println(packetAddress);
	                //Debug
					System.out.println("RECEIVED: " + sentence);
	                
	                //Game State
					//Moves to Stop Connection State when TC is sent from client
	                if(clientList.get(packetAddress+":"+Integer.toString(receivePacket.getPort())) != null){
	                	//Packet is from client currently playing
	                	
	                	//Stop Connection State
	                	//Removes client from list
	                	if(sentence.equals("TC")){
	                		// Terminate Connection from client
	                		clientList.remove(packetAddress+":"+Integer.toString(receivePacket.getPort()));
	                	}	
	                }
	                //Init State
	                //Moves to Game State when clients send playerName:className
	                else if(pendingClientList.get(packetAddress+":"+Integer.toString(receivePacket.getPort())) != null){
	                	//Packet is from pending client loading the game.
	                	
	                	String[] sa = sentence.split(":");
	                	
	                	if(sa.length == 2 && classList.contains(sa[1])){
	                		clientList.put(packetAddress+":"+Integer.toString(receivePacket.getPort()), pendingClientList.get(packetAddress+":"+Integer.toString(receivePacket.getPort())));
	                		pendingClientList.remove(packetAddress+":"+Integer.toString(receivePacket.getPort()));
	                	}
	                }
	                //NC State
	                //Moves to Init State on valid port number sent from client
	                else{
	                	//Packet is from unknown client
	                	//check if packet is a port number
                		int replyPort = Integer.parseInt(sentence.trim());
                		pendingClientList.put(packetAddress+":"+Integer.toString(receivePacket.getPort()), replyPort);
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
		
       
		
		public void run() {
			byte[] sendData = new byte[1024];
			DatagramSocket sendSocket;
			Set keys;
			Iterator iter;
			while(true){
				keys = pendingClientList.keySet();
				iter = keys.iterator();
				//Send Pulse to pendingClientList clients who have not received the map
				//Iterates through the pendingClientList 
				//    if client has not been satisfied with a send, then send the client the map
				//
				while(iter.hasNext()){
					Object o = iter.next();
					//System.out.println("Start Sending...");
					if(satisfiedPendingClientList.containsKey(o.toString())!= true){
						try {
							sendData = new byte[1024];
							int sendPort = pendingClientList.get(o);
							sendSocket = new DatagramSocket();
							String comboString[] = o.toString().split(":");
							InetAddress clientIP = InetAddress.getByName(comboString[0]);
							sendSocket.connect(clientIP, sendPort);
							sendData = new String("MP:"+nextMap).getBytes();
							System.out.println("Sending : " + sendPort);
							DatagramPacket packet = new DatagramPacket(sendData, sendData.length, clientIP, sendPort);
							sendSocket.send(packet);
							//System.out.println("Sent.");
							satisfiedPendingClientList.put(o.toString(), true);
							sendSocket.close();
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}	
					}
				}
				
				//TODO Send Pulse to clientList clients containing entity information
				
				
				try {
					Thread.sleep(200);
				} catch (InterruptedException e2) {
					e2.printStackTrace();
				}
			}
		}
	}
}
