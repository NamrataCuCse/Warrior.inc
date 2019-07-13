package GameState;

import Entity.Player;

import TileMap.Background;


import java.awt.*;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

public class MenuState extends GameState {
	
	private Background bg;
	private Player player;
	private int currentChoice = 0;
	private String[] options = {
		"START",
		"QUIT"
	};
	
	private Color titleColor;
	private Font titleFont;
	private Font font;
	private Font copy;
	
	public MenuState(GameStateManager gsm) {
		
		this.gsm = gsm;
		
		try {
			
			bg = new Background("/Backgrounds/menubg.png", 1);
			bg.setVector(-0.1, 0);
			
			titleColor = new Color(153, 0, 76);
			titleFont = new Font( "Serif", Font.ITALIC + Font.BOLD , 28);
			
			font = new Font("Serif", Font.BOLD, 12);
			copy = new Font( "Serif", Font.ITALIC  , 10);
			
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void init() {}
	
	public void update() {
		bg.update();
	}
	
	public void draw(Graphics2D g) {
		
		// draw bg
		bg.draw(g);
		
		// draw title
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("WARRIOR", 100, 70);
		g.setFont(copy);
		g.drawString("@ Namrata Bhattacharya", 200, 230);
			
		
		// draw menu options
		g.setFont(font);
		for(int i = 0; i < options.length; i++) {
			if(i == currentChoice) {
				g.setColor(Color.RED);
			}
			else {
				g.setColor(Color.black);
			}
			g.drawString(options[i], 145, 140 + i * 15);
		}
		
	}
	
	private void select() {
		if(currentChoice == 0) {
			gsm.setState(GameStateManager.LEVEL1STATE);
		}
		if(currentChoice == 1) {
			System.exit(0);
		}
	}
	
	public void handleInput() {}
	
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ENTER){
			select();
		}
		if(k == KeyEvent.VK_UP) {
			currentChoice--;
			if(currentChoice == -1) {
				currentChoice = options.length - 1;
			}
		}
		if(k == KeyEvent.VK_DOWN) {
			currentChoice++;
			if(currentChoice == options.length) {
				currentChoice = 0;
			}
		}
	}
	
	public void keyReleased(int k) {}
	
	
}











