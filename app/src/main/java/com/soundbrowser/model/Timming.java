package com.soundbrowser.model;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Timming {

	@Expose
	private String pivotStart;
	@Expose
	private String pivotEnd;

	private int acumulatedMiliSeconds;
	
	public Timming(String pivotStart, String pivotEnd){
		this.pivotStart = pivotStart;
		this.pivotEnd = pivotEnd;
	}
	
	/**
	 * 
	 * @return The pivotStart
	 */
	public String getPivotStart() {
		return pivotStart;
	}

	/**
	 * 
	 * @param pivotStart
	 *            The pivotStart
	 */
	public void setPivotStart(String pivotStart) {
		this.pivotStart = pivotStart;
	}

	/**
	 * 
	 * @return The pivotEnd
	 */
	public String getPivotEnd() {
		return pivotEnd;
	}

	/**
	 * 
	 * @param pivotEnd
	 *            The pivotEnd
	 */
	public void setPivotEnd(String pivotEnd) {
		this.pivotEnd = pivotEnd;
	}

	public int getAcumulatedMiliSeconds() {
		return acumulatedMiliSeconds;
	}

	public void setAcumulatedMiliSeconds(int acumulatedMiliSeconds) {
		this.acumulatedMiliSeconds = acumulatedMiliSeconds;
	}
}