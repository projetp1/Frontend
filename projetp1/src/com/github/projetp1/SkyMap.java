/**
 * 
 */
package com.github.projetp1;

import java.awt.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Calendar;


@SuppressWarnings("serial")
class Star extends JComponent {
	CelestialObject celestialObject;
	int d;
	
	Star(CelestialObject _celestialObject, int _scale, int _zoom, int _xOrigin, int _yOrigin)
	{
		int x, y;
		celestialObject = _celestialObject;
		d = getSizeForMagnitude(celestialObject.getMag())+1;
		if(celestialObject.getMag() < -20)
			d = 30;
		d = 30;
		x = (int)(celestialObject.getXReal() * _zoom * _scale) + _xOrigin;
		y = (int)(celestialObject.getYReal() * _zoom * _scale) + _yOrigin;
		setSize(2*d,2*d);
		setLocation(x, y);
		//setOpaque(false);		
        setBackground(Color.red);
	}
	
    @Override 
    protected void paintComponent(Graphics g)
    { 
        super.paintComponent(g);
        g.setColor(getColorForColorIndex(celestialObject.getColorIndex()));
        g.fillOval(d/2, d/2, d, d);
    }
    

	/**
     * Returns the diameter of the point who display the star
     * The diameter correspond to the star magnitude
     * @param _mag : Magnitude of the star
     * @return Size of the point in pixel 
     */
    private int getSizeForMagnitude(double _mag)
    {
        int size = 0;
        
        if (_mag >= 5.3)
            size = 1;
        else if (_mag >= 4.0)
            size = 2;
        else if (_mag >= 2.7)
            size = 3;
        else if (_mag >= 1.4)
            size = 4;
        else if (_mag >= 0.1)
            size = 5;
        else if (_mag >= -1.2)
            size = 6;
        else
            size = 7;
        
        return size;
    }
    

	/**
     * Returns the color of the point who display the star
     * The color correspond to the star color index
     * @param _colorIndex : Color index of the star
     * @return Color of the point 
     */    
    private Color getColorForColorIndex(double _colorIndex)
    {
    	Color c = Color.WHITE;
    	if(_colorIndex >= 1.41)
    		c = Color.RED;
    	else if(_colorIndex >= 0.82)
    		c = Color.ORANGE;
    	else if(_colorIndex >= 0.59)
    		c = Color.YELLOW;
    	else if(_colorIndex >= 0.31)
    		c = Color.YELLOW;
    	else if(_colorIndex >= 0.0)
    		c = Color.white;
    	else
    		c = Color.BLUE;
    	
    	return c;
    }
}

/**
 * @author   alexandr.perez
 */
@SuppressWarnings("serial")
public class SkyMap extends JLayeredPane {
	
	private int zoom = 1;
	private ArrayList<CelestialObject> celestialObjects;

	public SkyMap() {
		this.setBackground(Color.BLACK);
		this.setOpaque(true);
		try
		{
			DataBase db = new DataBase("hyg.db", ";");
			celestialObjects = db.starsForCoordinates(Calendar.getInstance(), 47.039448, 6.799734);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		repaint();
    }
	
	
	public void updateSkyMap() {
		//this.removeAll();
		this.repaint();
		/**
		int scale = (int)(this.getHeight()/5);
		int xOrigin = this.getWidth()/2; //TODO + scale * l'endroit où point le pic
		int yOrigin = this.getHeight()/2;
		for (CelestialObject celestialObject : celestialObjects)
			 this.add(new Star(celestialObject, scale, zoom, xOrigin, yOrigin));
			 */
	}
	
	
	public void paint(Graphics g)
	{
		int scale = (int)(this.getHeight()/5);
		int xOrigin = this.getWidth()/2; //TODO + scale * l'endroit où point le pic
		int yOrigin = this.getHeight()/2;
		for (CelestialObject celestialObject : celestialObjects)
		{
			int x, y;
			int d = getSizeForMagnitude(celestialObject.getMag())+1;
			if(celestialObject.getMag() < -20)
				d = 30;
			x = (int)(celestialObject.getXReal() * zoom * scale) + xOrigin;
			y = (int)(celestialObject.getYReal() * zoom * scale) + yOrigin;
			g.setColor(getColorForColorIndex(celestialObject.getColorIndex()));
	        g.fillOval(x, y, d, d);			
		}
	}
	
	/**
	public void updateArrow(CelestialObject _object) {
		
	}
	*/

	public void setZoom(int _zoom) {
		this.zoom = _zoom;
	}
	
	/**
     * Returns the diameter of the point who display the star
     * The diameter correspond to the star magnitude
     * @param _mag : Magnitude of the star
     * @return Size of the point in pixel 
     */
    private int getSizeForMagnitude(double _mag)
    {
        int size = 0;
        
        if (_mag >= 5.3)
            size = 1;
        else if (_mag >= 4.0)
            size = 2;
        else if (_mag >= 2.7)
            size = 3;
        else if (_mag >= 1.4)
            size = 4;
        else if (_mag >= 0.1)
            size = 5;
        else if (_mag >= -1.2)
            size = 6;
        else
            size = 7;
        
        return size;
    }
    

	/**
     * Returns the color of the point who display the star
     * The color correspond to the star color index
     * @param _colorIndex : Color index of the star
     * @return Color of the point 
     */    
    private Color getColorForColorIndex(double _colorIndex)
    {
    	Color c;
    	if(_colorIndex >= 1.41)
    		c = new Color(255,200,200);
    	else if(_colorIndex >= 0.82)
    		c = new Color(255,225,150);
    	else if(_colorIndex >= 0.59)
    		c = new Color(255,255,130);
    	else if(_colorIndex >= 0.31)
    		c = new Color(255,255,200);
    	else if(_colorIndex >= 0.0)
    		c = Color.WHITE;
    	else
    		c = new Color(215,215,255);
    	
    	return c;
    }
}
