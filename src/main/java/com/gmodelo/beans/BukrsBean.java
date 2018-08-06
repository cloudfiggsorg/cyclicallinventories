package com.gmodelo.beans;

public class BukrsBean {

	String bukrs; //society
	String bukrsDesc; // society description
	String werks; //ceco
	String werksDesc; //ceco description
	
	public String getBukrs() {
		return bukrs;
	}
	public void setBukrs(String bukrs) {
		this.bukrs = bukrs;
	}
	public String getBukrsDesc() {
		return bukrsDesc;
	}
	public void setBukrsDesc(String bukrsDesc) {
		this.bukrsDesc = bukrsDesc;
	}
	public String getWerks() {
		return werks;
	}
	public void setWerks(String werks) {
		this.werks = werks;
	}
	public String getWerksDesc() {
		return werksDesc;
	}
	public void setWerksDesc(String werksDesc) {
		this.werksDesc = werksDesc;
	}
	
	public BukrsBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public BukrsBean(String bukrs, String bukrsDesc, String werks, String werksDesc) {
		super();
		this.bukrs = bukrs;
		this.bukrsDesc = bukrsDesc;
		this.werks = werks;
		this.werksDesc = werksDesc;
	}
	@Override
	public String toString() {
		return "BukrsBean [bukrs=" + bukrs + ", bukrsDesc=" + bukrsDesc + ", werks=" + werks + ", werksDesc="
				+ werksDesc + "]";
	}
	
	
	
}
