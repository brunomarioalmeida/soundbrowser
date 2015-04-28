package com.soundbrowser.converters;

import java.io.Reader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.soundbrowser.persistence.model.SourceRoot;

/**
 * Created by bold on 17-11-2014.
 */
public class JsonToObjectConverter {

    private static final JsonToObjectConverter INSTANCE = 
       	new JsonToObjectConverter();
    private Gson gson;
    private static final double VERSION = 1.0d;
    
    public JsonToObjectConverter() {
        gson = new GsonBuilder().setVersion(VERSION).create();
	}

    public static SourceRoot convert(Reader reader) {
        Gson gson = new GsonBuilder().setVersion(VERSION).create();
        return gson.fromJson(reader, SourceRoot.class);
    }
    
    public com.soundbrowser.persistence.model.SourceRoot fromJson(Reader reader) {
        return gson.fromJson(
        	reader, com.soundbrowser.persistence.model.SourceRoot.class
        );
    }
    
    public static String convert(SourceRoot sR) {
        Gson gson = new GsonBuilder().setVersion(VERSION).create();
        return gson.toJson(sR);
    }
    
	public static Gson getGson() {
		return INSTANCE.gson;
	}
}
