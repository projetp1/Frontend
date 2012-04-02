package com.github.projetp1;

import java.io.*;

public class Serializer {

	public Serializer() {
	}
	
	public static void serialize(String _filename, Object _object)
	{
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try
		{
			fos = new FileOutputStream(_filename);
			out = new ObjectOutputStream(fos);
			out.writeObject(_object);
			out.close();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public Object deserialize(String _filename)
	{
		File f = new File(_filename);
		Object object = null;
		if(f.exists()) 
		{			
			FileInputStream fis = null;
			ObjectInputStream in = null;
			try
			{
				fis = new FileInputStream(_filename);
				in = new ObjectInputStream(fis);
				object = in.readObject();
				in.close();
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
			catch(ClassNotFoundException ex)
			{
				ex.printStackTrace();
			}
		} 
		return object;
	}

}
