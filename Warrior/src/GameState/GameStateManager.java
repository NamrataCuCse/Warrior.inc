package GameState;

import java.util.ArrayList;


import Entity.Player;

public class GameStateManager {
	
	private GameState[] gameStates;
	private int currentState;
	
	public static final int NUMGAMESTATES=3;
	public static final int MENUSTATE = 0;
	public static final int LEVEL1STATE = 1;
	public static final int ENDSTATE=2;
	
	
	public GameStateManager() {
		
		gameStates = new GameState[NUMGAMESTATES];
		
		currentState = MENUSTATE;
		loadState(currentState);
		
	}
	
	private void loadState(int state) {
	
	if(state==MENUSTATE)
		gameStates[state]=new MenuState(this); //it is creating a new instance
	
	if(state==LEVEL1STATE)
		gameStates[state]=new Level1State(this);
	
	if(state==ENDSTATE)
		gameStates[state]=new EndState(this);
	}
	
	private void unloadState(int state) { //game quit
		gameStates[state]=null;
	}
	
	public void setState(int state) {
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
		//gameStates[currentState].init();
	}
	
	
	public void update() {
		if(gameStates[currentState]!=null){
			
		gameStates[currentState].update();
		}
	}
	
	public void draw(java.awt.Graphics2D g) {
		if(gameStates[currentState]!=null){
		gameStates[currentState].draw(g);
		}
	}
	
	public void keyPressed(int k) {
		gameStates[currentState].keyPressed(k);
	}
	
	public void keyReleased(int k) {
		gameStates[currentState].keyReleased(k);
	}
	
}









