/**
 * 
 */
package com.github.projetp1;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;


class Star extends javax.swing.JPanel {
	CelestialObject celestialObject;
	
	Star(CelestialObject _celestialObject)
	{
		celestialObject = _celestialObject;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		double d = celestialObject.getMag();
		g.fillOval(10, 10, (int)d, (int)d);
	}
}

/**
 * @author   alexandr.perez
 */
public class SkyMap extends javax.swing.JLayeredPane implements KeyListener {
	
	private int zoom = 0;
	private double x, y, xOrigin, yOrigin, heightSky, widthSky;
	private ArrayList<CelestialObject> celestialObjects;
	/**
	 * 
	 */
	public SkyMap(int _xOrigin, int _yOrigin, int _heightSky, int _widthSky) {
		zoom = 0;
		xOrigin = _xOrigin;
		yOrigin = _yOrigin;
		heightSky = _heightSky;
		widthSky = _widthSky;
		 
		 
    }
	
	public void keyTyped(KeyEvent evt){}
	
	public void keyReleased(KeyEvent evt){}  

	public void keyPressed(KeyEvent evt) {
        if(evt.getKeyCode() == 37) //Left
        {
        	
        }
        else if(evt.getKeyCode() == 39) //Right
        {
        	
        }
        else if(evt.getKeyCode() == 38) // Up
        {

        }
        else if(evt.getKeyCode() == 40) // Up
        {
        	
        }
	}
	
	public void updateSkyMap() {
		 for (CelestialObject celestialObject : celestialObjects)
			 this.drawStar(celestialObject);
	}
	
	private void drawStar (CelestialObject _celestialObjects)
	{
		
	}
	
	/**
	public void updateArrow(CelestialObject _object) {
		
	}
	*/
	
	/**
	 * @param  _zoom
	 * @uml.property  name="zoom"
	 */
	public void setZoom(int _zoom) {
		this.zoom = _zoom;
	}


	/**
	 * @return
	 * @uml.property  name="arrayObject"
	 */
	public ArrayList<CelestialObject> getArrayObject() {
		return celestialObjects;
	}

	/**
	 * @param  _arrayObject
	 * @uml.property  name="arrayObject"
	 */
	public void setArrayObject(ArrayList<CelestialObject> _celestialObjects) {
		this.celestialObjects = _celestialObjects;
	}
}
