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
	private static Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	public static void serialize(String _filename, Settings _object)
	{
		try
		{
			BufferedWriter bw = new BufferedWriter(new FileWriter(_filename, false));
			jss.serialize(_object, bw);
			bw.close();
		}
		catch (IOException ex)
		{
			log.severe("Impossible d'écrire dans le fichier : " + ex.getLocalizedMessage());
		}
	}

	public static Settings deserialize(String _filename)
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(_filename));
			JSONDeserializer<Settings> jsd = new JSONDeserializer<Settings>();
			Settings o = jsd.deserialize(br);
			br.close();
			return o;
		}
		catch (FileNotFoundException ex)
		{
			log.severe("Le fichier n'existe pas : " + ex.getLocalizedMessage());
			return null;
		}
		catch (IOException ex)
		{
			log.severe("Impossible de lire le fichier : " + ex.getLocalizedMessage());
			return null;
		}
		catch (Exception ex)
		{
			log.severe("Erreur lors de la désérialisation : " + ex.getLocalizedMessage());
			new File(_filename).delete();
			return null;
		}
	}

}
