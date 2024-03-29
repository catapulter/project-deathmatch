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
	private BufferStrategy strategy;
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
	
	private static String GAME_STATE = "game";
	private static String NEW_STATE = "new";
	private static String MAP_STATE = "map";
	private static String NAME_STATE = "name";
	
	private PlayerClass archer, cleric, mage, warrior;
	private Player player;
	
	// CONSTRUCTORS
	
	public Client(GraphicsDevice defaultScreen) {
		this.graphicsDevice = defaultScreen;
	}

	public static void main(String[] args) {
		
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice defaultScreen = env.getDefaultScreenDevice();
		
		Client main = new Client(defaultScreen);
		main.start();
	}// main()
	
	// PUBLIC METHODS
	
	/* start()
	 *  Description - Called at the start of the program
	 */
	public void start() {
		
		// Load Player Classes
		loadPlayerClasses();
		
		// Load configuration file
		loadConfig("config.cfg");
		
		// Initialize variables
		initialize();
		
		// start server receive thread
		socket.connect(serverIP, serverPort);
		Thread receiveThread = new Thread(new ReceiveThread());
		receiveThread.start();
		
		byte[] sendData;
		
		// establish connection
		if(state.equals(NEW_STATE)) {
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
			
			state = MAP_STATE;
		}
						
		// Loop variables
		DatagramPacket packet;
		String message;
		
		while (true) {
			
			// Check if there are messages to process
			if(!receivedMessages.isEmpty()) {
				
				packet = receivedMessages.poll();
				DatagramPacket sendPacket;
				message = new String(packet.getData());
				
				if(state.equals(MAP_STATE)) {
					
					// Load map
					map = loadMap("maps/" + message.split(":")[1].trim() + ".map");
					
					// TODO
					// Just send a name and playerClass
					sendData = new String(player.getName() + ":" + player.getPlayerClass().getClassType()).getBytes();
					sendPacket = new DatagramPacket(sendData, sendData.length, serverIP, serverPort);
					try {
						socket.send(sendPacket);
					} catch (IOException e) {
						e.printStackTrace();
					}
					
//					state = NAME_STATE;
					state = GAME_STATE;
				
				} else if(state.equals(NAME_STATE)) {
					
					
					
				}
			}
			
			
			
	        if(state.equals(GAME_STATE)) {
				// Graphics manipulation loop
	        	System.out.println("FUCK!@!!!!!");
	        	
	        	Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
	        	
				map.drawBottom(g, 0, 0);
				
				map.drawTop(g, 0, 0);
				
				g.dispose();
				strategy.show();
//				state = "FUCK";
	        }
			
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
                	System.out.println(screenWidth + " x " + screenHeight);
                } else if(sa[0].trim().equals("name:class")) {
                	sa = sa[1].split(":");
                	sa[0] = sa[0].trim();
                	sa[1] = sa[1].trim();
                	if(sa[1].equals("archer")) {
                		player = new Player(sa[0], archer);
                	} else if(sa[1].equals("cleric")) {
                		player = new Player(sa[0], cleric);
                	} else if(sa[1].equals("mage")) {
                		player = new Player(sa[0], mage);
                	} else if(sa[1].equals("warrior")) {
                		player = new Player(sa[0], warrior);
                	}
                }
            }
		} catch (Exception e) {
			System.out.println("Could not load config file.");
			e.printStackTrace();
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
		
		// Initialize Canvas object
		canvas = new Canvas();
		canvas.setIgnoreRepaint(true);
		canvas.setBounds(0, 0, screenWidth, screenHeight);
		canvas.setBackground(Color.black);
		canvas.setVisible(true);
		
		// Initialize JFrame object
		frame = new JFrame("Project Deathmatch ~ Alpha");
		
//		frame.setSize(800,800);
		
//        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setIgnoreRepaint(true);
//        frame.setVisible(true);
        frame.setResizable(!graphicsDevice.isFullScreenSupported());
//        graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        
        if(graphicsDevice.isFullScreenSupported()) {
//            try {
                graphicsDevice.setFullScreenWindow(frame);
                canvas.validate();
//            }catch(Exception e){e.printStackTrace();}
        } else {
        	frame.setVisible(true);
        }
//        if(graphicsDevice.isDisplayChangeSupported()) {
//            try {
//                graphicsDevice.setDisplayMode(newDisplayMode);
//            }catch(Exception e){e.printStackTrace();}
//        }
        
        newDisplayMode = new DisplayMode(screenWidth, screenHeight, 32, 60);
        graphicsDevice.setDisplayMode(newDisplayMode);
        canvas.setSize(newDisplayMode.getWidth(), newDisplayMode.getHeight());
        
        // Add canvas to the frame and create buffer strategy
        frame.add(canvas);
		canvas.createBufferStrategy(2);
        strategy = canvas.getBufferStrategy();
        canvas.requestFocus();
		graphics = (Graphics2D) strategy.getDrawGraphics();
		
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
	
	/* loadPlayerClasses(String filename) {
	 * 	Description - Loads the class file 'filename'
	 */
	private void loadPlayerClasses() {
		archer = PlayerClass.loadPlayerClass("classes/archer.cls");
        cleric = PlayerClass.loadPlayerClass("classes/cleric.cls");
        mage = PlayerClass.loadPlayerClass("classes/mage.cls");
        warrior = PlayerClass.loadPlayerClass("classes/warrior.cls");
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
					System.out.println("Received " + received.toString().trim());
				}
			} catch (SocketException e) {
				System.out.println("Unable to open a socket connection on specified port number.");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("Unable to receive packet.");
				e.printStackTrace();
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
