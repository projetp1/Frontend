/**
 * 
 */
package com.github.projetp1;

import com.github.projetp1.ListModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.sun.servicetag.SystemEnvironment;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author alexandr.perez
 *
 */
@SuppressWarnings("serial")
public class MainView extends JFrame implements KeyListener {
	
	private Settings settings;
	public Settings getSettings() { return settings; }
	private Pic pic;
	public Pic getPic() { return pic; }
	SkyMap skymap;
	
	private Compass compassPanel;
	private Inclinometer inclinometerPanel;
	private Buttons buttonsPanel;
	private SearchBar searchBarPanel;
	private ZoomBar zoomBarPanel;
	private Help helpPanel;
	private SettingsConfig settingsPanel;
	private JLabel coordinate;
	private int zoom = 1;
	private int coord;
	private int degree;
	private double xOrigin = 0;
	private double yOrigin = 0;
	private double w = calculateScale();
	private double w_old = w;
	
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
				//w = calculateScale();
				degree++;
				coord++;
				compassPanel.setGreenNeedle(degree);
				compassPanel.setRedNeedle(-degree);
				inclinometerPanel.setRedNeedle(degree);
				inclinometerPanel.setGreenNeedle(-degree);
		    }
		};
		return new Timer (50, action);
  }
		      
	public MainView() {
		
		w = calculateScale();
		this.addKeyListener(this);
		name = new JLabel("<html>Nom de l'astre<br />Jupiter<br /><br />Coordonnées<br />13,123<br /><br />Masse<br />1,8986*10^27<br /><br />Magnitude<br />-2,8<br /><br />Distance(Terre)<br />628 000 000 km<br /><br />Diamètre<br />142983 km<br /><br />Température<br />-161°C<br /><br />Couleur<br />Beige</html>");
		name.setBounds(100, 100, 100, 200);
		name.setForeground(new Color(250,250,250));
		getLayeredPane().add(name);
		
		coordinate = new JLabel(coord + " °N");
		coordinate.setBounds(this.getWidth()-200, this.getHeight()-20, 200, 20);
		coordinate.setForeground(Color.WHITE);
		getLayeredPane().add(coordinate);
		

        buttonsPanel = new Buttons(0.1);
		buttonsPanel.setLocation((int)(width()/2-buttonsPanel.getWidth()/2), 5);
		
		helpPanel = new Help(1);
		helpPanel.setLocation((int)(width()/2-buttonsPanel.getWidth()/2-10*w), buttonsPanel.getHeight());
		
		settingsPanel = new SettingsConfig(1);
		settingsPanel.setLocation((int)(width()/2-2*buttonsPanel.getWidth()), buttonsPanel.getHeight());
		
		searchBarPanel = new SearchBar(w);
		searchBarPanel.setLocation(0, 5);
		
		zoomBarPanel = new ZoomBar(w);
		zoomBarPanel.setLocation(5, 5);
		
		compassPanel = new Compass(0.8);
		compassPanel.setLocation((int)(width()-10-compassPanel.getWidth()), 50);		
		
		inclinometerPanel = new Inclinometer(0.8);
		inclinometerPanel.setLocation((int)(width()-10-inclinometerPanel.getWidth()), (100+inclinometerPanel.getHeight()));


		skymap = new SkyMap("hyg.db",";",this);
		
		this.setFocusable(true);

		skymap.setSize(this.getWidth()-200,this.getHeight()-20);
		skymap.setLocation(200, 20);
		skymap.updateSkyMap();
		
		getLayeredPane().add(buttonsPanel);
		getLayeredPane().add(searchBarPanel);
		getLayeredPane().add(helpPanel);
		getLayeredPane().add(settingsPanel);
		getLayeredPane().add(zoomBarPanel);
		getLayeredPane().add(compassPanel);
		getLayeredPane().add(inclinometerPanel);
		getLayeredPane().add(skymap);

        this.setMinimumSize(new java.awt.Dimension(680, 420));
		
		this.setVisible(true);
		this.setExtendedState(this.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setBackground(Color.BLACK);
		repaint();
		//pack();
		
		Timer timer = createTimer();
		timer.start();
		
		
		settings = new Settings();
		Serializer.serialize("settings.lol",settings);
		
		pic = new Pic(this);
		
		
		addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
		
		repaint();
	}
	

	public void keyTyped(KeyEvent evt){}
	
	public void keyReleased(KeyEvent evt){}  

	public void keyPressed(KeyEvent evt) {
        if(evt.getKeyCode() == 37) //Left
        {
        	if(xOrigin > -1)
        		xOrigin -= 0.02;
        }
        else if(evt.getKeyCode() == 39) //Right
        {
        	if(xOrigin < 1)
        		xOrigin += 0.02;
        }
        else if(evt.getKeyCode() == 38) // Up
        {
        	if(yOrigin < 1)
        		yOrigin += 0.02;
        }
        else if(evt.getKeyCode() == 40) // Down
        {
        	if(yOrigin > -1)
        		yOrigin -= 0.02;
        }
        else if(evt.getKeyCode() == (int)'.') //+
        {
        	zoom++;
        }
        else if(evt.getKeyCode() == (int)'-') //-
        {
        	if(zoom>1)
        		zoom--;
        }

        skymap.setZoom(zoom);
        skymap.setXOrigin(xOrigin);
        skymap.setYOrigin(yOrigin);
        skymap.updateSkyMap();
		
		repaint();
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
		name.setText("<html>Nom de l'astre<br />" +
				_object.getProperName() +
				"<br /><br />Magnitude<br />" +
				_object.getMag() +
				"<br /><br />Distance(Terre)<br />" +
				_object.getDistance() +
				"<br /><br />Couleur<br />" +
				_object.getColorIndex() +
				"</html>");
		
		System.out.println("MainView.updateInfo()");
	}
	
	public ArrayList<CelestialObject> searchForTextInSearchField() {
		//request ddb
		//text form search field
		return null;
	}

	private double calculateScale()
	{
		double w =  width() * 0.15 / 345;
		double h =  height() * 0.30 / 350;
		if(w>h)w=h;
		if(w<.1)w=.1;
		return w;
	}

	int i;
	private void formComponentResized(java.awt.event.ComponentEvent evt) {
		
		
	    w = calculateScale();
	    
	    if (w - w_old > 0.001 || w- w_old < -0.001)
	    {
	    	w_old = w;
	    	
			buttonsPanel.setScale(w/3);
			compassPanel.setScale(w);
			inclinometerPanel.setScale(w);
			searchBarPanel.setScale(w);
			zoomBarPanel.setScale(w);
			helpPanel.setScale(w);
			settingsPanel.setScale(w);
			
			buttonsPanel.setLocation((int)(width()/2-buttonsPanel.getWidth()+(w*70)), 5);
			helpPanel.setLocation((int)(width()/2-buttonsPanel.getWidth()+(w*70)-10*w), buttonsPanel.getHeight()+(int)(20*w));
			settingsPanel.setLocation((int)(width()/2-settingsPanel.getWidth()+80*w), buttonsPanel.getHeight()+(int)(20*w));
			searchBarPanel.setLocation((int)(width()/2+buttonsPanel.getWidth()-(w*70)), (int)(buttonsPanel.getHeight()/2-10)+5);
			zoomBarPanel.setLocation(5, (int)(buttonsPanel.getHeight()/2-zoomBarPanel.getHeight()/2)+5);
			compassPanel.setLocation((int)(width()-compassPanel.getWidth())-20, 50);
			inclinometerPanel.setLocation((int)(width()-compassPanel.getWidth()+(w*70)), (100+inclinometerPanel.getHeight()));
			
			skymap.setBounds(0, 0, this.getWidth(), this.getHeight());
			skymap.setZoom(zoom);
			name.setBounds((int)(10*w), (int)(10*w), 100, this.getHeight());
			
			coordinate.setBounds(this.getWidth()-100, this.getHeight()-70, 100, 20);

	    }
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
    
    public static BufferedImage resizeImage2(BufferedImage bImage, double w, double h) {
        int destWidth = (int)(w);//*bImage.getWidth());
        int destHeight = (int)(h);//*bImage.getHeight());
        GraphicsConfiguration configuration = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        BufferedImage bImageNew = configuration.createCompatibleImage(destWidth, destHeight, 2);
        Graphics2D graphics = bImageNew.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        graphics.drawImage(bImage, 0, 0, destWidth, destHeight, 0, 0, bImage.getWidth(), bImage.getHeight(), null);
        graphics.dispose();

        return bImageNew;
    }

    private class Buttons extends JLayeredPane
    {
    	double scale;
    	BufferedImage imgSettings;
    	BufferedImage imgHelp;
    	public Buttons(double _scale)
    	{
    		scale = _scale;
    		try {
    			imgSettings = resizeImage(ImageIO.read(new File("res/SettingsIcon.png")), scale);
    			imgHelp = resizeImage(ImageIO.read(new File("res/HelpIcon.png")), scale);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		this.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    MouseClicked(evt);
                }
    		});
    		this.setBounds(0, 0, (int)(imgSettings.getWidth()*2), (int)(imgHelp.getHeight()));
    		this.setVisible(true);
    		this.repaint();
    	}
    	@Override 
        protected void paintComponent(Graphics g)
        { 
            super.paintComponent(g); 
            Graphics2D g2 = (Graphics2D) g; 
            g2.drawImage(imgSettings, 0, 0, null); 
            g2.drawImage(imgHelp, (int)(imgSettings.getWidth()+10*scale), 0, null);
        }

    	private void MouseClicked(java.awt.event.MouseEvent evt) {

    		if(evt.getX()<buttonsPanel.getWidth()/2)
    		{
    			//System.out.print("Settings ! \n");
    			if (settingsPanel.isVisible())
    				settingsPanel.setVisible(false);
    			else
    			{
    				if (helpPanel.isVisible())
    					helpPanel.setVisible(false);
    				settingsPanel.setVisible(true);
    			}
    		} else
    		{
    			//System.out.print("Help ! \n");
    			if (helpPanel.isVisible())
    				helpPanel.setVisible(false);
    			else
    			{
    				if (settingsPanel.isVisible())
    					settingsPanel.setVisible(false);
    				helpPanel.setVisible(true);
    			}
    		}
    	}

		public void setScale(double _scale)
		{
			scale = _scale;
			update();
		}
		public void update()
		{
			try {
				imgSettings = resizeImage(ImageIO.read(new File("res/SettingsIcon.png")), scale);
    			imgHelp = resizeImage(ImageIO.read(new File("res/HelpIcon.png")), scale);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.setBounds(0, 0, (int)(imgSettings.getWidth()*2), (int)(imgHelp.getHeight()));
			repaint();
		}
    }
   
    private class SettingsConfig extends JLayeredPane
    {
    	double scale;
    	int number = 5;
    	BufferedImage backgroundTop;
    	BufferedImage backgroundMid;
    	BufferedImage backgroundBot;
    	BufferedImage InternalTop;
    	BufferedImage[] InternalMid = new BufferedImage[number];
    	BufferedImage InternalBot;
    	JList settingsList;
    	ListModel settingsModel;
    	JScrollPane jScrollPane;
    	
    	public SettingsConfig(double _scale)
    	{
    		scale = _scale;
    		try {
    			backgroundTop = resizeImage(ImageIO.read(new File("res/settings-top-background.png")), scale/2);
    			backgroundMid = resizeImage(ImageIO.read(new File("res/settings-mid-background.png")), scale/2);
    			backgroundBot = resizeImage(ImageIO.read(new File("res/settings-bot-background.png")), scale/2);
    			InternalTop = resizeImage(ImageIO.read(new File("res/settings-top-internal.png")), scale/2);
    			for(int i = 0; i< number; i++)
    			{
    				InternalMid[i] = resizeImage(ImageIO.read(new File("res/settings-mid-internal.png")), scale/2);
    			}
    			InternalBot = resizeImage(ImageIO.read(new File("res/settings-bot-internal.png")), scale/2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		

    		settingsModel = new ListModel();
    		
    		String port[] = {"COM0", "COM1", "COM2","COM3", "COM4"};	//détection automatique à faire
    		String speed[] = {"test"};
    		String databit[] = {"test"};
    		String stopbit[] = {"test"};
    		String parity[] = {"test"};
    		String flowControl[] = {"test"};
    		String samplingRate[] = {"test"};
    		String databaseName[] = {"test"};
    		String imputDelimiter[] = {"test"};
    		String simulation[] = {"ON", "OFF"};
    		
    		settingsModel.setElement("port");
    		settingsModel.setElement("speed");
    		settingsModel.setElement("databit");
    		settingsModel.setElement("stopbit");
    		settingsModel.setElement("parity");
    		settingsModel.setElement("flowControl");
    		settingsModel.setElement("samplingRate");
    		settingsModel.setElement("databaseName");
    		settingsModel.setElement("imputDelimiter");
    		settingsModel.setElement("simulation");
    		
    		
    		settingsList = new JList();
    		settingsList.setModel(settingsModel);
    		jScrollPane = new JScrollPane();
    		jScrollPane.setViewportView(settingsList);
    		settingsList.setBounds(0, 0, 300, 400);
    		
    		this.add(jScrollPane);
    		
    		this.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    MouseClicked(evt);
                }
    		});
    		this.setBounds(0, 0, (int)(backgroundTop.getWidth()*2), (int)(500*scale));
    		this.setVisible(false);
    		this.repaint();
    	}
    	@Override 
        protected void paintComponent(Graphics g)
        { 
            super.paintComponent(g); 
            Graphics2D g2 = (Graphics2D) g; 
            g2.drawImage(backgroundTop, 0, 0, null); 
            g2.drawImage(backgroundMid, 0, backgroundTop.getHeight(), null);
            g2.drawImage(backgroundBot, 0, backgroundTop.getHeight()+backgroundMid.getHeight(), null);
            g2.drawImage(InternalTop, (int)(backgroundTop.getWidth()/2-InternalTop.getWidth()/2),  backgroundTop.getHeight()+25, null);
            for(int i = 0; i < number; i++)
            {
            	g2.drawImage(InternalMid[i], (int)(backgroundTop.getWidth()/2-InternalTop.getWidth()/2), backgroundTop.getHeight()+InternalTop.getHeight()+i*InternalMid[0].getHeight()+25, null);
            }
            	g2.drawImage(InternalBot, (int)(backgroundTop.getWidth()/2-InternalTop.getWidth()/2), backgroundTop.getHeight()+InternalTop.getHeight()+number*InternalMid[0].getHeight()+25, null);
		}

    	private void MouseClicked(java.awt.event.MouseEvent evt) {
    		if (evt.getY() > backgroundTop.getHeight()+25 && evt.getY() < backgroundTop.getHeight()+InternalTop.getHeight()+25)
    		{
    			System.out.print("1\n");
    		}
    	}
		public void setScale(double _scale)
		{
			scale = _scale;
			update();
		}
		public void update()
		{
			try {
				backgroundTop = resizeImage(ImageIO.read(new File("res/settings-top-background.png")), scale/2);
				backgroundMid = resizeImage(ImageIO.read(new File("res/settings-mid-background.png")), scale/2);
				backgroundBot = resizeImage(ImageIO.read(new File("res/settings-bot-background.png")), scale/2);
				InternalTop = resizeImage(ImageIO.read(new File("res/settings-top-internal.png")), scale/2);
				for(int i = 0; i < number; i++)
				{
					InternalMid[i] = resizeImage(ImageIO.read(new File("res/settings-mid-internal.png")), scale/2);
					jScrollPane.setBounds(0, 20*i, InternalMid[i].getWidth(), 100);
				}
				InternalBot = resizeImage(ImageIO.read(new File("res/settings-bot-internal.png")), scale/2);
    		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.setBounds(0, 0, (int)(backgroundTop.getWidth()), (int)(500*scale));
			repaint();
		}
    }
    
    private class Help extends JLayeredPane
    {
    	double scale;
    	BufferedImage backgroundTop;
    	BufferedImage backgroundMid;
    	BufferedImage backgroundBot;
    	BufferedImage internalTop;
    	BufferedImage internalMid;
    	BufferedImage internalBot;
    	
    	JLabel titre;
    	JLabel text;
    	
    	public Help(double _scale)
    	{
    		scale = _scale;
    		try {
    			backgroundTop = resizeImage(ImageIO.read(new File("res/haut-fond.png")), scale/2);
    			backgroundMid = resizeImage2(ImageIO.read(new File("res/mid-fond.png")), 1, 200*scale/2);
    			backgroundBot = resizeImage(ImageIO.read(new File("res/bas-fond.png")), scale/2);
    			internalTop = resizeImage(ImageIO.read(new File("res/haut-interieur.png")), scale/2);
    			internalMid = resizeImage2(ImageIO.read(new File("res/mid-interne.png")), 1, 50*scale/2);
    			internalBot = resizeImage(ImageIO.read(new File("res/bas-interieur.png")), scale/2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    			titre = new JLabel("Help", JLabel.CENTER);
    			titre.setFont(new Font("Calibri", Font.BOLD, 36));
    			titre.setBounds(0, backgroundTop.getHeight(), (int)(scale*345), (int)(scale*34));
    			titre.setForeground(Color.WHITE);
    			this.add(titre);
    			
    			text = new JLabel("<html>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque imperdiet, nisi<br />ornare molestie tempor, tellus mi dictum erat, at sagittis nunc dolor pulvinar justo.<br />Vivamus ullamcorper, arcu non laoreet suscipit, risus ligula consequat sapien, in<br />tempus turpis<br />elit imperdiet dolor. Proin elit augue, facilisis eu luctus at, pellentesque id tortor.<br />Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean sit amet imperdiet<br />libero. Etiam sagittis lorem non tellus mollis tristique. In euismod commodo nibh in<br />ultrices.<br /></html>");
    			text.setFont(new Font("Calibri", Font.BOLD, (int)(scale*36)));
    			text.setBounds(0, internalTop.getHeight(), (int)(scale*345), (int)(scale*34*8));
    			text.setForeground(Color.BLACK);
    			this.add(text);
    			
    			this.setBounds(0, 0, (int)(backgroundTop.getWidth()*2), (int)(500*scale));
    			this.setVisible(false);
    			
    			this.repaint();
    	}
    	@Override 
        protected void paintComponent(Graphics g)
        { 
            super.paintComponent(g); 
    		int posY = backgroundTop.getHeight();
            Graphics2D g2 = (Graphics2D) g; 
            g2.drawImage(backgroundTop, 0, 0, null); 
            g2.drawImage(backgroundMid, 0, posY, null);
            posY += backgroundMid.getHeight();
            g2.drawImage(backgroundBot, 0, posY, null);
            posY = backgroundTop.getHeight()+titre.getHeight();
            int posX = (int)(backgroundTop.getWidth()/2-internalTop.getWidth()/2);
            g2.drawImage(internalTop, posX, posY, null); 
            posY += internalTop.getHeight();
            g2.drawImage(internalMid, posX, posY, null);
            posY += internalMid.getHeight();
            g2.drawImage(internalBot, posX, posY, null);
        }

		public void setScale(double _scale)
		{
			scale = _scale;
			update();
		}
		public void update()
		{
			
			try {
    			backgroundTop = resizeImage(ImageIO.read(new File("res/haut-fond.png")), scale/2);
    			backgroundBot = resizeImage(ImageIO.read(new File("res/bas-fond.png")), scale/2);
    			internalTop = resizeImage(ImageIO.read(new File("res/haut-interieur.png")), scale/2);
    			
    			titre.setFont(new Font("Calibri", Font.BOLD,  (int)(scale*36)));
    			titre.setBounds(0, backgroundTop.getHeight(), backgroundTop.getWidth(), (int)(scale*35));
    		
    			text.setFont(new Font("Calibri", Font.BOLD,  (int)(scale*24)));
    			text.setBounds((int)(40*scale), backgroundTop.getHeight()+internalTop.getHeight(), (int)(internalTop.getWidth()), (int)(scale*26*9));
    			
    			
    			internalMid = resizeImage2(ImageIO.read(new File("res/mid-interne.png")), text.getWidth(), text.getHeight()-52*scale);
    			backgroundMid = resizeImage2(ImageIO.read(new File("res/mid-fond.png")), backgroundTop.getWidth(), text.getHeight()+titre.getHeight()*3);
    			internalBot = resizeImage(ImageIO.read(new File("res/bas-interieur.png")), scale/2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.setBounds(0, 0, (int)(backgroundTop.getWidth()*2), (int)(500*scale));
			repaint();
		}
    }
    
    private class ZoomBar extends JLayeredPane
    {
    	double scale;
    	int hig;
    	JSlider zoomSlider;
    	public ZoomBar(double _scale)
    	{
    		scale = _scale;
    		hig = (int)(30*scale);
    		this.setBounds(0, 0, (int)(width()/2), hig);
    		zoomSlider = new JSlider();
    		zoomSlider.setBounds(0, 0, (int)(width()/2), hig);
    		zoomSlider.setMinimum(1);
    		zoomSlider.setMaximum(40);
    		zoomSlider.setValue(zoom);
    		zoomSlider.setBackground(Color.BLACK);
    		this.add(zoomSlider);
			this.setVisible(true);
			this.repaint();
			zoomSlider.addChangeListener(new javax.swing.event.ChangeListener() {
	            public void stateChanged(javax.swing.event.ChangeEvent evt) {
	                jSlider1StateChanged(evt);
	            }
	        });
    	}
    	private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {
    		zoom = zoomSlider.getValue();
    		skymap.setZoom(zoom);
    		skymap.updateSkyMap();
    	}
		public void setScale(double _scale)
		{
			scale = _scale;
    		//hig = (int)(30*scale);
			hig = 30;
			update();
		}
		public void update()
		{
			
			this.setBounds(0, 0, (int)(width()/2-buttonsPanel.getWidth()/2-70*scale), hig);
			zoomSlider.setBounds(0, 0, (int)(width()/2-buttonsPanel.getWidth()/2-70*scale), hig);
			repaint();
		}
    }

    private class SearchBar extends JLayeredPane
    {
    	double scale;
    	int hig;
    	JTextField jtextField;
    	JList jList1;
    	ListModel list;
    	JScrollPane jScrollPane = new JScrollPane();
    	DataBase db;
    	ArrayList<CelestialObject> listCelestialObject;

    	
    	public SearchBar(double _scale)
    	{
    		scale = _scale;
    		hig = (int)(30*scale);
    		this.setBounds(0, 0, (int)(width()/2), hig);
    		jtextField = new JTextField();
    		jtextField.setBounds(0, 0, (int)(width()/2), hig);
    		jtextField.addKeyListener(new java.awt.event.KeyAdapter() {
    			public void keyReleased(java.awt.event.KeyEvent evt) {
                    try
					{
						jSlider1KeyReleased(evt);
					}
					catch (Exception ex)
					{
						// TODO Auto-generated catch block
						ex.printStackTrace();
					}
                }
            });

    		jtextField.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    MouseClicked(evt);
                }
    		});
    		
       
    		
    		list = new ListModel();
    		jList1 = new JList();
    		jList1.setModel(list);
    		jList1.setBounds(0, 0, 300, 400);
    		jScrollPane.setViewportView(jList1);
            
    		this.add(jtextField);
    		this.add(jScrollPane);
			this.setVisible(true);
			this.repaint();
    	}

    	private void MouseClicked(java.awt.event.MouseEvent evt) {
    		
    		jtextField.setText((String)jList1.getSelectedValue());

    	}
    	private void jSlider1KeyReleased(java.awt.event.KeyEvent evt) throws Exception {

    		System.out.print(jtextField.getText()+"\n");
    		if(jtextField.getText().length()>4)
    		{
    			db = new DataBase("hyg.db", ";");
    			listCelestialObject = new ArrayList<CelestialObject>();
    			System.out.print(jtextField.getText()+"\nlength = "+jtextField.getText().length()+"\n");
    			listCelestialObject = db.starsForText(jtextField.getText(), Calendar.getInstance(), 47.039448, 6.799734);
        	 	
    			if(listCelestialObject.size() != 0)
    			{	
    				for (int i = 0; i < listCelestialObject.size(); i++)
    				{
    					list.setElement(listCelestialObject.get(i).getId());
    					System.out.println(listCelestialObject.get(i).getDistance());
    				}
    			}
    			else
    				System.out.println("Aucun résultat n'a été trouvé dans la base de données");

			db.closeConnection();

    		}
			
        	if (list.getSize() > 0)
        	{
        		//System.out.print(listCelestialObject.get(0).getId());
            	//System.out.print(list.getElementAt(0));
            	jScrollPane.setBounds(0, 20, 300, 400);
            	jScrollPane.setVisible(true);
        	} else
        	{
        		jScrollPane.setVisible(false);
        	}
       
        	repaint();
    	 	
    	}
		public void setScale(double _scale)
		{
			scale = _scale;
			hig = 20;
			update();
		}
		public void update()
		{
			
			this.setBounds(0, 0, (int)(width()/2-buttonsPanel.getWidth()/2-70*scale-compassPanel.getWidth()), hig+(int)(100*scale));
    		jtextField.setBounds(0, 0, (int)(width()/2-buttonsPanel.getWidth()/2-70*scale-compassPanel.getWidth()), hig);
    		//complement.setBounds(0, hig, (int)(width()/2-buttonsPanel.getWidth()/2-70*scale-compassPanel.getWidth()), hig+(int)(100*scale));
    		repaint();
		}
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
			
			this.setBounds(0, 0, (int)(scale*345), (int)(scale*350));
			this.repaint();
			coordinate = new JLabel("-10:2'13'' N", JLabel.CENTER);
			coordinate.setFont(new Font("Calibri", Font.BOLD, 36));
			coordinate.setBounds(0, (int)(scale*310), (int)(scale*345), (int)(scale*34));
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
				background = resizeImage(ImageIO.read(new File("res/backgroundCompass.png")),scale);
			} catch (IOException e) {
				e.printStackTrace();
			}
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
			repaint();
		}
		
		public void setRedNeedle (double _angle) 
		{
            _angle = Math.toRadians(_angle);
            redAngle = _angle;
		}
		
		public void setGreenNeedle (double _angle) 
		{
            _angle = Math.toRadians(_angle);
            greenAngle =_angle;
		}
		
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
			coordinate = new JLabel("-10:2'13'' N", JLabel.CENTER);
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
	
	private JLayeredPane leftPanel;
	private JLabel name; 
}
