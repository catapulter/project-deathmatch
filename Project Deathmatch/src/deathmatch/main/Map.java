package deathmatch.main;

import java.awt.Graphics2D;
import java.awt.Image;

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
	public Map(String fileName){
		//TODO
		//Load all map data from fileName (ie Test.data)
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
