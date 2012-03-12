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

	Compass compassPanel;
	Inclinometer inclinometerPanel;
	int degree = 0;
	
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
				degree++;
				compassPanel.updateGreenNeedle(degree);
				compassPanel.updateRedNeedle(-degree);
				inclinometerPanel.updateGreenNeedle(degree);
				inclinometerPanel.updateRedNeedle(-degree);
		    }
		};
		return new Timer (10, action);
  }
	      
	public MainView() {
		
		
		
		
		
		
		//initComponents();
		//setLocationRelativeTo(null);
		
		
		compassPanel = new Compass();
		inclinometerPanel = new Inclinometer();
		inclinometerPanel.setLocation(400, 400);
		this.add(compassPanel);
		this.add(inclinometerPanel);
		getLayeredPane().add(compassPanel);
		getLayeredPane().add(inclinometerPanel);
		//jlp_compass.add(compassPanel);
		this.setVisible(true);
		this.setExtendedState(this.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setBackground(Color.BLACK);
		repaint();
		//pack();
		
		
		

		Timer timer = createTimer();
		timer.start();
		
		
		
		
		
		Settings set = new Settings();
		Serializer serializer = new Serializer();
		set.setPort("com4");
		Serializer.serialize("settings.lol",set);
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
	
	@SuppressWarnings("serial")
	private class Compass extends JLayeredPane
	{
		JLabel background;
		JLabel coordinate;
		Needle redNeedle = new Needle("RED");
		Needle greenNeedle = new Needle("GREEN");
		
		public Compass()
		{
			background = new JLabel(new ImageIcon("res/backgroundCompass.png"));
			background.setBounds(0, 0, 345, 304);
			this.add(background, new Integer(0));
			this.setVisible(true);
			this.setBackground(Color.BLACK);
			redNeedle.setBackground(this.getBackground());
			redNeedle.setBounds(0, 0, 345, 304);
			greenNeedle.setBackground(this.getBackground());
			greenNeedle.setBounds(0, 0, 345, 304);
			redNeedle.setOpaque(false);
			greenNeedle.setOpaque(false);
			this.updateGreenNeedle(120);
			this.updateRedNeedle(90);
			this.add(redNeedle, new Integer(1));
			this.add(greenNeedle, new Integer(2));
			
			this.setBounds(0, 0, 345, 350);
			this.repaint();
			coordinate = new JLabel("-10°2'13'' N", JLabel.CENTER);
			coordinate.setFont(new Font("Calibri", Font.BOLD, 36));
			coordinate.setBounds(0, 310, 345, 34);
			coordinate.setForeground(Color.WHITE);
			
			this.add(coordinate, new Integer(3));
		}
		
		public void updateRedNeedle (double _angle) 
		{
            _angle = Math.toRadians(_angle);
            redNeedle.rotate(_angle);			
		}
		
		public void updateGreenNeedle (double _angle) 
		{
            _angle = Math.toRadians(_angle);
            greenNeedle.rotate(_angle);
		}
		
		@SuppressWarnings("serial")
		private class Needle extends JPanel
		{
		    BufferedImage needleImage;
		    double angle = 0;
		    
			public Needle(String _color)
			{
				if(_color == "RED")
				{
					try 
					{
						needleImage = ImageIO.read(new File("res/aiguille_rouge.png"));
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
				else if(_color == "GREEN")
				{
					try
					{
						needleImage = ImageIO.read(new File("res/aiguille_vert.png"));
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
			
			public void rotate(double _angle)
			{
				angle = _angle;
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
		JLabel background;
		JLabel coordinate;
		Needle redNeedle = new Needle("RED");
		Needle greenNeedle = new Needle("GREEN");
		
		public Inclinometer()
		{
			background = new JLabel(new ImageIcon("res/backgroundInclinometer.png"));
			background.setBounds(0, 0, 194, 279);
			this.add(background, new Integer(0));
			this.setVisible(true);
			this.setBackground(Color.BLACK);
			redNeedle.setBackground(this.getBackground());
			redNeedle.setBounds(0, 0, 194, 279);
			greenNeedle.setBackground(this.getBackground());
			greenNeedle.setBounds(0, 0, 194, 279);
			redNeedle.setOpaque(false);
			greenNeedle.setOpaque(false);
			this.updateGreenNeedle(-45);
			this.updateRedNeedle(45);
			this.add(redNeedle, new Integer(1));
			this.add(greenNeedle, new Integer(2));
			
			this.setBounds(0, 0, 194, 324);
			this.repaint();
			coordinate = new JLabel("-10°2'13'' N", JLabel.CENTER);
			coordinate.setFont(new Font("Calibri", Font.BOLD, 36));
			coordinate.setBounds(0, 274, 194, 35);
			coordinate.setForeground(Color.WHITE);
			
			this.add(coordinate, new Integer(3));
		}
		
		public void updateRedNeedle (double _angle) 
		{
            _angle = Math.toRadians(_angle);
            redNeedle.rotate(_angle);			
		}
		
		public void updateGreenNeedle (double _angle) 
		{
            _angle = Math.toRadians(_angle);
            greenNeedle.rotate(_angle);
		}

		@SuppressWarnings("serial")
		private class Needle extends JPanel
		{
		    BufferedImage needleImage;
		    double angle = 0;
		    
			public Needle(String _color)
			{
				if(_color == "RED")
				{
					try
					{
						needleImage = ImageIO.read(new File("res/aiguille_rouge_inclinometer.png"));
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
				else if(_color == "GREEN")
				{
					try 
					{
						needleImage = ImageIO.read(new File("res/aiguille_vert_inclinometer.png"));
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
			
			public void rotate(double _angle)
			{
				angle = _angle;
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
	            g2.rotate(-angle, 10, needleImage.getHeight() / 2);  
	            g2.drawImage(needleImage, 0, 0, null); 
	        }
			
		}
	}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

        jPanel_informations = new javax.swing.JPanel();
        jPanel_fondNoir = new javax.swing.JPanel();
        jLabel_coordonees = new javax.swing.JLabel();
        jlp_zoom = new javax.swing.JLayeredPane();
        jlp_settings = new javax.swing.JLayeredPane();
        jlp_question = new javax.swing.JLayeredPane();
        jlp_searchBar = new javax.swing.JLayeredPane();
        jlp_compass = new javax.swing.JLayeredPane();
        jlp_height = new javax.swing.JLayeredPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(680, 420));

        jPanel_informations.setName("jPanel_informations"); // NOI18N

        javax.swing.GroupLayout jPanel_informationsLayout = new javax.swing.GroupLayout(jPanel_informations);
        jPanel_informations.setLayout(jPanel_informationsLayout);
        jPanel_informationsLayout.setHorizontalGroup(
            jPanel_informationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 112, Short.MAX_VALUE)
        );
        jPanel_informationsLayout.setVerticalGroup(
            jPanel_informationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 344, Short.MAX_VALUE)
        );

        jPanel_fondNoir.setBackground(new java.awt.Color(0, 0, 0));
        jPanel_fondNoir.setName("jPanel_fondNoir"); // NOI18N

        jLabel_coordonees.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_coordonees.setText("jLabel1");
        jLabel_coordonees.setName("jLabel_coordonees"); // NOI18N

        jlp_zoom.setBackground(new java.awt.Color(204, 204, 204));
        jlp_zoom.setName("jlp_zoom"); // NOI18N
        jlp_zoom.setOpaque(true);

        jlp_settings.setBackground(new java.awt.Color(204, 204, 204));
        jlp_settings.setName("jlp_settings"); // NOI18N
        jlp_settings.setOpaque(true);

        jlp_question.setBackground(new java.awt.Color(204, 204, 204));
        jlp_question.setName("jlp_question"); // NOI18N
        jlp_question.setOpaque(true);

        jlp_searchBar.setBackground(new java.awt.Color(204, 204, 204));
        jlp_searchBar.setName("jlp_searchBar"); // NOI18N
        jlp_searchBar.setOpaque(true);

        jlp_compass.setBackground(new java.awt.Color(204, 204, 204));
        jlp_compass.setName("jlp_compass"); // NOI18N
        jlp_compass.setOpaque(true);

        jlp_height.setBackground(new java.awt.Color(204, 204, 204));
        jlp_height.setName("jlp_height"); // NOI18N
        jlp_height.setOpaque(true);

        javax.swing.GroupLayout jPanel_fondNoirLayout = new javax.swing.GroupLayout(jPanel_fondNoir);
        jPanel_fondNoir.setLayout(jPanel_fondNoirLayout);
        jPanel_fondNoirLayout.setHorizontalGroup(
            jPanel_fondNoirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_fondNoirLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_fondNoirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jlp_height, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel_fondNoirLayout.createSequentialGroup()
                        .addComponent(jlp_zoom, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlp_settings, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlp_question, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel_fondNoirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlp_compass, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_fondNoirLayout.createSequentialGroup()
                                .addComponent(jlp_searchBar, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                                .addGap(86, 86, 86))))
                    .addComponent(jLabel_coordonees, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel_fondNoirLayout.setVerticalGroup(
            jPanel_fondNoirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_fondNoirLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_fondNoirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jlp_searchBar, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                    .addComponent(jlp_question)
                    .addComponent(jlp_settings)
                    .addComponent(jlp_zoom, javax.swing.GroupLayout.PREFERRED_SIZE, 20, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jlp_compass, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlp_height, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 142, Short.MAX_VALUE)
                .addComponent(jLabel_coordonees)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel_informations, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel_fondNoir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_fondNoir, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
            .addComponent(jPanel_informations, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>

    // Variables declaration - do not modify
    private javax.swing.JLabel jLabel_coordonees;
    private javax.swing.JPanel jPanel_fondNoir;
    private javax.swing.JPanel jPanel_informations;
    private javax.swing.JLayeredPane jlp_compass;
    private javax.swing.JLayeredPane jlp_height;
    private javax.swing.JLayeredPane jlp_question;
    private javax.swing.JLayeredPane jlp_searchBar;
    private javax.swing.JLayeredPane jlp_settings;
    private javax.swing.JLayeredPane jlp_zoom;
    // End of variables declaration
}
