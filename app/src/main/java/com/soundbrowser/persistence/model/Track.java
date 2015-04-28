
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

    @Expose
    private List<Timming> timmings = new ArrayList<Timming>();

    @ForeignCollectionField(eager = true)
	private ForeignCollection<Timming> timmings2; 
    
	@DatabaseField(foreign = true)
	private Item item;

	/**
     * 
     * @return
     *     The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 
     * @param url
     *     The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 
     * @return
     *     The size
     */
    public String getSize() {
        return size;
    }

    /**
     * 
     * @param size
     *     The size
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * 
     * @return
     *     The duration
     */
    public String getDuration() {
        return duration;
    }

    /**
     * 
     * @param duration
     *     The duration
     */
    public void setDuration(String duration) {
        this.duration = duration;
    }

    public List<Timming> getTimmings() {
    	return timmings;
    }
    public void setTimmings(List<Timming> timmings) {
    	this.timmings = timmings;
    }

    public ForeignCollection<Timming> getTimmings2() {
		return timmings2;
	}
	public void setTimmings2(ForeignCollection<Timming> timmings) {
		this.timmings2 = timmings;
	}

	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
}
