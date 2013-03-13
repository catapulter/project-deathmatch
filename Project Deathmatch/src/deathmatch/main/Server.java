package deathmatch.main;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class Server {

	// Class variables
	int maxPlayers; //max number of players for selected map
	String nextMap; //next map that the server will change to / broadcast to clients
	ArrayList<String> classList; // list of available character classes
	ArrayList<String> mapList; // list of available maps
	HashMap<String, Boolean> satisfiedPendingClientList;
	HashMap<String, Boolean> pendingClientList;  //Key: IPaddress:Port   Value: ReplyPort
													//Holds Pending Clients that are loading the map
	HashMap<String, Player> clientList;  //Key: IPaddress:Port   Value: ReplyPort
											//Holds Clients who are currently playing
	LinkedBlockingQueue<DatagramPacket> receiveMessages;
	private PlayerClass archer, cleric, mage, warrior;
	
	// Server Socket thingy
	DatagramSocket socket;
	
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
		//Initialize everything
		initialize();
		// Start the receive thread
		Thread receiveThread = new Thread(new ReceiveThread());
		receiveThread.start();
		
		// Start send thread
		Thread sendThread = new Thread(new SendThread());
		sendThread.start();
		
		// Read commands
		while(true) {
			//prompt server commands
			//checking queue for message
				//switch 
			String sentence = null;
			DatagramPacket headMessage;
			if(!receiveMessages.isEmpty()){
				try{
					headMessage = receiveMessages.poll();
					sentence = new String(headMessage.getData());
					String temp[] = headMessage.getAddress().toString().split("/");
					String packetAddress = temp[1];
					System.out.println(packetAddress);
	                //Debug
					System.out.println("RECEIVED: " + sentence);
					sentence = sentence.trim();
					
					//Game State
					//Moves to Stop Connection State when TC is sent from client
			        if(clientList.get(packetAddress+":"+Integer.toString(headMessage.getPort())) != null){
			        	//Packet is from client currently playing
			        	
			        	//Stop Connection State
			        	//Removes client from list
			        	if(sentence.equals("TC")){
			        		// Terminate Connection from client
			        		clientList.remove(packetAddress+":"+Integer.toString(headMessage.getPort()));
			        	}	
			        }
			        //Init State
			        //Moves to Game State when clients send playerName:className
			        else if(pendingClientList.get(packetAddress+":"+Integer.toString(headMessage.getPort())) != null){
			        	//Packet is from pending client loading the game.
			        	
			        	String[] sa = sentence.split(":");
			        	
			        	if(sa.length == 2 && classList.contains(sa[1])){
			        		PlayerClass newPlayerClass = null;
			        		if(sa[1].equals("archer")) newPlayerClass = archer;
			        		else if(sa[1].equals("mage")) newPlayerClass = mage;
			        		else if(sa[1].equals("warrior")) newPlayerClass = warrior;
			        		else if(sa[1].equals("cleric")) newPlayerClass = cleric;
			        		Player newPlayer = new Player(sa[0], newPlayerClass);
			        		clientList.put(packetAddress+":"+Integer.toString(headMessage.getPort()), newPlayer);
			        		pendingClientList.remove(packetAddress+":"+Integer.toString(headMessage.getPort()));
			        	}
			        }
			        //NC State
			        //Moves to Init State on valid port number sent from client
			        else{
			        	//Packet is from unknown client
			        	
			        	//check if packet is a port number
			        	
			    		if(sentence.equals("NC")){
				    		pendingClientList.put(packetAddress+":"+Integer.toString(headMessage.getPort()), true);
			    		}
			        }
				}catch(NumberFormatException e){
		            System.out.println("Unable to parse port from new clients packet.");
		            //Should this do anything here?
		        }
			}//if message queue is not empty
			
		}//while True
		
	}// start()
	
	// PRIVATE METHODS
	
	private void initialize(){
		//Initialize HashMaps
		clientList = new HashMap<String, Player>();
		satisfiedPendingClientList = new HashMap<String, Boolean>();
		pendingClientList = new HashMap<String, Boolean>();
		classList = new ArrayList<String>();
		mapList = new ArrayList<String>();
		receiveMessages = new LinkedBlockingQueue<DatagramPacket>();
		
		// Initialize Server Socket
		try {
			socket = new DatagramSocket(9001);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		
		
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
		
		 // Load PlayerClasses
        archer = PlayerClass.loadPlayerClass("classes/archer.cls");
        cleric = PlayerClass.loadPlayerClass("classes/cleric.cls");
        mage = PlayerClass.loadPlayerClass("classes/mage.cls");
        warrior = PlayerClass.loadPlayerClass("classes/warrior.cls");
		
	}//initialize()
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
			
			try {
				
				DatagramPacket receivePacket;
				while(true) {
					receivePacket = new DatagramPacket(receiveData, receiveData.length);
					//Debug
					System.out.println("Waiting to receive...");
					socket.receive(receivePacket);
					receiveMessages.put(receivePacket);	
				}
			} catch (SocketException e) {
				System.out.println("Unable to open a socket connection on specified port number.");
				System.exit(0);
			} catch (IOException e) {
				System.out.println("Unable to receive packet.");
			} catch(NumberFormatException e){
	            System.out.println("Unable to parse port from new clients packet.");
	            //Should this do anything here?
	        } catch (InterruptedException e) {
				System.out.println("Unable to put receivePacket into receiveMessages queue.");
			}
			
		}
	}
	
	private class SendThread
		implements Runnable {
		
       
		
		public void run() {
			byte[] sendData = new byte[1024];
			Set<String> keys;
			Iterator<String> iter;
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
							
							String comboString[] = o.toString().split(":");
							int sendPort = Integer.parseInt(comboString[1]);
							InetAddress clientIP = InetAddress.getByName(comboString[0]);
							sendData = new String("MP:"+nextMap).getBytes();
							System.out.println("Sending to " + sendPort);
							DatagramPacket packet = new DatagramPacket(sendData, sendData.length, clientIP, sendPort);
							socket.send(packet);
							System.out.println("Sent.");
							satisfiedPendingClientList.put(o.toString(), true);
							
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
