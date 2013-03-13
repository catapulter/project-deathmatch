package deathmatch.main;

import java.util.ArrayList;

public class Player extends Entity {

	private String name;
	private PlayerClass playerClass;
	private int currentHealth;
	private int currentEnergy;
	private int currentArmor;
	private int currentSpeed;
	private ArrayList<PlayerEffect> buffsDebuffs;
	
	
	public Player(String namePR, PlayerClass classPR) {
		
		name = namePR;
		//position.setX(100);
		//position.setY(100);
		playerClass = classPR;
		currentHealth = classPR.getHealth();
		currentEnergy = classPR.getEnergy();
		currentArmor = classPR.getArmor();
		currentSpeed = classPR.getSpeed();
	}
	public int getCurrentHealth(){ return currentHealth;}
	public void setCurrentHealth(int x){ currentHealth = x;}
	public int getCurrentEnergy(){ return currentEnergy;}
	public void setCurrentEnergy(int x){ currentEnergy = x;}
	public int getCurrentArmor(){ return currentArmor;}
	public void setCurrentArmor(int x){ currentArmor = x;}
	public int getCurrentSpeed(){ return currentSpeed;}
	public void setCurrentSpeed(int x){ currentSpeed = x;}
	public String getName(){return name;}
	public PlayerClass getPlayerClass(){return playerClass;}
	//Need buff stuff here
	
	
}
