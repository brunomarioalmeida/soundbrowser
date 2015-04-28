package com.soundbrowser.client.model;

//import org.apache.commons.lang3.builder.ToStringBuilder;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Track {

    private String url;
    private int sizeTrack;
	
    public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getSizeTrack() {
		return sizeTrack;
	}
	public void setSizeTrack(int sizeTrack) {
		this.sizeTrack = sizeTrack;
	}

//	public String toString(){
//		return ToStringBuilder.reflectionToString(this);
//	}
}
