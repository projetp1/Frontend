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
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;

import com.github.projetp1.Pic.PicMode;
import com.github.projetp1.rs232.RS232.PicArrowDirection;


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

	private static final double kMagnitudeMin = -1.44;
	private static final double kMagnitudeMax = 6.5;
	private static final int kZoomMin = 1;
	private static final int kZoomMax = 40;
	
	private SkyMap skymap = null;
	public Compass compassPanel;
	public Inclinometer inclinometerPanel;
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
	private double redArrowAzimuth;
	private double redArrowPitch;
	
	private PicArrowDirection lastArrowSent = null;
		
	/**
	 * Constructor
	 */
	public MainView()
	{

		this.setTitle(Messages.getString("MainView.Title"));
		
		ArrayList<Image> icons = new ArrayList<>();
		icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/logo16.png")));
		icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/logo32.png")));
		icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/logo48.png")));
		icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/logo128.png")));
		icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/logo256.png")));
		icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/logo512.png")));
		this.setIconImages(icons);
		icons = null;

		try
		{
			db = new DataBase("hyg.db",";");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		  
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

		settings = new Settings(null);
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

		skymap = new SkyMap(this);

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
		
		skymap.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
		      public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {

            	mouseWheel(evt);
            }
        });

		Color l_BackgroundColor = new Color(5, 30, 50);
		this.getContentPane().setBackground(l_BackgroundColor);
		this.setBackground(l_BackgroundColor);
		this.addKeyListener(this);
        this.setMinimumSize(new java.awt.Dimension(800, 600));
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setFocusable(true);
		this.update();
		pic = new Pic(this);
		pic.addObservateur(new Observateur(){
			public void updatePIC() {
				update();
			}
		});
	}


	/**
	 * This method updates the values form the PIC and sets the needles accordingly.
	 */
	private void update() {
		if(pic != null)
		{
			compassPanel.setGreenNeedle(pic.getAzimuth());
			inclinometerPanel.setGreenNeedle(pic.getPitch());
		}

		char hemNS = 'N', hemWE = 'E';
		double lat = skymap.getdLatitude(), lon = skymap.getdLongitude();

		if (lat < 0.0)
			hemNS = 'S';
		if (lon < 0.0)
			hemWE = 'W';
		
		coordinate.setText(decimalToDMS(Math.abs(lat)) + " " + hemNS + ", " + decimalToDMS(Math.abs(lon)) + " " + hemWE);

		skymap.updateSkyMap();	
		PicArrowDirection dir = null;
		if(pic != null && pic.getMode() == PicMode.GUIDING)
		{
			double greenAzimuth = pic.getAzimuth();
			double greenPitch = pic.getPitch();
			double resAzimuth = 0;
			double resPitch = greenPitch - redArrowPitch;
			
			if(greenAzimuth < 0)
				greenAzimuth += 360;
			if(redArrowAzimuth < 0)
				 resAzimuth = greenAzimuth - (redArrowAzimuth+360);
			else
				resAzimuth = greenAzimuth - redArrowAzimuth;
			
			if(resAzimuth < -10)
			{
				if(resPitch < -10)
				{
					dir = PicArrowDirection.NORTHEAST;
				}
				else if(resPitch > 10)
				{
					dir =PicArrowDirection.SOUTHEAST;
				}
				else
				{
					dir =PicArrowDirection.EAST;
				}
				
			}
			else if(resAzimuth > 10)
			{
				if(resPitch < -10)
				{
					dir =PicArrowDirection.NORTHWEST;
				}
				else if(resPitch > 10)
				{
					dir =PicArrowDirection.SOUTHWEST;
				}
				else
				{
					dir =PicArrowDirection.WEST;
				}
			}
			else
			{
				if(resPitch < -10)
				{
					dir =PicArrowDirection.NORTH;
				}
				else if(resPitch > 10)
				{
					dir =PicArrowDirection.SOUTH;
				}
				else
				{
					dir =PicArrowDirection.ONTARGET;
				}
			}
			double roll = pic.getRoll();
			if(roll < 0)
				roll += 360;
			roll /= 45;
			int num = (int) Math.round(roll);
			for(int i = 0; i < num; i++)
			{
				dir = addOneToDir(dir);
			}
			
			if(lastArrowSent == null)
				lastArrowSent = dir;
			else if(dir != lastArrowSent)
			{
				pic.setPicArrow(dir);
				lastArrowSent = dir;
			}
		}
				
	}
	
	private PicArrowDirection addOneToDir(PicArrowDirection dir)
	{
		switch(dir)
		{
			case NORTH:
				return PicArrowDirection.NORTHWEST;
			case NORTHEAST:
				return PicArrowDirection.NORTH;
			case EAST:
				return PicArrowDirection.NORTHEAST;
			case SOUTHEAST:
				return PicArrowDirection.EAST;
			case SOUTH:
				return PicArrowDirection.SOUTHEAST;
			case SOUTHWEST:
				return PicArrowDirection.SOUTH;
			case WEST:
				return PicArrowDirection.SOUTHWEST;
			case NORTHWEST:
				return PicArrowDirection.WEST;
			default:
				return PicArrowDirection.ONTARGET;
		}
	}
	
	/**
	 * Convert decimal coordinates to DMS
	 * 
	 * From: https://en.wikipedia.org/wiki/Geographic_coordinate_conversion#Java_Implementation
	 *
	 * @param coord The decimal coordinate
	 * @return A string in DMS form
	 */
	public static String decimalToDMS(double coord)
	{
		String output, degrees, minutes, seconds;

		// gets the modulus the coordinate divided by one (MOD1).
		// in other words gets all the numbers after the decimal point.
		// e.g. mod = 87.728056 % 1 == 0.728056
		//
		// next get the integer part of the coord. On other words the whole number part.
		// e.g. intPart = 87

		double mod = coord % 1;
		int intPart = (int) coord;

		// set degrees to the value of intPart
		// e.g. degrees = "87"

		degrees = String.valueOf(intPart);

		// next times the MOD1 of degrees by 60 so we can find the integer part for minutes.
		// get the MOD1 of the new coord to find the numbers after the decimal point.
		// e.g. coord = 0.728056 * 60 == 43.68336
		// mod = 43.68336 % 1 == 0.68336
		//
		// next get the value of the integer part of the coord.

		// e.g. intPart = 43

		coord = mod * 60;
		mod = coord % 1;
		intPart = (int) coord;

		// set minutes to the value of intPart.
		// e.g. minutes = "43"
		minutes = String.valueOf(intPart);

		// do the same again for minutes
		// e.g. coord = 0.68336 * 60 == 41.0016
		// e.g. intPart = 41
		coord = mod * 60;
		intPart = (int) coord;

		// set seconds to the value of intPart.
		// e.g. seconds = "41"
		seconds = String.valueOf(intPart);

		// I used this format for android but you can change it
		// to return in whatever format you like
		// e.g. output = "87/1,43/1,41/1"
		output = degrees + "° " + minutes + "' " + seconds + "''";

		// Standard output of D°M′S″
		// output = degrees + "°" + minutes + "'" + seconds + "\"";

		return output;
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
	 * 
	 */  
	public void mouseWheel(MouseWheelEvent evt)
	{
		if( evt.getWheelRotation() != 0)
		{
		if(zoom>1 || evt.getWheelRotation() < 0) 
			zoom-=evt.getWheelRotation();	

		double rayon = xOrigin*xOrigin + yOrigin*yOrigin;

		if(zoom >= kZoomMax)		
			zoom = kZoomMax;
		else if (evt.getWheelRotation()<0)
		{

			double diffX = evt.getX() - (skymap.getWidth()/2);	
			double diffY = evt.getY() - (skymap.getHeight()/2);		
			double scaleX = diffX / skymap.getWidth() *4/zoom;
			double scaleY = diffY / skymap.getHeight() *4/zoom * -1;
			scaleX /= zoom;
			scaleY /= zoom;
			if (rayon <= 1 || (xOrigin <= 0 && scaleX >= 0))
				xOrigin += scaleX;
			if (rayon <= 1 || (xOrigin >= 0 && scaleX <= 0))
				xOrigin += scaleX;
			if (rayon <= 1  || (yOrigin <= 0 && scaleY >= 0))
				yOrigin += scaleY;
			if (rayon <= 1  || (yOrigin >= 0 && scaleY <= 0))
				yOrigin += scaleY;
		
			}

		inclinometerPanel.setGreenNeedle(90-90*rayon);

		if (xOrigin < 0)
			compassPanel.setGreenNeedle(Math.atan(yOrigin/xOrigin)*180/Math.PI + 90);
		else
			compassPanel.setGreenNeedle(Math.atan(yOrigin/xOrigin)*180/Math.PI + 270);
        skymap.setXOrigin(xOrigin);
        skymap.setYOrigin(yOrigin);
		
        zoomBarPanel.zoomSlider.setValue(zoom);
        skymap.setZoom(zoom);
        skymap.updateSkyMap();
		}
	} 
	
	/**
	 * navigation on the skymap.
	 */  
	public void keyPressed(KeyEvent evt) {
		if(pic == null || pic.getMode() == PicMode.SIMULATION)
		{
			float l_fDelta = (float) (0.05 / zoom);
			double rayon = xOrigin*xOrigin + yOrigin*yOrigin;
	        if(evt.getKeyCode() == 37) //Left
	        {
	        	if(rayon < 1 || xOrigin >= 0)
	        		xOrigin -= l_fDelta;
	        }
	        else if(evt.getKeyCode() == 39) //Right
	        {
	        	if(rayon < 1 || xOrigin <= 0)
	        		xOrigin += l_fDelta;
	        }
	        else if(evt.getKeyCode() == 38) // Up
	        {
	        	if(rayon < 1 || yOrigin <= 0)
	        		yOrigin += l_fDelta;
	        }
	        else if(evt.getKeyCode() == 40) // Down
	        {
	        	if(rayon < 1 || yOrigin >= 0)
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

    		inclinometerPanel.setGreenNeedle(90-90*rayon);

    		if (xOrigin < 0)
    			compassPanel.setGreenNeedle(Math.atan(yOrigin/xOrigin)*180/Math.PI + 90);
    		else
    			compassPanel.setGreenNeedle(Math.atan(yOrigin/xOrigin)*180/Math.PI + 270);

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
			leftPanel.setText("<html>" + ((_object.getProperName() != null)?
					Messages.getString("MainView.StarName"):Messages.getString("MainView.CatalogNumber")) + "<br />" +
				((_object.getProperName() != null)?_object.getProperName():
				((_object.getHIP() != 0)?("HIP: " + _object.getHIP()):
				((_object.getHD() != 0)?("HD: " + _object.getHD()):
				((_object.getHR() != 0)?("HR: " + _object.getHR()):
				("Id: " + _object.getId()))))) +
				"<br /><br />" + Messages.getString("MainView.Magnitude") + "<br />" +
				_object.getMag() +
				"<br /><br />" + Messages.getString("MainView.DistanceToEarth") + "<br />" +
				(int)(_object.getDistance()*3.2616) +
				" " + Messages.getString("MainView.LY") + "<br /><br />" + Messages.getString("MainView.Colour") + 
				"<br />" + _object.getColorIndex() +
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
		if(w>h)
			w=h;
		if(w<.1)
			w=.1;
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

        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        graphics.drawImage(bImage, 0, 0, destWidth, destHeight, 0, 0, bImage.getWidth(), bImage.getHeight(), null);
        graphics.dispose();

        return bImageNew;
    } 
    
    /**
	 * resize the image by a arbitrary size
	 */  
    public static BufferedImage resizeImage2(BufferedImage bImage, int w, int h) {
        int destWidth = w;
        int destHeight = h;
        GraphicsConfiguration configuration = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        BufferedImage bImageNew = configuration.createCompatibleImage(destWidth, destHeight, 2);
        Graphics2D graphics = bImageNew.createGraphics();

        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

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
    			imgSettings = resizeImage(ImageIO.read(getClass().getResource("/SettingsIcon.png")), scale);
    			imgHelp = resizeImage(ImageIO.read(getClass().getResource("/HelpIcon.png")), scale);
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

    	private void MouseClicked(java.awt.event.MouseEvent evt)
    	{
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
				imgSettings = resizeImage(ImageIO.read(getClass().getResource("/SettingsIcon.png")), scale);
    			imgHelp = resizeImage(ImageIO.read(getClass().getResource("/HelpIcon.png")), scale);
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
    	@SuppressWarnings("rawtypes")
		ArrayList<JComboBox> comboBoxList = new ArrayList<JComboBox>();
    	
    	JSlider sliderMagnitude;
    	JLabel sliderValue;
    			
    	BufferedImage InternalBot;
    	
    	/**
    	 * Constructor
    	 * @param _scale
    	 */
    	public SettingsConfig(double _scale)
    	{
    		scale = _scale;
    		settingList.add(new JLabel(Messages.getString("MainView.Port")));
    		settingList.add(new JLabel(Messages.getString("MainView.Speed")));
    		settingList.add(new JLabel(Messages.getString("MainView.Databits")));
    		settingList.add(new JLabel(Messages.getString("MainView.Stopbits")));
    		settingList.add(new JLabel(Messages.getString("MainView.Parity")));
    		settingList.add(new JLabel(Messages.getString("MainView.FlowControl")));
    		settingList.add(new JLabel(Messages.getString("MainView.Magnitude")));
    		settingList.add(new JLabel(Messages.getString("MainView.Constellation")));
    		settingList.add(new JLabel(Messages.getString("MainView.Simulation")));
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
    		if (settings.getStopbit()!=jssc.SerialPort.STOPBITS_1_5)
    			comboBoxList.get(comboBoxList.size()-1).setSelectedItem(String.valueOf(settings.getStopbit()));
    		else
    			comboBoxList.get(comboBoxList.size()-1).setSelectedItem("1_5");
    		
    		String parity[] = { Messages.getString("MainView.None"), Messages.getString("MainView.Odd"), Messages.getString("MainView.Even"), Messages.getString("MainView.Mark"), Messages.getString("MainView.Space") };			
    		comboBoxList.add(new JComboBox<String>(parity));
    		switch (settings.getParity())
    		{
    			case jssc.SerialPort.PARITY_EVEN:
    				comboBoxList.get(comboBoxList.size()-1).setSelectedItem(Messages.getString("MainView.Even"));
    				break;
    			case jssc.SerialPort.PARITY_MARK:
    				comboBoxList.get(comboBoxList.size()-1).setSelectedItem(Messages.getString("MainView.Mark"));
    				break;
    			case jssc.SerialPort.PARITY_NONE:
    				comboBoxList.get(comboBoxList.size()-1).setSelectedItem(Messages.getString("MainView.None"));
    				break;
    			case jssc.SerialPort.PARITY_ODD:
    				comboBoxList.get(comboBoxList.size()-1).setSelectedItem(Messages.getString("MainView.Odd"));
    				break;
    			case jssc.SerialPort.PARITY_SPACE:
    				comboBoxList.get(comboBoxList.size()-1).setSelectedItem(Messages.getString("MainView.Space"));
    				break;
    			default:
    				comboBoxList.get(comboBoxList.size()-1).setSelectedItem(Messages.getString("MainView.None"));
    				break;
    		}
    		
    		String flowControl[] = { Messages.getString("MainView.None"), Messages.getString("MainView.RTSCTS_IN"), Messages.getString("MainView.RTSCTS_OUT"), Messages.getString("MainView.XONXOFF_IN"), Messages.getString("MainView.XONXOFF_OUT") }; 
    		comboBoxList.add(new JComboBox<String>(flowControl));
    		switch (settings.getFlowControl())
    		{
    			case jssc.SerialPort.FLOWCONTROL_NONE:
    				comboBoxList.get(comboBoxList.size()-1).setSelectedItem(Messages.getString("MainView.None"));
    				break;
    			case jssc.SerialPort.FLOWCONTROL_RTSCTS_IN:
    				comboBoxList.get(comboBoxList.size()-1).setSelectedItem(Messages.getString("MainView.RTSCTS_IN"));
    				break;
    			case jssc.SerialPort.FLOWCONTROL_RTSCTS_OUT:
    				comboBoxList.get(comboBoxList.size()-1).setSelectedItem(Messages.getString("MainView.RTSCTS_OUT"));
    				break;
    			case jssc.SerialPort.FLOWCONTROL_XONXOFF_IN:
    				comboBoxList.get(comboBoxList.size()-1).setSelectedItem(Messages.getString("MainView.XONXOFF_IN"));
    				break;		
    			case jssc.SerialPort.FLOWCONTROL_XONXOFF_OUT:	
    				comboBoxList.get(comboBoxList.size()-1).setSelectedItem(Messages.getString("MainView.XONXOFF_OUT"));	
    				break;	
    			default:
    				comboBoxList.get(comboBoxList.size()-1).setSelectedItem(Messages.getString("MainView.None"));	
    				break;		
    		}
    		sliderMagnitude = new JSlider();
    		sliderMagnitude.setMinimum((int)(kMagnitudeMin*100));
    		sliderMagnitude.setMaximum((int)(kMagnitudeMax*100));
    		sliderMagnitude.setValue((int)(settings.getMagnitude()*100));
    		sliderMagnitude.setOpaque(false);
    		
    		sliderValue = new JLabel();
    		sliderValue.setText(String.valueOf(settings.getMagnitude()));
    		
    		String constellation[]  = { Messages.getString("MainView.On"), Messages.getString("MainView.Off") };
    		comboBoxList.add(new JComboBox<String>(constellation));
    		comboBoxList.get(comboBoxList.size()-1).setSelectedItem((settings.getConstellation()) ? Messages.getString("MainView.On") : Messages.getString("MainView.Off"));

    		String simulation[] = { Messages.getString("MainView.On"), Messages.getString("MainView.Off") };
    		comboBoxList.add(new JComboBox<String>(simulation));
    		comboBoxList.get(comboBoxList.size()-1).setSelectedItem((settings.getSimulation()) ? Messages.getString("MainView.On") : Messages.getString("MainView.Off"));
    		
    		for(int i = 0; i < number-1; i++)  		
    		{
    			comboBoxList.get(i).addActionListener(new java.awt.event.ActionListener() {
    	            public void actionPerformed(java.awt.event.ActionEvent evt) {
    	                jComboBox1ActionPerformed(evt);
    	            }
    	        });
    		}
    		sliderMagnitude.addChangeListener(new javax.swing.event.ChangeListener() {
	            public void stateChanged(javax.swing.event.ChangeEvent evt) {
	            	jComboBox1ActionPerformed(null);
	            }
	        });
    		try {
    			backgroundTop = resizeImage(ImageIO.read(getClass().getResource("/settings-top-background.png")), scale/2);
    			backgroundMid = resizeImage(ImageIO.read(getClass().getResource("/settings-mid-background.png")), scale/2);
    			backgroundBot = resizeImage(ImageIO.read(getClass().getResource("/settings-bot-background.png")), scale/2);
    			InternalTop = resizeImage(ImageIO.read(getClass().getResource("/settings-top-internal.png")), scale/2);
    			for(int i = 0; i< number-3; i++)
    			{
    				InternalMid[i] = resizeImage(ImageIO.read(getClass().getResource("/settings-mid-internal.png")), scale/2);
    				
    				settingList.get(i).setBounds((int)(backgroundTop.getWidth()/2-InternalTop.getWidth()/2), backgroundTop.getHeight()+InternalTop.getHeight()+i*InternalMid[0].getHeight()+25, (int)(500*scale), 30);
                	settingList.get(i).setFont(new Font("Calibri", Font.BOLD, (int)(36*scale)));
        			settingList.get(i).setForeground(Color.BLACK);
                	this.add(settingList.get(i));
            		comboBoxList.get(i).setBounds((int)(backgroundTop.getWidth()/2-100*scale), backgroundTop.getHeight()+InternalTop.getHeight()+i*InternalMid[0].getHeight()+25, (int)(100*scale), 30);
            		this.add(comboBoxList.get(i));
                }
    			settingList.get(number-3).setBounds((int)(backgroundTop.getWidth()/2-InternalTop.getWidth()/2), backgroundTop.getHeight()+InternalTop.getHeight()+(number-3)*InternalMid[0].getHeight()+25, (int)(500*scale), 30);
            	settingList.get(number-3).setFont(new Font("Calibri", Font.BOLD, (int)(36*scale)));
    			settingList.get(number-3).setForeground(Color.BLACK);
            	this.add(settingList.get(number-3));
        		sliderMagnitude.setBounds((int)(backgroundTop.getWidth()/2-100*scale), backgroundTop.getHeight()+InternalTop.getHeight()+(number-3)*InternalMid[0].getHeight()+25, (int)(70*scale), 30);
        		this.add(sliderMagnitude);
        		sliderValue.setBounds((int)(backgroundTop.getWidth()/2-30*scale), backgroundTop.getHeight()+InternalTop.getHeight()+(number-3)*InternalMid[0].getHeight()+25, (int)(30*scale), 30);
        		this.add(sliderValue);
        		for(int i = number-2; i < number ; i++)
    			{
    				InternalMid[i] = resizeImage(ImageIO.read(getClass().getResource("/settings-mid-internal.png")), scale/2);
    				
    				settingList.get(i).setBounds((int)(backgroundTop.getWidth()/2-InternalTop.getWidth()/2), backgroundTop.getHeight()+InternalTop.getHeight()+i*InternalMid[0].getHeight()+25, (int)(500*scale), 30);
                	settingList.get(i).setFont(new Font("Calibri", Font.BOLD, (int)(36*scale)));
        			settingList.get(i).setForeground(Color.BLACK);
                	this.add(settingList.get(i));
            		comboBoxList.get(i-1).setBounds((int)(backgroundTop.getWidth()/2-100*scale), backgroundTop.getHeight()+InternalTop.getHeight()+i*InternalMid[0].getHeight()+25, (int)(100*scale), 30);
            		this.add(comboBoxList.get(i-1));
                }
    			InternalBot = resizeImage(ImageIO.read(getClass().getResource("/settings-bot-internal.png")), scale/2);
			} catch (IOException e) {
				e.printStackTrace();
			}
    		

    		titre = new JLabel(Messages.getString("MainView.Settings"), JLabel.CENTER);
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
            for(int i = 0; i < number-2; i++)
            {
            	g2.drawImage(InternalMid[i], (int)(backgroundTop.getWidth()/2-InternalTop.getWidth()/2), backgroundTop.getHeight()+InternalTop.getHeight()+i*InternalMid[0].getHeight()+titre.getHeight(), null);
            	
            	
            }
            	g2.drawImage(InternalBot, (int)(backgroundTop.getWidth()/2-InternalTop.getWidth()/2), backgroundTop.getHeight()+InternalTop.getHeight()+(number-2)*InternalMid[0].getHeight()+titre.getHeight(), null);
		}
    	
    	/**
    	 * this method was here only for avoid the user to click on the skymap behind the widows.
    	 * @param evt
    	 */
    	private void MouseClicked(java.awt.event.MouseEvent evt) {
    		//nothing
    	}
    		
    	private void	jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {

    		String l_oldPort = settings.getPort();
    		int i = 0;
			settings.setPort((comboBoxList.get(i).getSelectedItem() != null) ? comboBoxList.get(i)
					.getSelectedItem().toString() : Messages.getString("MainView.None")); //$NON-NLS-1$
			
			i++;
			settings.setSpeed(Integer.parseInt(comboBoxList.get(i).getSelectedItem().toString()));
			
			i++;
			settings.setDatabit(Integer.parseInt(comboBoxList.get(i).getSelectedItem().toString()));
			
			i++;
			settings.setStopbit(
					(comboBoxList.get(i).getSelectedItem().toString().equals("1"))?jssc.SerialPort.STOPBITS_1:
						(comboBoxList.get(i).getSelectedItem().toString().equals("2"))?jssc.SerialPort.STOPBITS_2:jssc.SerialPort.STOPBITS_1_5);
			
			i++;
			settings.setParity(
					(comboBoxList.get(i).getSelectedItem().toString().equals(Messages.getString("MainView.Odd")))?jssc.SerialPort.PARITY_ODD:
						(comboBoxList.get(i).getSelectedItem().toString().equals(Messages.getString("MainView.Even")))?jssc.SerialPort.PARITY_EVEN:
							(comboBoxList.get(i).getSelectedItem().toString().equals(Messages.getString("MainView.Mark")))?jssc.SerialPort.PARITY_MARK:
								(comboBoxList.get(i).getSelectedItem().toString().equals(Messages.getString("MainView.Space")))?jssc.SerialPort.PARITY_SPACE:jssc.SerialPort.PARITY_NONE);
			
			i++;
			settings.setFlowControl(
					(comboBoxList.get(i).getSelectedItem().toString().equals(Messages.getString("MainView.RTSCTS_IN")))?jssc.SerialPort.FLOWCONTROL_RTSCTS_IN:
						(comboBoxList.get(i).getSelectedItem().toString().equals(Messages.getString("MainView.RTSCTS_OUT")))?jssc.SerialPort.FLOWCONTROL_RTSCTS_OUT:
							(comboBoxList.get(i).getSelectedItem().toString().equals(Messages.getString("MainView.XONXOFF_IN")))?jssc.SerialPort.FLOWCONTROL_XONXOFF_IN:
								(comboBoxList.get(i).getSelectedItem().toString().equals(Messages.getString("MainView.XONXOFF_OUT")))?jssc.SerialPort.FLOWCONTROL_XONXOFF_OUT:jssc.SerialPort.FLOWCONTROL_NONE);
			
			settings.setMagnitude((double)sliderMagnitude.getValue()/100);
			skymap.setMagnitudeMax(settings.getMagnitude());
			sliderValue.setText(String.valueOf((double)sliderMagnitude.getValue()/100));
			
			i++;
			settings.setConstellation((comboBoxList.get(i).getSelectedItem().toString().equals(Messages.getString("MainView.On"))) ? true : false);
			skymap.setShowConstellations(settings.getConstellation());
			
			i++;
			settings.setSimulation((comboBoxList.get(i).getSelectedItem().toString().equals(Messages.getString("MainView.On"))) ? true : false);
			
			settings.saveToFile();
			
			if(!l_oldPort.equals(settings.getPort()))
				JOptionPane.showMessageDialog(this, Messages.getString("MainView.ChangePortImpossible"), Messages.getString("MainView.PortChange"), JOptionPane.INFORMATION_MESSAGE);
    	}
    	
		/** 
		 * update the scale variable and resize the components
    	 * @param _scale : the scalar
		 */
		public void update(double _scale)
		{
			scale = _scale;

			try {
				backgroundTop = resizeImage(ImageIO.read(getClass().getResource("/settings-top-background.png")), scale/2);

				titre.setFont(new Font("Calibri", Font.BOLD,  (int)(scale*36)));
				titre.setBounds(0, backgroundTop.getHeight(), backgroundTop.getWidth(), (int)(scale*35));
				
				backgroundBot = resizeImage(ImageIO.read(getClass().getResource("/settings-bot-background.png")), scale/2);
				InternalTop = resizeImage(ImageIO.read(getClass().getResource("/settings-top-internal.png")), scale/2);
				for(int i = 0; i < number-3; i++)
				{
					InternalMid[i] = resizeImage(ImageIO.read(getClass().getResource("/settings-mid-internal.png")), scale/2);
				  	settingList.get(i).setBounds((int)(backgroundTop.getWidth()/2-InternalTop.getWidth()/2+30*scale), backgroundTop.getHeight()+i*InternalMid[0].getHeight()+titre.getHeight()+(int)(13*scale), (int)(500*scale), (int)(30*scale));
				  	settingList.get(i).setFont(new Font("Calibri", Font.BOLD, (int)(36*scale)));
				  	comboBoxList.get(i).setBounds((int)(backgroundTop.getWidth()-300*scale), backgroundTop.getHeight()+i*InternalMid[0].getHeight()+titre.getHeight()+(int)(8*scale), (int)(250*scale), (int)(40*scale));
				  	comboBoxList.get(i).setFont(new Font("Calibri", Font.BOLD, (int)(25*scale)));
				}
				InternalMid[number-3] = resizeImage(ImageIO.read(getClass().getResource("/settings-mid-internal.png")), scale/2);
			  	settingList.get(number-3).setBounds((int)(backgroundTop.getWidth()/2-InternalTop.getWidth()/2+30*scale), backgroundTop.getHeight()+(number-3)*InternalMid[0].getHeight()+titre.getHeight()+(int)(13*scale), (int)(500*scale), (int)(30*scale));
			  	settingList.get(number-3).setFont(new Font("Calibri", Font.BOLD, (int)(36*scale)));
			  	sliderMagnitude.setBounds((int)(backgroundTop.getWidth()-300*scale), backgroundTop.getHeight()+(number-3)*InternalMid[0].getHeight()+titre.getHeight()+(int)(8*scale), (int)(180*scale), (int)(40*scale));
			  	sliderValue.setFont(new Font("Calibri", Font.BOLD, (int)(25*scale)));
			  	sliderValue.setBounds((int)(backgroundTop.getWidth()-120*scale), backgroundTop.getHeight()+(number-3)*InternalMid[0].getHeight()+titre.getHeight()+(int)(8*scale), (int)(70*scale), (int)(40*scale));
        		
				
				for(int i = number-2; i < number; i++)
				{
					InternalMid[i] = resizeImage(ImageIO.read(getClass().getResource("/settings-mid-internal.png")), scale/2);
				  	settingList.get(i).setBounds((int)(backgroundTop.getWidth()/2-InternalTop.getWidth()/2+30*scale), backgroundTop.getHeight()+i*InternalMid[0].getHeight()+titre.getHeight()+(int)(13*scale), (int)(500*scale), (int)(30*scale));
				  	settingList.get(i).setFont(new Font("Calibri", Font.BOLD, (int)(36*scale)));
				  	comboBoxList.get(i-1).setBounds((int)(backgroundTop.getWidth()-300*scale), backgroundTop.getHeight()+i*InternalMid[0].getHeight()+titre.getHeight()+(int)(8*scale), (int)(250*scale), (int)(40*scale));
				  	comboBoxList.get(i-1).setFont(new Font("Calibri", Font.BOLD, (int)(25*scale)));
				}
				backgroundMid = resizeImage2(ImageIO.read(getClass().getResource("/settings-mid-background.png")), backgroundTop.getWidth(), (number)*InternalMid[0].getHeight()+titre.getHeight()+ (int)(15*scale));
				InternalBot = resizeImage(ImageIO.read(getClass().getResource("/settings-bot-internal.png")), scale/2);
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
    			backgroundTop = resizeImage(ImageIO.read(getClass().getResource("/haut-fond.png")), scale/2);
    			backgroundMid = resizeImage2(ImageIO.read(getClass().getResource("/mid-fond.png")), 1, (int)(200*scale/2));
    			backgroundBot = resizeImage(ImageIO.read(getClass().getResource("/bas-fond.png")), scale/2);
    			internalTop = resizeImage(ImageIO.read(getClass().getResource("/haut-interieur.png")), scale/2);
    			internalMid = resizeImage2(ImageIO.read(getClass().getResource("/mid-interne.png")), 1, (int)(50*scale/2));
    			internalBot = resizeImage(ImageIO.read(getClass().getResource("/bas-interieur.png")), scale/2);
			} catch (IOException e) {
				e.printStackTrace();
			}
    			titre = new JLabel(Messages.getString("MainView.Help"), JLabel.CENTER);
    			titre.setFont(new Font("Calibri", Font.BOLD, 36));
    			titre.setBounds(0, backgroundTop.getHeight(), (int)(scale*345), (int)(scale*34));
    			titre.setForeground(Color.WHITE);
    			this.add(titre, new Integer(1));

    			text = new JLabel(Messages.getString("MainView.HelpText"));
    			text.setFont(new Font("Calibri", Font.BOLD, (int)(scale*36)));
    			text.setBounds(0, internalTop.getHeight(), (int)(scale*345), (int)(scale*34*8));
    			text.setForeground(Color.BLACK);
    			this.add(text, new Integer(2));

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
    			backgroundTop = resizeImage(ImageIO.read(getClass().getResource("/haut-fond.png")), scale/2);
    			backgroundBot = resizeImage(ImageIO.read(getClass().getResource("/bas-fond.png")), scale/2);
    			internalTop = resizeImage(ImageIO.read(getClass().getResource("/haut-interieur.png")), scale/2);

    			titre.setFont(new Font("Calibri", Font.BOLD,  (int)(scale*36)));
    			titre.setBounds(0, backgroundTop.getHeight(), backgroundTop.getWidth(), (int)(scale*35));

    			text.setFont(new Font("Calibri", Font.BOLD,  (int)(scale*24)));
    			text.setBounds(0, backgroundTop.getHeight() + internalTop.getHeight(), (int)(internalTop.getWidth()), (int)(scale*26*19));

    			internalMid = resizeImage2(ImageIO.read(getClass().getResource("/mid-interne.png")), text.getWidth(), text.getHeight()-(int)(52*scale));
    			backgroundMid = resizeImage2(ImageIO.read(getClass().getResource("/mid-fond.png")), backgroundTop.getWidth(), text.getHeight()+(int)(titre.getHeight()*3));
    			internalBot = resizeImage(ImageIO.read(getClass().getResource("/bas-interieur.png")), scale/2);
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
    		zoomSlider.setMinimum(kZoomMin);
    		zoomSlider.setMaximum(kZoomMax);
    		zoomSlider.setValue(zoom);
    		zoomSlider.setOpaque(false);
    		this.add(zoomSlider);
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
    	String[] keys = {"!id ", "!ProperName ", "!RA ", "!Dec ", "!Distance ", "!Mag ", "!ColorIndex ", "!HIP ", "!HD ", "!HR "};
    	JScrollPane jScrollPane = new JScrollPane();
    	ArrayList<CelestialObject> listCelestialObject = new ArrayList<CelestialObject>();

    	/**
    	 * Constructor
    	 * @param _scale
    	 */
    	@SuppressWarnings("unchecked")
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
        				jScrollPane.setVisible(true);
        				
        			}		
                }
    		});
    		
    		
    		listModelNameOrID = new ListModel<String>();
    		listModelObjects = new ArrayList<CelestialObject>();
    		listNameOrID = new JList<String>();
    		listNameOrID.setModel(listModelNameOrID);
    		listNameOrID.setBounds(0, 0, 300, 400);
    		listNameOrID.setFocusable(false);
    		jScrollPane.setFocusable(false);
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
        	if(searchFeature.split(" ").length > 1 && listModelObjects.size() > 0)
        	{
             	int index = listNameOrID.getSelectedIndex();
        		CelestialObject celObjt = listModelObjects.get(index);
        		updateInfo(celObjt);
        		skymap.setCelestialObjectSearched(celObjt);
        		
        		redArrowAzimuth = celObjt.getAzimuth() * 180 / Math.PI;
        		redArrowPitch = celObjt.getHeight() * 180 / Math.PI;
        		compassPanel.setRedNeedle(redArrowAzimuth);
        		inclinometerPanel.setRedNeedle(redArrowPitch);
        		compassPanel.setSearchMode(true);
        		inclinometerPanel.setSearchMode(true);
    			
        		if(pic != null && pic.getMode() != PicMode.SIMULATION)
        			pic.changeMode(PicMode.GUIDING);
        		
        		l_sSavedSearch = searchBarTextField.getText();
        		searchBarTextField.setText(listNameOrID.getSelectedValue().toString());
        		skymap.transferFocusBackward();
        		MainView.this.update();
        		return;
        	}
    		
        	if(listNameOrID.getSelectedValue() != null && searchBarText.length > 0)
        		{
        			String regex = searchBarText[searchBarText.length - 1] + "$";
        			searchBarTextField.setText(searchBarTextField.getText().replaceFirst(regex, listNameOrID.getSelectedValue().toString()));
            	}
        		
    	}
    	
    	/**
    	 * search in the database the stars corresponding with the textField.
    	 * @param evt
    	 */
		private void searchBarKeyReleased(java.awt.event.KeyEvent evt)
		{
			if (evt.getKeyCode() == 40) // down
			{
				listNameOrID.setSelectedIndex(listNameOrID.getSelectedIndex() + 1);
				jScrollPane.getVerticalScrollBar().setValue(listNameOrID.getSelectedIndex() * 18);
			}
			else if (evt.getKeyCode() == 38) // up
			{
				if (listNameOrID.getSelectedIndex() > -1)
					listNameOrID.setSelectedIndex(listNameOrID.getSelectedIndex() - 1);
				jScrollPane.getVerticalScrollBar().setValue(listNameOrID.getSelectedIndex() * 18);
			}
			else if (evt.getKeyCode() == 37 || evt.getKeyCode() == 39 || evt.getKeyCode() == 10) // left,
																									// right,
																									// enter
			{
				if (listModelNameOrID.getSize() > 0 && listNameOrID.getSelectedIndex() < 0)
				{
					listNameOrID.setSelectedIndex(0);
				}
				listNameOrIDMouseClicked(null);
				listNameOrID.setSelectedIndex(-1);
				jScrollPane.getVerticalScrollBar().setValue(0);
			}
			else if (evt.getKeyCode() == 32) // space
			{
				if (listNameOrID.getSelectedIndex() > -1)
				{
					searchBarTextField.setText(searchBarTextField.getText().substring(0,
							searchBarTextField.getText().length() - 1));
					listNameOrIDMouseClicked(null);
				}
				listNameOrID.setSelectedIndex(-1);
				jScrollPane.getVerticalScrollBar().setValue(0);
			}
			else
			{
				// listCelestialObject.clear();
				if (listModelNameOrID.getSize() > 0)
					listModelNameOrID.removeAll();
				listModelObjects.clear();

				boolean canQueryDB = true;
				
				String[] searchFeatures = searchBarTextField.getText().split(";");
				for (String searchFeature : searchFeatures)
				{
					for (String key : keys)
					{
						if (key.toLowerCase().startsWith(searchFeature.toLowerCase()))
						{
							listModelNameOrID.setElement(key);
						}
					}
					if (searchFeature.split(" ").length <= 1)
						canQueryDB = false;
				}
				if (canQueryDB)
				{
					try
					{
						listCelestialObject = db.starsForText(searchBarTextField.getText(),
								Calendar.getInstance(), skymap.getdLatitude(),
								skymap.getdLongitude());

						if (listCelestialObject.size() != 0)
						{
							for (CelestialObject celestialObject : listCelestialObject)
							{
								String nameOrId;
								if (celestialObject.getProperName() != null)
									nameOrId = celestialObject.getProperName();
								else
									nameOrId = String.valueOf(celestialObject.getId());

								if (!searchFeatures[0].startsWith("!ProperName"))
									nameOrId += " : ";

								switch (searchFeatures[0].split(" ")[0])
								{
									case "!id":
										nameOrId += celestialObject.getId();
										break;
									case "!ProperName":
										// Do nothing
										break;
									case "!RA":
										nameOrId += celestialObject.getRA();
										break;
									case "!Dec":
										nameOrId += celestialObject.getDec();
										break;
									case "!Distance":
										nameOrId += celestialObject.getDistance();
										break;
									case "!Mag":
										nameOrId += celestialObject.getMag();
										break;
									case "!ColorIndex":
										nameOrId += celestialObject.getColorIndex();
										break;
									case "!HIP":
										nameOrId += celestialObject.getHIP();
										break;
									case "!HD":
										nameOrId += celestialObject.getHD();
										break;
									case "!HR":
										nameOrId += celestialObject.getHR();
										break;
								}

								listModelNameOrID.setElement(nameOrId);
								listModelObjects.add(celestialObject);
							}
						}
						else
							listModelNameOrID.setElement(Messages.getString("MainView.NoResult"));
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
					}
				}

				if (listModelNameOrID.getSize() > 0)
				{
					int min = (listModelNameOrID.getSize() < 5) ? listModelNameOrID.getSize() * 21
							: (int) (200 * scale);
					jScrollPane.setBounds(0, 20, searchBarTextField.getWidth(), min);
					jScrollPane.setVisible(true);

				}
				else
					jScrollPane.setVisible(false);
			}
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
					cross = ImageIO.read(getClass().getResource("/Cross.png"));
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
				skymap.setCelestialObjectSearched(null);
	    		leftPanel.setText("");
        		updateInfo(null);
	    		skymap.updateSkyMap();
	    		searchBarTextField.setText(null);
	    		l_sSavedSearch = null;
	    		if(pic != null && pic.getMode() == PicMode.GUIDING)
	    			pic.changeMode(PicMode.POINTING);
        		compassPanel.setSearchMode(false);
        		inclinometerPanel.setSearchMode(false);
        		jScrollPane.setVisible(false);
        		leftPanel.setText(null);
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
	class Compass extends JLayeredPane
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
				background = resizeImage(ImageIO.read(getClass().getResource("/backgroundCompass.png")),scale);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			redNeedle = new Needle("/aiguille_rouge.png", scale);
			greenNeedle = new Needle("/aiguille_vert.png", scale);
			
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

			setSearchMode(false);
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
				background = resizeImage(ImageIO.read(getClass().getResource("/backgroundCompass.png")),scale);
			} catch (IOException e) {
				e.printStackTrace();
			}
			redNeedle.scale(scale);
            redNeedle.rotate(redAngle);
			redNeedle.setBounds(0, 0, (int)(scale*345), (int)(scale*304));
            greenNeedle.scale(scale);
            greenNeedle.rotate(greenAngle);
			greenNeedle.setBounds(0, 0, (int)(scale*345), (int)(scale*304));


			if(greenAngle < 0)
				greenAngle += 360;
			double l_dAngle = greenAngle - 22.5;
			if(l_dAngle < 0)
				l_dAngle += 360;
			int l_iAngle = (int) (l_dAngle / 45);
			String l_sDirection = "";
			
			switch (l_iAngle)
			{
				case 0:
					l_sDirection = "NE";
					break;
				case 1:
					l_sDirection = "E";
					break;
				case 2:
					l_sDirection = "SE";
					break;
				case 3:
					l_sDirection = "S";
					break;
				case 4:
					l_sDirection = "SO";
					break;
				case 5:
					l_sDirection = "O";
					break;
				case 6:
					l_sDirection = "NO";
					break;
				case 7:
					l_sDirection = "N";
					break;
				default:
					l_sDirection = "N";
					break;
			}


			try
			{
				coordinateCompass.setText(String.valueOf((int)(greenAngle)) + "° " + l_sDirection);
			}
			catch(Exception e)
			{
				log.warning(e.toString());
			}
			coordinateCompass.setFont(new Font("Calibri", Font.BOLD,  (int)(scale*36)));
			coordinateCompass.setBounds(0, (int)(scale*310), (int)(scale*345), (int)(scale*35));
			this.setSize((int)(scale*345), (int)(scale*350));
		}

		public void setSearchMode(boolean _searchMode)
		{
			redNeedle.setVisible(_searchMode);
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
					needleImage = resizeImage(ImageIO.read(getClass().getResource(adresseImage)),scale);
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
						needleImage = resizeImage(ImageIO.read(getClass().getResource(adresseImage)),scale);
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
	class Inclinometer extends JLayeredPane
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
				background = resizeImage(ImageIO.read(getClass().getResource("/backgroundInclinometer.png")),scale);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			redNeedle = new Needle("/aiguille_rouge_inclinometer.png", scale);
			greenNeedle = new Needle("/aiguille_vert_inclinometer.png", scale);
			
			
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

			setSearchMode(false);
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
		
        public void setSearchMode(boolean _searchMode)
		{
			redNeedle.setVisible(_searchMode);
		}
        
		/** 
		 *  update the scale variable and resize the components
		 * @param _scale : the scalar
		 */
		public void update(double _scale)
		{
			scale = _scale;
			try {
				background = resizeImage(ImageIO.read(getClass().getResource("/backgroundInclinometer.png")),scale);
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
				coordinateInclinometer.setText(String.valueOf((int)(greenAngle%360)) + "°");
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
					needleImage = resizeImage(ImageIO.read(getClass().getResource(adresseImage)),scale);
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
						needleImage = resizeImage(ImageIO.read(getClass().getResource(adresseImage)),scale);
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



