package snake;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

class Snake extends JFrame implements KeyListener, Runnable {

	static boolean running = true;
	static int speed = 50;
	
	static int health = 250;
	static JButton healthBar = new JButton();
	
	static Random r = new Random();
	static JLabel scoreL = new JLabel();
	
	static List<Integer> scores = new ArrayList<Integer>();
	static List<String> names = new ArrayList<String>();

	public static void main(String[] args) {
		new Snake();
		loadScores();
	}

	JPanel p1 = new JPanel();
	JPanel p2 = new JPanel();

	JButton[] bodyPart = new JButton[200];
	JButton bonusfood = new JButton();

	int x = 502, y = 252, gu = 2, directionx = 1, directiony = 0,
			difference = 0, oldx, oldy, score = 0;
	int[] bodyPartX = new int[300];
	int[] bodyPartY = new int[300];
	Point[] lbp = new Point[300];
	Point bfp = new Point();
	Thread thread1;
	boolean food = false, runl = false, runr = true, runu = true, rund = true,
			bonusFood = true;

	JMenuBar menuBar;
	JMenu game, help, level;

	Snake() {
		
		creatBar();
		setJMenuBar(menuBar);
		setTitle("Snake");
		setVisible(true);
		setResizable(false);
		setSize(530, 400);
		setBackground(Color.WHITE);

		// bonusfood = new JButton();
		// bonusfood.setEnabled(false);

		initializeUI();
		initializeValues();
		
		creatBar();

		p1.setLayout(null);
		p1.setVisible(true);
		

		getContentPane().setLayout(null);
		GridBagConstraints gb = new GridBagConstraints();

		p1.setBounds(10, 10, x, y);
		p1.setBackground(Color.WHITE);
		p1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		gb.gridx = 0;
		gb.gridy = 0;

		getContentPane().add(p1, gb);
		// p1.revalidate();
		// p1.repaint();

		p2.setLayout(new GridBagLayout());
		p2.setBackground(Color.WHITE);
		p2.setVisible(true);
		p2.setBounds(10, 275, 502, 40);
		p2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		gb.gridx = 0;
		gb.gridy = 1;
		getContentPane().add(p2, gb);

		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		addKeyListener(this);
		
		initializeSnake();

		thread1 = new Thread(this);
		thread1.start();
	}
	
	public void initializeUI(){
		
		GridBagConstraints gb = new GridBagConstraints();
		
		gb.insets = new Insets(4,4,4,4);
		scoreL.setText("Score: ");
		scoreL.setLocation(250, 250);
		p2.add(scoreL, gb);
		
		gb.gridx = 1;
		healthBar.setSize(health, 10);
		p2.add(healthBar, gb);

	}

	public void updateUI(){
		String scoreString = String.valueOf(score);
		
		scoreL.setText("Score: " + scoreString);
		
		if (health >= 250){
			 healthBar.setSize(250/2, 10);
		}
		                 
		else {
			healthBar.setSize(health/2, 10);
		}
	}
	
	public void initializeValues() {
		gu = 3;
		bodyPartX[0] = 101;
		bodyPartY[0] = 151;
		directionx = 10;
		directiony = 0;
		difference = 0;
		// speed = 100;
		score = 0;
		health = 250;
		food = false;
		runl = false;
		runr = true;
		runu = true;
		rund = true;
		bonusFood = true;
	}

	public void initializeSnake() {
		for (int i = 0; i < 3; i++) {
			bodyPart[i] = new JButton("");
			bodyPart[i].setSize(10, 10);
			bodyPart[i].setBackground(randomColor());
			bodyPart[i].setBorder(BorderFactory.createEtchedBorder());
			bodyPart[i].setEnabled(false);
			bodyPart[i].setEnabled(true);
			bodyPart[i].setBounds(bodyPartX[i], bodyPartY[i], 10, 10);
			p1.add(bodyPart[i]);
			bodyPartX[i + 1] = bodyPartX[i] - 10;
			bodyPartY[i + 1] = bodyPartY[i];
			
		}
	}

	public void creatBar() {
		menuBar = new JMenuBar();
		game = new JMenu("Game");

		JMenuItem newGame = new JMenuItem("New Game");
		JMenuItem exit = new JMenuItem("Exit");

		newGame.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				reset();
			}
		});

		exit.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		game.add(newGame);
		game.addSeparator();
		game.add(exit);

		menuBar.add(game);

		level = new JMenu("Level");

		menuBar.add(level);

		help = new JMenu("Help");

		JMenuItem instructions = new JMenuItem("Instructions");

		instructions.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

			}
		});

		help.add(instructions);
		menuBar.add(help);

		setJMenuBar(menuBar);
	}

	public void reset() {
		running = true;
		p1.removeAll();
		p1.repaint();
		initializeValues();
		initializeSnake();
		
	}

	public Color randomColor() {
		int colorValue = r.nextInt(5) + 1;
//		colorValue = 3;
		Color foodColor = Color.WHITE;
		if (colorValue == 1) {
			foodColor = new Color(146, 240, 248);
		} else if (colorValue == 2) {
			foodColor = new Color(125, 247, 156);
		} else if (colorValue == 3) {
			foodColor = new Color(248, 124, 124);
		} else if (colorValue == 4) {
			foodColor = new Color(244, 230, 128);
		} else if (colorValue == 5) {
			foodColor = new Color(184, 123, 251);
		}

		return foodColor;
	}

	public void growup() {
		bodyPart[gu] = new JButton();
		bodyPart[gu].setBackground(randomColor());
		bodyPart[gu].setEnabled(false);
		bodyPart[gu].setBorder(BorderFactory.createEtchedBorder());
		p1.add(bodyPart[gu]);

		int a = 11 + (10 * r.nextInt(48));
		int b = 11 + (10 * r.nextInt(23));

		bodyPartX[gu] = a;
		bodyPartY[gu] = b;
		bodyPart[gu].setBounds(a, b, 10, 10);

		gu++;

	}

	public void moveForward() {
		for (int i = 0; i < gu; i++) {
			lbp[i] = bodyPart[i].getLocation();
		}

		bodyPartX[0] += directionx;
		bodyPartY[0] += directiony;
		bodyPart[0].setBounds(bodyPartX[0], bodyPartY[0], 10, 10);

		for (int i = 1; i < gu; i++) {
			bodyPart[i].setSize(10, 10);
			bodyPart[i].setLocation(lbp[i - 1]);
		}

		if (bodyPartX[0] == x) {
			bodyPartX[0] = 10;
		} else if (bodyPartX[0] == 0) {
			bodyPartX[0] = x - 10;
		} else if (bodyPartY[0] == y) {
			bodyPartY[0] = 10;
		} else if (bodyPartY[0] == 0) {
			bodyPartY[0] = y - 10;
		}

		if (bodyPartX[0] == bodyPartX[gu - 1] && bodyPartY[0] == bodyPartY[gu - 1]) {
			food = false;
			score += 5;
			health += 30;
			if (score % 50 == 0 && bonusFood == true) {
				p1.add(bonusfood);
				bonusfood.setBounds((10 * r.nextInt(50)) + 1,
						(10 * r.nextInt(25) + 1), 10, 10);
				bfp = bonusfood.getLocation();
				bonusFood = false;
			}
		}

		if (bonusFood == false) {
			if (bfp.x <= bodyPartX[0] && bfp.y <= bodyPartY[0] && bfp.x + 10 >= bodyPartX[0]
					&& bfp.y + 10 >= bodyPartY[0]) {
				p1.remove(bonusfood);
				p1.repaint();
				health = 250;
				bonusFood = true;
			}
		}

		if (food == false) {
			growup();
			food = true;
		} else {
			bodyPart[gu - 1].setBounds(bodyPartX[gu - 1], bodyPartY[gu - 1], 10, 10);
		}

		for (int i = 1; i < gu; i++) {
			if (lbp[0] == lbp[i]) {
				try {
					thread1.join();
				} catch (InterruptedException ie) {
				}
				break;
			}
		}
		
		health -= 1;
		updateTurnable();
	}

	public void keyPressed(KeyEvent e) {
		if (runl == true && e.getKeyCode() == 37) {
			directionx = -10;
			directiony = 0;

		}
		if (runu == true && e.getKeyCode() == 38) {
			directionx = 0;
			directiony = -10;

		}
		if (runr == true && e.getKeyCode() == 39) {
			directionx = 10;
			directiony = 0;

		}
		if (rund == true && e.getKeyCode() == 40) {
			directionx = 0;
			directiony = 10;

		}
		
		if (e.getKeyCode() == KeyEvent.VK_SPACE ) {
			if(running == false){
				running = true;
			}
			if(running == true){
				running = false;
			}

		}
		
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	public void updateTurnable() {
		if (directiony == 10) {
			runu = false;
			runr = true;
			runl = true;
		} else if (directionx == -10) {
			runr = false;
			runu = true;
			rund = true;
		} else if (directionx == 10) {
			runl = false;
			runu = true;
			rund = true;
		} else if (directiony == -10) {
			rund = false;
			runr = true;
			runl = true;
		}
	}

	public void run() {
		while(running) {
			if (collisionTest()) {
				running = false;
				gameOver();

				
			} else {
				moveForward();
				updateUI();

			}

			try {
				Thread.sleep(speed);
			} catch (InterruptedException ie) {
			}
		}
	}
	
	public void gameOver(){
		
		Object[] options = { "Save Score", "Play Again", "Quit" };
		int reply = JOptionPane.showOptionDialog(null,
				"Would You like to save your score or play again ?",
				"Game Over", JOptionPane.YES_NO_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if (reply == 0){
			String name = "";
				name = (String)JOptionPane.showInputDialog(
			                    this,
			                    "Enter your name",
			                    "Customized Dialog",
			                    JOptionPane.PLAIN_MESSAGE,
			                    null,
			                    null,
			                    null);
				
			try {
				saveScore(name, score);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (reply == 1){
			reset();
		}
		if (reply == 2){
			System.exit(0);
		}
				
	}
	
	public void saveScore(String n, int s) throws IOException {
		try{
			
			PrintWriter outputFile = new PrintWriter(new BufferedWriter(new FileWriter("scores.txt", true)));
			
			outputFile.println(n);
			outputFile.println(s);
			outputFile.flush();
			outputFile.close();
			
		}catch(IOException ex){
			System.out.println(ex.getMessage());
		}
	}
	
	public static void loadScores() {
		int s1 = 0;
		int s2 = 0;
		int s3 = 0;
		int s4 = 0;
		int s5 = 0;
		int s6 = 0;
		int s7 = 0;
		int s8 = 0;
		int s9 = 0;
		int s10 = 0;
		
		int s1i = 0;
		int s2i = 0;
		int s3i = 0;
		int s4i = 0;
		int s5i = 0;
		int s6i = 0;
		int s7i = 0;
		int s8i = 0;
		int s9i = 0;
		int s10i = 0;
		
		try{
	      
			Scanner sc = new Scanner(new File("scores.txt"));
			
			while(sc.hasNextInt()){
				names.add(sc.next());
				scores.add(sc.nextInt());
			}
			for(int i = 0; i < scores.size(); i++){
				if (scores.get(i) > s1){
					s1 = scores.get(i);
					s1i = i;
				}
			}
			for(int i = 0; i < scores.size(); i++){
				if (scores.get(i) > s1 && i != s1i){
					s2 = scores.get(i);
					s2i = i;
				}
			}
			for(int i = 0; i < scores.size(); i++){
				if (scores.get(i) > s1 && i != s1i && i !=s2i){
					s3 = scores.get(i);
					s3i = i;
				}
			}
			for(int i = 0; i < scores.size(); i++){
				if (scores.get(i) > s1 && i != s1i && i != s2i && i != s3i){
					s4 = scores.get(i);
					s4i = i;
				}
			}
			for(int i = 0; i < scores.size(); i++){
				if (scores.get(i) > s1 && i != s1i && i != s2i && i != s3i && i != s4i){
					s5 = scores.get(i);
					s5i = i;
				}
			}
			for(int i = 0; i < scores.size(); i++){
				if (scores.get(i) > s1 && i != s1i && i != s2i && i != s3i && i != s4i && i != s5i){
					s6 = scores.get(i);
					s6i = i;
				}
			}
			for(int i = 0; i < scores.size(); i++){
				if (scores.get(i) > s1 && i != s1i && i != s2i && i != s3i && i != s4i && i != s5i && i != s6i){
					s7 = scores.get(i);
					s7i = i;
				}
			}
			for(int i = 0; i < scores.size(); i++){
				if (scores.get(i) > s1 && i != s1i && i != s2i && i != s3i && i != s4i && i != s5i && i != s6i && i != s7i){
					s8 = scores.get(i);
					s8i = i;
				}
			}
			for(int i = 0; i < scores.size(); i++){
				if (scores.get(i) > s1 && i != s1i && i != s2i && i != s3i && i != s4i && i != s5i && i != s6i && i != s7i && i != s8i){
					s9 = scores.get(i);
					s9i = i;
				}
			}
			for(int i = 0; i < scores.size(); i++){
				if (scores.get(i) > s1 && i != s1i && i != s2i && i != s3i && i != s4i && i != s5i && i != s6i && i != s7i && i != s8i && i != s9i){
					s10 = scores.get(i);
					s10i = i;
				}
			}
			
			System.out.println(names);
			System.out.println(scores);
			
			System.out.println(names.get(s1i)+ " " + s1);
			System.out.println(names.get(s2i)+ " " + s2);
			System.out.println(names.get(s3i)+ " " + s3);
			System.out.println(names.get(s4i)+ " " + s4);
			System.out.println(names.get(s5i)+ " " + s5);
			System.out.println(names.get(s6i)+ " " + s6);
			System.out.println(names.get(s7i)+ " " + s7);
			System.out.println(names.get(s8i)+ " " + s8);
			System.out.println(names.get(s9i)+ " " + s9);
			System.out.println(names.get(s10i)+ " " + s10);
			
			
			
	      
	    }
	    catch (IOException ex){
	      System.out.println(ex.getMessage());
	    }
	}



	public boolean collisionTest() {
		for (int i = 1; i < gu - 1; i++) {
			if (bodyPart[0].getX() + directionx == bodyPart[i].getX()
					&& bodyPart[0].getY() + directiony == bodyPart[i].getY()) {
				return true;

			}
			
		}
		if (bodyPartX[0] + directionx > 500) {
			return true;
		} else if (bodyPartX[0] + directionx < 0) {
			return true;
		} else if (bodyPartY[0] + directiony < 0) {
			return true;
		} else if (bodyPartY[0] + directiony > 241) {
			return true;
		}else if (health == 0){
			return true;
		}else
			return false;
	}
}