
package com.soundbrowser.persistence.model;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

@Generated("org.jsonschema2pojo")
public class Track extends BaseEntity {

	@Expose
    @DatabaseField
    private String url;
    
    @Expose
    @DatabaseField
    private String size;
    
    @Expose
    @DatabaseField
    private String duration;

    @ForeignCollectionField(eager = true)
	private ForeignCollection<Timming> timmings; 
    
	@DatabaseField(foreign = true)
	private Item item;

    @Expose
    private List<Timming> timmingsLst = new ArrayList<Timming>();

    // getters & setters
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }

    public String getDuration() {
        return duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }

    public List<Timming> getTimmingsLst() {
    	return timmingsLst;
    }
    public void setTimmingsLst(List<Timming> timmings) {
    	this.timmingsLst = timmings;
    }

    public ForeignCollection<Timming> getTimmings() {
		return timmings;
	}
	public void setTimmings(ForeignCollection<Timming> timmings) {
		this.timmings = timmings;
	}

	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
}
