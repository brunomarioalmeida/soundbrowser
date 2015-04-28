package com.soundbrowser.client.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Key {

	private long id;
	private String kind;
	private String appId;
	private AppNamespace appIdNamespace;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	public AppNamespace getAppIdNamespace() {
		return appIdNamespace;
	}
	public void setAppIdNamespace(AppNamespace appIdNamespace) {
		this.appIdNamespace = appIdNamespace;
	}
}
