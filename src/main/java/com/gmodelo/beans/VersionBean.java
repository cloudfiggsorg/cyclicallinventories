package com.gmodelo.beans;

public class VersionBean {
	int version;
	String name;
	String date;
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public VersionBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public VersionBean(int version, String name, String date) {
		super();
		this.version = version;
		this.name = name;
		this.date = date;
	}
	@Override
	public String toString() {
		return "VersionBean [version=" + version + ", name=" + name + ", date=" + date + "]";
	}
	
	

}
