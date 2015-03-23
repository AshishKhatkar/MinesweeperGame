/*
 * @author : Ashish Khatkar (ashish1610 on online judges)
 */
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.*;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;
public class GuiDraw 
{
	public GuiDraw(){}
	private static void drawImage(BufferedImage img, Graphics g, int x, int y)
	{
		g.drawImage(img, 60 + x * 30, 80 + y * 30, 30, 30, null);
	}
	
	/*
	 * Draws the timer
	 */
	public void drawTime(int ones, int tens, Graphics g, BufferedImage onesImage, BufferedImage tensImage)
	{
		g.drawImage(tensImage, 140, 30, 30, 30, null);
		g.drawImage(onesImage, 170, 30, 30, 30, null);
	}
	
	/*
	 * Draws the grid on the window
	 */
	public void draw_Grid(Grid board, HashMap<String, BufferedImage> images, Graphics g, int timeLeft)
	{
//		System.out.println(timeLeft);
		int ones = timeLeft % 10;
		int tens = timeLeft / 10;
		BufferedImage onesImage, tensImage;
		if(tens == 1)
			tensImage = images.get("one.GIF");
		else if(tens == 2)
			tensImage = images.get("two.GIF");
		else if(tens == 3)
			tensImage = images.get("three.GIF");
		else if(tens == 4)
			tensImage = images.get("four.GIF");
		else if(tens == 5)
			tensImage = images.get("five.GIF");
		else if(tens == 6)
			tensImage = images.get("six.GIF");
		else
			tensImage = images.get("yellowface.GIF");
		
		if(ones == 1)
			onesImage = images.get("one.GIF");
		else if(ones == 2)
			onesImage = images.get("two.GIF");
		else if(ones == 3)
			onesImage = images.get("three.GIF");
		else if(ones == 4)
			onesImage = images.get("four.GIF");
		else if(ones == 5)
			onesImage = images.get("five.GIF");
		else if(ones == 6)
			onesImage = images.get("six.GIF");
		else if(ones == 7)
			onesImage = images.get("seven.GIF");
		else if(ones == 8)
			onesImage = images.get("eight.GIF");
		else if(ones == 9)
			onesImage = images.get("nine.GIF");
		else
			onesImage = images.get("yellowface.GIF");
		drawTime(ones, tens, g, onesImage, tensImage);
		for(int i = 0; i < board.rows; ++i)
		{
			for(int j = 0; j < board.cols; ++j)
			{
				/*if current cell is empty*/
				if(board.gridStatus[i][j] == board.EMPTY)
					drawImage(images.get("blank.GIF"), g, j, i);
				
				/*if current cell is finished*/
				else if(board.gridStatus[i][j] == board.FINISH)
					drawImage(images.get("sunglassface.GIF"), g, j, i);
				
				/*if current cell is mine*/
				else if(board.gridStatus[i][j] == board.MINE)
					drawImage(images.get("mine.GIF"), g, j, i);
				
				/*if current cell is marked as containing mine*/
				else if(board.gridStatus[i][j] == board.MARKED)
					drawImage(images.get("yellowface.GIF"), g, j, i);
				
				/*if current cell is yet to be discovered*/
				else if(board.gridStatus[i][j] == board.UNDISCOVERED)
					drawImage(images.get("unclicked.GIF"), g, j, i);

				/*if current cell contains flag*/
				else if(board.gridStatus[i][j] == board.FLAG)
					drawImage(images.get("flag.GIF"), g, j, i);
				
				/*if current cell has value 1*/
				else if(board.gridStatus[i][j] == 1)
					drawImage(images.get("one.GIF"), g, j, i);

				/*if current cell has value 2*/
				else if(board.gridStatus[i][j] == 2)
					drawImage(images.get("two.GIF"), g, j, i);

				/*if current cell has value 3*/
				else if(board.gridStatus[i][j] == 3)
					drawImage(images.get("three.GIF"), g, j, i);

				/*if current cell has value 4*/
				else if(board.gridStatus[i][j] == 4)
					drawImage(images.get("four.GIF"), g, j, i);
				
				/*if current cell has value 5*/
				else if(board.gridStatus[i][j] == 5)
					drawImage(images.get("five.GIF"), g, j, i);
				
				/*if current cell has value 6*/
				else if(board.gridStatus[i][j] == 6)
					drawImage(images.get("six.GIF"), g, j, i);

				/*if current cell has value 7*/
				else if(board.gridStatus[i][j] == 7)
					drawImage(images.get("seven.GIF"), g, j, i);

				/*if current cell has value 8*/
				else if(board.gridStatus[i][j] == 8)
					drawImage(images.get("eight.GIF"), g, j, i);
			}
		}
	}
}
