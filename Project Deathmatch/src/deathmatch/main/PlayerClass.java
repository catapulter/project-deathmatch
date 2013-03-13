package deathmatch.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class PlayerClass {

	private String classType;
	private int health, energy, armor, speed;
	private Sprite sprite;
	
	
	public PlayerClass() {
		
		
	}
	
	public int getHealth() {
		return health;
	}
	
	public int getEnergy() {
		return energy;
	}
	
	public int getArmor() {
		return armor;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	static PlayerClass loadPlayerClass(String filename) {
		
		PlayerClass playerClass = new PlayerClass();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			String line;
			String[] sa;
			
			while((line = br.readLine()) != null) {
				sa = line.split("=");
				sa[0] = sa[0].trim();
				sa[1] = sa[1].trim();
				
				if(sa[0].equals("type")) {
					playerClass.classType = br.readLine().trim();
				} else if(sa[0].equals("health")) {
					playerClass.health = Integer.parseInt(br.readLine().split("=")[1].trim());
				} else if(sa[0].equals("energy")) {
					playerClass.energy = Integer.parseInt(br.readLine().split("=")[1].trim());
				} else if(sa[0].equals("armor")) {
					playerClass.armor = Integer.parseInt(br.readLine().split("=")[1].trim());
				} else if(sa[0].equals("speed")) {
					playerClass.speed = Integer.parseInt(br.readLine().split("=")[1].trim());
				}
			}
			
			return playerClass;
			
		} catch (IOException e) {
			e.printStackTrace();
			
			return null;
		}
	}
}
