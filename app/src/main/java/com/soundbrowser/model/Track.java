
package com.soundbrowser.model;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Track {

    @Expose
    private String url;
    @Expose
    private String size;
    @Expose
    private String duration;
    @Expose
    private List<Timming> timmings = new ArrayList<Timming>();
    
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

    /**
    * 
    * @return
    * The timmings
    */
    public List<Timming> getTimmings() {
    	return timmings;
    }

    /**
    * 
    * @param timmings
    * The timmings
    */
    public void setTimmings(List<Timming> timmings) {
    	this.timmings = timmings;
    }
}
