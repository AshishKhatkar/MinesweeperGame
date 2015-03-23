/*
 * @author : Ashish Khatkar (ashish1610 on online judges)
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.sound.sampled.*;
public class Game extends JPanel
{
	protected String playerName;
	protected int score;
	protected int gameTime;
	protected Grid board;
	protected boolean gameFinished;
	protected int lives;
	protected HashMap<String, BufferedImage> images;
	protected Clip audioClip;
	public static final JTextArea editTextArea = new JTextArea("Enter Name");
	public static final JTextArea uneditTextArea = new JTextArea();
	public static final JButton inputButton = new JButton("Enter");
	private static String name;
	public static boolean nameEntered;
	protected static HashMap<String, Integer> scoreMap;
	protected static Game gm;
	
	/*
	 * Constructor takes playerName as argument
	 */
	public Game(String playerName)
	{
		super(true);
		this.playerName = playerName;
		addMouseListener(new GameListener());
		initGame();
	}
	
	/*
	 * Initializes the game
	 */
	protected void initGame()
	{
		board = new Grid(9, 9, 20);
		gameFinished = false;
		lives = 0;
		gameTime = 60;
		score = 0;
		images = new HashMap<String, BufferedImage>();
		initImagesAndAudio();
		initScoreMap();
		super.repaint();
	}
	
	/*
	 * Initializes the scoremap
	 */
	protected void initScoreMap()
	{
		scoreMap = new HashMap<String, Integer>();
		BufferedReader br = null;
		try
		{
			br = new BufferedReader(new FileReader("scores/scores.txt"));
			scoreMap = new HashMap<String, Integer>();
			String str = br.readLine();
			while(str != null)
			{
				String[] data = str.split(" ");
				int sc = Integer.parseInt(data[1]);
				String pn = data[0];
				scoreMap.put(pn, sc);
			}
		}
		catch(Exception e)
		{
			System.out.println("File Not Found");
		}
		try
		{
			if(br != null)
				br.close();
		}
		catch(Exception e)
		{
			System.out.println("File can't be closed");
		}
	}
	
	/*
	 * Initializes the images and audio sound
	 */
	protected void initImagesAndAudio()
	{
		BufferedImage img;
		File dir = new File("images");
		for(File f : dir.listFiles())
		{
			try
			{
				img = ImageIO.read(new FileInputStream(f));
				images.put(f.getName(), img);
			}
			catch(IOException e)
			{
				System.out.println("Image file not found");
			}
		}
		
		File musicFile = new File("sounds/2cellos.wav");
		AudioInputStream stream;
		AudioFormat format;
		DataLine.Info info;
		try
		{
			stream = AudioSystem.getAudioInputStream(musicFile);
			format = stream.getFormat();
			info = new DataLine.Info(Clip.class, format);
			audioClip = (Clip)AudioSystem.getLine(info);
			audioClip.open(stream);
		}
		catch(Exception e)
		{
			System.out.println("Music file not found");
		}
	}
	
	/*
	 * Build the menu
	 */
	private JMenuBar buildMenu()
	{
		JMenuBar menu = new JMenuBar();
		JMenu menuGame = new JMenu("Game");
		menu.add(menuGame);
		
		JMenuItem menuItemNew = new JMenuItem("New Game");
		menuItemNew.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(nameEntered  == false)
					JOptionPane.showMessageDialog(Game.this, "Please enter your name first");
				else
				{
					inputButton.setVisible(false);
					initGame();
				}
			}
		});
		menuGame.add(menuItemNew);
		return menu;
	}
	
	/*
	 * Play the game
	 */
	public static void play()
	{
		JFrame frame = new JFrame("Minesweeper");
		gm = new Game("Ashish");
		frame.setContentPane(gm);
		frame.setJMenuBar(gm.buildMenu());
		frame.setSize(400, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		editTextArea.setBackground(Color.WHITE);
//		editTextArea.setForeground(Color.BLUE);
//		editTextArea.setSize(50, 60);
//		uneditTextArea.setEditable(false);
//		Container c = frame.getContentPane();
//		c.add(editTextArea, BorderLayout.SOUTH);
//		c.add(inputButton, BorderLayout.WEST);
//		c.add(uneditTextArea, BorderLayout.CENTER);
//		inputButton.addActionListener(new ActionListener()
//		{
//			@Override
//			public void actionPerformed(ActionEvent e)
//			{
//				name = editTextArea.getText().toString();
//				editTextArea.setText("");
//				inputButton.setText("Now click on new to play");
//				nameEntered = true;
//			}
//		});
		System.out.println(name);
		frame.setVisible(true);
//		gm.playerName = name;
		System.out.println(gm.playerName);
		gm.audioClip.start();
		gm.audioClip.loop(gm.audioClip.LOOP_CONTINUOUSLY);
	}
	
	/*
	 * PaintComponents implemented
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(nameEntered)
			board.drawGrid(g, images, gameTime);
	}
	
	/*
	 * Class for mouse click handling
	 */
	public class GameListener extends MouseAdapter
	{
		@Override
		public void mouseReleased(MouseEvent e)
		{
			if(gameFinished)
				return;
			
			int x = (e.getX() - 60) / 30;
			int y = (e.getY() - 80) / 30;
			if(x < 0 || y < 0 || x >= board.cols || y >= board.rows)
				return;
			
			int button = e.getButton();
			if(board.gridStatus[y][x] == board.UNDISCOVERED || board.gridStatus[y][x] == board.MARKED)
			{
				
				/*
				 * handles what will happen when we click left mouse button
				 */
				if(button == MouseEvent.BUTTON1)
				{
					handleLeftEvent(y, x);
				}
				
				/*
				 * handles what will happen when we click right mouse button
				 */
				if(button == MouseEvent.BUTTON3)
				{
					handleRightEvent(y, x);
				}
			}
			checkGameStatus();
			repaint();
		}
		
		/*
		 * Left click handler
		 */
		private void handleLeftEvent(int i, int j)
		{
			if(board.gridValue[i][j] == board.MINE)
			{
				lives--;
				if(lives < 0)
				{
					System.out.println(lives);
					gameFinished = true;
					youLose();
				}
				else
					oneMoreChanceLeft();
			}
			else if(board.gridValue[i][j] == board.FLAG)
			{
				board.gridStatus[i][j] = board.FLAG;
				lives++;
				lifeAdded();
			}
			else
				board.makeMove(i, j);
		}
		
		/*
		 * Right click handler
		 */
		private void handleRightEvent(int i, int j)
		{
			if(board.gridStatus[i][j] == board.MARKED)
			{
				board.markedCount++;
				board.unDiscoveredCount++;
				board.gridStatus[i][j] = board.UNDISCOVERED;
				if(board.gridValue[i][j] == board.MINE)
					board.mineCount++;
			}
			else if(board.gridStatus[i][j] == board.UNDISCOVERED && board.markedCount != 0)
			{
				board.markedCount--;
				board.unDiscoveredCount--;
				board.gridStatus[i][j] = board.MARKED;
				if(board.gridValue[i][j] == board.MINE)
					board.mineCount--;
			}
		}
		
		/*
		 * Handles the events when you lose
		 */
		private void youLose()
		{
			JOptionPane.showMessageDialog(Game.this, "Sorry, you lose !!");
			for(int i = 0; i < board.rows; ++i)
			{
				for(int j = 0; j < board.cols; ++j)
				{
					if(board.gridValue[i][j] == board.MINE)
						board.gridStatus[i][j] = board.MINE;
				}
			}
			writeScore();
		}
		
		/*
		 * Handles the events when you lose a life
		 */
		private void oneMoreChanceLeft()
		{
			JOptionPane.showMessageDialog(Game.this, "You have one more chance left to find all the mines !!");
		}
		
		/*
		 * Handles the events when you get flag
		 */
		private void lifeAdded()
		{
			JOptionPane.showMessageDialog(Game.this, "Congrats !!, You have earned one more life !!");
		}
		
		/*
		 * Checks if the game is over or not
		 */
		private void checkGameStatus()
		{
			if(board.mineCount == 0 || board.unDiscoveredCount == board.mineCount)
			{
				gameFinished = true;
				youWon();
			}
		}
		
		/*
		 * Handles the events when player wins the game
		 */
		private void youWon()
		{
			JOptionPane.showMessageDialog(Game.this, "Congrats !!, You won !!");
			for(int i = 0; i < board.rows; ++i)
			{
				for(int j = 0; j < board.cols; ++j)
				{
					if(board.gridValue[i][j] == board.MINE)
						board.gridStatus[i][j] = board.FINISH;
				}
			}
		}
	}
	
	/*
	 * Writes the score after game is over
	 */
	protected void writeScore()
	{
		BufferedWriter bw = null;
		try
		{
			System.out.println("here");
			bw = new BufferedWriter(new FileWriter("scores/scores.txt"));
			if(scoreMap.containsKey(gm.playerName))
			{
				scoreMap.remove(gm.playerName);
				scoreMap.put(gm.playerName, score);
			}
			else
				scoreMap.put(gm.playerName, score);
			System.out.println(playerName);
			for(String key : scoreMap.keySet())
			{
				int data = scoreMap.get(key);
				System.out.println(key + " " + data);
//				bw.write(key);
				CharSequence csq = key + " " + data;
				bw.append(csq);
//				bw.write(" ");
//				bw.write(data);
//				bw.write("\n");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("File can't be opened");
		}
		try
		{
			if(bw != null)
				bw.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("File can't be closed");
		}
	}
}
