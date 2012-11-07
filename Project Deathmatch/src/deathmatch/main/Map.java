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
	
	//xxx
	
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
	public void setMapImage(Image img){
		mapImage = img;
	}
	public void setMapTopImage(Image img){
		mapTopImage = img;
	}
	public void setMiniMapImage(Image img){
		miniMapImage = img;
	}
	public void setThumbnail(Image img){
		thumbnail = img;
	}
	public CollisionMap getMapCollision(){
		return mapCollision;
	}
	public void setMapCollision(CollisionMap colMap){
		mapCollision = colMap;
	}
	public int getRecommendedPlayers(){
		return recommendedPlayers;
	}
	public void setRecommendedPlayers(int myInt){
		recommendedPlayers = myInt;
	}
	public SpawnPoint[] getSpawnPoints(){
		return spawnPoints;
	}
	public void setSpawnPoints(SpawnPoint[] list){
		spawnPoints = list;
	}
	public ControlPoint[] getControlPoints(){
		return controlPoints;
	}
	public void setControlPoints(ControlPoint[] list){
		controlPoints = list;
	}
	public FlagPoint[] getFlagPoints(){
		return flagPoints;
	}
	public void setFlagPoints(FlagPoint[] list){
		flagPoints = list;
	}
	public String getAmbientSound(){
		return ambientSound;
	}
	public void setAmbientSound(String sound){
		ambientSound = sound;
	}
	public String getMapName(){
		return mapName;
	}
	public void setMapName(String name){
		mapName = name;
	}
	
	/*****************
	 * Description - loadMapVariables
	 * Loads the current Map with all of its 
	 * variables from a fileName passed in.
	 */
	private void loadMapVariables(String fileName){
		
	}
	
	//private draw(Graphics2D graphics){
		//draw Map
	//}
}
