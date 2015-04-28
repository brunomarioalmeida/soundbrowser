package com.soundbrowser.client.model;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

//import org.apache.commons.lang3.builder.ToStringBuilder;
//import org.apache.commons.lang3.builder.ToStringStyle;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {

	private String title;
//    private String pubDate;
    private Date pubDate;
    private Item[] itens;
	private Track track;
	private Key id;
	
    public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
//	public String getPubDate() {
	public Date getPubDate() {
		return pubDate;
	}
//	public void setPubDate(String pubDate) {
	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}
	public Item[] getItens() {
		return itens;
	}
	public void setItens(Item[] itens) {
		this.itens = itens;
	}
    public Track getTrack() {
		return track;
	}
	public void setTrack(Track track) {
		this.track = track;
	}
	public Key getId() {
		return id;
	}
	public void setId(Key key) {
		this.id = key;
	}

//	public String toString(){
//		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
//	}
}
