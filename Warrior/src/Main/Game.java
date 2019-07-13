package Main;

import javax.swing.JFrame;

public class Game {
	
	public static void main(String[] args) {
	
		JFrame window = new JFrame("WARRIOR INC.");
		window.setContentPane(new GamePanel());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack(); // sizes the frame so that all its content are at or above their preferred size
		window.setVisible(true);
		

}
}
