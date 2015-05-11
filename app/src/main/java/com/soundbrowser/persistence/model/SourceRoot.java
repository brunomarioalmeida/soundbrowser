
package com.soundbrowser.persistence.model;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.soundbrowser.converters.JsonToObjectConverter;

@Generated("org.jsonschema2pojo")
public class SourceRoot {

    @Expose
    private List<Item> item = new ArrayList<Item>();

    public List<Item> getItem() {
    	return item;
    }
    public void setItem(List<Item> item) {
    	this.item = item;
    }

    public String toJson() {
    	return JsonToObjectConverter.getGson().toJson(this);
    }
}
