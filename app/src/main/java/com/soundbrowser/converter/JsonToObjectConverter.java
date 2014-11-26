package com.soundbrowser.converter;

import java.io.Reader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.soundbrowser.model.SourceRoot;

/**
 * Created by bold on 17-11-2014.
 */
public class JsonToObjectConverter {

    private static final double VERSION = 1.0d;
    
	public static SourceRoot convert(Reader reader) {
        Gson gson = new GsonBuilder().setVersion(VERSION).create();
        return gson.fromJson(reader, SourceRoot.class);
    }

    public static String convert(SourceRoot sR) {
        Gson gson = new GsonBuilder().setVersion(VERSION).create();
        return gson.toJson(sR);
    }
}
