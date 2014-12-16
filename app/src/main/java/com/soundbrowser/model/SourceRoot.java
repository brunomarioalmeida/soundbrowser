
package com.soundbrowser.model;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class SourceRoot {

    @Expose
    private List<Item> item = new ArrayList<Item>();

    /**
    * 
    * @return
    * The item
    */
    public List<Item> getItem() {
    	return item;
    }

    /**
    * 
    * @param item
    * The item
    */
    public void setItem(List<Item> item) {
    	this.item = item;
    }
}
