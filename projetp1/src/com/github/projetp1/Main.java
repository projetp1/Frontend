package com.github.projetp1;

public class Main {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) 
	{
		System.out.print("Test !");

		System.out.print("je travail !\n");
		System.out.print("ici ");
		System.out.print("Nous avons notre première fenêtre !");
		MainFrame jfrMainFrame = new MainFrame();
		jfrMainFrame.setVisible(true);
		
		jfrMainFrame.getLblTest().setText("It works !");
	}

}
