package Entity;

import GameState.GameStateManager;

import TileMap.*;
import Audio.AudioPlayer;

import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;





import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class Player extends MapObject{
	
	// player stuff
	/*private int health;
	private int maxHealth;
	private int fire;
	private int maxFire;
	private boolean dead;*/
	private boolean flinching;
	private long flinchTimer;
	
	// fireball
	private boolean firing;
	private int fireCost;
	private int fireBallDamage;
	private ArrayList<FireBall> fireBalls;
	
	
	// animations
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {
		2, 8, 1, 2, 2
	};
	
	
	// animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int FIREBALL = 4;
	
	Graphics2D g;
	KeyEvent e;
	
	private HashMap<String,AudioPlayer> sfx;
	
	public Player(TileMap tm) {
		
		super(tm);
		
		width = 55;
		height = 55;
		cwidth = 20;
		cheight = 40; //edited
		
		moveSpeed = 0.3;
		maxSpeed = 1.6;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;
		
		facingRight = true;
		
		health = maxHealth = 5;
		fire = maxFire = 2500;
		
		fireCost = 200;
		fireBallDamage = 5;
		fireBalls = new ArrayList<FireBall>();
		
		
		// load sprites
		try {
			
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Player/soldier-friend.gif"
				)
			);
			
			sprites = new ArrayList<BufferedImage[]>();
			for(int i = 0; i < 5; i++) {
				
				BufferedImage[] bi =
					new BufferedImage[numFrames[i]];
				
				for(int j = 0; j < numFrames[i]; j++) {
					
					if(i != 6) {
						bi[j] = spritesheet.getSubimage(
								j * width,
								i * height,
								width,
								height
						);
					}
					else {
						bi[j] = spritesheet.getSubimage(
								j * width * 2,
								i * height,
								width,
								height
						);
					}
					
				}
				
				sprites.add(bi);
				
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(400);
		
		sfx=new HashMap<String,AudioPlayer>();
		sfx.put("jump",new AudioPlayer("/SFX/playerjump.mp3"));
		sfx.put("attack",new AudioPlayer("/SFX/playerattack.mp3"));
		sfx.put("hit",new AudioPlayer("/SFX/playerhit.mp3"));
	}

	
	public int getHealth() { return health; }
	public int getMaxHealth() { return maxHealth; }
	public int getFire() { return fire; }
	public int getMaxFire() { return maxFire; }
	
	public void setFiring() { 
		firing = true;
	}

	public void checkAttack(ArrayList<Enemy>enemies) {
		
		//loop through enemies
		for(int i=0;i<enemies.size();i++) {
			
			Enemy e=enemies.get(i);
			
			//fireballs
			for(int j=0;j<fireBalls.size();j++) {
				
				if(fireBalls.get(j).intersects(e)) {
					
					e.hit(fireBallDamage);
					fireBalls.get(j).setHit();
					break;
					
				}
				
				
			}
			
			//check enemy collision
			if(intersects(e)) {
				sfx.get("hit").play();
				hit(e.getDamage());
			}
			
		}
		
	}
	
	public void hit(int damage){
		if(flinching) return;
		health-=damage;
		if(health<0) health=0;
		if(health==0) {
		dead=true;
		if(dead==true) {
		
		moveSpeed=0;
		fire=maxFire=0;
		jumpStart=0;
		}
		}
		flinching=true;
		flinchTimer=System.nanoTime();
	}
	
	/*public void gameover(Graphics2D g){
		
		if(health==0)
		{
            g.setFont(new Font("Century Gothic" , Font.BOLD, 18));
            g.drawString("Press Enter to restart.", 80, 70);
        }
		}*/
	
	/*public void keyPressed(KeyEvent e) {
		
		
		if(health==0){
			
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				System.exit(0);
			}
		}
			
		
	}*/
		
	
	private void getNextPosition() {
		
		// movement
		if(left) {
			dx -= moveSpeed;
			if(dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		}
		else if(right) {
			dx += moveSpeed;
			if(dx > maxSpeed) {
				dx = maxSpeed;
			}
		}
		else {
			if(dx > 0) {
				dx -= stopSpeed;
				if(dx < 0) {
					dx = 0;
				}
			}
			else if(dx < 0) {
				dx += stopSpeed;
				if(dx > 0) {
					dx = 0;
				}
			}
		}
		
		// cannot move while attacking, except in air
		if(
		(currentAction == FIREBALL) &&
		!(jumping || falling)) {
			dx = 0;
		}
		
		// jumping
		if(jumping && !falling) {
			sfx.get("jump").play();
			dy = jumpStart;
			falling = true;	
		}
		
		// falling
		if(falling) {
			
			if(dy > 0) dy += fallSpeed * 0.1;
			else dy += fallSpeed;
			
			if(dy > 0) jumping = false;
			if(dy < 0 && !jumping) dy += stopJumpSpeed;
			
			if(dy > maxFallSpeed) dy = maxFallSpeed;
			
		}
		
	}
	
	public void update() {
		
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		//check attack has stopped
		if(currentAction==FIREBALL) {
			if(animation.hasPlayedOnce()) firing=false;
			
		}
		
		//fireball attack
		fire+=1;
		if(fire>maxFire) fire=maxFire;
		if(firing && currentAction !=FIREBALL) {
			if(fire>fireCost) {
				fire-=fireCost;
				FireBall fb=new FireBall(tileMap,facingRight);
				fb.setPosition(x,y);
				fireBalls.add(fb);
				
			}
		}
		//update fireballs
		for(int i=0;i<fireBalls.size();i++) {
			fireBalls.get(i).update();
			if(fireBalls.get(i).shouldRemove()) {
				fireBalls.remove(i);
				i--;
			}
		}
		
		//check done flinching
		if(flinching) {
			long elapsed=
					(System.nanoTime()-flinchTimer)/1000000;
			if(elapsed>1000) {
				flinching=false;
			}
		}
		
		// set animation
		 if(firing) {
			if(currentAction != FIREBALL) {
				sfx.get("attack").play();
				currentAction = FIREBALL;
				animation.setFrames(sprites.get(FIREBALL));
				animation.setDelay(100);
				width = 36;
			}
		}
		else if(dy > 0) {
			 if(currentAction != FALLING) {
				currentAction = FALLING;
				animation.setFrames(sprites.get(FALLING));
				animation.setDelay(100);
				width = 36;
			}
		}
		else if(dy < 0) {
			if(currentAction != JUMPING) {
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(-1);
				width = 36;
			}
		}
		else if(left || right) {
			if(currentAction != WALKING) {
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(40);
				width = 36;
			}
		}
		else {
			if(currentAction != IDLE) {
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(400);
				width = 36;
			}
		}
		
		animation.update();
		
		// set direction
		if(currentAction != FIREBALL) {
			if(right) facingRight = true;
			if(left) facingRight = false;
		}
		
	}
	
	public void draw(Graphics2D g) {
		
		setMapPosition();
		
		
		//draw fireballs
		for(int i=0;i<fireBalls.size();i++){
			fireBalls.get(i).draw(g);
		}
		
			
		
		// draw player
		if(flinching) {
			long elapsed =
				(System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed / 100 % 2 == 0) {
				return;
			}
		}
		
		/*if(facingRight) {
			g.drawImage(
				animation.getImage(),
				(int)(x + xmap - width / 2),
				(int)(y + ymap - height / 2),
				null
			);
		}
		else {
			g.drawImage(
				animation.getImage(),
				(int)(x + xmap - width / 2 + width),
				(int)(y + ymap - height / 2),
				-width,
				height,
				null
			);
			
		}*/
		
		super.draw(g);
		
	}
	
}

















