package com.soundbrowser.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.soundbrowser.model.SoundSource;

import java.io.Reader;

/**
 * Created by bold on 17-11-2014.
 */
public class JsonToObjectConverter {

    public static SoundSource convert(Reader reader) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(reader, SoundSource.class);
    }
}
