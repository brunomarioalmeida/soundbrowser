package com.soundbrowser.adapter;

import android.graphics.drawable.Drawable;

public class ItemRow {

	String itemName;
	Drawable icon;
    String url;

	public ItemRow(String itemName, String url, Drawable icon) {
		super();
		this.itemName = itemName;
		this.icon = icon;
		this.url = url;
	}
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
