package com.soundbrowser.services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import com.google.gson.GsonBuilder;
import com.soundbrowser.converters.JsonToObjectConverter;
import com.soundbrowser.persistence.model.SourceRoot;

public class PodcastSerializer {

	public void buildJsonNode(String node, String[] podcastArr) 
	{
		StringBuffer sB = new StringBuffer();
		sB.append("{\"item\": [{\"title\": \"" + node + "\", \"item\": [ ");
		for (String podcast : podcastArr)
			try {
				flatJsonFiles(podcast, sB);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		sB.append("]}]}");

		write2File(sB, "results/" + node.toLowerCase() + "_flat.json");
	}

	public void flatJsonFiles(String podcast, StringBuffer sB) 
	  throws UnsupportedEncodingException 
	{
		SourceRoot sourceRoot;
		
		sB.append("{\"item\": [{ \"title\": \"" + podcast + "\", \"item\": [");
		
		File folder = new File("results/" + podcast);
		File[] listOfFiles = folder.listFiles();
		for (File file : listOfFiles) 
			if(file.isFile()) {
				sourceRoot = JsonToObjectConverter.convert(
				    new InputStreamReader(
				    	this.getClass().getClassLoader().getResourceAsStream(
				    		podcast + "/" + file.getName()
				    	),
				    	"UTF-8"
				    )
				);
				sB.append(new GsonBuilder().create().toJson(sourceRoot) + ",");
			}
		sB.append("]}]},");
	}
	
	public void write2File(StringBuffer sB, String filename) 
	{
		// if file doesnt exists, then create it
//		if (!file.exists()) {
//			file.createNewFile();
//		}

		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(
				new FileWriter(
					new File(filename).getAbsoluteFile()
				)
			);
			bw.write(sB.toString().replaceAll(",null", "").replaceAll(",]", "]"));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
