
package com.example.locatenearby;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

public class Place implements Serializable{
	@DatabaseField(generatedId = true)
	int id;
	@DatabaseField
	String Name;
	@DatabaseField
	String type;
	@DatabaseField
	String url_icon;
	
	public String getUrl_icon() {
	
		return url_icon;
	}
	public void setUrl_icon(String url_icon) {
		this.url_icon = url_icon;
	}
	@DatabaseField
	Boolean isOpenNow;
	@DatabaseField
	Double longitute;
	@DatabaseField
	Double latitude;
	
	
	
	public Place(String name, String type, String url_icon, Boolean isOpenNow,
			Double longitute, Double latitude) {
		super();
		Name = name;
		this.type = type;
		this.url_icon = url_icon;
		this.isOpenNow = isOpenNow;
		this.longitute = longitute;
		this.latitude = latitude;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Boolean getIsOpenNow() {
		return isOpenNow;
	}
	public void setIsOpenNow(Boolean isOpenNow) {
		this.isOpenNow = isOpenNow;
	}
	public Double getLongitute() {
		return longitute;
	}
	public void setLongitute(Double longitute) {
		this.longitute = longitute;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Place() {
		super();
	}
	
	@Override
	public String toString() {
		return "Place [id=" + id + ", Name=" + Name + ", type=" + type
				+ ", url_icon=" + url_icon + ", isOpenNow=" + isOpenNow
				+ ", longitute=" + longitute + ", latitude=" + latitude + "]";
	}
	
	
	
	}
	
	

