package deathmatch.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JFrame;

public class Client 
		implements KeyListener,
		MouseListener,
		MouseMotionListener{

	// Class variables
    private JFrame frame;
    static GraphicsDevice graphicsDevice;
    static DisplayMode newDisplayMode;
    
	private Canvas canvas;
	private BufferStrategy buffer;
    private Graphics2D graphics;
	private int screenWidth, screenHeight;
	
	private String gameMode;
	private ArrayList<Entity> entities;
	private Map map;
	private int mapWidth, mapHeight;
	
	private InetAddress serverIP;
	private int serverPort;
	
	private DatagramSocket socket;
	private LinkedBlockingQueue<DatagramPacket> receivedMessages;
	private String state;
	
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
		
		// Initialize variables
		initialize();
		
		// Load configuration file
		loadConfig("config.cfg");
		
		// start server receive thread
		Thread receiveThread = new Thread(new ReceiveThread());
		receiveThread.start();
		
		byte[] sendData;
		
		// establish connection
		if(state.equals("new")) {
			try {
				sendData = new String("NC").getBytes();
				DatagramPacket packet = new DatagramPacket(sendData, sendData.length, serverIP, serverPort);
				socket.send(packet);
				
				// Debugging Code
				System.out.println("Sent.");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			state = "map";
		}
						
		// Loop variables
		DatagramPacket packet;
		String message;
		
		while (true) {
			
			// Check if there are messages to process
			if(!receivedMessages.isEmpty()) {
				
				packet = receivedMessages.poll();
				message = new String(packet.getData());
				
				if(state.equals("map")) {
					
					map = loadMap(message.split(":")[1].trim());
					
					
				}
			}
			
			
			
	        
			// Graphics manipulation loop
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
                } else if(sa[0].trim().equals("resolution")) {
                	sa = sa[1].split("x");
                	screenWidth = Integer.parseInt(sa[0].trim());
                	screenHeight = Integer.parseInt(sa[1].trim());
                }
            }
		} catch (Exception e) {
			System.out.println("Could not load config file.");
			System.exit(0);
		}
	}
	
	/* initialize()
	 *  Description - Initializes class variables
	 */
	private void initialize() {
		
		receivedMessages = new LinkedBlockingQueue<DatagramPacket>();
		
		state = "new";
		
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			System.out.println("Unable to open socket.");
			e.printStackTrace();
		}
		socket.connect(serverIP, serverPort);
		
		// Initialize Canvas object
		canvas = new Canvas();
		canvas.setIgnoreRepaint(true);
		canvas.setBounds(0, 0, screenWidth, screenHeight);
		canvas.setBackground(Color.black);
		canvas.setVisible(true);
		
		// Initialize JFrame object
		frame = new JFrame("Project Deathmatch ~ Alpha");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setIgnoreRepaint(true);
        frame.setVisible(true);
        graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        newDisplayMode = new DisplayMode(screenWidth, screenHeight, 32, 60);
        if(graphicsDevice.isFullScreenSupported()) {
            try {
                graphicsDevice.setFullScreenWindow(frame);
            }catch(Exception e){e.printStackTrace();}
        }
        if(graphicsDevice.isDisplayChangeSupported()) {
            try {
                graphicsDevice.setDisplayMode(newDisplayMode);
            }catch(Exception e){e.printStackTrace();}
        }
        
        // Add canvas to the frame and create buffer strategy
        frame.add(canvas);
		canvas.createBufferStrategy(2);
        buffer = canvas.getBufferStrategy();
        canvas.requestFocus();
		
		// Add action listeners to Client
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        canvas.addKeyListener(this);
	}
	
	/* loadMap(String filename)
	 *  Description - Loads a map give the string filename
	 */
	private Map loadMap(String filename) {
		
		// TODO Write code to read in map file
		return new Map(filename);
	}
	
	// THREAD CLASSES
	
	private class ReceiveThread
		implements Runnable {
		
		byte[] receiveData = new byte[1024];
		
		public void run() {
			System.out.println("Waiting to receive...");
			DatagramPacket receivedPacket;
			
			// Debuggin Variable
			String received;
			
			try {
				while(true) {
					receivedPacket = new DatagramPacket(receiveData, receiveData.length);
					socket.receive(receivedPacket);
					receivedMessages.put(receivedPacket);
					
					// Debugging Code
					received = new String(receivedPacket.getData());
					System.out.println("Received " + received.toString());
				}
			} catch (SocketException e) {
				System.out.println("Unable to open a socket connection on specified port number.");
				System.exit(0);
			} catch (IOException e) {
				System.out.println("Unable to receive packet.");
			} catch (InterruptedException e) {
				System.out.println("Unable to put packet into receivedMessages queue.");
				e.printStackTrace();
			}
		}
	}
	
	/*private class SendThread
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

	// ACTION LISTENERS
	
	@Override
	public void mouseDragged(MouseEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent event) {
		// TODO Auto-generated method stub
		
	}
	
}
