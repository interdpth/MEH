package org.zzl.minegaming.MEH;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main
{
	public static void main(String args[]) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
	{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		System.setProperty("sun.java2d.opengl","True");
		System.setProperty("sun.java2d.accthreshold","0");
		JFrame window = new MainGUI();
		window.setSize(1000, 600);
		window.setTitle("Map Editor of Happiness - No ROM Loaded");
		BankLoader.reset();
		window.setVisible(true);
		try
	    {
			
			PluginManager.loadAllPlugins();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
