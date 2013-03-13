package deathmatch.main;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Map {
	
	//Member Variables
	
	private Image mapImage;
	private Image mapTopImage;
	private Image miniMapImage;
	private Image thumbnail;
	private CollisionMap mapCollision;
	private int recommendedPlayers;
	private SpawnPoint[] spawnPoints;
	private ControlPoint[] controlPoints;
	private FlagPoint[] flagPoints;
	private String ambientSound;
	private String mapName;
	
//	   ______                 __                  __            
//	  / ____/___  ____  _____/ /________  _______/ /_____  _____
//	 / /   / __ \/ __ \/ ___/ __/ ___/ / / / ___/ __/ __ \/ ___/
//	/ /___/ /_/ / / / (__  ) /_/ /  / /_/ / /__/ /_/ /_/ / /    
//	\____/\____/_/ /_/____/\__/_/   \__,_/\___/\__/\____/_/     
	/*****************
	 * Description - Map Constructor
	 * Loads the current Map with all of its 
	 * variables from a fileName passed in.
	 */
	public Map(String filename){
		//TODO
		//Load all map data from fileName (ie Test.data)
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			
			Image sourceImage = null;
			String line;
			String[] sa;
			while((line = br.readLine()) != null) {
				sa = line.split("=");
				sa[0] = sa[0].trim();
				sa[1] = sa[1].trim();
				
				if(sa[0].equals("mapImage")) {
					try {
			            sourceImage = ImageIO.read(new File("Images/Maps"+sa[1]));
			        }catch(IOException e){e.printStackTrace();}
					GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
			        mapImage = gc.createCompatibleImage(sourceImage.getWidth(null), sourceImage.getHeight(null), Transparency.BITMASK);
			        mapImage.getGraphics().drawImage(sourceImage, 0, 0, null);
				} else if(sa[0].equals("mapTopImage")) {
					try {
			            sourceImage = ImageIO.read(new File("Images/Maps"+sa[1]));
			        }catch(IOException e){e.printStackTrace();}
					GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
			        mapTopImage = gc.createCompatibleImage(sourceImage.getWidth(null), sourceImage.getHeight(null), Transparency.BITMASK);
			        mapTopImage.getGraphics().drawImage(sourceImage, 0, 0, null);
				} else if(sa[0].equals("miniMapImage")) {
					try {
			            sourceImage = ImageIO.read(new File("Images/Maps"+sa[1]));
			        }catch(IOException e){e.printStackTrace();}
					GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
			        miniMapImage = gc.createCompatibleImage(sourceImage.getWidth(null), sourceImage.getHeight(null), Transparency.BITMASK);
			        miniMapImage.getGraphics().drawImage(sourceImage, 0, 0, null);
				} else if(sa[0].equals("thumbnail")) {
					try {
			            sourceImage = ImageIO.read(new File("Images/Maps"+sa[1]));
			        }catch(IOException e){e.printStackTrace();}
					GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
					thumbnail = gc.createCompatibleImage(sourceImage.getWidth(null), sourceImage.getHeight(null), Transparency.BITMASK);
					thumbnail.getGraphics().drawImage(sourceImage, 0, 0, null);
				} else if(sa[0].equals("recommendedPlayers")) {
					recommendedPlayers = Integer.parseInt(sa[1]);
				} else if(sa[0].equals("ambientSound")) {
					ambientSound = sa[1];
				}
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Member Functions
	
	public Image getMapImage(){
		return mapImage;
	}
	public Image getMapTopImage(){
		return mapTopImage;
	}
	public Image getMiniMapImage(){
		return miniMapImage;
	}
	public Image getThumbnail(){
		return thumbnail;
	}
	public CollisionMap getMapCollision(){
		return mapCollision;
	}
	public int getRecommendedPlayers(){
		return recommendedPlayers;
	}
	public SpawnPoint[] getSpawnPoints(){
		return spawnPoints;
	}
	public ControlPoint[] getControlPoints(){
		return controlPoints;
	}
	public FlagPoint[] getFlagPoints(){
		return flagPoints;
	}
	public String getAmbientSound(){
		return ambientSound;
	}
	public String getMapName(){
		return mapName;
	}
	public int getWidth(){
		return mapImage.getWidth(null);
	}
	public int getHeight(){
		return mapImage.getHeight(null);
	}
	
//	    ____                         __  ___     __  __              __    
//	   / __ \_________ __      __   /  |/  /__  / /_/ /_  ____  ____/ /____
//	  / / / / ___/ __ `/ | /| / /  / /|_/ / _ \/ __/ __ \/ __ \/ __  / ___/
//	 / /_/ / /  / /_/ /| |/ |/ /  / /  / /  __/ /_/ / / / /_/ / /_/ (__  ) 
//	/_____/_/   \__,_/ |__/|__/  /_/  /_/\___/\__/_/ /_/\____/\__,_/____/  
	
	
	/* drawBottom(Graphics2D graphics, int xPR, int yPR)
	 * 
	 */
	public void drawBottom(Graphics2D graphics, int xPR, int yPR) {
		
		graphics.drawImage(mapImage, 0, 0, null);
	}
	
	/* drawTop(Graphics2D graphics, int xPR, int yPR)
	 * 
	 */
	public void drawTop(Graphics2D graphics, int xPR, int yPR) {
		
		graphics.drawImage(mapTopImage, 0, 0, null);
	}
	
	/* drawMiniMap(Graphics2D graphics, int xPR, int yPR)
	 * 
	 */
	public void drawMiniMap(Graphics2D graphics, int xPR, int yPR) {
		
		graphics.drawImage(mapTopImage, 0, 0, null);
	}
	
}
