import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Random;

/*
 * @author : Ashish Khatkar (ashish1610 on online judges)
 */
public class Grid 
{
	protected int rows, cols, mineCount, markedCount, unDiscoveredCount, flagPosX, flagPosY;
	protected int[][] gridStatus;
	protected int[][] gridValue;
	protected final static int UNDISCOVERED = -1;
	protected final static int EMPTY = 0;
	protected final static int MARKED = 9;
	protected final static int MINE = 10;
	protected final static int FLAG = 11;
	protected final static int FINISH = 12;
	protected GuiDraw gui;
	
	public Grid(){}
	
	public Grid(int rows, int cols, int mineCount)
	{
		this.rows = rows;
		this.cols = cols;
		this.mineCount = mineCount;
		this.markedCount = mineCount;
		this.unDiscoveredCount = rows * cols;
		this.gridStatus = new int[rows][cols];
		this.gridValue = new int[rows][cols];
		gui = new GuiDraw();
		generateRandomGrid();
		printGrid();
	}
	
	/*
	 * Prints the grid for debugging purposes only
	 */
	private void printGrid()
	{
		for(int i = 0; i < rows; ++i)
		{
			for(int j = 0; j < cols; ++j)
			{
				if(gridValue[i][j] == FLAG)
					System.out.print("F ");
				else if(gridValue[i][j] == MINE)
					System.out.print("M ");
				else
					System.out.print(gridValue[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	/*
	 * Generates a random grid
	 */
	private void generateRandomGrid()
	{
		/*
		 * Mark all the mine positions false and initialize the grid
		 */
		for(int i = 0; i < rows; ++i)
		{
			for(int j = 0; j < cols; ++j)
			{
				gridValue[i][j] = EMPTY;
				gridStatus[i][j] = UNDISCOVERED;
			}
		}
		
		/*
		 * Create a new random function
		 */
		Random rand = new Random();
		
		/*
		 * Generate random position for flag_X and flag_Y
		 */
		flagPosX = rand.nextInt(rows);
		flagPosY = rand.nextInt(cols);
		gridValue[flagPosX][flagPosY] = FLAG;

		/*
		 * Generate random mineCount positions and mark them as containing mines
		 */
		int x, y;
		for(int i = 0; i < mineCount; ++i)
		{
			x = rand.nextInt(rows);
			y = rand.nextInt(cols);
			while((x == flagPosX && y == flagPosY) || gridValue[x][y] == MINE)
			{
				x = rand.nextInt(rows);
				y = rand.nextInt(cols);
			}
			// for debugging purposes only. Uncomment here to debug
			//System.out.println(x + " " + y);
			
			gridValue[x][y] = MINE;
		}
		
		/*
		 * Generate values at each cell
		 */
		for(int i = 0; i < rows; ++i)
		{
			for(int j = 0; j < cols; ++j)
			{
				if(gridValue[i][j] == EMPTY)
				{
					gridValue[i][j] = getNeighbourMineCount(i, j);
				}
			}
		}
	}
	
	/*
	 * Counts the number of neighbouring mines
	 */
	private int getNeighbourMineCount(int i, int j)
	{
		int cnt = 0;
		
		// upper left cell from current cell
		if(i > 0 && j > 0 && gridValue[i - 1][j - 1] == MINE)
			cnt++;

		// upper cell from current cell
		if(i > 0 && gridValue[i - 1][j] == MINE)
			cnt++;

		// upper right cell from current cell
		if(i > 0 && j < cols - 1 && gridValue[i - 1][j + 1] == MINE)
			cnt++;

		// left cell from current cell
		if(j > 0 && gridValue[i][j - 1] == MINE)
			cnt++;

		// right cell from current cell
		if(j < cols - 1 && gridValue[i][j + 1] == MINE)
			cnt++;

		// lower left cell from current cell
		if(i < rows - 1 && j > 0 && gridValue[i + 1][j - 1] == MINE)
			cnt++;

		// lower cell from current cell
		if(i < rows - 1 && gridValue[i + 1][j] == MINE)
			cnt++;

		// lower right cell from current cell
		if(i < rows - 1 && j < cols - 1 && gridValue[i + 1][j + 1] == MINE)
			cnt++;
		return cnt;
	}
	
	/*
	 * Draws the current grid on the window
	 */
	public void drawGrid(Graphics g, HashMap<String, BufferedImage> images, int timeLeft)
	{
		gui.draw_Grid(this, images, g, timeLeft);
	}
	
	/*
	 * Makes a move and calls generateMoves to generate further moves
	 */
	public void makeMove(int i, int j)
	{
		unDiscoveredCount--;
		gridStatus[i][j] = gridValue[i][j];
		if(gridValue[i][j] == EMPTY)
			generateMoves(i, j);
	}
	
	/*
	 * Generate further moves if the current cell was empty
	 */
	public void generateMoves(int i, int j)
	{
		/*
		 * check if upper left cell is not a mine and is undiscovered
		 */
		if(i > 0 && j > 0 && gridValue[i - 1][j - 1] != MINE && gridValue[i - 1][j - 1] != FLAG && gridStatus[i - 1][j - 1] == UNDISCOVERED)
		{
			unDiscoveredCount--;
			gridStatus[i - 1][j - 1] = gridValue[i - 1][j - 1];			
			/*
			 * if this cell is again empty call generate moves
			 */
			if(gridValue[i - 1][j - 1] == EMPTY)
				generateMoves(i - 1, j - 1);
		}
		
		/*
		 * check if upper cell is not a mine and is undiscovered
		 */
		if(i > 0 && gridValue[i - 1][j] != MINE && gridValue[i - 1][j] != FLAG && gridStatus[i - 1][j] == UNDISCOVERED)
		{
			unDiscoveredCount--;
			gridStatus[i - 1][j] = gridValue[i - 1][j];
			/*
			 * if this cell is again empty call generate moves
			 */
			if(gridValue[i - 1][j] == EMPTY)
				generateMoves(i - 1, j);
		}
		
		/*
		 * check if upper right cell is not a mine and is undiscovered
		 */
		if(i > 0 && j < cols - 1 && gridValue[i - 1][j + 1] != MINE && gridValue[i - 1][j + 1] != FLAG && gridStatus[i - 1][j + 1] == UNDISCOVERED)
		{
			unDiscoveredCount--;
			gridStatus[i - 1][j + 1] = gridValue[i - 1][j + 1];
			/*
			 * if this cell is again empty call generate moves
			 */
			if(gridValue[i - 1][j + 1] == EMPTY)
				generateMoves(i - 1, j + 1);
		}
		
		/*
		 * check if left cell is not a mine and is undiscovered
		 */
		if(j > 0 && gridValue[i][j - 1] != MINE && gridValue[i][j - 1] != FLAG && gridStatus[i][j - 1] == UNDISCOVERED)
		{
			unDiscoveredCount--;
			gridStatus[i][j - 1] = gridValue[i][j - 1];
			/*
			 * if this cell is again empty call generate moves
			 */
			if(gridValue[i][j - 1] == EMPTY)
				generateMoves(i, j - 1);
		}
		
		/*
		 * check if right cell is not a mine and is undiscovered
		 */
		if(j < cols - 1 && gridValue[i][j + 1] != MINE && gridValue[i][j + 1] != FLAG && gridStatus[i][j + 1] == UNDISCOVERED)
		{
			unDiscoveredCount--;
			gridStatus[i][j + 1] = gridValue[i][j + 1];
			/*
			 * if this cell is again empty call generate moves
			 */
			if(gridValue[i][j + 1] == EMPTY)
				generateMoves(i, j + 1);
		}
		
		/*
		 * check if lower left cell is not a mine and is undiscovered
		 */
		if(i < rows - 1 && j > 0 && gridValue[i + 1][j - 1] != MINE && gridValue[i + 1][j - 1] != FLAG && gridStatus[i + 1][j - 1] == UNDISCOVERED)
		{
			unDiscoveredCount--;
			gridStatus[i + 1][j - 1] = gridValue[i + 1][j - 1];
			/*
			 * if this cell is again empty call generate moves
			 */
			if(gridValue[i + 1][j - 1] == EMPTY)
				generateMoves(i + 1, j - 1);
		}
		
		/*
		 * check if lower cell is not a mine and is undiscovered
		 */
		if(i < rows - 1 && gridValue[i + 1][j] != MINE && gridValue[i + 1][j] != FLAG && gridStatus[i + 1][j] == UNDISCOVERED)
		{
			unDiscoveredCount--;
			gridStatus[i + 1][j] = gridValue[i + 1][j];
			/*
			 * if this cell is again empty call generate moves
			 */
			if(gridValue[i + 1][j] == EMPTY)
				generateMoves(i + 1, j);
		}
		
		/*
		 * check if lower right cell is not a mine and is undiscovered
		 */
		if(i < rows - 1 && j < cols - 1 && gridValue[i + 1][j + 1] != MINE && gridValue[i + 1][j + 1] != FLAG && gridStatus[i + 1][j + 1] == UNDISCOVERED)
		{
			unDiscoveredCount--;
			/*
			 * if this cell is again empty call generate moves
			 */
			gridStatus[i + 1][j + 1] = gridValue[i + 1][j + 1];
			if(gridValue[i + 1][j + 1] == EMPTY)
				generateMoves(i + 1, j + 1);
		}
	}
}
