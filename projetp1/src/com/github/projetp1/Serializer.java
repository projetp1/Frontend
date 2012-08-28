package com.github.projetp1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class Serializer
{
	private static JSONSerializer jss = new JSONSerializer();

	public static void serialize(String _filename, Object _object)
	{
		try
		{
			BufferedWriter bw = new BufferedWriter(new FileWriter(_filename, false));
			bw.write(jss.serialize(_object));
			bw.close();
		}
		catch (IOException ex)
		{
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).severe(
					"Impossible d'écrire dans le fichier : " + ex.getLocalizedMessage());
		}
	}

	public static Object deserialize(String _filename)
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(_filename));
			JSONDeserializer<Object> jsd = new JSONDeserializer<Object>();
			Object o = jsd.deserialize(br.readLine());
			br.close();
			return o;
		}
		catch (FileNotFoundException ex)
		{
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).severe(
					"Le fichier n'existe pas : " + ex.getLocalizedMessage());
			return null;
		}
		catch (IOException ex)
		{
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).severe(
					"Impossible de lire le fichier : " + ex.getLocalizedMessage());
			return null;
		}
		catch (Exception ex)
		{
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).severe(
					"Erreur lors de la désérialisation : " + ex.getLocalizedMessage());
			new File(_filename).delete();
			return null;
		}
	}

}
