package GameState;

import Main.GamePanel;




import TileMap.*;
import Entity.*;
import Entity.Enemies.*;
import Audio.AudioPlayer;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;



public class Level1State extends GameState {
	
	int c=0;
	
	private TileMap tileMap;
	private Background bg;
	
	private Player player;
	private Boss boss;
	
	private ArrayList<Enemy>enemies;
	private ArrayList<Explosion> explosions;
	
	private HUD hud;
	private AudioPlayer bgMusic;
	
	public Level1State(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}
	
	public void init() {
		
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/grasstileset.gif");
		tileMap.loadMap("/Maps/level1-1.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		bg = new Background("/Backgrounds/bg.png", 0.1);
		
		player = new Player(tileMap);
		player.setPosition(100, 100);
		
		populateEnemies();
		
		/*enemies=new ArrayList<Enemy>();
		Soldier s;
		s=new Soldier(tileMap);
		s.setPosition(200,200);
		enemies.add(s);*/
		
		explosions=new ArrayList<Explosion>();
		
		hud=new HUD(player);
		
		bgMusic=new AudioPlayer("/Music/level1.mp3");
		bgMusic.play();
		
	}
	
	private void populateEnemies() {
		
		enemies=new ArrayList<Enemy>();
		
		Soldier s;
		Boss b;
		Point[] points=new Point[] {
				new Point(200,100),
				new Point(860,200),
				new Point(1525,200),
				new Point(1680,200),
				new Point(1800,200),
				new Point(2800,200)
		};
		for(int i=0;i<points.length;i++) {
			if(i!=points.length-1){
			s=new Soldier(tileMap);
			s.setPosition(points[i].x,points[i].y);
			enemies.add(s);
			}
			if(i==points.length-1) {
				b=new Boss(tileMap);
				b.setPosition(points[i].x,points[i].y);
				enemies.add(b);
			}
		}
		
	}
	
	
	public void update() {
		
		// update player
		player.update();
		tileMap.setPosition(
			GamePanel.WIDTH / 2 - player.getx(),
			GamePanel.HEIGHT / 2 - player.gety()
		);
		
		
		//System.out.println(enemies.size()+"------");
		//System.out.println(+"------");
		//System.out.println(GamePanel.HEIGHT / 2 - player.getx()+"------");
		//System.out.println(GamePanel.HEIGHT / 2 - player.gety()+"------");
		if(GamePanel.WIDTH / 2 - player.getx()<-2400)
				 {
			for(int i=0;i<enemies.size();i++) {
				Enemy e=enemies.get(i);
				enemies.get(i).update();
				if(enemies.get(i).isDead()) {
				//System.out.println(i+"------");	
				enemies.remove(i);
				c=1;
				i--;
				explosions.add(new Explosion(e.getx(),e.gety()));

				}
			}
			//System.out.println(c+"------");
			
			if(GamePanel.WIDTH / 2 - player.getx()==-3010
					 && GamePanel.HEIGHT / 2 - player.gety()==-70 && c==1) { //edited
				c=0;
				player.dead=true;
			}
			
		}
		
		
		/*if(GamePanel.WIDTH / 2 - player.getx()==-3010
				 && GamePanel.HEIGHT / 2 - player.gety()==-80) {
			player.dead=true;
		}*/
		
		
		if(player.dead) {
			bgMusic.stop();
			gsm.setState(GameStateManager.ENDSTATE);	
		}
		
		//attack enemies
		player.checkAttack(enemies);
		
		//update all enemies
		for(int i=0;i<enemies.size()-1;i++) {
			Enemy e=enemies.get(i);
			enemies.get(i).update();
			if(enemies.get(i).isDead()) {
			//System.out.println(i+"------");	
			enemies.remove(i);
			i--;
			explosions.add(new Explosion(e.getx(),e.gety()));
			
			}
		}
		
		
		//update explosions
		for(int i=0;i<explosions.size();i++) {
			explosions.get(i).update();
			if(explosions.get(i).shouldRemove()) {
				explosions.remove(i);
				i--;
			}
		}
		
	}
	
	
	public void draw(Graphics2D g) {
		
		// draw bg
		bg.draw(g);
		
		// draw tilemap
		tileMap.draw(g);
		
		// draw player
		player.draw(g);
		
		//draw enemies
		for(int i=0;i<enemies.size();i++) {
			enemies.get(i).draw(g);
		}
		
		//draw explosions
		for(int i=0;i<explosions.size();i++) {
			explosions.get(i).setMapPosition(
					(int)tileMap.getx(),(int)tileMap.gety());
			explosions.get(i).draw(g);
		}
		
		//draw hud
		hud.draw(g);
		
	}
	
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_LEFT) player.setLeft(true);
		if(k == KeyEvent.VK_RIGHT) player.setRight(true);
		if(k == KeyEvent.VK_UP) player.setUp(true);
		if(k == KeyEvent.VK_DOWN) player.setDown(true);
		if(k == KeyEvent.VK_W) player.setJumping(true);
		
		if(k == KeyEvent.VK_F) player.setFiring();
	}
	
	public void keyReleased(int k) {
		if(k == KeyEvent.VK_LEFT) player.setLeft(false);
		if(k == KeyEvent.VK_RIGHT) player.setRight(false);
		if(k == KeyEvent.VK_UP) player.setUp(false);
		if(k == KeyEvent.VK_DOWN) player.setDown(false);
		if(k == KeyEvent.VK_W) player.setJumping(false);
		
	}
	
}












