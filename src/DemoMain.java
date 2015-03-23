/*
 * @author : Ashish Khatkar (ashish1610 on online judges)
 */
import java.io.IOException;
import javax.swing.SwingUtilities;
public class DemoMain
{
	public static void main(String[] args)
	{
		real();
	}
	private static void real()
	{
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run()
			{
				Game.play();
			}
		});
	}
}
