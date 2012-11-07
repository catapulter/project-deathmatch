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
	
	//Constructor
	/*****************
	 * Description - Map Constructor
	 * Loads the current Map with all of its 
	 * variables from a fileName passed in.
	 */
	Map(String fileName){
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
	
	
	//private draw(Graphics2D graphics){
		//draw Map
	//}
}
