/**
 * 
 */
package com.github.projetp1;

import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author alexandr.perez
 *
 */
public class MainView extends JFrame {

	private Settings settings;
	
	public Settings getSettings() {
		return settings;
	}


	private Compass compassPanel;
	private Inclinometer inclinometerPanel;
	private int degree = 90;
	private double w = ((100/Toolkit.getDefaultToolkit().getScreenSize().width)*this.getWidth());
	private double h = ((100/Toolkit.getDefaultToolkit().getScreenSize().height)*this.getHeight());
	
	private double width()
	{
		return this.getWidth();
	}
	
	private double height()
	{
		return this.getHeight();
	}
	
	/**
	 * 
	 */
	private Timer createTimer () {
		// Création d'une instance de listener 
		// associée au timer
		
		
		ActionListener action = new ActionListener () {
		    // Méthode appelée à chaque tic du timer
			public void actionPerformed (ActionEvent event)
			{
					w =  width() / Toolkit.getDefaultToolkit().getScreenSize().width;
					h =  height() / Toolkit.getDefaultToolkit().getScreenSize().height;
					if(w>h)w=h;
					if(w<.1)w=.1;
					degree++;
					compassPanel.setGreenNeedle(degree);
					compassPanel.setRedNeedle(-degree);
					compassPanel.setScale(w);
					compassPanel.setLocation((int)(width()-compassPanel.getWidth()), 50);
					inclinometerPanel.setRedNeedle(degree);
					inclinometerPanel.setGreenNeedle(-degree);
					inclinometerPanel.setScale(w); //update() appelé auto
					inclinometerPanel.setLocation((int)(width()-compassPanel.getWidth()+(w*70)), (100+inclinometerPanel.getHeight())); //TODO constante
		    }
		};
		return new Timer (50, action);
  }
	      
	public MainView() {

        this.setMinimumSize(new java.awt.Dimension(680, 420));

		compassPanel = new Compass(0.8);
		compassPanel.setLocation((int)(width()-10-compassPanel.getWidth()), 50);		
		
		inclinometerPanel = new Inclinometer(0.8);
		inclinometerPanel.setLocation((int)(width()-10-inclinometerPanel.getWidth()), (100+inclinometerPanel.getHeight()));

		getLayeredPane().add(compassPanel);
		getLayeredPane().add(inclinometerPanel);
		
		this.setVisible(true);
		this.setExtendedState(this.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setBackground(Color.BLACK);
		repaint();
		pack();
		
		Timer timer = createTimer();
		timer.start();
		
		
		settings = new Settings();
		Serializer serializer = new Serializer();
		settings.setPort("com4");
		Serializer.serialize("settings.lol",settings);
	}

	public void showHelpView() {
		
	}

	public void showSettingsView() {
		
	}
	
	public void updateCompass(double _degree) {
		
	}
	
	public void updateAngle(double _degree) {
		
	}
	
	public void setZoom(int _zoom) {
		//update skymap
	}
	
	public void updateInfo(CelestialObject _object) {
		
	}
	
	public ArrayList<CelestialObject> searchForTextInSearchField() {
		//request ddb
		//text form search field
		return null;
	}
	
    public static BufferedImage resizeImage(BufferedImage bImage, double _scale) {
        int destWidth = (int)(_scale*bImage.getWidth());
        int destHeight = (int)(_scale*bImage.getHeight());
        GraphicsConfiguration configuration = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        BufferedImage bImageNew = configuration.createCompatibleImage(destWidth, destHeight, 2);
        Graphics2D graphics = bImageNew.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        graphics.drawImage(bImage, 0, 0, destWidth, destHeight, 0, 0, bImage.getWidth(), bImage.getHeight(), null);
        graphics.dispose();

        return bImageNew;
    }
	
    private class Compass extends JLayeredPane
	{
		double scale = 1;
		double redAngle = 0;
		double greenAngle = 0;
		BufferedImage background;
		JLabel coordinate;
		Needle redNeedle;
		Needle greenNeedle;
		
		public Compass(double _scale)
		{
			scale = _scale;
			try {
				background = resizeImage(ImageIO.read(new File("res/backgroundCompass.png")),scale);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			repaint();
			this.setVisible(true);
			this.setBackground(Color.BLACK);
			
			redNeedle = new Needle("res/aiguille_rouge.png", scale);
			greenNeedle = new Needle("res/aiguille_vert.png", scale);
			
			redNeedle.setBackground(this.getBackground());
			redNeedle.setBounds(0, 0, (int)(scale*345), (int)(scale*304));
			redNeedle.setOpaque(false);
			this.add(redNeedle, new Integer(1));
			
			greenNeedle.setBackground(this.getBackground());
			greenNeedle.setBounds(0, 0, (int)(scale*345), (int)(scale*304));
			greenNeedle.setOpaque(false);
			this.add(greenNeedle, new Integer(2));

			this.setBounds(0, 0, (int)(scale*186), (int)(scale*324));;
			coordinate = new JLabel("-10°2'13'' N", JLabel.CENTER);
			coordinate.setFont(new Font("Calibri", Font.BOLD,  (int)(scale*36)));
			coordinate.setBounds(0, (int)(scale*310), (int)(scale*345), (int)(scale*35));
			coordinate.setForeground(Color.WHITE);
			
			this.add(coordinate, new Integer(3));
			
			this.setBounds(0, 0, (int)(scale*345), (int)(scale*350));
			this.repaint();
		}
		
		@Override 
        public Dimension getPreferredSize()
		{ 
            return new Dimension(background.getWidth(), background.getHeight()); 
        } 
        @Override 
        protected void paintComponent(Graphics g)
        { 
            super.paintComponent(g); 
            Graphics2D g2 = (Graphics2D) g; 
            g2.drawImage(background, 0, 0, null); 
        }
		
		public void setScale (double _scale)
		{
			scale = _scale;
			update();
		}
		
		public void update()
		{
			try {
				background = resizeImage(ImageIO.read(new File("res/backgroundCompass.png")),scale);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			repaint();
			redNeedle.scale(scale);
            redNeedle.rotate(redAngle);
			redNeedle.setBounds(0, 0, (int)(scale*345), (int)(scale*304));
            greenNeedle.scale(scale);
            greenNeedle.rotate(greenAngle);
			greenNeedle.setBounds(0, 0, (int)(scale*345), (int)(scale*304));
			try
			{
				coordinate.setText(String.valueOf(redAngle%360));
			}
			catch(Exception e)
			{
				System.out.print(e);
			}
			this.setBounds(0, 0, (int)(scale*345), (int)(scale*350));
			coordinate.setFont(new Font("Calibri", Font.BOLD,  (int)(scale*36)));
			coordinate.setBounds(0, (int)(scale*310), (int)(scale*345), (int)(scale*35));
		}
		
		public void setRedNeedle (double _angle) 
		{
            redAngle = _angle;
		}
		
		public void setGreenNeedle (double _angle) 
		{
            greenAngle =_angle;
		}
		
		@SuppressWarnings("serial")
		private class Needle extends JPanel
		{
		    BufferedImage needleImage;
		    double angle = 0;
		    double scale = 1;
		    String adresseImage;
		    
			public Needle(String _adresseImage, double _scale)
			{
				scale = _scale;
				adresseImage = _adresseImage;
				try
				{
					needleImage = resizeImage(ImageIO.read(new File(adresseImage)),scale);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			
			public void scale(double _scale)
			{
				if(scale != _scale)
				{
					scale = _scale;
					try {
						needleImage = resizeImage(ImageIO.read(new File(adresseImage)),scale);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			public void rotate(double _angle)
			{
				angle = Math.toRadians(_angle);
				repaint();
			}
			
			@Override 
            public Dimension getPreferredSize()
			{ 
                return new Dimension(needleImage.getWidth(), needleImage.getHeight()); 
            } 
            @Override 
            protected void paintComponent(Graphics g)
            { 
                super.paintComponent(g); 
                Graphics2D g2 = (Graphics2D) g; 
                g2.rotate(angle, needleImage.getWidth() / 2, needleImage.getHeight() / 2); 
                g2.drawImage(needleImage, 0, 0, null); 
            }
		}
	
	}
	
	
	@SuppressWarnings("serial")
	private class Inclinometer extends JLayeredPane
	{
		double scale = 1;
		double redAngle = 0;
		double greenAngle = 0;
		BufferedImage background;
		JLabel coordinate;
		Needle redNeedle;
		Needle greenNeedle;
		
		public Inclinometer(double _scale)
		{
			scale = _scale;
			try {
				background = resizeImage(ImageIO.read(new File("res/backgroundInclinometer.png")),scale);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			repaint();
			this.setVisible(true);
			this.setBackground(Color.BLACK);
			
			redNeedle = new Needle("res/aiguille_rouge_inclinometer.png", scale);
			greenNeedle = new Needle("res/aiguille_vert_inclinometer.png", scale);
			
			redNeedle.setBackground(this.getBackground());
			redNeedle.setBounds(0, 0, (int)(scale*186), (int)(scale*258));
			redNeedle.setOpaque(false);
			this.add(redNeedle, new Integer(1));
			
			greenNeedle.setBackground(this.getBackground());
			greenNeedle.setBounds(0, 0, (int)(scale*186), (int)(scale*258));
			greenNeedle.setOpaque(false);
			this.add(greenNeedle, new Integer(2));

			this.setBounds(0, 0, (int)(scale*186), (int)(scale*324));;
			coordinate = new JLabel("-10°2'13'' N", JLabel.CENTER);
			coordinate.setFont(new Font("Calibri", Font.BOLD,  (int)(scale*36)));
			coordinate.setBounds(0, (int)(scale*258), (int)(scale*186), (int)(scale*35));
			coordinate.setForeground(Color.WHITE);
			
			this.add(coordinate, new Integer(3));
		}
				
		@Override 
        public Dimension getPreferredSize()
		{ 
            return new Dimension(background.getWidth(), background.getHeight()); 
        } 
        @Override 
        protected void paintComponent(Graphics g)
        { 
            super.paintComponent(g); 
            Graphics2D g2 = (Graphics2D) g; 
            g2.drawImage(background, 0, 0, null); 
        }
		
		public void setScale (double _scale)
		{ 
			scale = _scale;
			update();
		}
		
		public void update()
		{
			try {
				background = resizeImage(ImageIO.read(new File("res/backgroundInclinometer.png")),scale);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			repaint();
			redNeedle.scale(scale);
            redNeedle.rotate(redAngle);
			redNeedle.setBounds(0, 0, (int)(scale*186), (int)(scale*258));
            greenNeedle.scale(scale);
            greenNeedle.rotate(greenAngle);
			greenNeedle.setBounds(0, 0, (int)(scale*186), (int)(scale*258));
			try
			{
				coordinate.setText(String.valueOf(redAngle%360));
			}
			catch(Exception e)
			{
				System.out.print(e);
			}
			this.setBounds(0, 0, (int)(scale*186), (int)(scale*324));
			coordinate.setFont(new Font("Calibri", Font.BOLD,  (int)(scale*36)));
			coordinate.setBounds(0, (int)(scale*258), (int)(scale*186), (int)(scale*35));
		}
        
		public void setRedNeedle (double _redAngle) 
		{
			redAngle = _redAngle;		
		}
		
		public void setGreenNeedle (double _greenAngle) 
		{
			greenAngle = _greenAngle;
		}

		@SuppressWarnings("serial")
		private class Needle extends JPanel
		{
		    BufferedImage needleImage;
		    double angle = 0;
		    double scale = 1;
		    String adresseImage;
		    
			public Needle(String _adresseImage, double _scale)
			{
				scale = _scale;
				adresseImage = _adresseImage;
				try
				{
					needleImage = resizeImage(ImageIO.read(new File(adresseImage)),scale);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			
			public void scale(double _scale)
			{
				if(scale != _scale)
				{
					scale = _scale;
					try {
						needleImage = resizeImage(ImageIO.read(new File(adresseImage)),scale);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			public void rotate(double _angle)
			{
				angle = Math.toRadians(_angle);				
				
				if(Math.sin(angle) < 0 && Math.cos(angle) < 0)
					angle = angle + 2 * (3*Math.PI/2 - angle);
				if(Math.sin(angle) > 0 && Math.cos(angle) < 0)
					angle = angle - 2 * (angle - Math.PI/2);
				
				repaint();
			}
			
			@Override 
	        public Dimension getPreferredSize()
			{ 
	            return new Dimension(needleImage.getWidth(), needleImage.getHeight()); 
	        } 
	        @Override 
	        protected void paintComponent(Graphics g)
	        { 
	            super.paintComponent(g); 
	            Graphics2D g2 = (Graphics2D) g;
	            g2.rotate(-angle, scale*5, needleImage.getHeight() / 2); //TODO voir valeur non constante
	            g2.drawImage(needleImage, 0, 0, null); 
	        }			
		}
	}
}
