package com.soundbrowser.persistence.model;

import com.j256.ormlite.field.DatabaseField;

public class BaseEntity {

    @DatabaseField(generatedId=true)
    private int id;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
