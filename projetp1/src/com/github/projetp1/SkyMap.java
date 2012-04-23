/**
 * 
 */
package com.github.projetp1;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * @author   alexandr.perez
 */
public class SkyMap extends javax.swing.JPanel implements KeyListener {
	
	private int zoom, x, y, xOrigin, yOrigin, heightSky, widthSky;
	private ArrayList<CelestialObject> celestialObjects;
	/**
	 * 
	 */
	public SkyMap(int xOrigin, int yOrigin, int heightSky, int widthSky) {
		 
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
