
package com.soundbrowser.persistence.model;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.Since;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

@Generated("org.jsonschema2pojo")
public class Item extends BaseEntity {

    @Since(1.0)
    @Expose
    @DatabaseField
    private String title;
    
    @Expose
    @DatabaseField
    private String pubDate;

    @Expose
	@DatabaseField(foreign = true, foreignAutoRefresh=true)
    private Track track;
    
	@DatabaseField(foreign = true)
	private Item parent;

	@ForeignCollectionField(eager = true)
	private ForeignCollection<Item> items; 

    @Expose
    private List<Item> itemLst = new ArrayList<Item>();
    
    @Since(1.1)
    private String fieldToBe;
    
    // getters & setters
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getPubDate() {
        return pubDate;
    }
    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public Track getTrack() {
        return track;
    }
    public void setTrack(Track track) {
        this.track = track;
    }

	public Item getParent() {
		return parent;
	}
	public void setParent(Item parent) {
		this.parent = parent;
	}

	public ForeignCollection<Item> getItems() {
		return items;
	}
	public void setItems(ForeignCollection<Item> items) {
		this.items = items;
	}
	
    public List<Item> getItemLst() 
    {
//    	for (Item i : items) 
//			itemLst.add(i);
    	
//    	if(itemLst.isEmpty())
//    		return itemLst;
//    	
//    	if(itemLst.get(0).getParent() == null)
//        	for (Item child : itemLst) 
//        		child.setParent(this);
    	
    	return itemLst;
    }
    public void setItemLst(List<Item> item) {
        this.itemLst = item;
    }

    public String getFieldToBe() {
		return fieldToBe;
	}
	public void setFieldToBe(String fieldToBe) {
		this.fieldToBe = fieldToBe;
	}
}
