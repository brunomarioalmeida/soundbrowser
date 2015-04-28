package com.soundbrowser.persistence.model;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;

@Generated("org.jsonschema2pojo")
public class Timming extends BaseEntity {

	@Expose
    @DatabaseField
	private String pivotStart;
	
	@Expose
    @DatabaseField
	private String pivotEnd;

	@DatabaseField(foreign = true)
	private Track track;

	private int acumulatedMiliSeconds;
	
	public Timming(){}

	public Timming(String pivotStart, String pivotEnd){
		this.pivotStart = pivotStart;
		this.pivotEnd = pivotEnd;
	}
	
	public String getPivotStart() {
		return pivotStart;
	}
	public void setPivotStart(String pivotStart) {
		this.pivotStart = pivotStart;
	}

	public String getPivotEnd() {
		return pivotEnd;
	}
	public void setPivotEnd(String pivotEnd) {
		this.pivotEnd = pivotEnd;
	}

	public int getAcumulatedMiliSeconds() {
		return acumulatedMiliSeconds;
	}
	public void setAcumulatedMiliSeconds(int acumulatedMiliSeconds) {
		this.acumulatedMiliSeconds = acumulatedMiliSeconds;
	}

	public Track getTrack() {
		return track;
	}
	public void setTrack(Track track) {
		this.track = track;
	}
}