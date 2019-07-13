package Entity;

import TileMap.TileMap;
import Audio.AudioPlayer;
import java.util.HashMap;

public class Enemy extends MapObject{
	
	protected int health;
	protected int maxHealth;
	protected boolean dead;
	protected int damage;
	
	protected boolean flinching;
	protected long flinchTimer;
	
	private HashMap<String,AudioPlayer> sfx;
	
	public Enemy(TileMap tm) {
		super(tm);
		
		sfx=new HashMap<String,AudioPlayer>();
		sfx.put("hit",new AudioPlayer("/SFX/enemyhit.mp3"));
		sfx.put("explode",new AudioPlayer("/SFX/explode.mp3"));
	
	}
	
	public boolean isDead() {
		return dead; }
	
	public int getDamage() {return damage; }
	
	public void hit (int damage) {
		if(dead || flinching) return;
		sfx.get("hit").play();
		health-=damage;
		if(health<0) health=0;
		if(health==0) {
		sfx.get("explode").play();	
		dead=true;
		}
		flinching=true;
		flinchTimer=System.nanoTime();
	}
	
	public void update() {}

}
