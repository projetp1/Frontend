/**
 * 
 */
package com.github.projetp1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import com.github.projetp1.Pic.PicMode;


/**
 * @author alexandr.perez and issa.barbier
 * 
 */

@SuppressWarnings("serial")
public class MainView extends JFrame implements KeyListener
{
	private Settings settings = null;
	public Settings getSettings()
	{
		return settings;
	}
	private Pic pic = null;
	public Pic getPic()
	{
		return pic;
	}
	private DataBase db = null;
	public DataBase getDataBase()
	{
		return db;
	}
	

	private Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private SkyMap skymap = null;
	private Compass compassPanel;
	private Inclinometer inclinometerPanel;
	private Buttons buttonsPanel;
	private SearchBar searchBarPanel;
	private ZoomBar zoomBarPanel;
	private Help helpPanel;
	private SettingsConfig settingsPanel;
	private JLabel coordinate;
	private JLabel leftPanel;
	private int zoom = 2;
	private double xOrigin = 0;
	private double yOrigin = 0;
	private double scale = 0.1;
	private double scale_old = scale;
	
	/**
	 * Constructor
	 */
	public MainView()
	{
		// TODO: Externalize this string
		this.setTitle("Projet P1");
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("res/moon_6.png"));

		try
		{
			db = new DataBase("hyg.db",";");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		settings = new Settings();
		skymap = new SkyMap(this);
		pic = new Pic(this);

		leftPanel = new JLabel("");
		leftPanel.setBounds(100, 100, 100, 200);
		leftPanel.setForeground(new Color(250, 250, 250));
		
		coordinate = new JLabel(0 + "° N, " + 0 + "° S", JLabel.RIGHT);
		coordinate.setBounds((int)(20 * scale), this.getHeight() - (int)(20 * scale), 200, 20);
		coordinate.setForeground(Color.WHITE);
		
        buttonsPanel = new Buttons(scale);
		buttonsPanel.setLocation((int)(width() / 2 - buttonsPanel.getWidth() / 2), 5);

		helpPanel = new Help(scale);
		helpPanel.setLocation((int)(width() / 2 - buttonsPanel.getWidth() / 2 - 10 * scale),
				buttonsPanel.getHeight());

		settingsPanel = new SettingsConfig(scale);
		settingsPanel.setLocation((int)(width() / 2 - 2 * buttonsPanel.getWidth()),
				buttonsPanel.getHeight());

		searchBarPanel = new SearchBar(scale);
		searchBarPanel.setLocation(0, 5);

		zoomBarPanel = new ZoomBar(scale);
		zoomBarPanel.setLocation(5, 5);

		compassPanel = new Compass(scale);
		compassPanel.setLocation((int)(width() - 10 - compassPanel.getWidth()), 50);

		inclinometerPanel = new Inclinometer(scale);
		inclinometerPanel.setLocation((int)(width() - 10 - inclinometerPanel.getWidth()),
				(100+inclinometerPanel.getHeight()));

		getLayeredPane().add(leftPanel);
		getLayeredPane().add(coordinate);
		getLayeredPane().add(buttonsPanel);
		getLayeredPane().add(helpPanel);
		getLayeredPane().add(settingsPanel);
		getLayeredPane().add(searchBarPanel);
		getLayeredPane().add(zoomBarPanel);
		getLayeredPane().add(compassPanel);
		getLayeredPane().add(inclinometerPanel);
		getLayeredPane().add(skymap);

		skymap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                skymapMouseClicked(evt);
            }
		});

		this.addComponentListener(new java.awt.event.ComponentAdapter(){
            public void componentResized(java.awt.event.ComponentEvent evt){
                formComponentResized(evt);
            }
        });
		
		pic.addObservateur(new Observateur(){
			public void updatePIC() {
				update();
			}
		});


		Color l_BackgroundColor = new Color(5, 30, 50);
		this.getContentPane().setBackground(l_BackgroundColor);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("res/moon_6.png"));
		this.addKeyListener(this);
        this.setMinimumSize(new java.awt.Dimension(800, 600));
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setFocusable(true);
		this.update();
	}


	/**
	 * This methode update the values form the pic.
	 */
	
	private void update() {
		if (pic != null)
		{
			compassPanel.setGreenNeedle(pic.getAzimuth());
			inclinometerPanel.setGreenNeedle(pic.getPitch());
	
			char hemNS = 'N', hemWE = 'E';
			double lat = pic.getLatitude(), lon = pic.getLongitude();
	
			if (lat < 0.0)
				hemNS = 'S';
			if (lon < 0.0)
				hemWE = 'W';
			
			coordinate.setText(Math.abs(lat) + "° " + hemNS + ", " + Math.abs(lon) + "° " + hemWE);
		}

		skymap.updateSkyMap();		
	}
	
	/**
	 * When the user click on the skymap, the other window hide and the skymap will be selected.
	 */  
	private void skymapMouseClicked(java.awt.event.MouseEvent evt) {
		settingsPanel.setVisible(false);
		helpPanel.setVisible(false);
		searchBarPanel.jScrollPane.setVisible(false);
		if(!this.hasFocus())
			skymap.transferFocusBackward();
	}
	
	/**
	 * 
	 */  
	public void keyTyped(KeyEvent evt){}
	
	/**
	 * 
	 */  
	public void keyReleased(KeyEvent evt){} 
	
	/**
	 * navigation on the skymap.
	 */  
	public void keyPressed(KeyEvent evt) {
		if(pic.getMode() == PicMode.SIMULATION)
		{
			float l_fDelta = (float) (0.05 / zoom);
	        if(evt.getKeyCode() == 37) //Left
	        {
	        	if(xOrigin > -1)
	        		xOrigin -= l_fDelta;
	        }
	        else if(evt.getKeyCode() == 39) //Right
	        {
	        	if(xOrigin < 1)
	        		xOrigin += l_fDelta;
	        }
	        else if(evt.getKeyCode() == 38) // Up
	        {
	        	if(yOrigin < 1)
	        		yOrigin += l_fDelta;
	        }
	        else if(evt.getKeyCode() == 40) // Down
	        {
	        	if(yOrigin > -1)
	        		yOrigin -= l_fDelta;
	        }
	        else if(evt.getKeyCode() == (int)'.') //zoom +
	        {
	        	zoom++;
	        }
	        else if(evt.getKeyCode() == (int)'-') //zoom -
	        {
	        	if(zoom>1)
	        		zoom--;
	        }

	        zoomBarPanel.zoomSlider.setValue(zoom);
	        
	        skymap.setZoom(zoom);
	        skymap.setXOrigin(xOrigin);
	        skymap.setYOrigin(yOrigin);
	        skymap.updateSkyMap();
		}		
    }
	
	/**
	 * Update the information of the star in the leftPanel.
	 */  
	public void updateInfo(CelestialObject _object) {
		if(_object != null)
		{
			leftPanel.setText("<html>Nom de l'astre<br />" +
				_object.getProperName() +
				"<br /><br />Magnitude<br />" +
				_object.getMag() +
				"<br /><br />Distance(Terre)<br />" +
				(int)(_object.getDistance()*3.2616) +
				" a.l<br /><br />Couleur<br />" +
				_object.getColorIndex() +
				"</html>");
		}		
	}
	
	/**
	 * calcul the beast scalar for resize the component
	 */  
	private double calculateScale()
	{
		double w =  width() * 0.15 / 345;
		double h =  height() * 0.30 / 350;
		if(w>h)w=h;
		if(w<.1)w=.1;
		return w;
	}
	
	/**
	 * resize all the component
	 */  
	private void formComponentResized(java.awt.event.ComponentEvent evt) {
		
	    scale = calculateScale();
	    
	    if (scale - scale_old > 0.001 || scale- scale_old < -0.001)
	    {
	    	scale_old = scale;
	    	
			buttonsPanel.update(scale/3);
			compassPanel.update(scale);
			inclinometerPanel.update(scale);
			searchBarPanel.update(scale);
			zoomBarPanel.update(scale);
			helpPanel.update(scale);
			settingsPanel.update(scale);
			
			buttonsPanel.setLocation((int)(width()/2-buttonsPanel.getWidth()+(scale*70)), 5);
			helpPanel.setLocation((int)(width()/2-buttonsPanel.getWidth()+(scale*70)-10*scale), buttonsPanel.getHeight()+(int)(20*scale));
			settingsPanel.setLocation((int)(width()/2-settingsPanel.getWidth()+80*scale), buttonsPanel.getHeight()+(int)(20*scale));
			searchBarPanel.setLocation((int)(width()/2+buttonsPanel.getWidth()-(scale*70)), (int)(buttonsPanel.getHeight()/2-10)+5);
			zoomBarPanel.setLocation(5, (int)(buttonsPanel.getHeight()/2-zoomBarPanel.getHeight()/2)+5);
			
			skymap.setBounds(0, 0, this.getWidth(), this.getHeight());
			skymap.setZoom(zoom);
			
			leftPanel.setBounds((int)(10*scale), (int)(10*scale), 150, this.getHeight());
			
			compassPanel.setLocation((int)(width()-compassPanel.getWidth())-20, 50);
			inclinometerPanel.setLocation((int)(width()-compassPanel.getWidth()+(scale*70)), (100+inclinometerPanel.getHeight()));
			//coordinate.setBounds(this.getWidth()-100, this.getHeight()-70, 100, 20);
			coordinate.setFont(new Font("Calibri", Font.BOLD, (int)(36*scale)));
			coordinate.setBounds((int)(80*scale), (int)(height()-100*(height() * 0.30 / 350)), (int)(width()-160*scale), (int)(35*scale));

	    }
	}
	
	/**
	 * resize an image by a scalar
	 */  
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
    
    /**
	 * resize the image by a arbitrary size
	 */  
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

	/**
	 * return the width of the main window.
	 */
	private double width()
	{
		return this.getWidth();
	}

	/**
	 * return the height of the main window.
	 */
	private double height()
	{
		return this.getHeight();
	}
	
    /**
	 * The Buttons class
	 */  
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
				e.printStackTrace();
			}
    		
    		this.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    MouseClicked(evt);
                }
    		});
    		this.setSize((int)(imgSettings.getWidth()*2), (int)(imgHelp.getHeight()));
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
    	
		/** 
		 * update the scale variable and resize the components
    	 * @param _scale : the scalar
		 */
		public void update(double _scale)
		{
			scale = _scale;
			try {
				imgSettings = resizeImage(ImageIO.read(new File("res/SettingsIcon.png")), scale);
    			imgHelp = resizeImage(ImageIO.read(new File("res/HelpIcon.png")), scale);
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.setSize((int)(imgSettings.getWidth()*2), (int)(imgHelp.getHeight()));
		}
    }
    
    /**
	 * The SettingsConfig class
	 */ 
    private class SettingsConfig extends JLayeredPane
    {
    	double scale;
    	int number;
    	BufferedImage backgroundTop;
    	BufferedImage backgroundMid;
    	BufferedImage backgroundBot;
    	BufferedImage InternalTop;
    	BufferedImage[] InternalMid;

    	JLabel titre;
    	ArrayList<JLabel> settingList = new ArrayList<JLabel>();
    	ArrayList<JComboBox> comboBoxList = new ArrayList<JComboBox>();
    			
    	BufferedImage InternalBot;
    	
    	/**
    	 * Constructor
    	 * @param _scale
    	 */
    	public SettingsConfig(double _scale)
    	{
    		scale = _scale;
    		settingList.add(new JLabel("port"));
    		settingList.add(new JLabel("speed"));
    		settingList.add(new JLabel("databit"));
    		settingList.add(new JLabel("stopbit"));
    		settingList.add(new JLabel("parity"));
    		settingList.add(new JLabel("flowControl"));
    	//	settingList.add(new JLabel("samplingRate"));
    	//	settingList.add(new JLabel("databaseName"));
    	//	settingList.add(new JLabel("ImputDelimiter"));
    		settingList.add(new JLabel("simulation"));
    		number = settingList.size();
    		InternalMid = new BufferedImage[number];
    		
    		String port[] = jssc.SerialPortList.getPortNames();
    		comboBoxList.add(new JComboBox<String>(port));
    		comboBoxList.get(comboBoxList.size()-1).setSelectedItem(settings.getPort());
    		
    		String speed[] = {"110", "300", "600", "1200", "4800", "9600", "14400",
    						"19200", "38400", "57600", "115200", "128000", "256000"};
    		comboBoxList.add(new JComboBox<String>(speed));
    		comboBoxList.get(comboBoxList.size()-1).setSelectedItem(String.valueOf(settings.getSpeed()));
    		
    		String databit[] = {"5", "6", "7", "8"};
    		comboBoxList.add(new JComboBox<String>(databit));
    		comboBoxList.get(comboBoxList.size()-1).setSelectedItem(String.valueOf(settings.getDatabit()));
    		
    		String stopbit[] = {"1", "2", "1_5"};
    		comboBoxList.add(new JComboBox<String>(stopbit));
    		comboBoxList.get(comboBoxList.size()-1).setSelectedItem(String.valueOf(settings.getStopbit()));
    		
    		String parity[] = {"NONE", "ODD", "EVEN", "MARK", "SPACE"};
    		comboBoxList.add(new JComboBox<String>(parity));
    		comboBoxList.get(comboBoxList.size()-1).setSelectedItem(settings.getParity());
    		
    		String flowControl[] = {"NONE", "RTSCTS_IN", "RTSCTS_OUT", "XONXOFF_IN","XONXOFF_OUT"};
    		comboBoxList.add(new JComboBox<String>(flowControl));
    		comboBoxList.get(comboBoxList.size()-1).setSelectedItem(settings.getFlowControl());
    		
    		/*
    		String samplingRate[] = {"25"};
    		comboBoxList.add(new JComboBox<String>(samplingRate));
    		comboBoxList.get(comboBoxList.size()-1).setSelectedItem(String.valueOf(settings.getSamplingRate()));
    		
    		String databaseName[] = {"hyz.db"};
    		comboBoxList.add(new JComboBox<String>(databaseName));
    		comboBoxList.get(comboBoxList.size()-1).setSelectedItem(settings.getDatabaseName());
    		
    		String imputDelimiter[] = {";", ":"};
    		comboBoxList.add(new JComboBox<String>(imputDelimiter));
    		comboBoxList.get(comboBoxList.size()-1).setSelectedItem(settings.getInputDelimiter());
    		*/
    		
    		String simulation[] = {"ON", "OFF"};
    		comboBoxList.add(new JComboBox<String>(simulation));
    		comboBoxList.get(comboBoxList.size()-1).setSelectedItem((settings.getSimulation())?"ON":"OFF");
    		
    		for(int i = 0; i < number; i++)  		
    		{
    			comboBoxList.get(i).addActionListener(new java.awt.event.ActionListener() {
    	            public void actionPerformed(java.awt.event.ActionEvent evt) {
    	                jComboBox1ActionPerformed(evt);
    	            }
    	        });
    		}
    		
    		try {
    			backgroundTop = resizeImage(ImageIO.read(new File("res/settings-top-background.png")), scale/2);
    			backgroundMid = resizeImage(ImageIO.read(new File("res/settings-mid-background.png")), scale/2);
    			backgroundBot = resizeImage(ImageIO.read(new File("res/settings-bot-background.png")), scale/2);
    			InternalTop = resizeImage(ImageIO.read(new File("res/settings-top-internal.png")), scale/2);
    			for(int i = 0; i< number; i++)
    			{
    				InternalMid[i] = resizeImage(ImageIO.read(new File("res/settings-mid-internal.png")), scale/2);
    				
    				settingList.get(i).setBounds((int)(backgroundTop.getWidth()/2-InternalTop.getWidth()/2), backgroundTop.getHeight()+InternalTop.getHeight()+i*InternalMid[0].getHeight()+25, (int)(500*scale), 30);
                	settingList.get(i).setFont(new Font("Calibri", Font.BOLD, (int)(36*scale)));
        			settingList.get(i).setForeground(Color.BLACK);
                	this.add(settingList.get(i));
            		comboBoxList.get(i).setBounds((int)(backgroundTop.getWidth()/2-100*scale), backgroundTop.getHeight()+InternalTop.getHeight()+i*InternalMid[0].getHeight()+25, (int)(100*scale), 30);
            		this.add(comboBoxList.get(i));
                	
                }
    			InternalBot = resizeImage(ImageIO.read(new File("res/settings-bot-internal.png")), scale/2);
			} catch (IOException e) {
				e.printStackTrace();
			}
    		

    		titre = new JLabel("Settings", JLabel.CENTER);
			titre.setFont(new Font("Calibri", Font.BOLD, (int)(36*scale)));
			titre.setBounds(0, backgroundTop.getHeight(), (int)(scale*345), (int)(scale*34));
			titre.setForeground(Color.WHITE);
			this.add(titre);
    		
    		this.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    MouseClicked(evt);
                }
    		});
    		this.setBounds(0, 0, (int)(backgroundTop.getWidth()*2), (int)(500*scale));
    		this.setVisible(false);
    	}
    	@Override 
        protected void paintComponent(Graphics g)
        { 
            super.paintComponent(g); 
            Graphics2D g2 = (Graphics2D) g; 
            g2.drawImage(backgroundTop, 0, 0, null); 
            g2.drawImage(backgroundMid, 0, backgroundTop.getHeight(), null);
            g2.drawImage(backgroundBot, 0, backgroundTop.getHeight()+backgroundMid.getHeight(), null);
            g2.drawImage(InternalTop, (int)(backgroundTop.getWidth()/2-InternalTop.getWidth()/2),  backgroundTop.getHeight()+titre.getHeight(), null);
            for(int i = 0; i < number; i++)
            {
            	g2.drawImage(InternalMid[i], (int)(backgroundTop.getWidth()/2-InternalTop.getWidth()/2), backgroundTop.getHeight()+InternalTop.getHeight()+i*InternalMid[0].getHeight()+titre.getHeight(), null);
            	
            	
            }
            	g2.drawImage(InternalBot, (int)(backgroundTop.getWidth()/2-InternalTop.getWidth()/2), backgroundTop.getHeight()+InternalTop.getHeight()+number*InternalMid[0].getHeight()+titre.getHeight(), null);
		}
    	
    	/**
    	 * this method was here only for avoid the user to click on the skymap behind the widows.
    	 * @param evt
    	 */
    	private void MouseClicked(java.awt.event.MouseEvent evt) {
    		//nothing
    	}
    		
    	private void	jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {

    		int i = 0;
    		settings.setPort((comboBoxList.get(i).getSelectedItem() != null)?comboBoxList.get(i).getSelectedItem().toString():"NONE");
    		i++;
    		settings.setSpeed(Integer.parseInt(comboBoxList.get(i).getSelectedItem().toString()));
    		i++;
    		settings.setDatabit(Integer.parseInt(comboBoxList.get(i).getSelectedItem().toString()));
    		i++;
    		settings.setStopbit(Integer.parseInt(comboBoxList.get(i).getSelectedItem().toString()));
    		i++;
    		settings.setParity(comboBoxList.get(i).getSelectedItem().toString());
    		i++;
    		settings.setFlowControl(comboBoxList.get(i).getSelectedItem().toString());
    		/*
    		i++;
    		settings.setSamplingRate(Integer.parseInt(comboBoxList.get(i).getSelectedItem().toString()));
    		i++;
    		settings.setDatabaseName(comboBoxList.get(i).getSelectedItem().toString());
    		i++;
    		settings.setInputDelimiter(comboBoxList.get(i).getSelectedItem().toString());
    		i++;
    		*/
    		settings.setSimulation((comboBoxList.get(i).getSelectedItem().toString().equals("ON"))?true:false);
    		Serializer.serialize("settings.conf",settings);
    		
    	}
    	
		/** 
		 * update the scale variable and resize the components
    	 * @param _scale : the scalar
		 */
		public void update(double _scale)
		{
			scale = _scale;

			try {
				backgroundTop = resizeImage(ImageIO.read(new File("res/settings-top-background.png")), scale/2);

				titre.setFont(new Font("Calibri", Font.BOLD,  (int)(scale*36)));
				titre.setBounds(0, backgroundTop.getHeight(), backgroundTop.getWidth(), (int)(scale*35));
				
				backgroundBot = resizeImage(ImageIO.read(new File("res/settings-bot-background.png")), scale/2);
				InternalTop = resizeImage(ImageIO.read(new File("res/settings-top-internal.png")), scale/2);
				for(int i = 0; i < number; i++)
				{
					InternalMid[i] = resizeImage(ImageIO.read(new File("res/settings-mid-internal.png")), scale/2);
				  	settingList.get(i).setBounds((int)(backgroundTop.getWidth()/2-InternalTop.getWidth()/2+30*scale), backgroundTop.getHeight()+InternalTop.getHeight()+i*InternalMid[0].getHeight()+titre.getHeight()+(int)(10*scale), (int)(500*scale), (int)(30*scale));
				  	settingList.get(i).setFont(new Font("Calibri", Font.BOLD, (int)(36*scale)));
				  	comboBoxList.get(i).setBounds((int)(backgroundTop.getWidth()-300*scale), backgroundTop.getHeight()+InternalTop.getHeight()+i*InternalMid[0].getHeight()+titre.getHeight()+(int)(4*scale), (int)(250*scale), (int)(40*scale));
				  	comboBoxList.get(i).setFont(new Font("Calibri", Font.BOLD, (int)(25*scale)));
				}
				backgroundMid = resizeImage2(ImageIO.read(new File("res/settings-mid-background.png")), backgroundTop.getWidth(), (number+2)*InternalMid[0].getHeight()+titre.getHeight()+ (int)(15*scale));
				InternalBot = resizeImage(ImageIO.read(new File("res/settings-bot-internal.png")), scale/2);
    		} catch (IOException e) {
				e.printStackTrace();
			}

			this.setBounds(0, 0, (int)(backgroundTop.getWidth()), backgroundTop.getHeight()+backgroundMid.getHeight()+backgroundBot.getHeight());
		}
    }
   
    /**
	 * The Help class
	 */ 
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
    	/**
    	 * Constructor
    	 * @param _scale
    	 */
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
    			
    			this.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        MouseClicked(evt);
                    }
        		});
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

    	/**
    	 * this method was here only for avoid the user to click on the skymap behind the widows.
    	 * @param evt
    	 */
    	private void MouseClicked(java.awt.event.MouseEvent evt) {
    		//nothing
    	}
    	
		/** 
		 * update the scale variable and resize the components
    	 * @param _scale : the scalar
		 */
		public void update(double _scale)
		{
			scale = _scale;
			
			try {
    			backgroundTop = resizeImage(ImageIO.read(new File("res/haut-fond.png")), scale/2);
    			backgroundBot = resizeImage(ImageIO.read(new File("res/bas-fond.png")), scale/2);
    			internalTop = resizeImage(ImageIO.read(new File("res/haut-interieur.png")), scale/2);
    			
    			titre.setFont(new Font("Calibri", Font.BOLD,  (int)(scale*36)));
    			titre.setBounds(0, backgroundTop.getHeight(), backgroundTop.getWidth(), (int)(scale*35));
    		
    			text.setFont(new Font("Calibri", Font.BOLD,  (int)(scale*24)));
    			text.setBounds((int)(40*scale), backgroundTop.getHeight()+internalTop.getHeight(), (int)(internalTop.getWidth()), (int)(scale*26*9));
    			
    			
    			internalMid = resizeImage2(ImageIO.read(new File("res/mid-interne.png")), text.getWidth(), text.getHeight()-52*scale);
    			backgroundMid = resizeImage2(ImageIO.read(new File("res/mid-fond.png")), backgroundTop.getWidth(), text.getHeight()+titre.getHeight()*2.7);
    			internalBot = resizeImage(ImageIO.read(new File("res/bas-interieur.png")), scale/2);
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.setBounds(0, 0, (int)(backgroundTop.getWidth()), backgroundTop.getHeight()+backgroundMid.getHeight()+backgroundBot.getHeight());
		}
    }
   
    /**
	 * The ZoomBar class
	 */ 
    private class ZoomBar extends JLayeredPane
    {
    	double scale;
    	int hig;
    	JSlider zoomSlider;
    	
    	/**
    	 * Constructor
    	 * @param _scale
    	 */
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
    		zoomSlider.setOpaque(false);
    		this.add(zoomSlider);
			//this.setVisible(true);
			zoomSlider.addChangeListener(new javax.swing.event.ChangeListener() {
	            public void stateChanged(javax.swing.event.ChangeEvent evt) {
	                jSlider1StateChanged(evt);
	            }
	        });
			zoomSlider.setFocusable(false);
    	}
    	
    	/**
    	 * Called then the value of the slider change
    	 * This method update the skymap
    	 * @param evt
    	 */
    	private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {
    		zoom = zoomSlider.getValue();
    		skymap.setZoom(zoom);
    		skymap.updateSkyMap();
    	}
    			
		/** 
		 * update the scale variable and resize the components
    	 * @param _scale : the scalar
		 */
		public void update(double _scale)
		{
			scale = _scale;
			hig = 30;
			
			this.setBounds(0, 0, (int)(width()/2-buttonsPanel.getWidth()/2-70*scale), hig);
			zoomSlider.setBounds(0, 0, (int)(width()/2-buttonsPanel.getWidth()/2-70*scale), hig);
		}
    }
    
    /**
	 * The SearchBar class
	 */ 
    private class SearchBar extends JLayeredPane
    {
    	double scale;
    	int hig;
    	String l_sSavedSearch = null;
    	JTextField searchBarTextField;
    	StopButton stopSearchButon;
    	JList<String> listNameOrID;
    	ListModel<String> listModelNameOrID;
    	ArrayList<CelestialObject> listModelObjects;
    	String[] keys = {"!id ", "!ProperName ", "!RA ", "!Dec ", "!Distance ", "!Mag ", "!ColorIndex "};
    	JScrollPane jScrollPane = new JScrollPane();
    	ArrayList<CelestialObject> listCelestialObject = new ArrayList<CelestialObject>();

    	/**
    	 * Constructor
    	 * @param _scale
    	 */
    	public SearchBar(double _scale)
    	{     		
    		scale = _scale;
    		hig = (int)(300*scale);
    		this.setBounds(0, 0, (int)(width()/2), hig);
    		searchBarTextField = new JTextField();
    		searchBarTextField.setBounds(0, 0, (int)(width()/2), hig);
    		searchBarTextField.addKeyListener(new java.awt.event.KeyAdapter() {
    			public void keyReleased(java.awt.event.KeyEvent evt) {
                    try
					{
						searchBarKeyReleased(evt);
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
					}
                }
            });
    		
    		searchBarTextField.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                	if(l_sSavedSearch != null)
        			{
        				searchBarTextField.setText(l_sSavedSearch);
        				l_sSavedSearch = null;
        			}		
                }
    		});
    		
    		
    		listModelNameOrID = new ListModel<String>();
    		listModelObjects = new ArrayList<CelestialObject>();
    		listNameOrID = new JList<String>();
    		listNameOrID.setModel(listModelNameOrID);
    		listNameOrID.setBounds(0, 0, 300, 400);
    		jScrollPane.setFocusable(false);
    		listNameOrID.setFocusable(false);
    		jScrollPane.setViewportView(listNameOrID);

    		stopSearchButon = new StopButton(scale);
    		stopSearchButon.setBounds(0,searchBarTextField.getWidth(), 300, 400);
    		stopSearchButon.setFocusable(false);
    		
    		
    		
    		
    		listNameOrID.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                	listNameOrIDMouseClicked(evt);
                }
    		});
    		
    		this.add(searchBarTextField, new Integer(1));
    		this.add(jScrollPane, new Integer(2));
    		this.add(stopSearchButon, new Integer(3));
    	}
    	
    	
    	/**
    	 * used then the user select an element of the list.
    	 * @param evt
    	 */
    	private void listNameOrIDMouseClicked(java.awt.event.MouseEvent evt) {

    		
        	jScrollPane.setVisible(false);
        	String[] searchBarText = searchBarTextField.getText().split("[; ]");
        	String[] searchFeatures = searchBarTextField.getText().split(";");
    		String searchFeature = searchFeatures[searchFeatures.length - 1];
    				
			//log.info("\n>" + searchFeature + "<->" + searchFeature.split(" ").length + "\n");
        	if(searchFeature.split(" ").length > 1)
        	{
             	int index = listNameOrID.getSelectedIndex();
        		CelestialObject celObjt = listModelObjects.get(index);
        		updateInfo(celObjt);
        		skymap.setCelestialObjectPointed(celObjt);
        		
        		double l_dDegreeCompassObjectSearched = -celObjt.getAzimuth() * 180 / Math.PI;
        		double l_dAngleInclinometerObjectSearched = celObjt.getHeight() * 180 / Math.PI;
        		compassPanel.setRedNeedle(l_dDegreeCompassObjectSearched);
        		inclinometerPanel.setRedNeedle(l_dAngleInclinometerObjectSearched);
    			
        		pic.setMode(PicMode.GUIDING);
        		
        		l_sSavedSearch = searchBarTextField.getText();
        		searchBarTextField.setText(listNameOrID.getSelectedValue().toString());
        		skymap.transferFocusBackward();
        		MainView.this.update();
        		return;
        	}
    		
        	String regex = searchBarText[searchBarText.length - 1] + "$";
        	searchBarTextField.setText(searchBarTextField.getText().replaceFirst(regex, listNameOrID.getSelectedValue().toString()));
    		
    	}
    	
    	/**
    	 * search in the database the stars corresponding with the textField.
    	 * @param evt
    	 */
    	private void searchBarKeyReleased(java.awt.event.KeyEvent evt) 
    	{    		
    		//listCelestialObject.clear();
    		if (listModelNameOrID.getSize()>0)
    			listModelNameOrID.removeAll();
    		listModelObjects.clear();
    		
    		boolean canQueryDB = true;
    		String[] searchFeatures = searchBarTextField.getText().split(";");
    		for (String searchFeature : searchFeatures)
			{
	    		for (String key : keys)
				{
					if(key.toLowerCase().startsWith(searchFeature.toLowerCase()))
	     			{
						listModelNameOrID.setElement(key);
	     			}
		     	}
	    		if(searchFeature.split(" ").length <= 1)
	    			canQueryDB = false;
			}
	     	if(canQueryDB)
	     	{
	     		try{
	     			if(pic == null)
	     				listCelestialObject = db.starsForText(searchBarTextField.getText(), Calendar.getInstance(), 47.039448, 6.799734);
	     			else
	     				listCelestialObject = db.starsForText(searchBarTextField.getText(), Calendar.getInstance(), pic.getLatitude(), pic.getLongitude());
	     			
		     		if(listCelestialObject.size() != 0)
		     		{	
		     			for (CelestialObject celestialObject : listCelestialObject)
		     			{
		     				if(celestialObject.getProperName() != null)
		     					listModelNameOrID.setElement(celestialObject.getProperName());
		     				else
		     					listModelNameOrID.setElement(String.valueOf(celestialObject.getId()));
		     				listModelObjects.add(celestialObject);
		     			}
		     		}
		     		else
		     			listModelNameOrID.setElement("Aucun résultat n'a été trouvé dans la base de données");

	     			} catch(Exception ex)
	     			{
	     				ex.printStackTrace();
	     			}
     		}
	     	
	        if (listModelNameOrID.getSize() > 0)
	        {
	        	int min = (listModelNameOrID.getSize() < 5)?listModelNameOrID.getSize()*21:(int)(200*scale);
	        	jScrollPane.setBounds(0, 20, searchBarTextField.getWidth(), min);
	           	jScrollPane.setVisible(true);
	           	
	        } 
	        else
	        	jScrollPane.setVisible(false);
	        
    	}
    	
        
    	/** 
		 * update the scale variable and resize the components
		 * @param _scale : the scalar
		 */
		public void update(double _scale)
		{
			scale = _scale;
			hig = 20;
    		
			searchBarTextField.setBounds(0, 0, (int)(width()/2-buttonsPanel.getWidth()/2-70*scale-compassPanel.getWidth()), hig);
    		int min = (listModelNameOrID.getSize() < 5)?listModelNameOrID.getSize()*21:(int)(200*scale);
        	jScrollPane.setBounds(0, hig, searchBarTextField.getWidth(), min);
        	stopSearchButon.setBounds(searchBarTextField.getWidth()-hig,searchBarTextField.getHeight()/2-hig/2+(2), hig, hig);
        	this.setBounds(0, 0, (int)(width()/2-buttonsPanel.getWidth()/2-70*scale-compassPanel.getWidth()+stopSearchButon.getWidth()), hig+(int)(400*scale));
			    		
		}	
		
		private class StopButton extends JPanel
		{
	    	BufferedImage cross;
			public StopButton(double _scale){
				try
				{
					cross = resizeImage2(ImageIO.read(new File("res/Cross.png")),hig/2, hig/2);
				}
				catch (IOException ex)
				{
					ex.printStackTrace();
				}
				this.addMouseListener(new java.awt.event.MouseAdapter() {
		            public void mouseClicked(java.awt.event.MouseEvent evt) {
		            	stopSearchActionPerformed(evt);
		            }
		        });
				this.setOpaque(false);
			}
			
			private void stopSearchActionPerformed(java.awt.event.MouseEvent evt) {
	    		skymap.setCelestialObjectPointed(null);
	    		repaint();
	    	}
			
			@Override 
	        protected void paintComponent(Graphics g)
	        { 
	            super.paintComponent(g); 
	            Graphics2D g2 = (Graphics2D) g; 
	            g2.drawImage(cross, 0, 0, null); 
	        }
			
		}
    }

    /**
	 * The Compass class
	 */ 
	private class Compass extends JLayeredPane
	{
		double scale = 1;
		double redAngle = 0;
		double greenAngle = 0;
		BufferedImage background;
		JLabel coordinateCompass;
		Needle redNeedle;
		Needle greenNeedle;
		
		/**
		 * Constructor
		 * @param _scale : the scalar for resize the components.
		 */
		public Compass(double _scale)
		{
			scale = _scale;
			try {
				background = resizeImage(ImageIO.read(new File("res/backgroundCompass.png")),scale);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
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
			coordinateCompass = new JLabel("-10:2'13'' N", JLabel.CENTER);
			coordinateCompass.setFont(new Font("Calibri", Font.BOLD, 36));
			coordinateCompass.setBounds(0, (int)(scale*310), (int)(scale*345), (int)(scale*34));
			coordinateCompass.setForeground(Color.WHITE);
			this.add(coordinateCompass, new Integer(3));
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
		
		/** 
		 * update the scale variable and resize the components
		 * @param _scale : the scalar
		 */
		public void update (double _scale)
		{
			scale = _scale;
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
				coordinateCompass.setText(String.valueOf(greenAngle%360));
			}
			catch(Exception e)
			{
				log.warning(e.toString());
			}
			coordinateCompass.setFont(new Font("Calibri", Font.BOLD,  (int)(scale*36)));
			coordinateCompass.setBounds(0, (int)(scale*310), (int)(scale*345), (int)(scale*35));
			this.setSize((int)(scale*345), (int)(scale*350));
		}

		/** 
		 *  Used for update the red needle angle.
		 *  @param _redAngle : It's the new angle for the red needle
		 */
		public void setRedNeedle (double _angle) 
		{
            redAngle = _angle;
            this.update(scale);
		}
		
		/** 
		 *  Used for update the green needle angle.
		 *  @param _redAngle : It's the new angle for the green needle
		 */
		public void setGreenNeedle (double _angle) 
		{
            greenAngle =_angle;
            this.update(scale);
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

	/**
	 * The Inclinometer class
	 */ 
	private class Inclinometer extends JLayeredPane
	{
		double scale = 1;
		double redAngle = 0;
		double greenAngle = 0;
		BufferedImage background;
		JLabel coordinateInclinometer;
		Needle redNeedle;
		Needle greenNeedle;
		
		/** 
		 * Inclinometer
		 * Constructor
		 * @param _scale : It's the scale for resize the components
		 */
		public Inclinometer(double _scale)
		{
			scale = _scale;
			try {
				background = resizeImage(ImageIO.read(new File("res/backgroundInclinometer.png")),scale);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
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

			coordinateInclinometer = new JLabel("-10:2'13'' N", JLabel.CENTER);
			coordinateInclinometer.setFont(new Font("Calibri", Font.BOLD,  (int)(scale*36)));
			coordinateInclinometer.setBounds(0, (int)(scale*258), (int)(scale*186), (int)(scale*35));
			coordinateInclinometer.setForeground(Color.WHITE);

			this.setSize((int)(scale*186), (int)(scale*324));;
			this.add(coordinateInclinometer, new Integer(3));
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
		
		/** 
		 *  update the scale variable and resize the components
		 * @param _scale : the scalar
		 */
		public void update(double _scale)
		{
			scale = _scale;
			try {
				background = resizeImage(ImageIO.read(new File("res/backgroundInclinometer.png")),scale);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			redNeedle.scale(scale);
			redNeedle.rotate(redAngle);
			redNeedle.setBounds(0, 0, (int)(scale*186), (int)(scale*258));
			
            greenNeedle.scale(scale);
            greenNeedle.rotate(greenAngle);
			greenNeedle.setBounds(0, 0, (int)(scale*186), (int)(scale*258));
			try
			{
				coordinateInclinometer.setText(String.valueOf(greenAngle%360));
			}
			catch(Exception e)
			{
				log.warning(e.toString());
			}
			this.setSize((int)(scale*186), (int)(scale*324));
			coordinateInclinometer.setFont(new Font("Calibri", Font.BOLD,  (int)(scale*36)));
			coordinateInclinometer.setBounds(0, (int)(scale*258), (int)(scale*186), (int)(scale*35));
		}
        
		/** 
		 *  Used for update the red needle angle.
		 *  @param _redAngle : It's the new angle for the red needle
		 */
		public void setRedNeedle (double _redAngle) 
		{
			redAngle = _redAngle;
            this.update(scale);		
		}
		
		/** 
		 *  Used for update the green needle angle.
		 *  @param _redAngle : It's the new angle for the green needle
		 */
		public void setGreenNeedle (double _greenAngle) 
		{
			greenAngle = _greenAngle;
            this.update(scale);
		}

		/** 
		 *  private class Needle
		 */
		private class Needle extends JPanel
		{
			BufferedImage needleImage;
			double angle = 0;
			double scale = 1;
			String adresseImage;
			    
			/** 
			 *  private class Needle
			 *  @param _adressImage : the path of the image
			 *  @param _scale : the scale for resize the image
			 */
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
				
			/**
			 * used of resize the components
			 * @param _scale : the scalar with the components are resized
			 */
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
			
			/**
			 * used for rotate the needles
			 * @param _angle : the new angle of the needle.
			 */
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
				g2.rotate(-angle, scale*5, needleImage.getHeight() / 2);
				g2.drawImage(needleImage, 0, 0, null); 
			}
		}
	}
}



