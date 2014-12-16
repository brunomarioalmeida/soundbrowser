
package com.soundbrowser.model;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.Since;

@Generated("org.jsonschema2pojo")
public class Item {

    @Since(1.0)
    @Expose
    private String title;
    @Expose
    private String pubDate;
    @Expose
    private Track track;
    @Expose
    private List<Item> item = new ArrayList<Item>();
    
    private Item parent;

    @Since(1.1)
    private String fieldToBe;
    
	public String getFieldToBe() {
		return fieldToBe;
	}

	public void setFieldToBe(String fieldToBe) {
		this.fieldToBe = fieldToBe;
	}

    /**
     * 
     * @return
     *     The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 
     * @return
     *     The item
     */
    public List<Item> getItem() 
    {
    	if(item.isEmpty())
    		return item;
    	
    	if(item.get(0).getParent() == null)
        	for (Item child : item) 
        		child.setParent(this);
    	
    	return item;
    }

    /**
     * 
     * @param item
     *     The item
     */
    public void setItem(List<Item> item) {
        this.item = item;
    }

    /**
     * 
     * @return
     *     The pubDate
     */
    public String getPubDate() {
        return pubDate;
    }

    /**
     * 
     * @param pubDate
     *     The pubDate
     */
    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    /**
     * 
     * @return
     *     The track
     */
    public Track getTrack() {
        return track;
    }

    /**
     * 
     * @param track
     *     The track
     */
    public void setTrack(Track track) {
        this.track = track;
    }

	public Item getParent() {
		return parent;
	}

	public void setParent(Item parent) {
		this.parent = parent;
	}
}
